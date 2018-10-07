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
    }

    private Processor m_Handler;

    private boolean running;
    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    private Logger m_Logger;

    public RequestListener(Processor handler, int port) throws SocketException, IOException {
        m_Handler = handler;
        socket = new DatagramSocket(port);
        m_Logger = new Logger("UDP Server " + port);
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                m_Logger.Log("Failed to receive message");
            }

            m_Logger.Log("Processing new request...");
            Message request = new Message(packet);

            String responsePayload = "";
            switch (request.getOpCode()) {
                case UPDATE_RECORD_INDEX: {
                    try {
                        responsePayload = m_Handler.updateRecordUuid(RecordIdentifier.fromString(request.getData())).toString();
                    } catch (Exception ex) {
                        m_Logger.Log("Failed to handle update record uuid request");
                    }
                }
                default: {
                    m_Logger.Log("Unhandle request: " + request.toString());
                }
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            Message response = new Message(OperationCode.ACK_UPDATE_RECORD_INDEX, responsePayload, address, port);

            try {
                socket.send(response.getPacket());
            } catch (IOException ex) {
                m_Logger.Log("Failed to send message");
            }
        }

        socket.close();
    }

    public void shutdown() {
        running = false;
    }

}