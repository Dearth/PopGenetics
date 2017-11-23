package serialization_test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;

public class ObjectUtil {

    static final Base64 base64 = new Base64();

//    public static String serializeObjectToString(Object object) throws IOException 
//    {
//        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
//        {
//            objectOutputStream.writeObject(object);
//            objectOutputStream.flush();
//            return new String(base64.encode(arrayOutputStream.toByteArray()));
//        }
//    }
//
//    public static Object deserializeObjectFromString(String objectString) throws IOException, ClassNotFoundException 
//    {
//    	byte[] arr = base64.decode(objectString);
//        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arr);
//        GZIPInputStream gzipInputStream = new GZIPInputStream(arrayInputStream);
//        ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
//        {
//            return objectInputStream.readObject();
//        }
//    }
    
    
    public static String serializeObjectToString(Object object) throws IOException 
    {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
        {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return new String(base64.encode(arrayOutputStream.toByteArray()));
        }
    }

    public static Object deserializeObjectFromString(String objectString) throws IOException, ClassNotFoundException 
    {
    	byte[] arr = base64.decode(objectString);
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arr);
//        GZIPInputStream gzipInputStream = new GZIPInputStream(arrayInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
        {
            return objectInputStream.readObject();
        }
    }
}