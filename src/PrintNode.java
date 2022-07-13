import java.util.ArrayList;

/**
 * Print Node Contains PRINT stuff
 * @author Abhinav Singhal
 */
public class PrintNode extends StatementNode{
    /**
     * List of nodes including variable, string, and numbers
     */
    private final ArrayList<Node> list;

    /**
     * Constructs a PrintNode
     * @param list The list of Nodes
     */
    public PrintNode(ArrayList<Node> list){
        this.list = list;
    }

    /**
     * public accessor
     * @return the list
     */
    public ArrayList<Node> getList(){
        return list;
    }

    /**
     * ToString override
     * @return formatted string
     */
    public String toString(){
        String str = "PrintNode:";
        for (Node node : list) {
            str = str + " " + node.toString();
        }
        return str;
    }
}
