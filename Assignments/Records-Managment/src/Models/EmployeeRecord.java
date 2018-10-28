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

/**
 *
 * @author cmcarthur
 */
public class EmployeeRecord extends Record {

    public EmployeeRecord(int recordId, String firstName, String lastName, int employeeNumber, String mailId, ProjectIdentifier projectId) {
        super(new RecordIdentifier(RecordType.EMPLOYEE, recordId), firstName, lastName, employeeNumber, mailId);
        m_ProjectId = projectId;
    }

    public ProjectIdentifier getProjectId() {
        return m_ProjectId;
    }

    public void setProjectId(String projectId) throws Exception {
        m_ProjectId.setId(projectId);
    }

    private ProjectIdentifier m_ProjectId;

    @Override
    public String toString() {
        return "EmployeeRecord{" + super.toString() + ", m_ProjectId=" + m_ProjectId + '}';
    }

    public static EmployeeRecord fromString(String data) {

        System.out.println(data);
        
        EmployeeRecord prasedResult = null;

        if (data.substring(0, "EmployeeRecord{".length()).equals("EmployeeRecord{")) {
            data = data.substring("EmployeeRecord{".length());
        }
        if (data.substring(0, "Record{m_RecordId=ER".length()).equals("Record{m_RecordId=ER")) {
            data = data.substring("Record{m_RecordId=ER".length());
        }
        if (data.charAt(5) == ',') {
            try {
                prasedResult = new EmployeeRecord(Integer.parseInt(data.substring(0, 5)), "", "", 0, "", new ProjectIdentifier(0));
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
        if (data.substring(0, "m_ProjectId=".length()).equals("m_ProjectId=")) {
            data = data.substring("m_ProjectId=".length());
            if (prasedResult != null) {
                try {
                    prasedResult.setProjectId(data.substring(0, data.indexOf('}')));
                } catch (Exception ex) {
                    
                }
            }
        }

        return prasedResult;
    }
}
