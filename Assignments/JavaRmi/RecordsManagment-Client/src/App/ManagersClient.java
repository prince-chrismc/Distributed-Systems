package App;

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
import Client.RegionalClient;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Region;

/**
 *
 * @author c_mcart
 */
public class ManagersClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Welcome to Christopher McArthur's Distributed Employee Management System
        // DEMS Login...
        // Human Resource Manager ID:
        ///// CA1111
        // ID.size == 6 ~~> Try agains!
        // switch ( ID.prefix ) --> new RegionalClient for that region
        //                      ~~> try again!
        // Perform Operation { Create... Edit... Get... Exit} While ! Exit
        try {
            RegionalClient Canada = new RegionalClient("CA1234");

            System.out.println("Server Has: " + Canada.getRecordCount() + " Records.");

            Canada.createMRecord("john", "smith", 1001, "johm.smith@example.com", new Project(new ProjectIdentifier(0), "Huge Project", "Rich Client"), Region.CA.toString());

            Canada.createMRecord("jane", "doe", 36978, "jane.dow@example.com", new Project(new ProjectIdentifier(23), "Huge Project", "Rich Client"), Region.US.toString());

            Canada.createERecord("james", "bond", 1001, "johm.smith@example.com", "P23001");

            System.out.println("Server Has: " + Canada.getRecordCount() + " Records.");

        } catch (Exception e) {
            System.out.println("   --> ERROR : Internal Client <--");
            e.printStackTrace();
        }
    }
}
