# Assignments
This directory contains all the assignments for the class along with a project [`RecordsManagment`](https://github.com/prince-chrismc/Distributed-Systems/tree/master/Assignments/Records-Managment) contains all the core implmentation that is not specific to the framework required for any given assignment. The goal is to abstract the bulk of the work such that it can be re-used throught the various assignments and into the project.

**Number** | **Name** | **Status**
:---: | :--- | ---
1 | [Java RMI](https://github.com/prince-chrismc/Distributed-Systems/tree/master/Assignments/Java-Rmi) | Completed
2 | [CORBA](https://github.com/prince-chrismc/Distributed-Systems/tree/master/Assignments/Corba) | _WIP_

### Notes
##### Creating Java Files from a .idl
For each .idl file run `idlj -fall <file_name>` and correct package naming if you are not using the default package

##### Starting the CORABA ORB
Run the following command `start orbd -ORBInitialPort 1050` for Windows or `orbd -ORBInitialPort 1050 &` on Linux
