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

import Interface.Region;
import Models.Record;
import Utility.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author cmcarthur
 */
public class RecordTransferAgent {

    private Record m_Record;
    private Region m_DstRegion;
    final private Logger m_Logger;

    public RecordTransferAgent(Record record, Region srcRegion, Region dstRegion) throws IOException {
        this.m_Record = record;
        this.m_DstRegion = dstRegion;
        m_Logger = new Logger(srcRegion.getPrefix());
    }

    public boolean InitateTransfer() {
        return InitateTransfer(0);
    }

    private boolean InitateTransfer(int retryCounter) {

        try {
            m_Logger.Log("Asking [" + m_DstRegion + "] if it contains '" + m_Record.getRecordId() + "'.");

            if (checkRemoteForRecord(0)) {
                m_Logger.Log("Remote [" + m_DstRegion + "] report duplicate of '" + m_Record.getRecordId() + "'. Corrective behavoir?");
                return false; // ERROR if its not found
            }

            // Do the transfer
            
            if (!checkRemoteForRecord(0)) {
                m_Logger.Log("Remote [" + m_DstRegion + "] failed to accept transfer of '" + m_Record.getRecordId() + "'. Retrying...");
                return InitateTransfer(++retryCounter);
            }

        } catch (Exception ex) {
            m_Logger.Log("Unable to transfer '" + m_Record.getRecordId() + "' to remote server [" + m_DstRegion + "] due to communication break down!");
            return false;
        }

        return false;
    }

    private boolean checkRemoteForRecord(int retryCounter) throws Exception {
        try {

            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            Message request = new Message(OperationCode.DOES_RECORD_EXIST, m_Record.getRecordId().toString(), address, m_DstRegion.toInt());

            socket.send(request.getPacket());

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            socket.setSoTimeout(1000); // Set timeout in case packet is lost
            socket.receive(packet);

            Message response = new Message(packet);

            if (response.getOpCode() != OperationCode.ACK_DOES_RECORD_EXIST) {
                throw new Exception("Response mismatch to op code");
            }

            if ("NOT FOUND".equals(response.getData())) {
                return false;
            } else if (response.getData().equals(m_Record.getRecordId().toString())) {
                return true;
            } else {
                throw new Exception("Unexpected response when check record's existance on " + m_DstRegion);
            }

        } catch (Exception ex) {
            if (retryCounter < 10) {
                m_Logger.Log("Failed to get record count from [" + m_DstRegion + "]. trying...");
                System.out.println(ex);
                return checkRemoteForRecord(++retryCounter);
            }
        }

        throw new Exception("Unable to communicate with " + m_DstRegion);
    }
}
