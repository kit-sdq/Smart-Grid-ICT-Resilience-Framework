package smartgrid.model.topo.profiles.completions

import java.util.ArrayList
import java.util.List
import org.apache.log4j.Logger
import smartgrid.model.topo.profiles.smartgridprofiles.api.SmartGridProfilesAPI
import smartgrid.model.topo.profiles.smartgridprofiles.api.SmartGridStereotypesAPI
import smartgridtopo.SmartGridTopology

class CompletionExecuter {
	val private static Logger logger = Logger.getLogger(CompletionExecuter)

	val List<Completion<?, ?>> completions

	public new(Completion<?, ?>... completions) {
		completions = new ArrayList(completions)
	}

	def public executeCompletions(SmartGridTopology smartGridTopology) {
		if (!SmartGridProfilesAPI.isSmartGridProfileApplied(smartGridTopology.eResource)) {
			logger.info("SmartGrid Profile is not applied to " + smartGridTopology + ". No completions are executed")
			return
		}
		val completionsToExecute = collectExecutableCompletions(smartGridTopology)
		completionsToExecute.forEach[it.execute]
	}

	private def collectExecutableCompletions(SmartGridTopology smartGridTopology) {
		val contents = smartGridTopology.eAllContents
		val completionsToExecute = new ArrayList<ExecutableCompletion>
		contents.forEach [ eObj |
			for (completion : completions) {
				if (completion.typeToComplete.isInstance(eObj)) {
//					val isApplied = SmartGridStereotypesAPI.hasStereotype() TODO improve check if stereotype is present
					val completionObject = SmartGridStereotypesAPI.getStereotype(eObj, completion.referenceName,
						completion.stereotypeName, completion.typeOfCompletionObject)
					if (completionObject != null) // TODO Hack
						completionsToExecute.add(new ExecutableCompletion(completion, eObj, completionObject))
				}
			}
		]
		return completionsToExecute
	}
}

class ExecutableCompletion {
	private val Completion<?, ?> completion
	private val Object objectToComplete
	private val Object completionObject

	new(Completion<?, ?> completion, Object objectToComplete, Object completionObject) {
		this.completion = completion
		this.objectToComplete = objectToComplete
		this.completionObject = completionObject
	}

	def execute() {
		val t = completion.typeToComplete.cast(objectToComplete)
		val u = completion.typeOfCompletionObject.cast(completionObject)
		completion.executeCompletion(t, u)
	}

}
