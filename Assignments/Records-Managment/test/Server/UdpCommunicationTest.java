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

import Models.Project;
import Models.ProjectIdentifier;
import Models.Region;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cmcarthur
 */
public class UdpCommunicationTest {

    static private ReentrantLock lock = new ReentrantLock();
    static private RegionalServer Canada;
    static private RegionalServer UnitedStates;
    static private RegionalServer UnitedKingdom;

    public UdpCommunicationTest() {
    }

    @BeforeClass
    static public void setupRealRegionalServers() throws IOException {
        Canada = new RegionalServer(Region.CA);
        UnitedStates = new RegionalServer(Region.US);
        UnitedKingdom = new RegionalServer(Region.UK);
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

        System.out.println("Sending Packet... Testing for timming");
        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
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

        System.out.println("Sending Packet... Testing for timming");
        socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
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

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
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
        lock.lock();
        try {
            final String result = Server.getRecordCount("");
            assertEquals("Models.Regions should have TIMEOUT status", expected, result);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void doesRecordNotExistInServer() throws Exception {
        RegionalServer Server = new RegionalServer(TestRegion.SIX);
        Server.Start();

        final String TEST_MANAGER_RECORD = "MR6541";

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.DOES_RECORD_EXIST, TEST_MANAGER_RECORD, address, TestRegion.SIX.toInt());

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("request must be answered with an ACK", OperationCode.ACK_DOES_RECORD_EXIST, response.getOpCode());
        assertEquals("record should not be found", "NOT FOUND", response.getData());
    }

    @Test
    public void doesRecordExistInServer() throws Exception {
        String newManagerRecordId = "TBA";
        RegionalServer Server = new RegionalServer(TestRegion.SEVEN);
        Server.Start();

        lock.lock();
        try {
            Canada.Start();
            UnitedStates.Start();
            UnitedKingdom.Start();

            newManagerRecordId = Server.createManagerRecord("XX0000", "John", "Smith", 25165, "john.smith@example.com",
                    new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        } finally {
            Canada.Stop();
            UnitedStates.Stop();
            UnitedKingdom.Stop();
            lock.unlock();
        }

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.DOES_RECORD_EXIST, newManagerRecordId, address, TestRegion.SEVEN.toInt());

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message responseOne = new Message(packet);

        assertEquals("request must be answered with an ACK", OperationCode.ACK_DOES_RECORD_EXIST, responseOne.getOpCode());
        assertEquals("record should be found", newManagerRecordId, responseOne.getData());
        
                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());
        socket.receive(packet);
        Message responseTwo = new Message(packet);

        assertEquals("request must be answered with an ACK", OperationCode.ACK_DOES_RECORD_EXIST, responseTwo.getOpCode());
        assertEquals("record should be found", newManagerRecordId, responseTwo.getData());
    }

    @Test
    public void canStopServer() throws Exception {
        TestRegion currentRegion = TestRegion.EIGTH;

        RegionalServer Server = new RegionalServer(currentRegion);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, currentRegion.toInt());

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get Record cound must be answered with an ACK", OperationCode.ACK_GET_RECORD_COUNT, response.getOpCode());
        assertEquals("UK RS should have Zero records", currentRegion.getPrefix() + " 0", currentRegion.getPrefix() + " " + response.getData());

        Server.Stop();

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        try {
            socket.setSoTimeout(5000); // Set timeout in case packet is lost
            socket.receive(packet);
        } catch (IOException e) {
            assertEquals("Scoket should time out", "Receive timed out", e.getMessage());
        }
    }

    @Test
    public void canRestartServer() throws Exception {
        TestRegion currentRegion = TestRegion.NINE;

        RegionalServer Server = new RegionalServer(currentRegion);
        Server.Start();

        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, currentRegion.toInt());

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.setSoTimeout(5000); // Set timeout in case packet is lost
        socket.receive(packet);

        Message response = new Message(packet);

        assertEquals("Get Record cound must be answered with an ACK", OperationCode.ACK_GET_RECORD_COUNT, response.getOpCode());
        assertEquals("UK RS should have Zero records", currentRegion.getPrefix() + " 0", currentRegion.getPrefix() + " " + response.getData());

        Server.Stop();

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        try {
            socket.setSoTimeout(5000); // Set timeout in case packet is lost
            socket.receive(packet);
        } catch (IOException e) {
            assertEquals("Scoket should time out", "Receive timed out", e.getMessage());
        }

        Server.Start();

                System.out.println("Sending Packet... Testing for timming");
socket.send(request.getPacket());

        socket.receive(packet);

        Message responseTwo = new Message(packet);

        assertEquals("Get Record cound must be answered with an ACK", OperationCode.ACK_GET_RECORD_COUNT, responseTwo.getOpCode());
        assertEquals("UK RS should have Zero records", currentRegion.getPrefix() + " 0", currentRegion.getPrefix() + " " + responseTwo.getData());

    }
}
