package PopGenetics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.codec.binary.Base64;

// Note: this utility code is an adapted subset of a code fragment found on StackOverflow, here: https://stackoverflow.com/questions/6055476/how-to-convert-object-to-string-in-java 

public class ObjectUtil {

	static final Base64 base64 = new Base64();
 
    public static String serializeObjectToString(Object object) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
			objectOutputStream.writeObject(object);
            objectOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new String(base64.encode(arrayOutputStream.toByteArray()));
    }

    public static Object deserializeObjectFromString(String objectString) {
    	byte[] arr = base64.decode(objectString);
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arr);
        try {
        	ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
        	return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
        	e.printStackTrace();
        	return new Object();
        }
    }
    
}