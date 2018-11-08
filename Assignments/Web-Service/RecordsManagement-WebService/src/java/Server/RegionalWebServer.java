/*
The MIT License

Copyright 2018 Chris McArthur, prince.chrismc(at)gmail(dot)com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package Server;

import Models.Project;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author cmcarthur
 */
@WebService(serviceName = "RegionalWebServer")
public class RegionalWebServer {

    /**
     * DEMS - Web service - operation to create a new manager record
     *
     * @param managerID the login credentials of the HR manger entering the
     * record
     * @param firstName first name of the new manager
     * @param lastName last name of the new manager
     * @param employID employee id of the new manager
     * @param mailID email of the new manager
     * @param project project of the new manager
     * @param location location of the new manager
     * @return the record ID when successful or an ERROR message
     */
    @WebMethod(operationName = "createManagerRecord")
    public String createManagerRecord(@WebParam(name = "managerID") String managerID, @WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName, @WebParam(name = "employID") int employID, @WebParam(name = "mailID") String mailID, @WebParam(name = "project") Project project, @WebParam(name = "location") String location) {
        //TODO write your implementation code here:
        return "MR11111";
    }
}
