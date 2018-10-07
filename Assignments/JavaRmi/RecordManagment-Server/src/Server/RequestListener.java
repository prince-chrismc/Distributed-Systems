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
import Models.Region;
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
public class RequestListener extends Thread {

    public interface Processor {

        public RecordIdentifier updateRecordUuid(RecordIdentifier newRecordId);

        public int getCurrentRecordCount();
    }

    private Processor m_Handler;

    private boolean running;
    private DatagramSocket socket;
    private Logger m_Logger;

    public RequestListener(Processor handler, Region region) throws SocketException, IOException {
        m_Handler = handler;
        socket = new DatagramSocket(region.toInt());
        m_Logger = new Logger(region.getPrefix());
    }

    @SuppressWarnings("UnusedAssignment")
    public void run() {
        running = true;
        m_Logger.Log("Ready...");

        while (running) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                m_Logger.Log("Failed to receive message");
                System.out.println(ex);
            }

            m_Logger.Log("Processing new request...");
            Message request = new Message(packet);

            String responsePayload = "ERROR";
            OperationCode responseCode = OperationCode.INVALID;
            
            switch (request.getOpCode()) {
                case UPDATE_RECORD_INDEX: {
                    try {
                        responsePayload = m_Handler.updateRecordUuid(RecordIdentifier.fromString(request.getData())).toString();
                        responseCode = OperationCode.ACK_UPDATE_RECORD_INDEX;
                        m_Logger.Log("Answering Request for record index");
                    } catch (Exception ex) {
                        m_Logger.Log("Failed to handle update record index request");
                        System.out.println(ex);
                    }
                }
                break;
                case GET_RECORD_COUNT: {
                    responsePayload = String.valueOf(m_Handler.getCurrentRecordCount());
                    responseCode = OperationCode.ACK_GET_RECORD_COUNT;
                    m_Logger.Log("Answering Request for record count '" + responsePayload + "'.");
                }
                break;
                default: {
                    m_Logger.Log("Unhandle request: " + request.toString());
                }
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
