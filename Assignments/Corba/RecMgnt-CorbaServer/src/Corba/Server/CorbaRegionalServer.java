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
package Corba.Server;

import Interface.Corba.DEMS.Project;
import Interface.Corba.DEMS.RegionalRecordManipulatorPOA;
import Interface.Corba.DEMS.RemoteException;
import Models.Region;
import Server.RegionalServer;
import java.io.IOException;
import org.omg.CORBA.*;

/**
 *
 * @author cmcarthur
 */
public class CorbaRegionalServer extends RegionalRecordManipulatorPOA {

    public CorbaRegionalServer(Region region) throws IOException {
        //m_Region = region;
        m_Server = new RegionalServer(region);
    }

    private ORB orb;
    //final private Region m_Region;
    final private RegionalServer m_Server;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void Start() {
        m_Server.Start();
    }

    // implement shutdown() method
    @Override
    public void shutdown() {
        orb.shutdown(false);
    }

    @Override
    public String createMRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) throws RemoteException {
        try {
            Models.Project project = new Models.Project(projects);
            return m_Server.createManagerRecord(firstName, lastName, employeeID, mailID, project, location);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "ERROR";
    }

    @Override
    public String createERecord(String firstName, String lastName, int employeeID, String mailID, String projectId) throws RemoteException {
        return m_Server.createEmployeeRecord(firstName, lastName, employeeID, mailID, projectId);
    }

    @Override
    public String editRecord(String recordID, String feildName, Any newValue) throws RemoteException {
        return m_Server.editRecord(recordID, feildName, newValue);
    }

    @Override
    public String getRecordCount() throws RemoteException {
        return m_Server.getRecordCount();
    }
}
