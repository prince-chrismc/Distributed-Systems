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
import Models.Region;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author cmcarthur
 */
@WebService(serviceName = "CentralServer")
public class CentralServer {

    private final RegionalServer Canada;
    private final RegionalServer UnitedStates;
    private final RegionalServer UnitedKingdom;

    public CentralServer() throws IOException {
        this.Canada = new RegionalServer(Region.CA);
        Canada.Start();

        this.UnitedStates = new RegionalServer(Region.US);
        UnitedStates.Start();

        this.UnitedKingdom = new RegionalServer(Region.UK);
        UnitedKingdom.Start();
    }

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
    @WebMethod(operationName = "createMRecord")
    public String createMRecord(@WebParam(name = "managerID") String managerID,
            @WebParam(name = "firstName") String firstName,
            @WebParam(name = "lastName") String lastName,
            @WebParam(name = "employID") int employID,
            @WebParam(name = "mailID") String mailID,
            @WebParam(name = "project") Object project,
            @WebParam(name = "location") String location) {
        Region region;
        try {
            region = Region.fromString(managerID.substring(0,2));
        } catch (Exception ex) {
            return "ERROR: Invalid manager id: " + ex.getMessage();
        }
        
        switch(region)
        {
            case CA: return Canada.createManagerRecord(managerID, firstName, lastName, employID, mailID, (Project) project, location);
            case US: return UnitedStates.createManagerRecord(managerID, firstName, lastName, employID, mailID, (Project) project, location);
            case UK: return UnitedKingdom.createManagerRecord(managerID, firstName, lastName, employID, mailID, (Project) project, location);
            default:
                break;
        }
        
        return "ERROR";
    }

    @WebMethod(operationName = "createERecord")
    public String createERecord(@WebParam(name = "managerID") String managerID,
            @WebParam(name = "firstName") String firstName,
            @WebParam(name = "lastName") String lastName,
            @WebParam(name = "employID") int employID,
            @WebParam(name = "mailID") String mailID,
            @WebParam(name = "projectId") String projectId) {
        Region region;
        try {
            region = Region.fromString(managerID.substring(0,2));
        } catch (Exception ex) {
            return "ERROR: Invalid manager id: " + ex.getMessage();
        }
        
        switch(region)
        {
            case CA: return Canada.createEmployeeRecord(managerID, firstName, lastName, employID, mailID, projectId);
            case US: return UnitedStates.createEmployeeRecord(managerID, firstName, lastName, employID, mailID, projectId);
            case UK: return UnitedKingdom.createEmployeeRecord(managerID, firstName, lastName, employID, mailID, projectId);
            default:
                break;
        }
        
        return "ERROR";
    }

    @WebMethod(operationName = "editRecord")
    public String editRecord(@WebParam(name = "managerID") String managerID,
            @WebParam(name = "recordID") String recordID,
            @WebParam(name = "feildName") String feildName,
            @WebParam(name = "newValue") Object newValue) {
        Region region;
        try {
            region = Region.fromString(managerID.substring(0,2));
        } catch (Exception ex) {
            return "ERROR: Invalid manager id: " + ex.getMessage();
        }
        
        switch(region)
        {
            case CA: return Canada.editRecord(managerID, recordID, feildName, newValue);
            case US: return UnitedStates.editRecord(managerID, recordID, feildName, newValue);
            case UK: return UnitedKingdom.editRecord(managerID, recordID, feildName, newValue);
            default:
                break;
        }
        
        return "ERROR";
    }

    @WebMethod(operationName = "transferRecord")
    public String transferRecord(@WebParam(name = "managerID") String managerID,
            @WebParam(name = "recordID") String recordID,
            @WebParam(name = "region") String region) {
        Region managerRegion;
        try {
            managerRegion = Region.fromString(managerID.substring(0,2));
        } catch (Exception ex) {
            return "ERROR: Invalid manager id: " + ex.getMessage();
        }
        
        switch(managerRegion)
        {
            case CA: return Canada.transferRecord(managerID, recordID, region);
            case US: return UnitedStates.transferRecord(managerID, recordID, region);
            case UK: return UnitedKingdom.transferRecord(managerID, recordID, region);
            default:
                break;
        }
        
        return "ERROR";
    }

    @WebMethod(operationName = "getRecordCount")
    public String getRecordCount(@WebParam(name = "managerID") String managerID) {
        Region region;
        try {
            region = Region.fromString(managerID.substring(0,2));
        } catch (Exception ex) {
            return "ERROR: Invalid manager id: " + ex.getMessage();
        }
        
        switch(region)
        {
            case CA: return Canada.getRecordCount(managerID);
            case US: return UnitedStates.getRecordCount(managerID);
            case UK: return UnitedKingdom.getRecordCount(managerID);
            default:
                break;
        }
        
        return "ERROR";
    }
}
