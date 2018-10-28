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

/**
 *
 * @author cmcarthur
 */
public enum TestRegion implements Interface.Region {
    ZERO(7000),
    ONE(7001),
    TWO(7002),
    THREE(7003),
    FOUR(7004),
    FIVE(7005),
    SIX(7006),
    SEVEN(7007),
    EIGTH(7008),
    NINE(7009),
    TEN(7010),
    ELEVEN(7011);
    
    TestRegion(int ID ) {
        m_UUID = ID;
    }

    @Override
    public int toInt() {
        return m_UUID;
    }


    @Override
    public String getPrefix() {
        return toString();
    }

    private int m_UUID;
}
