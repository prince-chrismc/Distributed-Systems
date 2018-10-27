/* 
    MIT License

    Copyright (c) 2018 Chris Mc, prince.chrismc(at)gmail(dot)com

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
package Server;

import Models.RecordIdentifier;
import Interface.Region;
import Utility.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author cmcarthur
 */
public class RequestListener implements Runnable {

    public interface Processor {

        public RecordIdentifier updateRecordUuid(RecordIdentifier newRecordId);

        public int getCurrentRecordCount();

        public String doesRecordExists(String data);
    }

    private Processor m_Handler;

    private boolean m_ShouldContinueWorking;
    private boolean m_ProcessingHasBegun;

    private Region m_Region;
    private DatagramSocket socket;
    private Logger m_Logger;

    public RequestListener(Processor handler, Region region) throws SocketException, IOException {
        m_Handler = handler;
        m_Region = region;
        m_Logger = new Logger(region.getPrefix());
        m_ShouldContinueWorking = false;
        m_ProcessingHasBegun = false;
    }

    public void Stop() {
        m_ShouldContinueWorking = false;
        m_ProcessingHasBegun = false;
        socket.close();
    }

    public void Wait() {
        while (m_ProcessingHasBegun == false) {
            m_Logger.Log("Waiting for Processing to be available");
        }
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(m_Region.toInt());
            m_ShouldContinueWorking = true;
        } catch (SocketException ex) {
            m_ShouldContinueWorking = false;
            m_Logger.Log("Failed to create socket due to: " + ex.getMessage());
        }

        if (m_ShouldContinueWorking) {
            m_Logger.Log("Ready...");
            m_ProcessingHasBegun = true;
        }

        while (m_ShouldContinueWorking) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                m_Logger.Log("Failed to receive message due to: " + ex.getMessage());
                return;
            }

            m_Logger.Log("Processing new request...");
            Message request = new Message(packet);

            String responsePayload = "ERROR";
            OperationCode responseCode = OperationCode.INVALID;

            switch (request.getOpCode()) {
                case UPDATE_RECORD_INDEX:
                    try {
                        responsePayload = m_Handler.updateRecordUuid(RecordIdentifier.fromString(request.getData())).toString();
                        responseCode = OperationCode.ACK_UPDATE_RECORD_INDEX;
                        m_Logger.Log("Answering Request for record index");
                    } catch (Exception ex) {
                        m_Logger.Log("Failed to handle update record index request");
                        System.out.println(ex);
                    }
                    break;
                case GET_RECORD_COUNT:
                    responsePayload = String.valueOf(m_Handler.getCurrentRecordCount());
                    responseCode = OperationCode.ACK_GET_RECORD_COUNT;
                    m_Logger.Log("Answering Request for record count '" + responsePayload + "'.");
                    break;
                case DOES_RECORD_EXIST:
                    responsePayload = m_Handler.doesRecordExists(request.getData());
                    responseCode = OperationCode.ACK_DOES_RECORD_EXIST;
                    m_Logger.Log("Answering Request for does record exist '" + responsePayload + "'.");
                    break;
                default:
                    m_Logger.Log("Unhandle request: " + request.toString());
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            Message response = new Message(responseCode, responsePayload, address, port);

            try {
                socket.send(response.getPacket());
            } catch (IOException ex) {
                m_Logger.Log("Failed to send message");
                System.out.println(ex);
            }
        }

        socket.close();
    }
}
