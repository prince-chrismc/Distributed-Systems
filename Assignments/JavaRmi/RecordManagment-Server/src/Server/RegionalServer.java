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
import Models.ManagerRecord;
import Models.Project;
import Models.RecordIdentifier;
import Models.RecordType;
import Models.RecordsMap;
import Models.Region;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author c_mcart
 */
public class RegionalServer extends UnicastRemoteObject implements RegionalRecordManipulator {

    private Region m_Region;
    private RecordsMap m_Records;

    public RegionalServer(Region region) throws RemoteException {
        super();
        m_Region = region;
        m_Records = new RecordsMap();
    }

    public String getUrl() {
        return "rmi://localhost/" + m_Region.toString();
    }

    @Override
    public int getRecordCount() throws RemoteException {
        return 2;
    }

    @Override
    public void createMRecord(String firstName, String lastName, int employeeID, String mailID, Project projects) throws RemoteException {

        m_Records.addRecord(new ManagerRecord(new RecordIdentifier(RecordType.MANAGER, 1001), firstName, lastName, employeeID, mailID, projects, m_Region));

        System.out.println("Created Manager Record: " + m_Records.toString());
        
    }
}
