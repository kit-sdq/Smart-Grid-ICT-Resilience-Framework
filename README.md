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

### Running using command line
* the commands which are given to the command line differ from the regular ones used that they must have as a first parameter an ID in the form of a filepath which will be used to apply all the commands to the same controller. They must have also a filepath where the return object (if exists) should be saved.
* The syntax of the commands should be like:
    * INIT_TOPO   filepath(InitializationMap)   filepath(TopologyContainer)   returnpath(ICTElements) returnpath(Topology) returnpath(ScenarioState)
    * GET_MODIFIED_POWERSPECS   filepath(InitializationMap)   filepath(Topology) filepath(ScenarioState)  filepath(Powerspecs)   filepath(PowerAssigned)   returnpath(ModifiedPowerSpec)   returnpath(Dysfunctional smartcomponents)

