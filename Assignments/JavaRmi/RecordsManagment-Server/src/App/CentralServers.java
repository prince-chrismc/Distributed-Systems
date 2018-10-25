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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Models.Region;
import Rmi.Server.RmiRegionalServer;
import java.io.IOException;

/**
 *
 * @author c_mcart
 */
public class CentralServers {

    public static void main(String[] args) {
        try {
            // https://stackoverflow.com/a/52552583/8480874
            Registry registry = LocateRegistry.createRegistry(12345);

            RmiRegionalServer Canada = new RmiRegionalServer(Region.CA);
            registry.rebind(Canada.getUrl(), Canada);
            Canada.Start();

            RmiRegionalServer UnitedStates = new RmiRegionalServer(Region.US);
            registry.rebind(UnitedStates.getUrl(), UnitedStates);
            UnitedStates.Start();

            RmiRegionalServer UnitedKingdom = new RmiRegionalServer(Region.UK);
            registry.rebind(UnitedKingdom.getUrl(), UnitedKingdom);
            UnitedKingdom.Start();

        } catch (IOException e) {
            System.out.println("   --> ERROR : Internal Server <--");
            System.out.println(e);
        }
    }
}
