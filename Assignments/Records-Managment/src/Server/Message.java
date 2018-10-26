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

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 *
 * @author cmcarthur
 */
public class Message {

    /*
    
    Simple message definition for server-server communication
    inspired by HTTP:
        OperationCode + "\r\n" + { data }   
    
     */
    private OperationCode m_Code;
    private String m_Data;
    InetAddress m_Addr;
    int m_Port;

    public Message(DatagramPacket packet) {
        
        String payload = new String(packet.getData(), 0, packet.getLength());
        
        this.m_Code = OperationCode.fromString( payload.substring(0, payload.indexOf("\r\n")) );
        this.m_Data = payload.substring(payload.indexOf("\r\n") + 2 );
        this.m_Addr = packet.getAddress();
        this.m_Port = packet.getPort();
    }

    public Message(OperationCode code, String data, InetAddress addr, int port) {
        this.m_Code = code;
        this.m_Data = data;
        this.m_Addr = addr;
        this.m_Port = port;
    }

    public DatagramPacket getPacket() {
        String payload = m_Code.toString() + "\r\n" + m_Data;
        return new DatagramPacket(payload.getBytes(), payload.length(), m_Addr, m_Port);
    }
    
    public String getData(){
        return m_Data;
    }

    public OperationCode getOpCode() {
        return m_Code;
    }

    @Override
    public String toString() {
        return "Message{" + "code=" + m_Code + ", data=" + m_Data + ", addr=" + m_Addr + ", port=" + m_Port + '}';
    }
}
