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
}
