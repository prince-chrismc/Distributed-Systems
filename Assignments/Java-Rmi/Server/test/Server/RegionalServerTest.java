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

import Interface.Rmi.RegionalRecordManipulator;
import Models.Region;
import Rmi.Server.RmiRegionalServer;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cmcarthur
 */
public class RegionalServerTest {

    static private Registry registry;

    public RegionalServerTest() {
    }

    @BeforeClass
    static public void setupRegistry() throws RemoteException {
        registry = LocateRegistry.createRegistry(12345);
    }

    @Test
    public void canLaunchServer() throws RemoteException, IOException {
        RmiRegionalServer UnitedStates = new RmiRegionalServer(Region.US);
        registry.rebind("canLaunchServer", UnitedStates);
        UnitedStates.Start();
    }

    @Test
    public void canLookupServer() throws RemoteException, NotBoundException, IOException {
        RmiRegionalServer Canada = new RmiRegionalServer(Region.CA);
        registry.rebind(Canada.getUrl(), Canada);

        RegionalRecordManipulator Remote = (RegionalRecordManipulator) registry.lookup("rmi://localhost/" + Region.CA.toString());

        assertNotEquals("Remote Interface must be obtained", null, Remote);
    }
}
