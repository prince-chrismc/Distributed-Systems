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
package Client;

import Models.Region;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Interface.RegionalRecordManipulator;
import Models.Project;
import Utility.Logger;
import java.io.IOException;

/**
 *
 * @author cmcarthur
 */
public class RegionalClient implements RegionalRecordManipulator {

    private String m_HRID;
    private Region m_Region;
    private RegionalRecordManipulator m_Remote;
    private Logger m_Logger;

    public RegionalClient(String id) throws RemoteException, NotBoundException, IOException, Exception {
        m_HRID = id;
        m_Region = Region.fromString(id.substring(0, 2));

        Registry registry = LocateRegistry.getRegistry(12345);
        m_Remote = (RegionalRecordManipulator) registry.lookup("rmi://localhost/" + m_Region.toString());
        
        m_Logger = new Logger( m_HRID );
        m_Logger.Log(m_HRID + " has connected!");
    }

    @Override
    public int getRecordCount() throws RemoteException {
        return m_Remote.getRecordCount();
    }

    @Override
    public void createMRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) throws RemoteException {
        m_Remote.createMRecord(firstName, lastName, employeeID, mailID, projects, location);
    }

    @Override
    public void createERecord(String firstName, String lastName, int employeeID, String mailID, String projectId) throws RemoteException {
        m_Remote.createERecord(firstName, lastName, employeeID, mailID, projectId);
    }

    @Override
    public void editRecord(String recordID, String feildName, Object newValue) throws RemoteException {
        m_Remote.editRecord(recordID, feildName, newValue);
    }
}
