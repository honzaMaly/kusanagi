package cz.jan.maly.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;

/**
 * A simple class with generic serialize and deserialize method implementations
 *
 * @author pankaj
 */
public class SerializationUtil {

    /**
     * Deserialize to <T> from given file
     *
     * @param fileName
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] bytes = IOUtils.toByteArray(fis);
        fis.close();
        return (T) SerializationUtils.deserialize(bytes);
    }

    /**
     * Serialize the given object and save it to file
     *
     * @param object
     * @param fileName
     * @param <T>
     * @throws IOException
     */
    public static <T extends Serializable> void serialize(T object, String fileName) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

}
