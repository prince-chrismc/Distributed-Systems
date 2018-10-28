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

import Models.EmployeeRecord;
import Models.Feild;
import Models.ManagerRecord;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Record;
import Models.RecordIdentifier;
import Models.RecordsMap;
import Interface.Region;
import Utility.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;

/**
 *
 * @author c_mcart
 */
public class RegionalServer implements RequestListener.Processor {

    final private Region m_Region;
    final private RecordsMap m_Records;
    final private RequestListener m_Listener;
    final private Logger m_Logger;
    final private RecordUuidTracker m_IdTracker;
    private Thread m_ListenerThread;

    public RegionalServer(Region region) throws IOException {
        m_Region = region;
        m_Records = new RecordsMap();
        m_Logger = new Logger(m_Region.getPrefix());
        m_Listener = new RequestListener(this, m_Region);
        m_IdTracker = new RecordUuidTracker(m_Region);
    }

    public void Start() {
        m_ListenerThread = new Thread(m_Listener);
        m_ListenerThread.start();
        m_Listener.Wait();
        m_Logger.Log(m_Region.toString() + " is running!");
    }

    public void Stop() {
        try {
            m_Listener.Stop();
            m_ListenerThread.join();
            m_Logger.Log(m_Region.toString() + " has shutdown!");
        } catch (InterruptedException ex) {
            m_Logger.Log(m_Region.toString() + " --> ERROR <-- Failed to shutdown -- " + ex.getMessage());
        }
    }

    @Override
    public int getCurrentRecordCount() {
        m_Logger.Log("Reporting the number of records...");
        synchronized (m_Records) {
            return m_Records.count();
        }
    }

    public String createManagerRecord(String managerID, String firstName, String lastName, int employeeID, String mailID, Project projects, String location) {
        try {
            Models.Region region = Models.Region.fromString(location);
            RecordIdentifier newID = m_IdTracker.getNextManagerId();

            synchronized (m_Records) {
                m_Records.addRecord(new ManagerRecord(newID.getUUID(), firstName, lastName, employeeID, mailID, projects, region));
            }

            m_Logger.Log("Created Manager Record: " + m_Records.toString());
            return newID.toString();
        } catch (Exception e) {
            m_Logger.Log("Failed to Create Manager Record! Cause: " + e.getMessage());
            return "ERROR " + e.getMessage();
        }
    }

    public String createEmployeeRecord(String managerID, String firstName, String lastName, int employeeID, String mailID, String projectId) {
        try {
            ProjectIdentifier projID = new ProjectIdentifier(-1);
            projID.setId(projectId);
            RecordIdentifier newID = m_IdTracker.getNextEmployeeId();

            synchronized (m_Records) {
                m_Records.addRecord(new EmployeeRecord(newID.getUUID(), firstName, lastName, employeeID, mailID, projID));
            }

            m_Logger.Log("Created Employee Record: " + m_Records.toString());
            return newID.toString();
        } catch (Exception e) {
            m_Logger.Log("Failed to Create Employee Record! Cause: " + e.getMessage());
            return "ERROR " + e.getMessage();
        }
    }

    public String editRecord(String managerID, String recordID, String feildName, Object newValue) {
        synchronized (m_Records) {
            Record record = null;
            try {
                Feild feild = Feild.fromString(feildName);

                record = m_Records.removeRecord(recordID);

                if (record == null) {
                    throw new Exception("Invalid Record ID");
                }

                switch (record.getRecordId().getType()) {
                    case MANAGER:
                        editManagerRecord(record, feild, newValue);
                        break;
                    case EMPLOYEE:
                        editEmployeeRecord(record, feild, newValue);
                        break;
                    default:
                        throw new Exception("Invalid record type");
                }

                m_Records.addRecord(record);
                m_Logger.Log("Successfully modified '" + feildName + "' for record [ " + recordID + " ]    " + m_Records.toString());
                return recordID;

            } catch (Exception e) {

                if (record != null) {
                    m_Records.addRecord(record);
                }

                m_Logger.Log("Failed to Edit " + feildName + "! Cause: " + e.getMessage());
                return "ERROR " + e.getMessage();
            }
        }
    }

    private void editManagerRecord(Record record, Feild feild, Object newValue) throws Exception {
        if (record.getClass() != ManagerRecord.class) {
            throw new Exception("Invalid record type");
        }

        switch (feild) {
            case FIRST_NAME:
            case LAST_NAME:
            case EMPLOYEE_ID:
            case MAIL_ID:
                record.manipulateFeild(feild, newValue);
                break;
            case PROJECT_ID:
                if (newValue.getClass() == String.class) {
                    ((ManagerRecord) record).setProjectId((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case PROJECT_NAME:
                if (newValue.getClass() == String.class) {
                    if (record.getClass() == ManagerRecord.class) {
                        ((ManagerRecord) record).setProjectName((String) newValue);
                    } else {
                        throw new Exception("Invalid record type");
                    }
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case PROJECT_CLIENT:
                if (newValue.getClass() == String.class) {
                    ((ManagerRecord) record).setProjectClient((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case LOCATION:
                if (newValue.getClass() == String.class) {
                    ((ManagerRecord) record).setRegion(Models.Region.fromString((String) newValue));
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            default:
                throw new Exception("Unknow Feild");
        }
    }

    private void editEmployeeRecord(Record record, Feild feild, Object newValue) throws Exception {
        if (record.getClass() != EmployeeRecord.class) {
            throw new Exception("Invalid record type");
        }

        switch (feild) {
            case FIRST_NAME:
            case LAST_NAME:
            case EMPLOYEE_ID:
            case MAIL_ID:
                record.manipulateFeild(feild, newValue);
                break;
            case PROJECT_ID:
                if (newValue.getClass() == String.class) {
                    ((EmployeeRecord) record).setProjectId((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            default:
                throw new Exception("Unknow Feild");
        }
    }

    public String getRecordCount(String managerID) {
        m_Logger.Log("Reporting the number of records for all regions...");

        String retval = m_Region.getPrefix() + " ";
        synchronized (m_Records) {
            retval += m_Records.count();
        }

        for (Models.Region region : Models.Region.values()) {
            if (m_Region == region) {
                continue;
            }

            retval += " " + getRegionalCount(region, 0);
        }

        m_Logger.Log("Reporting { " + retval + " } for total records.");
        return retval;
    }

    private String getRegionalCount(Region region, int retryCounter) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, region.toInt());

            socket.send(request.getPacket());

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            socket.setSoTimeout(1000); // Set timeoue in case packet is lost
            socket.receive(packet);

            Message response = new Message(packet);

            if (response.getOpCode() != OperationCode.ACK_GET_RECORD_COUNT) {
                throw new Exception("Response mismatch to op code");
            }

            return region.getPrefix() + " " + response.getData();

        } catch (Exception ex) {

            if (retryCounter < 10) {
                m_Logger.Log("Failed to get record count from [" + region + "] due to : " + ex.getMessage() + ". Retrying...");
                return getRegionalCount(region, ++retryCounter);
            } else {
                return region.getPrefix() + " TIMEOUT";
            }
        }
    }

    @Override
    public RecordIdentifier updateRecordUuid(RecordIdentifier newRecordId) {
        return m_IdTracker.updateRecordUuid(newRecordId);
    }

    public String transferRecord(String managerID, String recordID, String remoteSeverName) {
        m_Logger.Log("Beginning process to transfer '" + recordID + "'...");

        Models.Region dstRegion;
        Record record;

        try {
            dstRegion = Models.Region.fromString(remoteSeverName);
        } catch (Exception ex) {
            return "ERROR " + ex.getMessage();
        }

        m_Logger.Log("Will be attempting transfer of '" + recordID + "' to remote server [" + dstRegion + "]...");

        synchronized (m_Records) {
            record = m_Records.removeRecord(recordID);

            if (record == null) {
                m_Logger.Log("Failed to get record '" + recordID + "' from internal storage.");
                return "ERROR 404 record not found";
            }
        }

        RecordTransferAgent transferAgent;
        try {
            transferAgent = new RecordTransferAgent(record, m_Region, dstRegion);
        } catch (IOException ex) {
            return "ERROR " + ex.getMessage();
        }

        m_Logger.Log("Transfer of '" + recordID + "' to [" + dstRegion + "] in progress...");
        if (transferAgent.InitateTransfer()) {
            return record.getRecordId().toString();
        } else {
            m_Logger.Log("Failed to transfer record '" + recordID + "' returing copy to internal storage.");
            synchronized (m_Records) {
                m_Records.addRecord(record);
            }
        }

        return "ERROR transfer incomplete";
    }

    @Override
    public String doesRecordExists(String recordID) {
        synchronized (m_Records) {
            Record record = m_Records.removeRecord(recordID);

            if (record == null) {
                return "NOT FOUND";
            } else {
                m_Records.addRecord(record);
                return record.getRecordId().toString();
            }
        }
    }

    @Override
    public String attemptRecordTransfer(String data) {

        if (data.charAt(0) == 'M') {
            ManagerRecord newRecord = ManagerRecord.fromString(data);
            if (newRecord != null) {
                synchronized (m_Records) {
                    m_Records.addRecord(newRecord);
                    m_Logger.Log("Accepted transfered of '" + newRecord.getRecordId() + "'     " + m_Records.toString());
                    return newRecord.getRecordId().toString();
                }
            }
        } else if (data.charAt(0) == 'E') {
            EmployeeRecord newRecord = EmployeeRecord.fromString(data);
            if (newRecord != null) {
                synchronized (m_Records) {
                    m_Records.addRecord(newRecord);
                    m_Logger.Log("Accepted transfered of '" + newRecord.getRecordId() + "'     " + m_Records.toString());
                    return newRecord.getRecordId().toString();
                }
            }
        }

        return "ERROR";
    }
}
