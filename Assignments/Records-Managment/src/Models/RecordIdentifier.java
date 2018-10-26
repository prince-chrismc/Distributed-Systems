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
public class RecordIdentifier {

    public static RecordIdentifier fromString(String data) throws Exception {
        if (data.length() != 7) {
            throw new Exception("Invalid Record ID!");
        }

        RecordType type = RecordType.fromString(data.substring(0, 2));

        if (type != RecordType.EMPLOYEE
                && type != RecordType.MANAGER) {
            throw new Exception("Invalid Record type!");
        }

        int uuid = Integer.parseInt(data.substring(2));
        if (uuid > 99999) {
            throw new Exception("Invalid Record UUID!");
        }

        return new RecordIdentifier(type, uuid);
    }

    public RecordIdentifier(RecordType type, int uuid) {
        m_Type = type;
        m_UUID = (uuid <= 99999) ? uuid : -1;
    }

    @Override
    public String toString() {
        return m_Type.toString() + String.format("%05d", m_UUID);
    }

    public RecordType getType() {
        return m_Type;
    }

    public int getUUID() {
        return m_UUID;
    }

    private final RecordType m_Type;
    private final int m_UUID;
}
