## Lecture Three Material

- Local Versus Remote Calls
   - Caller --> Called
   - Caller -- Stub --> Transport Layer --> Skeleton -- Called
   - Lots more steps in a remote call !
- remote object reference
   - uniquely IDs each object
   - remote interface defines public API of object
- Stub/Skeleton // sperate IPC communication from function implementation process channel server and service
   - marshalling
   - synchronization

### Java RMI Architecture
- Client Object --> Directory Service
- Client Object --> Interface of Server Object
- client/server have middleware layer and transpoer layer to cross
- Naming: defines a URL at which the remote object can be found // similar idea to DNS

##### RMI vs Socket API:
- socket API is 'closer' to the OS offering less overhead and more performance with a higher program complexity
- RMI API is a high level abstract reducing program complexity at a lower efficiency

##### Server Multithreading
- Single Thread / Iterative Server: one read thread, handles incomming requests sequential in order, blocking
   - idling, complex RPC implementation, 
- Multhi-threaded / Concurrent Server: one thread per connection, handled independently
   - must design mutual exclusion between threads, handle concurrency problems and data consistency, larger overhead
- Upcalls / Event: One thread per request, everything independent and stateless

- Thread/Request === UDP Communication
- Thread/Connection === TCP Communication
- Thread/Object === Sequential Service/Object

##### Performance
- marshalling
- operation despatch
- data copy { app --> kernel --> network... and back }
- context switching threads
- protocol bandwidth
- network enviroment

### Communication Methods
- Callback
- Polling
- Two way communication

###### RMI Callback
- Server __AND__ Client interface

// Works like ProcessChannel
