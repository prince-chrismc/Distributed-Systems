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

import Interface.Corba.DEMS.RegionalRecordManipulator;
import Interface.Corba.DEMS.RegionalRecordManipulatorHelper;
import Models.Region;
import Corba.Server.CorbaRegionalServer;
import java.util.stream.Stream;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

/**
 *
 * @author cmcarthur
 */
public class CentralServers {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if(args.length < 4 ) {
            args = Stream.of("-ORBInitialPort", "1050", "-ORBInitialHost", "localhost").toArray(String[]::new);
        }
                
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
            System.out.println(" --> Internal Server ERROR: ");
            System.out.println(e);
        }

        System.out.println("Central Server Exiting ...");
    }
}
