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
import java.util.Objects;

/**
 *
 * @author cmcarthur
 */
public class Record implements Serializable{

    protected Record(RecordIdentifier recordId, String firstName, String lastName, int employeeNumber, String mailId) {
        m_RecordId = recordId;
        m_FirstName = firstName;
        m_LastName = lastName;
        m_EmployeeNumber = employeeNumber;
        m_MailId = mailId;
    }

    public RecordIdentifier getRecordId() {
        return m_RecordId;
    }

    public String getFirstName() {
        return m_FirstName;
    }

    public String getLastName() {
        return m_LastName;
    }

    public int getEmployeeNumber() {
        return m_EmployeeNumber;
    }

    public String getMailId() {
        return m_MailId;
    }
    
    public String getHashIndex(){
        return Character.toString( m_LastName.charAt(0) );
    }

    public void setFirstName(String firstName) {
        m_FirstName = firstName;
    }

    public void setLastName(String lastName) {
        m_LastName = lastName;
    }

    public void setEmployeeNumber(int employeeNumber) {
        m_EmployeeNumber = employeeNumber;
    }

    public void setMailId(String mailId) {
        m_MailId = mailId;
    }

    
    
    private final RecordIdentifier m_RecordId;

    private String m_FirstName;
    private String m_LastName;
    private int m_EmployeeNumber;
    private String m_MailId;

    @Override
    public String toString() {
        return "Record{" + "m_RecordId=" + m_RecordId + ", m_FirstName=" + m_FirstName + ", m_LastName=" + m_LastName + ", m_EmployeeNumber=" + m_EmployeeNumber + ", m_MailId=" + m_MailId + '}';
    }

    public boolean equals(String rhs) {
        return m_RecordId.toString().equals(rhs);
    }
}
