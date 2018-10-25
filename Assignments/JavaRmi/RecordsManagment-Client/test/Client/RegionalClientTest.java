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

import Models.Feild;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Region;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cmcarthur
 */
public class RegionalClientTest {

    private RegionalClient Canada;

    public RegionalClientTest() throws Exception {
        Canada = new RegionalClient("CA1234");
    }

    @Before
    public void setRmiRegistry() {
        System.setProperty("java.rmi.server.hostname","127.0.0.1");
    }
    
    @Test
    public void canCreateManageRecords() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        Canada.createMRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getRegionalRecordCount());
        Canada.createMRecord("jane", "doe", 36978, "jane.dow@example.com", new Project(new ProjectIdentifier(23), "Huge Project", "Rich Client"), Region.US.toString());
        assertEquals("Should only be two new records", startNumberOfRecords + 2, Canada.getRegionalRecordCount());
    }

    @Test
    public void canCreateEmployeeRecord() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        Canada.createERecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getRegionalRecordCount());
    }

    @Test
    public void canEditMangerRecords() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        String recordId = Canada.createMRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getRegionalRecordCount());

        Canada.editRecord(recordId, Feild.LOCATION.toString(), Region.UK.toString());
        Canada.editRecord(recordId, Feild.EMPLOYEE_ID.toString(), 98765);

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getRegionalRecordCount());
    }

    @Test
    public void canEditEmployeeRecords() throws Exception {

        int startNumberOfRecords = Canada.getRegionalRecordCount();
        String recordId = Canada.createERecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getRegionalRecordCount());

        Canada.editRecord(recordId, Feild.FIRST_NAME.toString(), "James");
        Canada.editRecord(recordId, Feild.LAST_NAME.toString(), "BOND");
        Canada.editRecord(recordId, Feild.EMPLOYEE_ID.toString(), 9007);

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getRegionalRecordCount());
    }

    @Test
    public void carConnectToMayOffices() throws Exception {
        RegionalClient US = new RegionalClient("US1234");
        RegionalClient UK = new RegionalClient("UK1234");

        US.getRegionalRecordCount();
        UK.getRegionalRecordCount();
    }

    @Test
    public void canCreateRecordsInAllRegions() throws Exception {

        RegionalClient US = new RegionalClient("US1234");
        RegionalClient UK = new RegionalClient("UK1234");

        int CanadianRecordCounter = Canada.getRegionalRecordCount();
        int AmerianRecordCounter = US.getRegionalRecordCount();
        int BritishRecordCounter = UK.getRegionalRecordCount();

        Canada.createERecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());

        UK.createMRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.UK.toString());
        assertEquals("Should only be one new record", ++BritishRecordCounter, UK.getRegionalRecordCount());
        assertEquals("Should not be a new record", CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());

        US.createMRecord("jane", "doe", 36978, "jane.dow@example.com", new Project(new ProjectIdentifier(23), "Huge Project", "Rich Client"), Region.US.toString());
        assertEquals("Should only be two new records", ++AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());
    }

    @Test
    public void canEditRecordsInAllRegions() throws Exception {

        RegionalClient US = new RegionalClient("US1234");
        RegionalClient UK = new RegionalClient("UK1234");

        int CanadianRecordCounter = Canada.getRegionalRecordCount();
        int AmerianRecordCounter = US.getRegionalRecordCount();
        int BritishRecordCounter = UK.getRegionalRecordCount();

        String CanadianEmployeeRecordId = Canada.createERecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());

        String BritishEmployeeRecordId = UK.createMRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.UK.toString());
        assertEquals("Should only be one new record", ++BritishRecordCounter, UK.getRegionalRecordCount());
        assertEquals("Should not be a new record", CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());

        assertEquals("Modified Record ID should match", BritishEmployeeRecordId, UK.editRecord(BritishEmployeeRecordId, Feild.FIRST_NAME.toString(), "James"));
        assertEquals("Modified Record ID should match", BritishEmployeeRecordId, UK.editRecord(BritishEmployeeRecordId, Feild.LAST_NAME.toString(), "BOND"));
        assertEquals("Modified Record ID should match", BritishEmployeeRecordId, UK.editRecord(BritishEmployeeRecordId, Feild.EMPLOYEE_ID.toString(), 9007));
        assertEquals("Should not be a new record", CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());

        Canada.createMRecord("jane", "doe", 36978, "jane.dow@example.com", new Project(new ProjectIdentifier(23), "Huge Project", "Rich Client"), Region.US.toString());
        assertEquals("Should only be two new records", ++CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());

        assertEquals("Modified Record ID should match", CanadianEmployeeRecordId, Canada.editRecord(CanadianEmployeeRecordId, Feild.FIRST_NAME.toString(), "James"));
        assertEquals("Modified Record ID should match", CanadianEmployeeRecordId, Canada.editRecord(CanadianEmployeeRecordId, Feild.LAST_NAME.toString(), "BOND"));
        assertEquals("Modified Record ID should match", CanadianEmployeeRecordId, Canada.editRecord(CanadianEmployeeRecordId, Feild.EMPLOYEE_ID.toString(), 9007));
        assertEquals("Should not be a new record", CanadianRecordCounter, Canada.getRegionalRecordCount());
        assertEquals("Should not be a new record", AmerianRecordCounter, US.getRegionalRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UK.getRegionalRecordCount());
    }
}
