package smartgrid.newsimcontrol.rcp.commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.controller.LocalController;

public abstract class ControllerCommand {

	LocalController controller; 
	protected static final Logger LOG = Logger.getLogger(ControllerCommand.class);

	
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

}
