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
import Models.ManagerRecord;
import Models.Project;
import Models.ProjectIdentifier;
import Models.RecordsMap;
import Models.Region;
import Utility.Logger;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author c_mcart
 */
public class RegionalServer extends UnicastRemoteObject implements RegionalRecordManipulator {

    private Region m_Region;
    private RecordsMap m_Records;
    private Logger m_Logger;

    public RegionalServer(Region region) throws RemoteException, IOException {
        super();
        m_Region = region;
        m_Records = new RecordsMap();
        m_Logger = new Logger( m_Region.getPrefix() );
        
        m_Logger.Log(m_Region.toString() + " is running!");
    }

    public String getUrl() {
        return "rmi://localhost/" + m_Region.toString();
    }

    @Override
    public int getRecordCount() throws RemoteException {
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

        m_Logger.Log("Created Manager Record : " + m_Records.toString());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
