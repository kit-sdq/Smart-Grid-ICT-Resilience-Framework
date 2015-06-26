package smartgridsecurity.graphiti;

import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

import smartgridsecurity.graphiti.helper.GraphitiHelper;

/**
 * Specific Graphiti editor implementation.
 * @author mario
 *
 */
public class SGSDiagramEditor extends DiagramEditor {
	
	public SGSDiagramEditor() {
		super();
		GraphitiHelper.getInstance().setDiagramContainer(this);
	}
	
	@Override
	protected DiagramBehavior createDiagramBehavior() {
		return new SGSDiagramBehavior(this);
	}

}
