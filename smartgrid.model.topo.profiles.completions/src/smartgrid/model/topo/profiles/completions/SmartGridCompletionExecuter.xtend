package smartgrid.model.topo.profiles.completions

import smartgrid.model.topo.profiles.completions.CompletionExecuter

class SmartGridCompletionExecuter extends CompletionExecuter {
	new(){
		super(new ReplicationCompletionForSmartMeter())
	}
}