package smartgrid.newsimcontrol.application.commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import couplingToICT.SimcontrolException;
import smartgrid.newsimcontrol.controller.LocalController;

public abstract class ControllerCommand {

	LocalController controller; 
	
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
	
	public Object ReadObjectFromFile(String filepath) {
		 
        try {
 
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
 
            Object obj = objectIn.readObject();
 
            objectIn.close();
            return obj;

        } catch (IOException|ClassNotFoundException ex) {
            return null;
        }
    }
	
	public void WriteObjectToFile(Object serObj, String filepath) {
		 
        try {
 
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
