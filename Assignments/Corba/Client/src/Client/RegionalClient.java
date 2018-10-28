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

import Interface.Corba.DEMS.Project;
import Interface.Corba.DEMS.RegionalRecordManipulator;
import Interface.Corba.DEMS.RegionalRecordManipulatorHelper;
import Interface.Corba.DEMS.RemoteException;
import Models.Region;
import Utility.Logger;
import java.io.IOException;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 *
 * @author cmcarthur
 */
public class RegionalClient {

    final private ORB orb;
    final private String m_HRID;
    final private Region m_Region;
    final private RegionalRecordManipulator m_Remote;
    final private Logger m_Logger;

    public RegionalClient(ORB orb, String id) throws IOException, Exception {
        this.orb = orb;
        m_HRID = id;
        m_Region = Region.fromString(id.substring(0, 2));

        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        m_Remote = (RegionalRecordManipulator) RegionalRecordManipulatorHelper.narrow(ncRef.resolve_str(m_Region.toString()));

        m_Logger = new Logger(m_HRID);
        m_Logger.Log(m_HRID + " has connected!");
    }

    public String createManagerRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) throws RemoteException {
        return m_Remote.createMRecord(m_HRID, firstName, lastName, employeeID, mailID, projects, location);
    }

    public String createEmployeeRecord(String firstName, String lastName, int employeeID, String mailID, String projectId) throws RemoteException {
        return m_Remote.createERecord(m_HRID, firstName, lastName, employeeID, mailID, projectId);
    }

    public String editRecord(String recordID, String feildName, String newValue) throws RemoteException {
        Any toPass = orb.create_any();
        toPass.insert_string(newValue);
        return m_Remote.editRecord(m_HRID, recordID, feildName, toPass);
    }

    public String editRecord(String recordID, String feildName, int newValue) throws RemoteException {
        Any toPass = orb.create_any();
        toPass.insert_long(newValue);
        return m_Remote.editRecord(m_HRID, recordID, feildName, toPass);
    }

    public String getRecordCount() throws RemoteException {
        return m_Remote.getRecordCount(m_HRID);
    }

    public int getRegionalRecordCount() throws RemoteException {
        String allDesc = m_Remote.getRecordCount(m_HRID);

        allDesc = allDesc.substring(allDesc.indexOf(m_Region.getPrefix()) + 3);
        allDesc = allDesc.substring(0, allDesc.indexOf(" "));

        return Integer.parseInt(allDesc);
    }
}
