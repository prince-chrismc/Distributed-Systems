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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author cmcarthur
 */
@Path("regionalwebserverport")
public class RegionalWebServerPort {

    private Servee_client.RegionalWebServer port;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RegionalWebServerPort
     */
    public RegionalWebServerPort() {
        port = getPort();
    }

    /**
     * Invokes the SOAP method createtest
     * @param parameter resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("createtest/")
    public String getCreatetest(@QueryParam("parameter") String parameter) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.createtest(parameter);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     * Invokes the SOAP method hello
     * @param name resource URI parameter
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("hello/")
    public String getHello(@QueryParam("name") String name) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.String result = port.hello(name);
                return result;
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     *
     */
    private Servee_client.RegionalWebServer getPort() {
        try {
            // Call Web Service Operation
            Servee_client.RegionalWebServer_Service service = new Servee_client.RegionalWebServer_Service();
            Servee_client.RegionalWebServer p = service.getRegionalWebServerPort();
            return p;
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }
}
