# Records Manament - DEMS - CORBA
This assignment consists of three projects:
- Records Management { Models, Interface, Server, Utility }
- Server { Wrapper, Executable }
- Client { Implementation, Executable }

The deisgn follows a pseudo MVC style layout since the main data storage elements will be re-used for the future assignments; this concept was extended to the `RegionalServer` implmentation which has now been moved into the base project since all ofthe manipulation of the `RecordsMap` and Server-Server UDP communication is common but may require more functionality. This means the controllers are simple wrapper that only contain the CORBA implementation and test code.

### Models
> All of the models defined are still used in the RMI implementation with zero code duplication

Class | Description
:---: | :---
Feild             | Indicats the possible feild names which can be edited
Region            | Enum to handle the prefixing, name and port for any region
ProjectIdentifier<sup>*</sup> | Contains all the formatting of a project ID
Project<sup>*</sup>           | Contains a ProjectIdentifier and other information to define a project
RecordType        | Enum to help with ID formatting and identification
RecordIdentifier  | Contains all the formatting and management for an ID
Record            | Contains the RecordIdentifier and all the required feilds to record a human
ManagerRecord     | Specialization for a managers record with all information
EmployeeRecord    | Specialization for an employee record with all information
RecordsMap        | Hashmap of linked list of records with access control

><sup>*</sup> - _Wraps CORBA Object_ // No longer directly contains the data structures but is still resposible for all the formatting

### Interface
The interface definitions are present in this category, written in IDL and compiled into java; The Corba defined implementation for the various stubs, helpers, POAs can also be found in this section as they are shared by both the client and server implementations.

### Utility
Just a little file and console logging utilty to make this easier

> Nothing has changed from the RMI implementation with zero code duplication

### Server ( Common )
This package handles all the implementation details for the server for the UDP server-server communication. The servers use a stripped down custom protocol I developped at work; its and operation code and data segment seperated by a CRLF inspired by HTTP. 

Class | Description
:---: | :---
OperationCode  | Various operation performed between the servers
Message | Custom format of a message with translation to Datagram packet
RegionalServer<sup>1</sup> | This is the RMI implementation of the interface on the server side and access all the specialized modules
RequestListener<sup>1</sup> | UDP server to receive requests from other servers with a listener for callback to answer them
RecordUuidTracker | This component talks to the other servers to determine the next available ID; used when creating new records.
RecordTransferAgent<sup>2</sup> | Manages all the stages required for transfering a record.

><sup>1</sup> - Upgraded to support new features
>
><sup>2</sup> - New Module

##### `OperationCode` and `Message`
Together these are used to define the implement the simple "Send and Wait" protocol providing an at least once execution of requests.

##### `RequestListener`
Main UDP server which waits for incomming messages from the other servers. Defines a callback interface to obtain the data for the responses from an owner of a `RecordsMap`. This only handles UDP communication. The interface is implemented by `RegionalServer`.

##### `RecordUuidTracker`
This component tracks the "next ID to use" for _Employees_ or _Managers_; when creating a new record it initiates communication with the other servers to agree upon the next ID to use.

##### `RecordTransferAgent`
Thid object is responsible for transfering a record to a remote server. It ensures the record does not exist, attempts the transfer and validates the completion of the transfer.

##### `RegionalServer`
this is the main Server object which was two main roles:
- Control a `RecordsMap` to garauntees data consistency with many concurrent clients/requests
- Perform communication with the other servers. Mostly done using the objects above; only `getRecordCount()` is implemented in this class.

## Server
The main executable simply starts three `RegionalServer` one for each region { Canada, United-States, and the United-Kingdom } and binds them within CORBA to make them available.

## Client
This project has the RMI implemntation of the client, which only finds its remote counter part to call using the CORBA Naming Service. The main executable contains the control logic to allows a _Human Resource Manager_ to accessthe system and to provide the information required to perform the various operations.
   1. Create manager record
   2. Create employee record
   3. Edit an existing record
   4. Transfer record to remote location
   5. Display total number of records
