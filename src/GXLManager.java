import net.sourceforge.gxl.GXLDocument;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class GXLManager {

    public static GXLDocument getGXLDocument(String fileName) {
        GXLDocument gxlDocument = null;
        try {
            gxlDocument = new GXLDocument(new File(fileName));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return gxlDocument;
    }

    public static void saveGXLDocument(GXLDocument gxlDocument, String fileName) {
        try {
            gxlDocument.write(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
