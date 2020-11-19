package Map;

import Map.OutputStructure.AdjacencyListNode;

public class BuildMap {

    public int[][] adjacencyMatrix(InputMap[] inputMaps) {
        if (inputMaps == null || inputMaps.length == 0) {
            return null;
        }
        int max = Integer.MIN_VALUE;
        for (InputMap temp : inputMaps) {
            if (temp.getStart() > max) {
                max = temp.getStart();
            }
            if (temp.getEnd() > max) {
                max = temp.getEnd();
            }
        }
        int[][] data = new int[max][max];
        for (InputMap temp : inputMaps) {
            data[temp.getStart() - 1][temp.getEnd() - 1] = temp.getLength();
        }
        return data;
    }

    public AdjacencyListNode[] adjacencyList(InputMap[] inputMaps) {
        if (inputMaps == null || inputMaps.length == 0) {
            return null;
        }
        int max = Integer.MIN_VALUE;
        for (InputMap temp : inputMaps) {
            if (temp.getStart() > max) {
                max = temp.getStart();
            }
            if (temp.getEnd() > max) {
                max = temp.getEnd();
            }
        }
        AdjacencyListNode[] nodes = new AdjacencyListNode[max];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new AdjacencyListNode();
            nodes[i].setPoint(i + 1);
        }
        for (InputMap temp : inputMaps) {
            AdjacencyListNode node = new AdjacencyListNode();
            node.setPoint(temp.getEnd());
            node.setData(temp.getLength());
            node.setNext(nodes[temp.getStart() - 1].getNext());
            nodes[temp.getStart() - 1].setNext(node);
        }
        return nodes;
    }

}
