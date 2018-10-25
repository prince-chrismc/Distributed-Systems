/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Interface.Corba.DEMS.RegionalRecordManipulator;
import Interface.Corba.DEMS.RegionalRecordManipulatorHelper;
import Models.Region;
import Server.CorbaRegionalServer;
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

        try {
            // create and initialize the ORB //// get reference to rootpoa & activate the POAManager
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // LEt's get a reference to the NamingService of CORBA
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            
            // --------------------------------------------------------------------------------------------------------------

            
            // create servant and register it with the ORB
            CorbaRegionalServer Canada = new CorbaRegionalServer(Region.CA);
            Canada.setORB(orb);
            // get object reference from the servant
            org.omg.CORBA.Object refCanada = rootpoa.servant_to_reference(Canada);
            RegionalRecordManipulator CanadianHref = RegionalRecordManipulatorHelper.narrow(refCanada);
            // Create path and bind Canada reference
            NameComponent pathCanada[] = ncRef.to_name(Region.CA.toString());
            ncRef.rebind(pathCanada, CanadianHref);

            Canada.Start();
            System.out.println("Canada Server ready and waiting ...");
            
            
            // --------------------------------------------------------------------------------------------------------------
            
            // create servant and register it with the ORB
            CorbaRegionalServer UnitedStates = new CorbaRegionalServer(Region.US);
            UnitedStates.setORB(orb);
            // get object reference from the servant
            org.omg.CORBA.Object refUnitedStates = rootpoa.servant_to_reference(UnitedStates);
            RegionalRecordManipulator UnitedStatesHref = RegionalRecordManipulatorHelper.narrow(refUnitedStates);
            // Create path and bind United States reference
            NameComponent pathUnitedStates[] = ncRef.to_name(Region.US.toString());
            ncRef.rebind(pathUnitedStates, UnitedStatesHref);

            UnitedStates.Start();
            System.out.println("United States Server ready and waiting ...");
            
            // --------------------------------------------------------------------------------------------------------------

            
                        // create servant and register it with the ORB
            CorbaRegionalServer UnitedKingdom = new CorbaRegionalServer(Region.UK);
            UnitedKingdom.setORB(orb);
            // get object reference from the servant
            org.omg.CORBA.Object refUnitedKingdom = rootpoa.servant_to_reference(UnitedKingdom);
            RegionalRecordManipulator UnitedKingdomHref = RegionalRecordManipulatorHelper.narrow(refUnitedKingdom);
            // Create path and bind United Kingdom reference
            NameComponent pathUnitedKingdom[] = ncRef.to_name(Region.UK.toString());
            ncRef.rebind(pathUnitedKingdom, UnitedKingdomHref);

            UnitedKingdom.Start();
            System.out.println("United States Server ready and waiting ...");
            
                        // --------------------------------------------------------------------------------------------------------------

                        
                        
            // wait for invocations from clients
            for (;;) {
                orb.run();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Central Server Exiting ...");

    }

}
