import java.util.ArrayList;

/**
 * InputNode contains INPUT Statements
 * @author Abhinav Singhal
 * @version 1.0
 */
public class InputNode extends StatementNode{
    /**
     * Constant String of an Input Statement
     */
    private final StringNode constStr;
    /**
     * Variables contained by InputNODE
     */
    private final ArrayList<VariableNode> list;

    /**
     * Constructs InputNode
     * @param list List of Variables
     * @param constStr Constant String at the beginning
     */
    public InputNode(ArrayList<VariableNode> list, StringNode constStr){
        this.constStr = constStr;
        this.list = list;
    }

    /**
     * Construct InputNode without Constant String
     * @param list List of Variables
     */
    public InputNode(ArrayList<VariableNode> list){
        this.constStr = null;
        this.list = list;
    }

    /**
     * Public accessor for the string
     * @return the string
     */
    public StringNode returnString(){
        return constStr;
    }

    /**
     * Public accessor for the list
     * @return list of variables
     */
    public ArrayList<VariableNode> getList(){
        return list;
    }

    /**
     * toString Override
     * @return returns formatted string
     */
    public String toString() {
        String str = "InputNode:";
        //check for string
        if(constStr != null) str = str + " " + constStr + ",";
        for (VariableNode node : list){
            str = str + " " + node.toString();
        }
        return str;
    }
}
