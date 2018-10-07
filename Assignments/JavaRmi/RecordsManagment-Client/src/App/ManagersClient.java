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
import Models.Region;
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
            System.out.println("Please enter your 'Human Resources Manager ID': ");
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
            do{
                System.out.println("What operation would you like to perform?");
                System.out.println("   1. Create manager record");
                System.out.println("   2. Create employee record");
                System.out.println("   3. Edit an existing record");
                System.out.println("   4. Display total number of records");
                System.out.println("   5. Exit");
                System.out.println("Selection: ");
                
                int userSelection = reader.nextInt();
                
                switch( userSelection ){
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
                        System.out.println( client.getRecordCount() );
                        break;
                    case 5:
                        System.out.println( "Good-bye!" );
                        userRequestedExit = true;
                        break;
                    default:
                        System.out.println( "Invalid selection" );
                        break;
                }

            }while(!userRequestedExit);
            
        } catch (Exception ex) {
            System.out.println("Unable to perform requested operation! System failure! Please try again later...");
            System.exit(-1);
        }
    }

    private static void createManagerRecord(RegionalClient client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void createEmployeeRecord(RegionalClient client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void editExistingRecord(RegionalClient client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
