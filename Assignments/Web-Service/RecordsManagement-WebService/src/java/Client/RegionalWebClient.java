/*
 * The MIT License
 *
 * Copyright 2018 cmcarthur.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Client;

import Models.Region;
import Utility.Logger;
import java.io.IOException;

/**
 *
 * @author cmcarthur
 */
public class RegionalWebClient {
    final private String m_HRID;
    final private Region m_Region;
    final private CentralServer m_Remote;
    final private Logger m_Logger;

    public RegionalWebClient(String id) throws IOException, Exception {
        m_HRID = id;
        m_Region = Region.fromString(id.substring(0, 2));

        CentralServer_Service service = new CentralServer_Service();
        m_Remote = service.getCentralServerPort();

        m_Logger = new Logger(m_HRID);
        m_Logger.Log(m_HRID + " has connected!");
    }
    
    public String createManagerRecord(String firstName, String lastName, int employeeID, String mailID, Project projects, String location) {
        return m_Remote.createMRecord(m_HRID, firstName, lastName, employeeID, mailID, projects, location);
    }

    public String createEmployeeRecord(String firstName, String lastName, int employeeID, String mailID, String projectId) {
        return m_Remote.createERecord(m_HRID, firstName, lastName, employeeID, mailID, projectId);
    }

    public String editRecord(String recordID, String feildName, Object newValue) {
        return m_Remote.editRecord(m_HRID, recordID, feildName, newValue);
    }
    
    public String transferRecord(String recordID, Region region) {
        return m_Remote.transferRecord(m_HRID, recordID, region.toString());
    }

    public String getRecordCount() {
        return m_Remote.getRecordCount(m_HRID);
    }

    public int getRegionalRecordCount() {
        String allDesc = m_Remote.getRecordCount(m_HRID);

        allDesc = allDesc.substring(allDesc.indexOf(m_Region.getPrefix()) + 3);
        allDesc = allDesc.substring(0, allDesc.indexOf(" "));

        return Integer.parseInt(allDesc);
    }
    
}
