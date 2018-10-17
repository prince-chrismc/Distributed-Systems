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
 * @author cmcarthur
 */
public enum Feild {
    FIRST_NAME("first name"),
    LAST_NAME("last name"),
    EMPLOYEE_ID("employee id"),
    MAIL_ID("mail id"),
    PROJECT_ID("project id"),
    PROJECT_NAME("project name"),
    PROJECT_CLIENT("project client"),
    LOCATION("location");

    private String m_Name;

    private Feild(String name) {
        m_Name = name;
    }

    @Override
    public String toString() {
        return m_Name;
    }

    public static Feild fromString(String name) throws Exception {

        for (Feild feild : Feild.values()) {
            if (feild.toString().equals(name)) {
                return feild;
            }
        }

        throw new Exception("Invalid Feild!");
    }
}
