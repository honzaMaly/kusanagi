package cz.jan.maly.utils;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A simple class with generic serialize and deserialize method implementations
 *
 * @author pankaj
 */
public class SerializationUtil {

    /**
     * Get all files with extension in folder
     *
     * @param directoryName
     * @return
     */
    public static Set<File> getAllFilesInFolder(String directoryName, String extension) {
        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList == null) {
            return new HashSet<>();
        }
        return Arrays.stream(directory.listFiles()).filter(File::isFile)
                .filter(file -> Files.getFileExtension(file.getPath()).equals(extension))
                .collect(Collectors.toSet());
    }

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
        GZIPInputStream giz = new GZIPInputStream(new FileInputStream(fileName));
        byte[] bytes = IOUtils.toByteArray(giz);
        giz.close();
//        FileInputStream fis = new FileInputStream(fileName);
//        byte[] bytes = IOUtils.toByteArray(fis);
//        fis.close();
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
        GZIPOutputStream gz = new GZIPOutputStream(new FileOutputStream(fileName));
        ObjectOutputStream oos = new ObjectOutputStream(gz);
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

}
