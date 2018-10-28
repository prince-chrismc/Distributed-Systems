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
public class ProjectIdentifier implements Serializable {

    public ProjectIdentifier(int uuid) throws Exception {
        if (uuid > 99999) {
            throw new Exception("Invalid ID!");
        }

        m_Id = new Interface.Corba.DEMS.ProjectIdentifier(m_Prefix, uuid);
    }

    @Override
    public String toString() {
        return m_Prefix + String.format("%05d", m_Id.m_UUID);
    }

    public void setId(String projectId) throws NumberFormatException, Exception {
        if (projectId.startsWith(m_Prefix) && projectId.length() == 6) {
            m_Id.m_UUID = Integer.parseInt(projectId.substring(m_Prefix.length()));
        } else {
            throw new Exception("Invalid ID!");
        }
    }

    public Interface.Corba.DEMS.ProjectIdentifier getRawId() {
        return m_Id;
    }

    static final String m_Prefix = "P";
    private Interface.Corba.DEMS.ProjectIdentifier m_Id;
}
