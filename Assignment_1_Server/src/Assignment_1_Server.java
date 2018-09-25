/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


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
            LocateRegistry.createRegistry( 12345 );
        }
        catch( Exception e )
        {
            System.out.println( "   --> ERROR : Internal Server <--"  ); 
            e.printStackTrace();
        }
    }
    
}
