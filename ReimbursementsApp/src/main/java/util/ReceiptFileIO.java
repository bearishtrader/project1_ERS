package util;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReceiptFileIO {
    static Logger logger = Logger.getLogger(ReceiptFileIO.class);

    static public byte [] getReceiptFileBytes(String path) {
        File file = new File(path);
        byte[] b = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(b);
        } catch (FileNotFoundException e) {
            logger.error("Test receipt file not found.");
            logger.error(e);
            b = null;
        } catch (IOException e1) {
            logger.error("Error reading test receipt file.");
            logger.error(e1);
            b = null;
        }
        return b;
    }
}
