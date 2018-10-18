## Lecture One Material

#### What Is a Distributed System?
a collection of autonomous hosts that that are connected through a computer network. Each _host executes components_ and operates a distribution middleware, which enables the components to coordinate their activities in such a way that users **perceive the system as a single, integrated computing facility**

#### Requirements to being a Distributed System:
- Concurrency
   - components can execute in different processes
   - components access shared resources
   - should not breach data consistency
- Resource Sharing
   - can run on any hawrdware/software in any system
   - Manager controls access, naming, concurrency
   - model describing various interactions
- Openness
   - ability to add extensions and improvements
   - defined interface
   - backwards compatability
   - resolve data representation differences
- Scalabilty
   - systems able to accommodate more users or provide the service 'faster'
   - componenst do not change ( simply add/remove them )
- Fault Tolerence
   - Survive hardware, software or network fails
   - must maintain service
   - obtained through recover and redundancy
- High Availability
   - Zero down-time
- Transparency
   - Hide resource distributrion
   - perceived by user as single application
   - Covers { Access, Location, Migration, Relocation, Replication, Concurrency, Failure, Persistence }

#### What problems are solved?
- Connect users to remote resources
- hide distribution
- define integration/usuage/syntax
- offers scalability

#### Characteristics
Distributed | Centralized
---|---
Multiple autonomous components | One component with non-autonomous parts
Components are not shared by all users | Component shared by users all the time
Resources may not be accessible | All resources accessible
Software runs in concurrent processes on different processors | Software runs in a single process
Multiple Points of control | Single Point of control
Multiple Points of failure | Single Point of failure

#### Limitations
- Independent Failures must be tolorated
- Communication { Unreliable, Insecure, Costly |
- Must provide high availablility and transparency
- Atomicity is difficult whit no grand master

## Remote Procedure Calls ( RPC )
