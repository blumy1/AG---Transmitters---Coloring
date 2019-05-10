import net.sourceforge.gxl.*;

import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Provide name of the file with the graph you want to color");
            return;
        }

        String fileName = args[0];
        String fileNameToSave = args[0];
        if (args.length > 1) {
            fileNameToSave = args[1];
        }

        GXLDocument gxlDocument = GXLManager.getGXLDocument(fileName);
        GXLGraph gxlGraph = (GXLGraph) gxlDocument.getElement("graph");

        HashMap<GXLNode, LinkedList<GXLNode>> connections = GraphManager.getConnectionsAsMap(gxlGraph);

        System.out.println("Colors used: " + GraphManager.colorGraph(connections));

        for (int i=0; i<gxlGraph.getGraphElementCount(); i++) {
            if (gxlGraph.getGraphElementAt(i).getClass() != GXLNode.class) continue;
            GXLNode node = (GXLNode) gxlGraph.getGraphElementAt(i);
            GXLAttributedElement attributedElement = node.getAttr("color");
            if (attributedElement == null) continue;

            GXLInt gxlInt = (GXLInt) node.getAttr("color").getValue();
            System.out.println(node.getID() + " -> " + gxlInt.getIntValue());
        }

        GXLManager.saveGXLDocument(gxlDocument, fileNameToSave);
    }
}
