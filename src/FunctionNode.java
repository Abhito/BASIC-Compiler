import java.util.ArrayList;

/**
 * FunctionNode stores functions names and parameters.
 * @author Abhinav Singhal
 */
public class FunctionNode extends Node{
    /**
     * Name of the function
     */
    private final String function;
    /**
     * The parameters for the function
     */
    private final ArrayList<Node> params;

    /**
     * Constructor for functions without parameters
     * @param function name
     */
    public FunctionNode(String function){
        this.function = function;
        this.params = null;
    }

    /**
     * Constructor for functions with parameters
     * @param function name
     * @param params parameters
     */
    public FunctionNode(String function, ArrayList<Node> params){
        this.function = function;
        this.params = params;
    }

    /**
     * public accessor for function name
     * @return name
     */
    public String getFunction() {
        return function;
    }

    /**
     * public accessor for parameters
     * @return parameters
     */
    public ArrayList<Node> getParams() {
        return params;
    }

    /**
     * ToString Override
     * @return string
     */
    public String toString() {
        String str = "FunctionNode(" + function;
        if(params != null){ //if parameters exist
            for (Node node : params) {
                str = str + ", " + node.toString();
            }
        }
        return str + ")";
    }
}
