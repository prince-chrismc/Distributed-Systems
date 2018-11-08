/*
 * The MIT License
 *
 * Copyright 2018 cmcarthur.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Server;

import Models.Project;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cmcarthur
 */
public class RegionalWebServerTest {

    public RegionalWebServerTest() {
    }

    /**
     * Test of createManagerRecord method, of class RegionalWebServer.
     */
    @Test
    public void testCreateManagerRecord() {
        String managerID = null;
        String firstName = "john";
        String lastName = "smith";
        int employID = 6548;
        String mailID = "john.smith@example.com";
        Project project = null;
        String location = "CA";
        RegionalWebServer instance = new RegionalWebServer();
        String expResult = "MR11111";
        String result = instance.createMRecord(managerID, firstName, lastName, employID, mailID, project, location);
        assertEquals(expResult, result);
    }

}
