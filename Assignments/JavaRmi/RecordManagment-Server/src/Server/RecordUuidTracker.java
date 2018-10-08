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
import Models.RecordType;
import Models.Region;
import Utility.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author cmcarthur
 */
public class RecordUuidTracker {

    final private Region m_Region;
    private RecordIdentifier m_CurrentManagerId;
    private RecordIdentifier m_CurrentEmployeeId;
    final private Logger m_Logger;

    public RecordUuidTracker(Region region) throws IOException {
        m_Region = region;
        m_CurrentManagerId = new RecordIdentifier(RecordType.MANAGER, 10000);
        m_CurrentEmployeeId = new RecordIdentifier(RecordType.EMPLOYEE, 0);
        m_Logger = new Logger(m_Region.getPrefix());
    }

    public RecordIdentifier updateRecordUuid(RecordIdentifier newRecordId) {
        switch (newRecordId.getType()) {
            case MANAGER:
                return updateManagerRecordUuid(newRecordId);
            case EMPLOYEE:
                return updateEmployeeRecordUuid(newRecordId);
        }

        return null;
    }

    public RecordIdentifier updateManagerRecordUuid(RecordIdentifier newRecordId) {
        if (m_CurrentManagerId.getUUID() < newRecordId.getUUID()) {
            m_Logger.Log("Updating next manager UUID to be '" + newRecordId.toString() + "'.");
            m_CurrentManagerId = newRecordId;
            return m_CurrentManagerId;
        } else {
            m_Logger.Log("Suggesting greater value for next ID... Current value is '" + m_CurrentManagerId.toString() + "'.");
            return new RecordIdentifier(RecordType.MANAGER, m_CurrentManagerId.getUUID() + 1);
        }
    }

    public RecordIdentifier updateEmployeeRecordUuid(RecordIdentifier newRecordId) {
        if (m_CurrentEmployeeId.getUUID() < newRecordId.getUUID()) {
            m_Logger.Log("Updating next employee UUID to be '" + newRecordId.toString() + "'.");
            m_CurrentEmployeeId = newRecordId;
            return m_CurrentEmployeeId;
        } else {
            m_Logger.Log("Suggesting greater value for next ID... Current value is '" + m_CurrentEmployeeId.toString() + "'.");
            return new RecordIdentifier(RecordType.EMPLOYEE, m_CurrentEmployeeId.getUUID() + 1);
        }
    }

    public synchronized RecordIdentifier getNextManagerId() {
        m_Logger.Log("Attempting to determine next manager UUID...");
        RecordIdentifier nextId = new RecordIdentifier(RecordType.MANAGER, m_CurrentManagerId.getUUID() + 1);
        for (Region region : Region.values()) {
            if (m_Region == region) {
                continue;
            }

            nextId = notifyRegionWithUpdate(region, nextId);
        }

        m_Logger.Log("Determine next manager UUID to be '" + nextId.toString() + "'.");
        m_CurrentManagerId = nextId;
        return m_CurrentManagerId;
    }

    public synchronized RecordIdentifier getNextEmployeeId() {
        m_Logger.Log("Attempting to determine next employee UUID...");
        RecordIdentifier nextId = new RecordIdentifier(RecordType.EMPLOYEE, m_CurrentEmployeeId.getUUID() + 1);
        for (Region region : Region.values()) {
            if (m_Region == region) {
                continue;
            }

            nextId = notifyRegionWithUpdate(region, nextId);
        }

        m_Logger.Log("Determine next employee UUID to be '" + nextId.toString() + "'.");
        m_CurrentEmployeeId = nextId;
        return m_CurrentEmployeeId;
    }

    private RecordIdentifier notifyRegionWithUpdate(Region region, RecordIdentifier nextId) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            Message request = new Message(OperationCode.UPDATE_RECORD_INDEX, nextId.toString(), address, region.toInt());

            socket.send(request.getPacket());

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            Message response = new Message(packet);

            if (response.getOpCode() != OperationCode.ACK_UPDATE_RECORD_INDEX) {
                throw new Exception("Response mismatch to op code");
            }

            return RecordIdentifier.fromString(response.getData());

        } catch (Exception ex) {
            m_Logger.Log("Failed to notify [" + region + "] with indexing update. Retying...");
            System.out.println(ex);

            return notifyRegionWithUpdate(region, nextId);
        }

    }
}
