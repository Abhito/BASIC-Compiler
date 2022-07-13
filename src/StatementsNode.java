import java.util.ArrayList;

/**
 * StatementsNode maintains a list of Nodes
 * @author Abhinav Singhal
 * @version 1.2
 */
public class StatementsNode extends Node{
    /**
     * the list of Statement Nodes
     */
    private final ArrayList<StatementNode> list;

    /**
     * Constructs a StatementsNode
     * @param list list of StatementNodes
     */
    public StatementsNode(ArrayList<StatementNode> list) {
        this.list = list;
    }

    /**
     * Public accessor for StatementsNode
     * @return List of StatementNodes
     */
    public ArrayList<StatementNode> getList(){
        return list;
    }

    /**
     * toString Override for StatementsNode
     * Prints each StatementNode on its own line
     * @return string
     */
    public String toString() {
        String str = "StatementsNode:\n";
        for(StatementNode node: list){
            str = str + " " + node.toString() + "\n";
        }
        return str;
    }
}
