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
package Server;

import Models.Region;
import Corba.Server.CorbaRegionalServer;
import Interface.Corba.DEMS.RegionalRecordManipulator;
import Interface.Corba.DEMS.RegionalRecordManipulatorHelper;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 *
 * @author cmcarthur
 */
public class RegionalServerTest {

    static private ORB orb;
    static private POA rootpoa;
    static private NamingContextExt ncRef;

    public RegionalServerTest() {
    }

    @BeforeClass
    static public void setupRegistry() throws AdapterInactive, InvalidName {
        String[] args = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
        orb = ORB.init(args, null);

        rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootpoa.the_POAManager().activate();

        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        ncRef = NamingContextExtHelper.narrow(objRef);
    }

    @Test
    public void canLaunchServer() throws Exception {
        CorbaRegionalServer Canada = new CorbaRegionalServer(Region.CA);
        Canada.setORB(orb);
        // get object reference from the servant
        org.omg.CORBA.Object refCanada = rootpoa.servant_to_reference(Canada);
        Interface.Corba.DEMS.RegionalRecordManipulator CanadianHref = RegionalRecordManipulatorHelper.narrow(refCanada);
        // Create path and bind Canada reference
        NameComponent pathCanada[] = ncRef.to_name(Region.CA.toString());
        ncRef.rebind(pathCanada, CanadianHref);

        Canada.Start();
    }

    @Test
    public void canLookupServer() throws Exception {
        CorbaRegionalServer Canada = new CorbaRegionalServer(Region.CA);
        Canada.setORB(orb);
        // get object reference from the servant
        org.omg.CORBA.Object refCanada = rootpoa.servant_to_reference(Canada);
        Interface.Corba.DEMS.RegionalRecordManipulator CanadianHref = RegionalRecordManipulatorHelper.narrow(refCanada);
        // Create path and bind Canada reference
        NameComponent pathCanada[] = ncRef.to_name(Region.CA.toString());
        ncRef.rebind(pathCanada, CanadianHref);

        RegionalRecordManipulator Remote = (RegionalRecordManipulator) RegionalRecordManipulatorHelper.narrow(ncRef.resolve_str(Region.CA.toString()));

        assertNotEquals("Remote Interface must be obtained", null, Remote);
    }
}
