package me.client.compiler;

/**
 * LabeledStatementNode handles Labeled statements
 * @author Abhinav Singhal
 * @version 1.0
 */
public class LabeledStatementNode extends StatementNode {
    /**
     * Statement contains assigned StatementNode
     */
    private final StatementNode statement;
    /**
     * Label is the name of the label
     */
    private final String label;

    /**
     * Constructor for the class
     * @param label Reference to the label value
     * @param statement The StatementNode
     */
    public LabeledStatementNode(String label, StatementNode statement){
        this.statement = statement;
        this.label = label;
    }

    /**
     * public accessor for label
     * @return label
     */
    public String getLabel(){
        return label;
    }

    /**
     * public accessor for Statement
     * @return statementNode
     */
    public StatementNode getStatement(){
        return statement;
    }

    /**
     * toString Override
     * @return formatted String
     */
    public String toString() {
        return "LabeledStatementNode(" + label + ", " + statement + ")";
    }
}
