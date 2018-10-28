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
import Models.Feild;
import Models.Region;
import Models.ProjectIdentifier;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 *
 * @author cmcarthur
 */
public class RegionalClientTest {

    static private ORB orb;

    private RegionalClient Canada;
    private RegionalClient UnitedStates;
    private RegionalClient UnitedKingdom;

    public RegionalClientTest() {
    }

    @BeforeClass
    static public void setupRegistry() throws AdapterInactive, InvalidName {
        String[] args = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
        orb = ORB.init(args, null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        Canada = new RegionalClient(orb, "CA1234");
        UnitedStates = new RegionalClient(orb, "US1234");
        UnitedKingdom = new RegionalClient(orb, "UK1234");
    }

    /**
     * Test of createManagerRecord method, of class RegionalClient.
     */
    @Test
    public void testCreateManagerRecord() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        String managerOne = Canada.createManagerRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0).getRawId(), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getRegionalRecordCount());
        String managerTwo = Canada.createManagerRecord("jane", "doe", 36978, "jane.dow@example.com", new Project(new ProjectIdentifier(23).getRawId(), "Huge Project", "Rich Client"), Region.US.toString());
        assertEquals("Should only be two new records", startNumberOfRecords + 2, Canada.getRegionalRecordCount());
        assertNotEquals("Manager IDs should be unique", managerOne, managerTwo);
    }

    /**
     * Test of createEmployeeRecord method, of class RegionalClient.
     */
    @Test
    public void testCreateEmployeeRecord() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        Canada.createEmployeeRecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", startNumberOfRecords + 1, Canada.getRegionalRecordCount());
    }

    /**
     * Test of editRecord method, of class RegionalClient.
     */
    @Test
    public void canEditMangerRecords() throws Exception {
        int startNumberOfRecords = Canada.getRegionalRecordCount();
        String recordId = Canada.createManagerRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0).getRawId(), "Huge Project", "Rich Client"), Region.CA.toString());
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getRegionalRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.editRecord(recordId, Feild.LOCATION.toString(), Region.UK.toString()));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(recordId, Feild.EMPLOYEE_ID.toString(), 98765));

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getRegionalRecordCount());
    }

    @Test
    public void canEditEmployeeRecords() throws Exception {

        int startNumberOfRecords = Canada.getRegionalRecordCount();
        String recordId = Canada.createEmployeeRecord("james", "bond", 1001, "johm.smith@example.com", "P23001");
        assertEquals("Should only be one new record", ++startNumberOfRecords, Canada.getRegionalRecordCount());

        assertEquals("Record ID should not change", recordId, Canada.editRecord(recordId, Feild.FIRST_NAME.toString(), "James"));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(recordId, Feild.LAST_NAME.toString(), "BOND"));
        assertEquals("Record ID should not change", recordId, Canada.editRecord(recordId, Feild.EMPLOYEE_ID.toString(), 9007));

        assertEquals("Should only be same number of records", startNumberOfRecords, Canada.getRegionalRecordCount());
    }
}
