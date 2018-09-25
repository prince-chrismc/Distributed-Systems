/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author c_mcart
 */
public enum Region {
    CA(6000, "Canada", "CA"),
    US(6001, "United-States", "US"),
    UK(6002, "United-Kingdom", "UK");

    private Region(int ID, String name, String prefix)
    {
        m_UUID = ID;
        m_Name = name;
        m_Prefix = prefix;
    }
    
    public int toInt() { return m_UUID; }
    public String toString() { return m_Name; }
    public String getPrefix() { return m_Prefix; }
    
    private int    m_UUID;
    private String m_Name;
    private String m_Prefix;

}
