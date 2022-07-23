package me.client.compiler;

import java.util.ArrayList;

/**
 * The Parser class parses a given lexed line
 * @author Abhinav Singhal
 * @version 2.0
 */

public class Parser {
    /**
     * List managed by the methods in this class
     */
    private final ArrayList<Token> list;

    /**
     *  Labelcounter counts how many labels there are in one line
     */
    private int labelcounter;

    /**
     * linecounter counts the amount of lines in the code
     * Currently is not used. May be used in the future.
     */
    private int linecounter = 0;

    /**
     * Creates a Parser Object
     * @param list The List of Tokens
     */
    public Parser(ArrayList<Token> list){
        this.list = list;
    }

    /**
     * Parses the list
     * @return Returns Parsed Node
     * @throws Exception Throws any Exceptions
     */
    public Node parse() throws Exception{
        return Statements(); //Calls Statements
    }

    /**
     * Statements creates a StatementsNode
     * @return AST Node
     * @throws Exception Throws any errors
     */
    private Node Statements() throws Exception {
        ArrayList<StatementNode> statements = new ArrayList<>();
        while(list.size() > 1) { //Token list is empty
            StatementNode statement = Statement();
            if(statement != null) {
                statements.add(statement);
                labelcounter = 0; //reset labelcount
                if (matchAndRemove(Token.Types.EndOfLine) != null) linecounter++;
            }
        }
        return new StatementsNode(statements);
    }

    /**
     * Statement process one statement and returns a Node
     * @return A Node
     * @throws Exception Throws any exception
     */
    private StatementNode Statement() throws Exception {
        //if Label Statement
        Token label = matchAndRemove(Token.Types.LABEL);
        if(label != null) return labelStatement(label);
        //if IF Statement
        else if(matchAndRemove(Token.Types.IF) != null) return ifStatement();
        //if For Statement
        else if(matchAndRemove(Token.Types.FOR) != null) return forStatement();
        //if Next Statement
        else if(matchAndRemove(Token.Types.NEXT) != null) return nextStatement();
        //if Return Statement
        else if(matchAndRemove(Token.Types.RETURN) != null){
            return new ReturnNode();
        }
        //if gosub statement
        else if(matchAndRemove(Token.Types.GOSUB) != null) return gosubStatement();
        //if Print statement
        else if(matchAndRemove(Token.Types.PRINT) != null) return printStatement();
        //if Data Statement
        else if(matchAndRemove(Token.Types.DATA) != null)return dataStatement();
        //if Read Statement
        else if(matchAndRemove(Token.Types.READ) != null) return readStatement();
        //if Input Statement
        else if(matchAndRemove(Token.Types.INPUT) != null) return inputStatement();
        else if(matchAndRemove(Token.Types.EndOfLine) != null) return null;
        else return Assignment(); //else its an assignment.
    }

    /**
     * ifStatement handles and returns ifNode
     * @return ifNode
     * @throws Exception throws error if problem found
     */
    private IfNode ifStatement() throws Exception{
        BooleanOperationNode expression = BooleanExpression();//create boolean expression
        //check for THEN keyword
        if(matchAndRemove(Token.Types.THEN) == null) throw new Exception("No THEN statement after IF.");
        VariableNode label;
        try { //cast to VariableNode, will throw error if it fails
            label = (VariableNode) Factor();
        } catch(Exception e){
            throw new Exception("No LABEL found after THEN.");
        }
        if(label == null) throw new Exception("No Label to GOTO.");
        return new IfNode(expression, label);
    }

    /**
     * labelStatement handles labels
     * @param label The Token for the label
     * @return labelStatementNode
     * @throws Exception Throws exception if error found
     */
    private LabeledStatementNode labelStatement(Token label) throws Exception{
        labelcounter++; //increment labelcount everytime there is a labelstatement
        if(labelcounter > 1) throw new Exception("There is more than one label per line.");
        return new LabeledStatementNode(label.getValue(), Statement());
    }

    /**
     * nextStatement() returns a NextNode or throws an error
     * @return a NextNode
     * @throws Exception if there is no variableNode
     */
    private NextNode nextStatement() throws Exception{
        VariableNode node;
        try {
            node = (VariableNode) Factor();
        } catch(Exception e){
            throw new Exception("There is no identifier next to NEXT Statement.");
        }
        return new NextNode(node);
    }

    /**
     * ForStatement() returns a ForNode or throws errors
     * @return a ForNode
     * @throws Exception when a error is encountered
     */
    private ForNode forStatement() throws Exception {
        AssignmentNode node = Assignment(); //first part
        Node limit;
        if (matchAndRemove(Token.Types.TO) != null) { //check for "TO"
            limit = Factor();
            if(limit == null) throw new Exception("No limit value present.");
        }
        else throw new Exception("There is no TO Statement for FOR Statement.");
        //return ForNode with default increment
        if (matchAndRemove(Token.Types.STEP) == null) return new ForNode(node, limit);
        else{
            Node increment = Factor();
            if(increment == null) throw new Exception("STEP Statement without increment.");
            return new ForNode(node, limit, increment);
        }
    }

    /**
     * gosubStatement() returns a GosubNode or throws error
     * @return a GosubNode
     * @throws Exception throw error when encountered
     */
    private GosubNode gosubStatement() throws Exception{
        VariableNode node;
        try {
            node = (VariableNode) Factor();
        } catch(Exception e){
            throw new Exception("There is no identifier next to Gosub Statement.");
        }
        return new GosubNode(node);
    }
    /**
     * inputStatement returns an Input Node or null
     * @return InputNode
     * @throws Exception Throw any errors
     */
    private InputNode inputStatement() throws Exception{
        //Checks for a string constant
        Token string = matchAndRemove(Token.Types.STRING);
        if(string != null){
            Token comma = matchAndRemove(Token.Types.COMMA);
            if(comma == null) throw new Exception("There is no comma after String Constant.");
        }
        //Creates a list of variables
        ArrayList<VariableNode> variables = readList(); //Reuse method from readStatement
        if(variables == null) throw new Exception("Input Statement without any variables.");
        //If no string constant then make node with just list
        if(string == null)return new InputNode(variables);
        else return new InputNode(variables, new StringNode(string.getValue()));
    }

    /**
     * dataStatement creates a DataNode and returns it
     * @return DataNode or null
     * @throws Exception Throws error from dataList()
     */
    private DataNode dataStatement() throws Exception {
        ArrayList<Node> data = dataList();
        if(data == null) return null;
        else return new DataNode(data);
    }

    /**
     * dataList constructs a array list of Nodes and then returns it
     * @return ArrayList of nodes or null
     * @throws Exception Throws any errors from Factor()
     */
    private ArrayList<Node> dataList() throws Exception {
        ArrayList<Node> data = new ArrayList<>();
        Node node = Factor();
        if(node == null) return null;
        data.add(node);
        while (matchAndRemove(Token.Types.COMMA) !=null){//loop for every comma
            node = Factor();
            data.add(node);
        }
        return data;
    }

    /**
     * readStatement makes a ReadNode and returns it
     * @return ReadNode or null
     * @throws Exception Throws exception from readList()
     */
    private ReadNode readStatement() throws Exception {
        ArrayList<VariableNode> reads = readList();
        if(reads == null) return null;
        else return new ReadNode(reads);
    }

    /**
     * readList constructs an arrayList of Variable Nodes
     * @return A array of Variable Nodes or null
     * @throws Exception Throws any errors.
     */
    private ArrayList<VariableNode> readList() throws Exception {
        ArrayList<VariableNode> reads = new ArrayList<>();
        VariableNode variable;
        try { //cast to VariableNode, will throw error if it fails
            variable = (VariableNode) Factor();
        } catch(Exception e){
            throw new Exception("Number or String instead of variable after READ or INPUT statement.");
        }
        if (variable == null) return null;
        reads.add(variable);
        while(matchAndRemove(Token.Types.COMMA) != null ){
            try {
                variable = (VariableNode) Factor();
            } catch(Exception e){
                throw new Exception("Number or String instead of variable after READ or INPUT statement.");
            }
            reads.add(variable);
        }
        return reads;
    }

    /**
     * PrintStatement returns a PrintNode or null
     * @return PrintNode or NUll
     * @throws Exception Throws any errors
     */
    private PrintNode printStatement() throws Exception {
        ArrayList<Node> prints = printList();
        if(prints == null) return null;
        else return new PrintNode(prints);
    }

    /**
     * PrintList processes a list of expressions
     * @return Returns a list of nodes
     * @throws Exception passes any errors
     */
    private ArrayList<Node> printList() throws Exception {
        ArrayList<Node> prints = new ArrayList<>();
        Node expression = Expression();
        if(expression == null) return null;
        prints.add(expression);
        //if there are commas
        while(matchAndRemove(Token.Types.COMMA) != null ){
            expression = Expression();
            prints.add(expression);
        }
        return prints;
    }

    /**
     * Assignment creates an AssignmentNode
     * @return AssignmentNode
     * @throws Exception throws Assignment
     */
    private AssignmentNode Assignment() throws Exception {
        VariableNode left;
        try {
            left = (VariableNode) Factor();
        } catch(Exception e){
            throw new Exception("No Variable found.");
        }
        if(left == null) return null;
        if(matchAndRemove(Token.Types.EQUALS) != null){
            Node expression = Expression();
            if(expression == null)
                throw new Exception("No value found after equals.");
            else return new AssignmentNode(left, expression);
        }
        else throw new Exception("Variable is all alone.");
    }
    /**
     * Matches the token with given type and removes it if match
     * @param token Type to check with
     * @return Token.Type of token
     */
    private Token matchAndRemove(Token.Types token){
        if (token != list.get(0).getType()) return null;
        else{
            Token returnToken = list.get(0);
            list.remove(0); //Removes Token from list
            return returnToken;
        }
    }

    /**
     * BooleanExpression creates a boolean expression.
     * @return boolean expression
     * @throws Exception Throw errors
     */
    private BooleanOperationNode BooleanExpression() throws Exception{
        Node left = Expression();
        if(left == null) throw new Exception("If statement without any expressions.");
        Token operator = getOperator();
        if(operator == null) throw new Exception("Boolean expression needs operator.");
        Node secondExpression = Expression();
        if(operator.getType() == Token.Types.GREATER_THAN)
            return new BooleanOperationNode(BooleanOperationNode.operator.greaterthan, left, secondExpression);
        else if(operator.getType() == Token.Types.GREATER_THAN_EQUALS)
            return new BooleanOperationNode(BooleanOperationNode.operator.greaterthanequals, left, secondExpression);
        else if(operator.getType() == Token.Types.LESS_THAN)
            return new BooleanOperationNode(BooleanOperationNode.operator.lessthan, left, secondExpression);
        else if(operator.getType() == Token.Types.LESS_THAN_EQUALS)
            return new BooleanOperationNode(BooleanOperationNode.operator.lessthanequals, left, secondExpression);
        else if(operator.getType() == Token.Types.NOTEQUALS)
            return new BooleanOperationNode(BooleanOperationNode.operator.notequals, left, secondExpression);
        else return new BooleanOperationNode(BooleanOperationNode.operator.equals, left, secondExpression);
    }

    /**
     * Works similarly to getOperation
     * @return The operator
     */
    private Token getOperator(){
        if(matchAndRemove(Token.Types.GREATER_THAN) != null) return new Token(Token.Types.GREATER_THAN);
        else if(matchAndRemove(Token.Types.GREATER_THAN_EQUALS) != null)
            return new Token(Token.Types.GREATER_THAN_EQUALS);
        else if(matchAndRemove(Token.Types.LESS_THAN) != null) return new Token(Token.Types.LESS_THAN);
        else if(matchAndRemove(Token.Types.LESS_THAN_EQUALS) != null)
            return new Token(Token.Types.LESS_THAN_EQUALS);
        else if(matchAndRemove(Token.Types.NOTEQUALS) != null) return new Token(Token.Types.NOTEQUALS);
        else if(matchAndRemove(Token.Types.EQUALS) != null) return new Token(Token.Types.EQUALS);
        else return null;
    }

    /**
     * Expression class handles addition and subtraction
     * @return Node
     * @throws Exception Throws any exception
     */
    private Node Expression() throws Exception{
        Node left = functionInvocation();
        if(left == null){
            left = Term();
            if(left == null) return null;
            return getRightSideExpression(left);//check right side
        }
        else return left; //return function
    }

    /**
     * functionInvocation handles function calls. A function can have 0 to many parameters.
     * @return FunctionNode
     * @throws Exception throws any errors
     */
    private FunctionNode functionInvocation() throws Exception{
        Token function = matchAndRemove(Token.Types.FUNCTION);
        if(function == null) return null; //no function found
        else{
            if(matchAndRemove(Token.Types.LPAREN) == null) throw new Exception("Function without parenthesis");
            else{
                ArrayList<Node> paramlist = printList();
                if(matchAndRemove(Token.Types.RPAREN) == null)
                    throw new Exception("Function does not have closing parenthesis.");
                else {
                    if(paramlist == null) return new FunctionNode(function.getValue());//function without parameters
                    else return new FunctionNode(function.getValue(), paramlist);//function with parameters
                }
            }
        }
    }

    /**
     * Checks the Right side of the Expression and returns it
     * @param left The left part of the expression that has already been processed
     * @return The Right side of the expression which can be used as the left
     * @throws Exception Throws error when there is a syntax problem
     */
    public Node getRightSideExpression(Node left) throws Exception {
        Token operation = getOperation(1);
        if(operation == null) return left;
        Node secondTerm = Term();
        if(secondTerm == null)
            throw new Exception("Found + or - without another number.");
        Node op;
        if(operation.getType() == Token.Types.PLUS)
            op = new MathOpNode(MathOpNode.operation.add, left, secondTerm);
        else op = new MathOpNode(MathOpNode.operation.subtract, left, secondTerm);
        return getRightSideExpression(op);
    }

    /**
     * Returns the current operation to perform
     * @param mode Changes mode depending on which method called it.
     * @return Token containing the operation.
     */
    private Token getOperation(int mode){
        Token token = null;
        if(mode == 1) {
            if (matchAndRemove(Token.Types.PLUS) != null) token = new Token(Token.Types.PLUS);
            else if (matchAndRemove(Token.Types.MINUS) != null) token = new Token(Token.Types.MINUS);
        }
        else if(mode == 2) {
            if (matchAndRemove(Token.Types.DIVIDE) != null) token = new Token(Token.Types.DIVIDE);
            else if (matchAndRemove(Token.Types.MULTIPLY) != null) token = new Token(Token.Types.MULTIPLY);
        }
        return token;
    }
    /**
     * Term method handles multiplication and divide
     * @return Node
     * @throws Exception Throw any exception
     */
    private Node Term() throws Exception{
        Node left = Factor();
        if(left == null) return null;
        return getRightSideTerm(left); //Check right side of term
    }

    /**
     * Checks the Right side of the Term and returns it
     * @param left The left part of the Term that has already been processed
     * @return The Right side of the Term which can be used as the left
     * @throws Exception Throws error when there is a syntax problem
     */
    public Node getRightSideTerm(Node left) throws Exception {
        Token operation = getOperation(2);
        if(operation == null) return left;
        Node secondFactor = Factor();
        if(secondFactor == null)
            throw new Exception("Found * or / without a number on the right.");
        Node op;
        if(operation.getType() == Token.Types.MULTIPLY)
            op = new MathOpNode(MathOpNode.operation.times, left, secondFactor);
        else op = new MathOpNode(MathOpNode.operation.divide, left, secondFactor);
        return getRightSideTerm(op);
    }

    /**
     * Factor returns a number and handles parenthesis
     * @return A Node
     * @throws Exception throws any exception
     */
    private Node Factor() throws Exception{
        //How Factor Handles numbers
        Token number = matchAndRemove(Token.Types.NUMBER);
        if(number != null){
            //check if the number is float or integer
            if(number.getValue().contains("."))
                return new FloatNode(Float.parseFloat(number.getValue()));
            else return new IntegerNode(Integer.parseInt(number.getValue()));
        }
        //How Factor handles parentheses
        if(matchAndRemove(Token.Types.LPAREN) != null){
            Node expression = Expression();
            if(expression == null)
                throw new Exception("Found left parenthesis with right one");
            if(matchAndRemove(Token.Types.RPAREN) != null)
                return expression;
            throw new Exception("Found left parenthesis and expression without right parenthesis.");
        }
        //How Factor handles Identifier
        Token identifier = matchAndRemove(Token.Types.IDENTIFIER);
        if(identifier != null) return new VariableNode(identifier.getValue());
        //Factor handles STRING
        Token string = matchAndRemove(Token.Types.STRING);
        if(string != null) return new StringNode(string.getValue());

        return null;
    }
}
