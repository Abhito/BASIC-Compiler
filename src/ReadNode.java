import java.util.ArrayList;

/**
 * ReadNode is created from READ Statements
 * @author Abhinav Singhal
 * @version 1.0
 */
public class ReadNode extends StatementNode{
    /**
     * List of variables nodes
     */
    private final ArrayList<VariableNode> list;

    /**
     * Constructs ReadNode
     * @param list list of Variable Node
     */
    public ReadNode(ArrayList<VariableNode> list){
        this.list = list;
    }

    /**
     * public accessor for list
     * @return list
     */
    public ArrayList<VariableNode> getList(){
        return list;
    }

    /**
     * toString Override
     * @return formatted string
     */
    public String toString() {
        String str = "ReadNode:";
        for (VariableNode node : list) {
            str = str + " " + node.toString();
        }
        return str;
    }
}
