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
package Models;

/**
 *
 * @author c_mcart
 */
public enum Region implements Interface.Region {
    CA(6000, "Canada", "CA"),
    US(6001, "United-States", "US"),
    UK(6002, "United-Kingdom", "UK");

    public static Region fromString(String location) throws Exception {

        for (Region region : Region.values()) {
            if ((region.getPrefix() == null ? location == null : region.getPrefix().equals(location))
                    || (region.toString() == null ? location == null : region.toString().equals(location))) {
                return region;
            }
        }

        throw new Exception("Invalid Location!");
    }

    private Region(int ID, String name, String prefix) {
        m_UUID = ID;
        m_Name = name;
        m_Prefix = prefix;
    }

    @Override
    public int toInt() {
        return m_UUID;
    }

    @Override
    public String toString() {
        return m_Name;
    }

    @Override
    public String getPrefix() {
        return m_Prefix;
    }

    private int m_UUID;
    private String m_Name;
    private String m_Prefix;

}
