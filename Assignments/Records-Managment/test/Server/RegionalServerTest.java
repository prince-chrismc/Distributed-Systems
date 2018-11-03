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

import Models.Feild;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Region;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cmcarthur
 */
public class RegionalServerTest {

    static private RegionalServer Canada;
    static private RegionalServer UnitedStates;
    static private RegionalServer UnitedKingdom;

    public RegionalServerTest() {
    }

    @BeforeClass
    static public void setupRealRegionalServers() throws IOException {
        Canada = new RegionalServer(Region.CA);
        UnitedStates = new RegionalServer(Region.US);
        UnitedKingdom = new RegionalServer(Region.UK);

        Canada.Start();
        UnitedStates.Start();
        UnitedKingdom.Start();
    }

    @Test
    public void canCreateManagerRecord() throws Exception {
        int startNumberOfRecords = Canada.getCurrentRecordCount();
        Canada.createManagerRecord(null, "john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getCurrentRecordCount());
    }

    @Test
    public void canCreateEmployeeRecord() throws Exception {
        int startNumberOfRecords = Canada.getCurrentRecordCount();
        Canada.createEmployeeRecord(null, "james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getCurrentRecordCount());
    }

    @Test
    public void canEditMangerRecords() throws Exception {
        int startNumberOfRecords = Canada.getCurrentRecordCount();
        String recordId = Canada.createManagerRecord(null, "john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getCurrentRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.editRecord(null, recordId, Feild.LOCATION.toString(), Region.UK.toString()));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(null, recordId, Feild.EMPLOYEE_ID.toString(), 98765));

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getCurrentRecordCount());
    }

    @Test
    public void canEditEmployeeRecords() throws Exception {

        int startNumberOfRecords = Canada.getCurrentRecordCount();
        String recordId = Canada.createEmployeeRecord(null, "james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getCurrentRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.editRecord(null, recordId, Feild.FIRST_NAME.toString(), "James"));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(null, recordId, Feild.LAST_NAME.toString(), "BOND"));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(null, recordId, Feild.EMPLOYEE_ID.toString(), 9007));

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getCurrentRecordCount());
    }

    @Test
    public void canTransferMangerRecords() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();
        int BritishRecordCounter = UnitedKingdom.getCurrentRecordCount();

        String recordId = Canada.createManagerRecord(null, "john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.transferRecord(null, recordId, Region.UK.toString()));

        assertEquals("Should one less record", --CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should one more new record", ++BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());
    }

    @Test
    public void canTransferEmployeeRecords() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();
        int BritishRecordCounter = UnitedKingdom.getCurrentRecordCount();

        String recordId = Canada.createEmployeeRecord(null, "james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.transferRecord(null, recordId, Region.UK.toString()));

        assertEquals("Should one less record", --CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should one more new record", ++BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());
    }

    @Test
    public void transferToUnknowRemoteFails() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();

        String recordId = Canada.createManagerRecord(null, "john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getCurrentRecordCount());

        assertTrue("Should return ERROR message",
                Canada.transferRecord(null, recordId, "HELLO WORLD").startsWith("ERROR"));

        assertEquals("Should same number of records", CanadianRecordCounter, Canada.getCurrentRecordCount());
    }

    @Test
    public void transferToSameServerIsRejected() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();

        String recordId = Canada.createManagerRecord(null, "john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getCurrentRecordCount());

        assertEquals("Should return REJECTED! message", "REJECTED!",
                Canada.transferRecord(null, recordId, Region.CA.toString()));

        assertEquals("Should same number of records", CanadianRecordCounter, Canada.getCurrentRecordCount());
    }

    @Test
    public void transferNonExistantRecordFails() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();
        int BritishRecordCounter = UnitedKingdom.getCurrentRecordCount();

        assertTrue("Should return 404 error message",
                Canada.transferRecord(null, "REAL_BAD_RECORD_ID34523462456", Region.UK.toString()).startsWith("ERROR 404"));

        assertEquals("Should same number of records", CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());
    }

    @Test
    public void testPostTransferRecordNotFound() throws Exception {
        int CanadianRecordCounter = Canada.getCurrentRecordCount();
        int BritishRecordCounter = UnitedKingdom.getCurrentRecordCount();

        String recordId = Canada.createEmployeeRecord(null, "james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.transferRecord(null, recordId, Region.UK.toString()));

        assertEquals("Should one less record", --CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should one more new record", ++BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());
        
        assertTrue("Should return 404 error message",
                Canada.transferRecord(null, recordId, Region.UK.toString()).startsWith("ERROR 404"));
        
        assertEquals("Should same number of records", CanadianRecordCounter, Canada.getCurrentRecordCount());
        assertEquals("Should not be a new record", BritishRecordCounter, UnitedKingdom.getCurrentRecordCount());
    }
}
