/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Models.Region;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author c_mcart
 */
public class RegionalServer extends UnicastRemoteObject implements RegionalRecordManager {

    public RegionalServer(Region region) throws RemoteException  {
        super();
        m_Region = region;
    }
    
    public String getUrl(){
        return "rmi://localhost/" + m_Region.toString();
    }
    
    @Override
    public int getRecordCount() throws RemoteException {
        return 2;
    }
    
    private Region m_Region;
}
