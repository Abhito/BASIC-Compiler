/**
 * IfNode stores if statements.
 * @author Abhinav Singhal
 */
public class IfNode extends StatementNode{
    /**
     * Node stores boolean expression
     */
    private final BooleanOperationNode node;
    /**
     * label is the line to GOTO
     */
    private final VariableNode label;

    /**
     * Constructor for class
     * @param node Boolean expression
     * @param label Reference to a label
     */
    public IfNode(BooleanOperationNode node, VariableNode label){
        this.node = node;
        this.label = label;
    }

    /**
     * public accessor for node
     * @return node
     */
    public BooleanOperationNode getexpression() {
        return node;
    }

    /**
     * public accessor for label
     * @return label
     */
    public VariableNode getLabel() {
        return label;
    }

    /**
     * toString override
     * @return formatted string
     */
    public String toString() {
        return "IfNode(" + node + ", " + label + ")";
    }
}
