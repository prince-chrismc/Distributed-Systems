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
public class UdpCommunicationTest {

    public UdpCommunicationTest() {
    }

    @Test
    public void canLaunchServer() throws IOException {
        RegionalServer Server = new RegionalServer(TestRegion.ZERO);
        Server.Start();
    }

    @Test
    public void canAnswerRecordCount() throws Exception {
        TestRegion currentRegion = TestRegion.TWO;
        
        RegionalServer Server = new RegionalServer(currentRegion);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, currentRegion.toInt());

        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(1000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get Record cound must be answered with an ACK", OperationCode.ACK_GET_RECORD_COUNT, response.getOpCode());
        assertEquals("UK RS should have Zero records", currentRegion.getPrefix() + " 0", currentRegion.getPrefix() + " " + response.getData());
    }

    @Test
    public void canUpdateEmployeeId() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.THREE);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.UPDATE_RECORD_INDEX, "ER30002", address, TestRegion.THREE.toInt());

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
        RegionalServer Server = new RegionalServer(TestRegion.FOUR);
        Server.Start();
        
        final String TEST_MANAGER_RECORD = "MR26792";

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.UPDATE_RECORD_INDEX, TEST_MANAGER_RECORD, address, TestRegion.FOUR.toInt());

        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(1000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get next id must be answered with an ACK", OperationCode.ACK_UPDATE_RECORD_INDEX, response.getOpCode());
        assertEquals("RS should have accepted new ID", TEST_MANAGER_RECORD, response.getData());
    }

    @Test
    public void doesGetRecordCountTimeout() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.FIVE);
        Server.Start();
        
        final String expected = TestRegion.FIVE.getPrefix() + " 0 CA TIMEOUT US TIMEOUT UK TIMEOUT";
        final String result = Server.getRecordCount("");
        assertEquals("Models.Regions should have TIMEOUT status", expected, result);
    }
}
