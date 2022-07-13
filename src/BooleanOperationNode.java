/**
 * BooleanOperationNode contains boolean expressions
 * @author Abhinav Singhal
 */
public class BooleanOperationNode extends Node{
    /**
     * Just like MathOpNode, this class has its own enum
     */
    public enum operator{
        greaterthan, greaterthanequals, lessthan, lessthanequals, notequals, equals
    }

    /**
     * The left and right of the expression
     */
    private final Node left, right;
    /**
     * The operator sign
     */
    private final operator sign;

    /**
     * The constructor for the class
     * @param sign the operator
     * @param left left side
     * @param right right side
     */
    public BooleanOperationNode(operator sign, Node left, Node right){
        this.sign = sign;
        this.left = left;
        this.right = right;
    }

    /**
     * public accessor for left
     * @return left
     */
    public Node getLeft() {
        return left;
    }

    /**
     * public accessor for right
     * @return right
     */
    public Node getRight() {
        return right;
    }

    /**
     * public accessor for sign
     * @return operator
     */
    public operator getSign() {
        return sign;
    }

    /**
     * toString override
     * @return formatted string
     */
    public String toString() {
        return "(BooleanOpNode: " + sign + ", Left: " + left + ", Right: " + right + ")";
    }
}
