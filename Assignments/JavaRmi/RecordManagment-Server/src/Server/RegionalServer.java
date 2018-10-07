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

import Interface.RegionalRecordManipulator;
import Models.EmployeeRecord;
import Models.Feild;
import Models.ManagerRecord;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Record;
import Models.RecordIdentifier;
import Models.RecordsMap;
import Models.Region;
import Utility.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 *
 * @author c_mcart
 */
public class RegionalServer extends UnicastRemoteObject implements RegionalRecordManipulator, RequestListener.Processor {

    private Region m_Region;
    private RecordsMap m_Records;
    private RequestListener m_Listener;
    private Logger m_Logger;
    
    

    public RegionalServer(Region region) throws RemoteException, IOException {
        super();
        m_Region = region;
        m_Records = new RecordsMap();
        m_Logger = new Logger(m_Region.getPrefix());
        m_Listener = new RequestListener(this, m_Region.toInt());
    }
    
    public void Start(){
        m_Listener.start();
        m_Logger.Log(m_Region.toString() + " is running!");
    }

    public String getUrl() {
        return "rmi://localhost/" + m_Region.toString();
    }

    @Override
    public int getCurrentRecordCount() {
        m_Logger.Log("Reporting the number of records...");
        return m_Records.count();
    }

    @Override
    public void createMRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) throws RemoteException {

        try {
            Region region = Region.fromString(location);
            m_Records.addRecord(new ManagerRecord(1001, firstName, lastName, employeeID, mailID, projects, region));
        } catch (Exception e) {
            m_Logger.Log("Failed to Create Manager Record!");
            e.printStackTrace();
        }

        m_Logger.Log("Created Manager Record: " + m_Records.toString());
    }

    @Override
    public void createERecord(String firstName, String lastName, int employeeID, String mailID, String projectId) throws RemoteException {

        try {
            ProjectIdentifier projID = new ProjectIdentifier(-1);
            projID.setId(projectId);
            m_Records.addRecord(new EmployeeRecord(54321, firstName, lastName, employeeID, mailID, projID));
        } catch (Exception e) {
            m_Logger.Log("Failed to Create Employee Record!");
            e.printStackTrace();
        }

        m_Logger.Log("Created Employee Record: " + m_Records.toString());
    }

    @Override
    public void editRecord(String recordID, String feildName, Object newValue) throws RemoteException {
        try {
            Feild feild = Feild.fromString(feildName);

            Record record = m_Records.removeRecord(recordID);

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

        } catch (Exception e) {
            m_Logger.Log("Failed to Edit " + feildName + "!");
            e.printStackTrace();
        }
    }

    private void editManagerRecord(Record record, Feild feild, Object newValue) throws Exception {
        if (record.getClass() != ManagerRecord.class) {
            throw new Exception("Invalid record type");
        }

        switch (feild) {
            case FIRST_NAME:
                if (newValue.getClass() == String.class) {
                    record.setFirstName((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case LAST_NAME:
                if (newValue.getClass() == String.class) {
                    record.setLastName((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case EMPLOYEE_ID:
                if (newValue.getClass() == Integer.class) {
                    record.setEmployeeNumber((int) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case MAIL_ID:
                if (newValue.getClass() == String.class) {
                    record.setMailId((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
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
                    ((ManagerRecord) record).setRegion(Region.fromString((String) newValue));
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
                if (newValue.getClass() == String.class) {
                    record.setFirstName((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case LAST_NAME:
                if (newValue.getClass() == String.class) {
                    record.setLastName((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case EMPLOYEE_ID:
                if (newValue.getClass() == Integer.class) {
                    record.setEmployeeNumber((int) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
                break;
            case MAIL_ID:
                if (newValue.getClass() == String.class) {
                    record.setMailId((String) newValue);
                } else {
                    throw new Exception("Invalid paramater");
                }
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

    @Override
    public String getRecordCount() throws RemoteException {
        m_Logger.Log("Reporting the number of records for all regions...");
        String retval = m_Region.getPrefix() + " " + m_Records.count();
        for (Region region : Region.values()) {
            if (m_Region == region) {
                continue;
            }
            
            retval += " " + getRegionalCount(region);
        }
        
        m_Logger.Log("Reporting { " + retval + " } for total records.");
        return retval;
    }

    private String getRegionalCount(Region region) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            Message request = new Message(OperationCode.GET_RECORD_COUNT, "", address, region.toInt());
            
            socket.send(request.getPacket());
            
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            
            Message response = new Message(packet);
            
            if( response.getOpCode() != OperationCode.ACK_GET_RECORD_COUNT ){
                throw new Exception("Response mismatch to op code");
            }
            
            return region.getPrefix() + " " + response.getData();
            
        } catch (Exception ex) {
            m_Logger.Log("Failed to get record count from [" + region + "]. trying...");
            System.out.println(ex);
            
            return getRegionalCount(region);
        }
    }
}
