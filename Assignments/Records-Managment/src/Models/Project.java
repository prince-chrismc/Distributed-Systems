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
public class Project implements Serializable {

    public Project(ProjectIdentifier id, String name, String client) {
        m_Id = id;
        m_Project = new Interface.Corba.DEMS.Project( m_Id.getRawId(), name, client );
    }

    public Project(Interface.Corba.DEMS.Project project) throws Exception {
        m_Id = new ProjectIdentifier( project.m_Id.m_UUID  );
        m_Project = project;
    }

    public ProjectIdentifier getId() {
        return m_Id;
    }

    public void setId(ProjectIdentifier id) {
        m_Id = id;
        m_Project.m_Id.m_UUID = id.getRawId().m_UUID;
    }

    public String getName() {
        return m_Project.m_Name;
    }

    public void setName(String name) {
        m_Project.m_Name = name;
    }

    public String getClient() {
        return m_Project.m_Client;
    }

    public void setClient(String client) {
        m_Project.m_Client = client;
    }

    private ProjectIdentifier m_Id;
    private Interface.Corba.DEMS.Project m_Project;

    @Override
    public String toString() {
        return "Project{" + "m_Id=" + m_Id + ", m_Name=" + m_Project.m_Name + ", m_Client=" + m_Project.m_Client + '}';
    }
    
    
}
