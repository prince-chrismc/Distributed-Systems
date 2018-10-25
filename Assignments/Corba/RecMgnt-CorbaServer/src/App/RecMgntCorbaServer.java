/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Interface.Corba.DEMS.RegionalRecordManipulator;
import Interface.Corba.DEMS.RegionalRecordManipulatorHelper;
import Server.ServerObject;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

/**
 *
 * @author cmcarthur
 */
public class RecMgntCorbaServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
            try{
      // create and initialize the ORB //// get reference to rootpoa &amp; activate the POAManager
      ORB orb = ORB.init(args, null);      
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();
 
      // create servant and register it with the ORB
      ServerObject addobj = new ServerObject();
      addobj.setORB(orb); 
 
      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(addobj);
      RegionalRecordManipulator href = RegionalRecordManipulatorHelper.narrow(ref);
 
      org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
      NameComponent path[] = ncRef.to_name( "ABC" );
      ncRef.rebind(path, href);
 
      System.out.println("Addition Server ready and waiting ...");
 
      // wait for invocations from clients
      for (;;){
	  orb.run();
      }
    } 
 
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
 
      System.out.println("HelloServer Exiting ...");
 
    }
    
}
