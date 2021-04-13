package smartgrid.newsimcontrol.rcp.commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;

import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.controller.LocalController;
import smartgridinput.ScenarioState;
import smartgridinput.SmartgridinputPackage;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartgridtopoPackage;

public abstract class ControllerCommand {

	LocalController controller; 
	protected static final Logger LOG = Logger.getLogger(ControllerCommand.class);
	public enum ObjectType  {
			topo,
			input
	};
	
	public ControllerCommand(LocalController controller) {
		this.controller = controller;
	}
	
	public void execute(String[] args) throws SimcontrolException, InterruptedException {
		if (checkArguments(args))
			doCommand(args);
	}
	
	public abstract boolean allow();
	
	public abstract boolean checkArguments(String[] args);
	
	public abstract void doCommand(String[] args) throws SimcontrolException, InterruptedException;
	
	public Object readObjectFromFile(String filepath) {
		 
        return readObjectFromFile(filepath, null);
    }
	
	public Object readObjectFromFile(String filepath, ObjectType objectType) {
		 
		if (objectType != null) {
			return readTopoOrInput(filepath, objectType);
		}
		
        try {
 
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
 
            Object obj = objectIn.readObject();
 
            objectIn.close();
            return obj;

        } catch (IOException|ClassNotFoundException ex) {
        	LOG.error("The input can't be read from the filepath: " + filepath);
            return null;
        }
    }
	
	private Object readTopoOrInput(String filepath, ObjectType objectType) {
		switch (objectType) {
		case input:
			return loadInput(filepath);
		case topo:
			return loadTopology(filepath);
		default:
			return null;
		}
	}

	public void writeObjectToFile(Object serObj, String filepath) {
		 
        try {
 
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
 
        } catch (IOException ex) {
        	LOG.error("The output can't be saved to the filepath: " + filepath);
            ex.printStackTrace();
        }
    }
	
	/**
     * @param path
     *            path of the ScenarioState to be used
     * @return The read ScenarioState file
     */
    protected static ScenarioState loadInput(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridinput/1.0",
                SmartgridinputPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridinput",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (ScenarioState) resource.getContents().get(0);
    }

    /**
     * @param path
     *            path of the ScenarioTopology to be used
     * @return The read ScenarioTopology file
     */
    protected static SmartGridTopology loadTopology(final String path) {

        final ResourceSet resourceSet = new ResourceSetImpl();

        resourceSet.getPackageRegistry().put("http://www.modelversioning.org/emfprofile/application/1.1",
                EMFProfileApplicationPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://sdq.ipd.uka.de/smartgridtopo/1.1", SmartgridtopoPackage.eINSTANCE);

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("smartgridtopo",
                new XMIResourceFactoryImpl());
        final Resource resource = resourceSet.getResource(URI.createFileURI(path), true);
        return (SmartGridTopology) resource.getContents().get(0);
    }

}
