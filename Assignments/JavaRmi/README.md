# Records Manament - DEMS - Java RMI
This project consists of three projects:
- Records Management { Models, Interface, Utility }
- Server { Implementation, Executable }
- Client { Implementation, Executable }

This is a pseudo MVC style layout since the main data storage objects will be re-used for the future assignments and project. This seperation also make it easy to divide the execution into seperate .jar

### Models
- Feild               // Indicats the possible feild names which can be edited
- Region              // Enum to handle the prefixing, name and port for any region
- ProjectIdentifier   // Contains all the formatting a project ID
- Project             // Contains a ProjectIdentifier and other information consisting of a project
- RecordType          // Enum to help with ID formatting
- RecordIdentifier    // Contains all the formatting and management for an ID
- Record              // Contains the RecordIdentifier and all the common feilds
- ManagerRecord       // Specialization for a managers record with all information
- EmployeeRecord      // Specialization for an employee record with all information
- RecordsMap          // Hashmap of linked list of records manages accessing all of them

### Interface
This section contains one interface for the RMI server and client to implement. It follows the standard proposal set forth for the class.

### Utility
Just a little file and console logging utilty to make this easier

## Server
This project handles all the implementation details for the server components varrying from UDP server-server communication and the RMI implementation. The servers use a stripped down custom protocol I developped at work; its and operation code and data segment seperated by a CRLF inspired by HTTP. 
- OperationCode      // Various operation performed between the servers
- Message            // Custom format of a message with translation to Datagram packet
- RegionalServer     // This is the RMI implementation of the interface on the server side and access all the specialized modules
- RequestListener    // UDP server to receive requests from other servers with a listener for callback to answer them
- RecordUuidTracker  // This component talks to the other servers to determine the next available ID; used when creating new records.

The main executable simply starts three regional server one for Canada, United-States, and the United-Kingdom.

## Client
This project has the RMI implemntation of the client, which only finds its remote counter part to call with the RMI Registry. There is also a JUnit test suite to exercise the basic functionality. The main executable has a boat load of logic to allow a user to perform the various operations for creation and editing of records.
