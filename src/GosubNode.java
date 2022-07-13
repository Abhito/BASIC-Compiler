/**
 * GosubNode contains GOSUB Statement
 * @author Abhinav Singhal
 * @version 1.0
 */
public class GosubNode extends StatementNode{
    /**
     * Identifier that represents a label
     */
    private final VariableNode identifier;

    /**
     * Constructs a GosubNode
     * @param identifier The identifier
     */
    public GosubNode(VariableNode identifier){
        this.identifier = identifier;
    }

    /**
     * Public accessor for Node
     * @return node
     */
    public VariableNode getIdentifier(){
        return identifier;
    }

    /**
     * ToString Override
     * @return returns formatted string
     */
    public String toString() {
        return "GosubNode: " + identifier.toString();
    }
}
