/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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
     * This is a sample web service operation
     * @param txt
     * @return 
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     * @param parameter
     * @return 
     */
    @WebMethod(operationName = "createtest")
    public String createtest(@WebParam(name = "parameter") String parameter) {
         
        return "created " + parameter;
    }
}
