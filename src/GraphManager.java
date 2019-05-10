import net.sourceforge.gxl.*;

import java.util.*;

public class GraphManager {
    public static List<GXLNode> getGraphNodes(GXLGraph gxlGraph) {
        List<GXLNode> nodes = new ArrayList<>();
        int count = gxlGraph.getGraphElementCount();
        for (int i=0; i<count; i++) {
            GXLGraphElement element = gxlGraph.getGraphElementAt(i);
            if (element.getClass() == GXLNode.class) {
                nodes.add((GXLNode) element);
            }
        }
        return nodes;
    }

    public static LinkedList<GXLNode>[] getConnections(GXLGraph gxlGraph, List<GXLNode> nodes) {
        LinkedList<GXLNode>[] adj = new LinkedList[nodes.size()];
        for (int i=0; i<gxlGraph.getGraphElementCount(); i++) {
            GXLGraphElement element = gxlGraph.getGraphElementAt(i);
            if (element.getClass() == GXLEdge.class) {
                GXLEdge edge = (GXLEdge) element;
                GXLNode source = (GXLNode) edge.getSource();
                GXLNode target = (GXLNode) edge.getTarget();

                int sourceIndex = nodes.indexOf(source);
                int targetIndex = nodes.indexOf(target);

                if (sourceIndex != -1) {
                    if (adj[sourceIndex] == null) {
                        adj[sourceIndex] = new LinkedList<>();
                    }
                    adj[sourceIndex].add(target);
                }

                if (targetIndex != -1) {
                    if (adj[targetIndex] == null) {
                        adj[targetIndex] = new LinkedList<>();
                    }
                    adj[targetIndex].add(source);
                }
            }
        }
        return adj;
    }

    public static HashMap<GXLNode, LinkedList<GXLNode>> getConnectionsAsMap(GXLGraph gxlGraph) {
        HashMap<GXLNode, LinkedList<GXLNode>> connections = new HashMap<>();
        int count = gxlGraph.getGraphElementCount();
        for (int i=0; i<count; i++) {
            GXLGraphElement element = gxlGraph.getGraphElementAt(i);
            if (element.getClass() == GXLEdge.class) {
                GXLEdge edge = (GXLEdge) element;
                GXLNode source = (GXLNode) edge.getSource();
                GXLNode target = (GXLNode) edge.getTarget();
                LinkedList<GXLNode> sourceList = getList(connections, source);
                LinkedList<GXLNode> targetList = getList(connections, target);

                sourceList.add(target);
                targetList.add(source);

                connections.put(source, sourceList);
                connections.put(target, targetList);
            } else if (element.getClass() == GXLNode.class) {
                GXLNode node = (GXLNode) element;
                if (!connections.containsKey(node)) {
                    connections.put(node, new LinkedList<>());
                }
            }
        }
        return connections;
    }

    private static LinkedList<GXLNode> getList(HashMap<GXLNode, LinkedList<GXLNode>> connections, GXLNode node) {
        LinkedList<GXLNode> nodeConnections;
        if (connections.containsKey(node)) {
            nodeConnections = connections.get(node);
        } else {
            nodeConnections = new LinkedList<>();
        }
        return nodeConnections;
    }

    public static int colorGraph(HashMap<GXLNode, LinkedList<GXLNode>> connections) {
        if (connections.size() < 1) return 0;

        int colorsUsed = 0;
        for (Map.Entry<GXLNode, LinkedList<GXLNode>> entry : connections.entrySet()) {
            GXLNode node = entry.getKey();
//            System.out.println("Coloring - " + node.getID());
            LinkedList<GXLNode> adjNodes = entry.getValue();
            HashSet<Integer> colorsOfNeighbours = new HashSet<>();

            for (GXLNode currNode : adjNodes) {
                Integer color = getNodeAttrValue(currNode, "color");
                if (color != null) {
                    colorsOfNeighbours.add(color);
                }
            }

            if (colorsOfNeighbours.size() == 0) {
                node.setAttr("color", new GXLInt(0));
                continue;
            }

            int freeColor = 0;
            while (colorsOfNeighbours.contains(freeColor)) {
                freeColor++;
            }

            node.setAttr("color", new GXLInt(freeColor));
            colorsUsed = Math.max(colorsUsed, freeColor);
        }
        return colorsUsed + 1;
    }

    public static Integer getNodeAttrValue(GXLNode node, String attrName) {
        GXLAttributedElement attributedElement = node.getAttr(attrName);
        if (attributedElement == null) return null;

        GXLInt gxlInt = (GXLInt) node.getAttr(attrName).getValue();
        return gxlInt.getIntValue();
    }

    public static int getNodeIntID(GXLNode node) {
        String ID = node.getID();
        return Integer.valueOf(ID.substring(ID.length()-1));
    }
}
