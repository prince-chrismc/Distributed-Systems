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
package Rmi.Server;

import Server.RegionalServer;
import Interface.Rmi.RegionalRecordManipulator;
import Models.Project;
import Interface.Region;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author c_mcart
 */
public class RmiRegionalServer extends UnicastRemoteObject implements RegionalRecordManipulator {
    final private Region m_Region;
    final private RegionalServer m_Server;
    
    public RmiRegionalServer(Region region) throws RemoteException, IOException {
        super();
        m_Region = region;
        m_Server = new RegionalServer( region );
    }

    public void Start() {
        m_Server.Start();
    }

    public String getUrl() {
        return "rmi://localhost/" + m_Region.toString();
    }

    @Override
    public String createMRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) throws RemoteException {
        return m_Server.createManagerRecord(null, firstName, lastName, employeeID, mailID, projects, location);
    }

    @Override
    public String createERecord(String firstName, String lastName, int employeeID, String mailID, String projectId) throws RemoteException {
        return m_Server.createEmployeeRecord(null, firstName, lastName, employeeID, mailID, projectId);
    }

    @Override
    public String editRecord(String recordID, String feildName, Object newValue) throws RemoteException {
        return m_Server.editRecord(null, recordID, feildName, newValue);
    }

    @Override
    public String getRecordCount() throws RemoteException {
        return m_Server.getRecordCount(null);
    }
}
