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
package Models;

import java.io.Serializable;

/**
 *
 * @author cmcarthur
 */
public class ManagerRecord extends Record implements Serializable {

    private Region m_Region;
    private Project m_Project;

    public ManagerRecord(int recordId, String firstName, String lastName, int employeeNumber, String mailId, Project projects, Region region) {
        super(new RecordIdentifier(RecordType.MANAGER, recordId), firstName, lastName, employeeNumber, mailId);
        m_Region = region;
        m_Project = projects;

    }

    @Override
    public String toString() {
        return "ManagerRecord{" + super.toString() + ", m_Region=" + m_Region + ", m_Project=" + m_Project + '}';
    }

    public Region getRegion() {
        return m_Region;
    }

    public void setRegion(Region region) {
        this.m_Region = region;
    }

    public Project getProject() {
        return m_Project;
    }

    public void setProjectId(String projectId) throws Exception {
        m_Project.getId().setId(projectId);
    }

    public void setProjectName(String projectName) {
        m_Project.setName(projectName);
    }

    public void setProjectClient(String projectClient) {
        m_Project.setClient(projectClient);
    }

    static public ManagerRecord fromString(String data) {

        ManagerRecord prasedResult = null;

        if (data.substring(0, "ManagerRecord{".length()).equals("ManagerRecord{")) {
            data = data.substring("ManagerRecord{".length());
        }
        if (data.substring(0, "Record{m_RecordId=MR".length()).equals("Record{m_RecordId=MR")) {
            data = data.substring("Record{m_RecordId=MR".length());
        }
        if (data.charAt(5) == ',') {
            try {
                prasedResult = new ManagerRecord(Integer.parseInt(data.substring(0, 5)), "", "", 0, "", new Project( new ProjectIdentifier(0), "", "" ), Region.CA);
            } catch (Exception ex) {
                
            }
            data = data.substring(7);
        }
        if (data.substring(0, "m_FirstName=".length()).equals("m_FirstName=")) {
            data = data.substring("m_FirstName=".length());
            if (prasedResult != null) {
                prasedResult.setFirstName(data.substring(0, data.indexOf(',')));
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_LastName=".length()).equals("m_LastName=")) {
            data = data.substring("m_LastName=".length());
            if (prasedResult != null) {
                prasedResult.setLastName(data.substring(0, data.indexOf(',')));
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_EmployeeNumber=".length()).equals("m_EmployeeNumber=")) {
            data = data.substring("m_EmployeeNumber=".length());
            if (prasedResult != null) {
                prasedResult.setEmployeeNumber(Integer.parseInt(data.substring(0, data.indexOf(','))));
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_MailId=".length()).equals("m_MailId=")) {
            data = data.substring("m_MailId=".length());
            if (prasedResult != null) {
                prasedResult.setMailId(data.substring(0, data.indexOf('}')));
            }
            data = data.substring(data.indexOf('}') + 3);
        }
        if (data.substring(0, "m_Region=".length()).equals("m_Region=")) {
            data = data.substring("m_Region=".length());
            if (prasedResult != null) {
                try {
                    prasedResult.setRegion(Region.fromString(data.substring(0, data.indexOf(','))));
                } catch (Exception ex) {
                    
                }
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_Project=Project{m_Id=".length()).equals("m_Project=Project{m_Id=")) {
            data = data.substring("m_Project=Project{m_Id=".length());
            if (prasedResult != null) {
                try {
                    prasedResult.setProjectId(data.substring(0, data.indexOf(',')));
                } catch (Exception ex) {
                    
                }
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_Name=".length()).equals("m_Name=")) {
            data = data.substring("m_Name=".length());
            if (prasedResult != null) {
                prasedResult.setProjectName(data.substring(0, data.indexOf(',')));
            }
            data = data.substring(data.indexOf(',') + 2);
        }
        if (data.substring(0, "m_Client=".length()).equals("m_Client=")) {
            data = data.substring("m_Client=".length());
            if (prasedResult != null) {
                prasedResult.setProjectClient(data.substring(0, data.indexOf('}')));
            }
        }

        return prasedResult;
    }
}
