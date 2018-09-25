/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Models.Region;
import java.rmi.RemoteException;

/**
 *
 * @author c_mcart
 */
public class RegionalServer implements RegionalRecordManager {

    public RegionalServer(Region region) {
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
