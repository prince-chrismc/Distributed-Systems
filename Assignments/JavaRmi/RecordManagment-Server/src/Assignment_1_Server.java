/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Models.Region;
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
            
            // https://stackoverflow.com/a/52552583/8480874
            Registry registry = LocateRegistry.createRegistry( 12345 );
            
            RegionalServer Canada = new RegionalServer(Region.CA);			   		   
	    registry.rebind(Canada.getUrl(), Canada);
 
	    System.out.println("Addition Server is ready.");
        }
        catch( Exception e )
        {
            System.out.println( "   --> ERROR : Internal Server <--"  ); 
            e.printStackTrace();
        }
    }
}
