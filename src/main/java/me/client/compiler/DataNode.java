package me.client.compiler;

import java.util.ArrayList;

/**
 * DataNode is created from DATA Statements
 */
public class DataNode extends StatementNode {
    /**
     * List of Nodes for DATA
     */
    private final ArrayList<Node> list;

    /**
     * Constructs DataNode
     * @param list list of nodes
     */
    public DataNode(ArrayList<Node> list){
        this.list = list;
    }

    /**
     * Public accessor for list
     * @return list
     */
    public ArrayList<Node> getList(){
        return list;
    }

    /**
     * toString Override
     * @return formatted string
     */
    public String toString() {
        String str = "DataNode:";
        for (Node node : list) {
            str = str + " " + node.toString();
        }
        return str;
    }
}
