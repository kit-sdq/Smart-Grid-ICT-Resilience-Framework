package smartgrid.newsimcontrol.tests.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TestHelper {

	public static void WriteObjectToFile(Object serObj, String filepath) {
		 
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
