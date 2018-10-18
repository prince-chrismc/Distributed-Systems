## Lecture Two Material
Basic overview of networking...

- Interprocess Communication
   - facilities provided by OS
   - high level abstraction offered
- Terminology
- network components
- routing in a WAN
   - routing table
- unreliable
- Internet protocols
   - uni,multi,any cast
   - addresses
   - port number
- UDP
- TCP
- OSI Stack ( 7 Layers )
- IP Layers...
- Sockets


#### Remote Procedure Calls using Sockets
- Request-Reply // How process channel works
- building stage // how center work ( inital setup ) client reading port
- data in message _marshalling_ and _unmarshalling_ // socket message builder/parse functions
- Semanting
   - executed <= 1 : at most once
   - executed == 1 : exactly once // Not possible!
   - executed >= 1 : at least once
- synchronous or asynchronous
