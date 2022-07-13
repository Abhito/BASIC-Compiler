/**
 * The MathOpNode AST Node Class
 * @author Abhinav Singhal
 * @version 1.2
 */
public class MathOpNode extends Node{

    /**
     * The operations that this class can be
     */
    public enum operation{
        add, subtract, times, divide
    }

    /**
     * The operands
     */
    private final Node left, right;
    /**
     * The operation
     */
    private final operation sign;

    /**
     * Constructor for class
     * @param sign The operation to perform
     * @param left The left operand
     * @param right The right operand
     */
    public MathOpNode(operation sign, Node left, Node right){
        this.sign = sign;
        this.left = left;
        this.right = right;
    }

    /**
     * Getter for private enum operation
     * @return The operation the class represents
     */
    public operation getOperation(){return sign;}

    /**
     * Getter for left Node
     * @return left Node
     */
    public Node getLeft(){return left;}

    /**
     * Getter for right Node
     * @return right Node
     */
    public Node getRight(){ return right;}

    /**
     * toString Override for this class
     * @return String version of class
     */
    public String toString() {
        return "(MathOp: " + sign + ", Left: " + left + ", Right: " + right + ")";
    }
}
