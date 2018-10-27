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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author cmcarthur
 */
public class RegionalServerTest {

    public RegionalServerTest() {
    }

    @Test
    public void canLaunchServer() throws IOException {
        RegionalServer Server = new RegionalServer(TestRegion.US);
        Server.Start();
    }

    @Test
    public void canGetRecordCount() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.UK);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, TestRegion.UK.toInt());

        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(1000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get Record cound must be answered with an ACK", OperationCode.ACK_GET_RECORD_COUNT, response.getOpCode());
        assertEquals("UK RS should have Zero records", "UK 0", TestRegion.UK.getPrefix() + " " + response.getData());
    }

    @Test
    public void canUpdateEmployeeId() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.TK);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.UPDATE_RECORD_INDEX, "ER30002", address, TestRegion.TK.toInt());

        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(1000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get next id must be answered with an ACK", OperationCode.ACK_UPDATE_RECORD_INDEX, response.getOpCode());
        assertEquals("RS should have accepted new ID", "ER30002", response.getData());
    }

    @Test
    public void canUpdateManagerId() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.AU);
        Server.Start();
        
        final String TEST_MANAGER_RECORD = "MR26792";

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.UPDATE_RECORD_INDEX, TEST_MANAGER_RECORD, address, TestRegion.AU.toInt());

        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(1000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get next id must be answered with an ACK", OperationCode.ACK_UPDATE_RECORD_INDEX, response.getOpCode());
        assertEquals("RS should have accepted new ID", TEST_MANAGER_RECORD, response.getData());
    }
}
