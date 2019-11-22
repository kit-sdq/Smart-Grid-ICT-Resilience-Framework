# Smart-Grid-ICT-Resilience-Framework
This part of the framework covers analyses of the ICT aspects of the Smart Grid. It is developed in the scope of the helmholtz project about the resilience of criticial infrastructures.

## User Guide

### Prerequisites
* Eclipse Modeling Tools (tested with 2019-09)
* Sirius
* [MDSDProfiles](https://sdqweb.ipd.kit.edu/wiki/MDSDProfiles)
* couplingSG-Project


### Set Up
* Clone repo with the added parameter `--recurse-submodules` to include the submodule couplingSG
    * you need access to the [couplingSG repository](https://git.scc.kit.edu/hgf-sg-coupling/couplingSG)
* run `mvn verify` in root folder to build and execute all tests 
    * test execution is currently disabled
* Install package from file `releng/edu.kit.ipd.sdq.smartgrid.updatesite/target/edu.kit.ipd.sdq.smartgrid.updatesite-1.0.0-SNAPSHOT.zip`

### Running
* an example for the usage of the RMI interface can be found under `smartgrid.newsimcontrol.tests.client.TestClientRMI`
* the rmi server starts automatically with eclipse
