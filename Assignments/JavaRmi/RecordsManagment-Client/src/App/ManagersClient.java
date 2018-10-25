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
package App;

import Client.RegionalClient;
import Models.Project;
import Models.ProjectIdentifier;
import Models.Region;
import Models.Feild;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 *
 * @author c_mcart
 */
public class ManagersClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Christopher McArthur's Distributed Employee Management System\r\n");

        System.out.println("DEMS Login...");

        boolean loginIsValid = true;
        String userLogin;
        Scanner reader = new Scanner(System.in);

        do {
            System.out.print("Please enter your 'Human Resources Manager ID': ");
            userLogin = reader.nextLine();

            if (userLogin.length() != 6) {
                System.out.println("Invalid ID length. Format region code + 4 digits. Ex: 'CA1111'");
                loginIsValid = false;
            }

            try {
                Region.fromString(userLogin.substring(0, 2));
            } catch (Exception ex) {
                System.out.println("Invalid ID region code. Available { CA, US, UK }. Ex: 'CA1111'");
                loginIsValid = false;
            }

        } while (!loginIsValid);

        RegionalClient client = null;
        try {
            client = new RegionalClient(userLogin);
        } catch (Exception ex) {
            System.out.println("Unable to establish connection! Please try again later...");
            System.exit(-1);
        }

        try {
            boolean userRequestedExit = false;
            do {
                System.out.println("What operation would you like to perform?");
                System.out.println("   1. Create manager record");
                System.out.println("   2. Create employee record");
                System.out.println("   3. Edit an existing record");
                System.out.println("   4. Display total number of records");
                System.out.println("   5. Exit");
                System.out.print("Selection: ");

                int userSelection = reader.nextInt();

                switch (userSelection) {
                    case 1:
                        createManagerRecord(client);
                        break;
                    case 2:
                        createEmployeeRecord(client);
                        break;
                    case 3:
                        editExistingRecord(client);
                        break;
                    case 4:
                        System.out.println(client.getRecordCount());
                        break;
                    case 5:
                        System.out.println("Good-bye!");
                        userRequestedExit = true;
                        break;
                    default:
                        System.out.println("Invalid selection");
                        break;
                }

            } while (!userRequestedExit);

        } catch (Exception ex) {
            System.out.println("Unable to perform requested operation! System failure! Please try again later...");
            System.exit(-1);
        }
    }

    private static void createManagerRecord(RegionalClient client) throws RemoteException, Exception {
        Scanner reader = new Scanner(System.in);

        System.out.println("To create a new manager record certain information is required...");

        System.out.println("First Name:");
        String firstName = reader.nextLine();

        System.out.println("Last Name:");
        String lastName = reader.nextLine();

        System.out.println("Employee ID:");
        int employeeId = reader.nextInt();
        reader.nextLine();
        
        System.out.println("Email:");
        String mailId = reader.nextLine();

        System.out.println("Project ID: (5 digits)");
        int projectId = reader.nextInt();
        reader.nextLine();

        System.out.println("Project Name:");
        String projectName = reader.nextLine();

        System.out.println("Project Client:");
        String projectClient = reader.nextLine();

        System.out.println("Location:");
        String location = reader.nextLine();

        String newRecordId = client.createMRecord(firstName, lastName, employeeId, mailId, new Project(
                new ProjectIdentifier(projectId), projectName, projectClient), location);

        System.out.println("Successfully created record: " + newRecordId);

    }

    private static void createEmployeeRecord(RegionalClient client) throws RemoteException {
        Scanner reader = new Scanner(System.in);

        System.out.println("To create a new employee record certain information is required...");

        System.out.println("First Name:");
        String firstName = reader.nextLine();

        System.out.println("Last Name:");
        String lastName = reader.nextLine();

        System.out.println("Employee ID:");
        int employeeId = reader.nextInt();
        reader.nextLine();

        System.out.println("Email:");
        String mailId = reader.nextLine();

        System.out.println("Project ID: (P#####) ");
        String projectId = reader.nextLine();

        String newRecordId = client.createERecord(firstName, lastName, employeeId, mailId, projectId);

        System.out.println("Successfully created record: " + newRecordId);

    }

    private static void editExistingRecord(RegionalClient client) throws RemoteException {
        Scanner reader = new Scanner(System.in);

        System.out.println("To create a new employee record certain information is required...");
        System.out.print("   Possible feild values are { ");
        for (Feild feild : Feild.values()) {
            System.out.print(feild.toString() + ", ");
        }
        System.out.println(" }");

        System.out.println("Record ID:");
        String recordId = reader.nextLine();

        System.out.println("Feild Name:");
        String feildName = reader.nextLine();

        Object newValue;
        System.out.println("Enter value for " + feildName + ":");
        if (feildName.equals("employee id")) {
            newValue = reader.nextInt();
            reader.nextLine();
        } else {
            newValue = reader.nextLine();
        }

        String newRecordId = client.editRecord(recordId, feildName, newValue);

        System.out.println("Successfully edited record: " + newRecordId);
    }
}
