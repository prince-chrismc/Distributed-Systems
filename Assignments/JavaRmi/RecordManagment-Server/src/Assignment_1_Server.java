/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Models.Region;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

import Server.RegionalServer;


/**
 *
 * @author c_mcart
 */
public class Assignment_1_Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try
        {
            String PolicySettingsFileLocation = "file:" + System.getProperty("user.dir") + "/security.policy";            
            System.setProperty("java.security.policy", PolicySettingsFileLocation);
            
            LocateRegistry.createRegistry( 12345 );
            
            RegionalServer Canada = new RegionalServer(Region.CA);			   		   
	    Naming.rebind(Canada.getUrl(), Canada);
 
	    System.out.println("Addition Server is ready.");
        }
        catch( Exception e )
        {
            System.out.println( "   --> ERROR : Internal Server <--"  ); 
            e.printStackTrace();
        }
    }
    
}
