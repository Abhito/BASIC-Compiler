/**
 * AssignmentNode contains methods for assignments
 * @author Abhinav Singhal
 */
public class AssignmentNode extends StatementNode{
    /**
     * Variable that is being assigned
     */
    private final VariableNode variable;

    /**
     * Value being assigned to variable
     */
    private final Node value;

    /**
     * Constructs an AssignmentNode
     * @param variable the variable to assign value
     * @param value the value to assign
     */
    public AssignmentNode(VariableNode variable, Node value){
        this.variable = variable;
        this.value = value;
    }

    /**
     * public accessor for name
     * @return
     */
    public VariableNode getVariable(){
        return variable;
    }

    /**
     * public accessor for value
     * @return
     */
    public Node getValue(){
        return value;
    }

    /**
     * toString Override
     * @return Formatted string
     */
    public String toString(){
        return "AssignmentNode(" + variable + ", " + value + ")";
    }
}
