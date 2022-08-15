package me.client.compiler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import me.client.App;

import java.util.*;

/**
 * Interpreter takes in an AST Node and will interpret it.
 * @author Abhinav Singhal
 * @version 1.5
 */
public class Interpreter {

    /**
     *  All of our required global variables
     */
    private final ArrayList<StatementNode> astList;
    private final HashMap<String, Integer> intData = new HashMap<String, Integer>();
    private final HashMap<String, Float> floatData = new HashMap<String, Float>();
    private final HashMap<String, String> stringData = new HashMap<String, String>();
    private final HashMap<String, Node> labelData = new HashMap<String, Node>();
    private ArrayList<Node> globalData;

    /**
     * Constructor for class
     * @param ast The ast node to interpret.
     */
    public Interpreter(StatementsNode ast){
        astList = ast.getList();
    }

    /**
     * Initialize calls our walk methods
     * @throws Exception Throws any exceptions
     */
    public void initialize() throws Exception {
        walkforLabel();
        walkforFor();
        walkforData();
        walkforNext();
        interpret(astList.get(0));
    }

    /**
     * Interprets the statements in a StatementsNode
     * @param statement The first statement of the statements Node
     * @throws Exception Throws any errors encountered
     */
    public void interpret(StatementNode statement) throws Exception{
        boolean done = false; //variable to check if we are done going through statements
        Stack<StatementNode> stackGoSub = new Stack<StatementNode>(); //stack that contains statements after gosub
        while(!done) {
            //If statement is a READ statement
            if (statement.getClass().equals(ReadNode.class)) {
                //test to make sure that DATA collection has enough values for READ
                if (globalData.size() < ((ReadNode) statement).getList().size() - 1)
                    throw new Exception("There are more READ variables than DATA values");
                for (int i = 0; i < ((ReadNode) statement).getList().size(); i++) {
                    //if variable is string type
                    if (checkVariableType(((ReadNode) statement).getList().get(i), '$')) {
                        if (globalData.get(0).getClass().equals(StringNode.class)) {
                            StringNode string = (StringNode) globalData.get(0);
                            stringData.put(((ReadNode) statement).getList().get(i).getName(), string.returnString());
                            globalData.remove(0); //remove value from collection
                        } else throw new Exception("Data does not match String Type.");
                    }
                    //if variable is a float type
                    else if (checkVariableType(((ReadNode) statement).getList().get(i), '%')) {
                        if (globalData.get(0).getClass().equals(FloatNode.class)) {
                            FloatNode number = (FloatNode) globalData.get(0);
                            floatData.put(((ReadNode) statement).getList().get(i).getName(), number.getFloat());
                            globalData.remove(0);
                        } else throw new Exception("Data does not match Float Type.");
                    }
                    //if not any of the above then it's assumed to be Integer type so check if current data is integer
                    else if (globalData.get(0).getClass().equals(IntegerNode.class)) {
                        IntegerNode number = (IntegerNode) globalData.get(0);
                        intData.put(((ReadNode) statement).getList().get(i).getName(), number.getInteger());
                        globalData.remove(0);
                    } else throw new Exception("Data does not match Integer Type or any other type");
                }
                //default next statement change
                if(statement.getNext() == null) done = true;
                else statement = statement.getNext();
            }
            //if statement is a Assignment Statement
            else if (statement.getClass().equals(AssignmentNode.class)) {
                assignmentStatement((AssignmentNode) statement);
                if(statement.getNext() == null) done = true;
                else statement = statement.getNext();
            }
            //If statement is a INPUT Statement
            else if (statement.getClass().equals(InputNode.class)) {
                String output = "";
                //check if INPUT has a string output
                if(((InputNode) statement).returnString() != null) {
                    System.out.println(((InputNode) statement).returnString().returnString()); //print INPUT string
                    Label label = new Label();
                    output = ((InputNode) statement).returnString().returnString();
                    label.setText(((InputNode) statement).returnString().returnString());
                    App.outputCode.add(label);
                }
                for (int i = 0; i < ((InputNode) statement).getList().size(); i++) {
                    String input = Window.prompt("INPUT: " + output, "");
                    Label label = new Label();
                    label.setText(input);
                    label.addStyleDependentName("line");
                    App.outputCode.add(label);

                    //check for variable type
                    if (checkVariableType(((InputNode) statement).getList().get(i), '$')) {
                        stringData.put(((InputNode) statement).getList().get(i).getName(), input);
                    } else if (checkVariableType(((InputNode) statement).getList().get(i), '$')) {
                        try {
                            Float number = Float.parseFloat(input);
                            floatData.put(((InputNode) statement).getList().get(i).getName(), number);
                        }catch(Exception e){
                            throw new Exception("Input does not match float variable type.");
                        }
                    } else {
                        try {
                            int number = Integer.parseInt(input);
                            intData.put(((InputNode) statement).getList().get(i).getName(), number);
                        } catch(Exception e){
                            throw new Exception("Input does not match integer variable type");
                        }
                    }
                }

                if(statement.getNext() == null) done = true;
                else statement = statement.getNext();
            }
            //if statement is a PRINT statement
            else if (statement.getClass().equals(PrintNode.class)) {
                System.out.print("\n");//output on new line
                StringBuilder print = new StringBuilder();
                //loop through each child of PRINT
                for (int i = 0; i < ((PrintNode) statement).getList().size(); i++) {
                    //if child is variable
                    if (((PrintNode) statement).getList().get(i).getClass().equals(VariableNode.class)) {
                        VariableNode variable = (VariableNode) ((PrintNode) statement).getList().get(i);
                        //go through each case
                        if (checkVariableType(variable, '$')) {
                            System.out.print(stringData.get(variable.getName()));
                            print.append(stringData.get(variable.getName()));
                        } else if (checkVariableType(variable, '%')) {
                            System.out.print(floatData.get(variable.getName()));
                            print.append(floatData.get(variable.getName()));
                        }
                        else {
                            System.out.print(intData.get(variable.getName()));
                            print.append(intData.get(variable.getName()));
                        }
                    }
                    //if child is stringNode
                    else if (((PrintNode) statement).getList().get(i).getClass().equals(StringNode.class)) {
                        StringNode string = (StringNode) ((PrintNode) statement).getList().get(i);
                        System.out.print(string.returnString());
                        print.append(string.returnString());
                    }
                    //for last part its assumed that it's an expression, a number, or a function
                    else {
                        Node node = evaluateMathop(((PrintNode) statement).getList().get(i), 0);
                        //use method depending on the class of Node
                        if(node.getClass().equals(FloatNode.class)) {
                            System.out.print(((FloatNode) node).getFloat());
                            print.append(((FloatNode) node).getFloat());
                        }
                        else if(node.getClass().equals(IntegerNode.class)) {
                            System.out.print(((IntegerNode) node).getInteger());
                            print.append(((IntegerNode) node).getInteger());
                        }
                        else if(node.getClass().equals(StringNode.class)) {
                            System.out.print(((StringNode) node).returnString());
                            print.append(((StringNode) node).returnString());
                        }
                    }
                }
                Label label = new Label();
                label.setText(new String(print));
                label.addStyleDependentName("line");
                App.outputCode.add(label);
                //statementLines.add(new String(print));
                if(statement.getNext() == null) done = true;
                else statement = statement.getNext();
            }
            //if statement is a IF statement
            else if(statement.getClass().equals(IfNode.class)){
                //check if boolean expression is true
                if(booleanExpression(((IfNode) statement).getexpression())){
                    //if label exists
                    if(labelData.containsKey(((IfNode) statement).getLabel().getName())) {
                        StatementNode statementnode =
                                (StatementNode) labelData.get(((IfNode) statement).getLabel().getName());
                        statementnode.setNext(null); //remove reference to next to prevent loops
                        interpret(statementnode); //interpret label
                    }
                    else throw new Exception("Label has not been initialized.");
                }
                if(statement.getNext() == null) done = true;
                else statement = statement.getNext();
            }
            //if statement is a FOR statement
            else if(statement.getClass().equals(ForNode.class)){
                AssignmentNode assignment = ((ForNode) statement).getVariable();
                IntegerNode step = (IntegerNode) ((ForNode) statement).getIncrement();
                boolean metLimit;
                //if variable already exists then change value
                if (intData.containsKey(assignment.getVariable().getName())) {
                    int value = intData.get(assignment.getVariable().getName());
                    intData.replace(assignment.getVariable().getName(), value + step.getInteger());
                }
                else { //else create new variable
                    IntegerNode value = (IntegerNode) evaluateMathop(assignment.getValue(), 0);
                    intData.put(assignment.getVariable().getName(), value.getInteger());
                }

                //Change boolean expression depending on STEP Value
                if(step.getInteger() > 0) {
                    metLimit = booleanExpression(new BooleanOperationNode(BooleanOperationNode.operator.lessthanequals,
                            new IntegerNode(intData.get(assignment.getVariable().getName())),
                            ((ForNode) statement).getLimit()));
                }
                else{
                    metLimit = booleanExpression(new BooleanOperationNode(
                            BooleanOperationNode.operator.greaterthanequals,
                            new IntegerNode(intData.get(assignment.getVariable().getName())),
                            ((ForNode) statement).getLimit()));
                }
                if(metLimit) statement = statement.getNext();
                else {
                    //go to special NextNode reference
                    statement = (StatementNode) ((ForNode) statement).getNextNode();
                    intData.remove(assignment.getVariable().getName()); //remove local variable
                    if(statement == null) done = true;
                }
            }
            //if statement is NEXT statement
            else if(statement.getClass().equals(NextNode.class)){
                //loop back to For Statement
                statement = (StatementNode) ((NextNode) statement).getFor();
            }
            //if statement is a GOSUB Statement
            else if(statement.getClass().equals(GosubNode.class)){
                stackGoSub.push(statement.getNext()); //push next statement into stack
                //set next node to label node
                statement = (StatementNode) labelData.get(
                        ((GosubNode) statement).getIdentifier().getName());
                if(statement == null) throw new Exception("Label does not exist in GOSUB statement.");
            }
            //if statement is a RETURN statement
            else if(statement.getClass().equals(ReturnNode.class)){
                //if stack is empty move on to next statement
                if(stackGoSub.empty()){
                    if(statement.getNext() == null) done = true;
                    else statement = statement.getNext();
                }
                else {
                    statement = stackGoSub.pop();
                    if(statement == null) done = true;
                }
            }
        }
    }

    /**
     * assignmentStatement evaluates expressions
     * @param assignment what to evaluate
     * @throws Exception throw any errors
     */
    private void assignmentStatement(AssignmentNode assignment) throws Exception {
        //check if variable is a string type
        if (checkVariableType(assignment.getVariable(), '$')) {
            //make sure value on other side is a string
            if (assignment.getValue().getClass().equals(StringNode.class)) {
                StringNode str = (StringNode) assignment.getValue();
                //check if variable is already in hashmap, if so update it
                if (stringData.containsKey(assignment.getVariable().getName()))
                    stringData.replace(assignment.getVariable().getName(), str.returnString());
                else stringData.put(assignment.getVariable().getName(), str.returnString());
            }
            else if(assignment.getValue().getClass().equals(FunctionNode.class)){
                FunctionNode function = (FunctionNode) assignment.getValue();
                stringData.put(assignment.getVariable().getName(), stringFunctions(function));
            }
            //other side of assign is another variable
            else if(assignment.getValue().getClass().equals(VariableNode.class)){
                VariableNode variable = (VariableNode) assignment.getValue();
                String str = stringData.get(variable.getName()); //get value of variable
                if(str == null) throw new Exception("Conflicting variable types or " +
                        "variable has not been declared");
                if (stringData.containsKey(assignment.getVariable().getName()))
                    stringData.replace(assignment.getVariable().getName(), str);
                else stringData.put(assignment.getVariable().getName(), str);
            }
            else throw new Exception("Trying to assign non String value to String variable.");
        }
        //check if variable is a float type
        else if (checkVariableType(assignment.getVariable(), '%')) {
            FloatNode answer = (FloatNode) evaluateMathop(assignment.getValue(), 0);
            if (floatData.containsKey(assignment.getVariable().getName()))
                floatData.replace(assignment.getVariable().getName(), answer.getFloat());
            else floatData.put(assignment.getVariable().getName(), answer.getFloat());
        }
        //default case is a int variable
        else {
            IntegerNode answer = (IntegerNode) evaluateMathop(assignment.getValue(), 0);
            if (intData.containsKey(assignment.getVariable().getName()))
                intData.replace(assignment.getVariable().getName(), answer.getInteger());
            else intData.put(assignment.getVariable().getName(), answer.getInteger());
        }
    }

    /**
     * booleanExpression evaluates boolean expressions
     * @param statement the statement to evaluate
     * @return boolean true or false
     * @throws Exception Throw errors
     */
    private boolean booleanExpression(BooleanOperationNode statement) throws Exception {
        FloatNode leftSide = (FloatNode) evaluateMathop(statement.getLeft(), 1);
        FloatNode rightSide = (FloatNode) evaluateMathop(statement.getRight(), 1);
        BooleanOperationNode.operator operator = statement.getSign();
        boolean result = false;

        if(operator == BooleanOperationNode.operator.equals){
            if(leftSide.getFloat() == rightSide.getFloat()) result = true;
        }
        else if(operator == BooleanOperationNode.operator.greaterthan){
            if(leftSide.getFloat() > rightSide.getFloat()) result = true;
        }
        else if(operator == BooleanOperationNode.operator.lessthan){
            if(leftSide.getFloat() < rightSide.getFloat()) result = true;
        }
        else if(operator == BooleanOperationNode.operator.greaterthanequals){
            if(leftSide.getFloat() >= rightSide.getFloat()) result = true;
        }
        else if(operator == BooleanOperationNode.operator.lessthanequals){
            if(leftSide.getFloat() <= rightSide.getFloat()) result = true;
        }
        else if(operator == BooleanOperationNode.operator.notequals){
            if(leftSide.getFloat() != rightSide.getFloat()) result = true;
        }

        return result;
    }
    /**
     * Handles string functions
     * @param function input function
     * @return string output of function
     * @throws Exception Throw exception if it's not a string type
     */
    private String stringFunctions(FunctionNode function) throws Exception {
        //for NUM$
        if(function.getFunction().equals("NUM$")){
            //if number is integer
            if(function.getParams().get(0).getClass().equals(IntegerNode.class)) {
                IntegerNode number = (IntegerNode) function.getParams().get(0);
                return String.valueOf(number.getInteger());
            }
            //if its a variable
            else if(function.getParams().get(0).getClass().equals(VariableNode.class)){
                VariableNode variable = (VariableNode) function.getParams().get(0);
                if(checkVariableType(variable, '%')){
                    return String.valueOf(floatData.get(variable.getName()));
                }
                else if(checkVariableType(variable, '$')) throw new Exception("Can't use NUM$ on String.");
                else return String.valueOf(intData.get(variable.getName()));
            }
            //or it's a float
            else{
                FloatNode number = (FloatNode) function.getParams().get(0);
                return String.valueOf(number.getFloat());
            }
        }
        //next three are for left, right, and middle.
        else if(function.getFunction().equals("LEFT$")){
            String str = doesStringExist(function.getParams().get(0));
            int left = doesIntExist(function.getParams().get(1));
            return str.substring(0, left);
        }
        else if(function.getFunction().equals("RIGHT$")){
            String str = doesStringExist(function.getParams().get(0));
            int right = doesIntExist(function.getParams().get(1));
            return str.substring(str.length() - right);
        }
        else if(function.getFunction().equals("MID$")) {
            String str = doesStringExist(function.getParams().get(0));
            int first = doesIntExist(function.getParams().get(1));
            int second = doesIntExist(function.getParams().get(2));
            return str.substring(first, first + second);
        }
        else throw new Exception("No String return type function found.");
    }

    /**
     * Used to check if Node is VariableNode or StringNode
     * @param variable Node to check
     * @return string value
     * @throws Exception If node does not contain string
     */
    private String doesStringExist(Node variable) throws Exception{
        if(variable.getClass().equals(VariableNode.class)){
            return stringData.get(((VariableNode) variable).getName());
        }
        else if(variable.getClass().equals(StringNode.class)) return ((StringNode) variable).returnString();
        else throw new Exception("No String value found.");
    }

    /**
     * Used to check if Node is VariableNode or IntegerNode
     * @param variable Node to check
     * @return int value stored in Node
     * @throws Exception If Node does not contain integer
     */
    private int doesIntExist(Node variable) throws Exception {
        if(variable.getClass().equals(VariableNode.class)){
            return intData.get(((VariableNode) variable).getName());
        }
        else if(variable.getClass().equals(IntegerNode.class)) return ((IntegerNode) variable).getInteger();
        else throw new Exception("No Int value found.");
    }

    /**
     * Handles float functions
     * @param function the function to perform
     * @return The functions return value
     * @throws Exception If function is not float
     */
    private FloatNode floatfunctions(FunctionNode function) throws Exception{
        if(function.getFunction().equals("VAL%")){
            String str = doesStringExist(function.getParams().get(0));
            return new FloatNode(Float.parseFloat(str));
        }
        else throw new Exception("No Int return type function found.");
    }

    /**
     * Handles integer functions
     * @param function The function to perform
     * @return The functions return value
     * @throws Exception If function is not int type
     */
    private IntegerNode intfunctions(FunctionNode function) throws Exception {
        if(function.getFunction().equals("RANDOM")){
            Random rand = new Random();
            return new IntegerNode(rand.nextInt());
        }
        else if(function.getFunction().equals("VAL")){
            String str = doesStringExist(function.getParams().get(0));
            return new IntegerNode(Integer.parseInt(str));
        }
        else throw new Exception("No Int return type function found.");
    }

    /**
     * evaluateMathop evaluates a MathOpNode
     * @param number The node to evaluate
     * @param booleantrue sets the mode of the method, makes it so method only returns floats
     * @return An IntegerNode, FloatNode, or MathOpNode
     * @throws Exception Throw error of types don't match
     */
    private Node evaluateMathop(Node number, int booleantrue) throws Exception{
        //if a number node return
        if(number.getClass().equals(FloatNode.class)){
            return number;
        }
        else if(number.getClass().equals(IntegerNode.class)) {
            if(booleantrue == 1) return new FloatNode(((IntegerNode) number).getInteger());
            else return number;
        }
        //if a variable return value of variable
        else if(number.getClass().equals(VariableNode.class)){
            if(checkVariableType((VariableNode) number, '%'))
                return new FloatNode(floatData.get(((VariableNode) number).getName()));
            //can't be a string
            else if(checkVariableType((VariableNode) number, '$'))
                throw new Exception("Can't perform operations on Strings.");
            else {
                if(booleantrue == 1){
                    return new FloatNode(intData.get(((VariableNode) number).getName()));
                }
                else return new IntegerNode(intData.get(((VariableNode) number).getName()));
            }
        }
        //if its a function node
        else if(number.getClass().equals(FunctionNode.class)){
            //matches the function with every possible function
            //if no match then throw exception
            try {
                return intfunctions((FunctionNode) number);
            } catch(Exception e){
                try {
                    return floatfunctions((FunctionNode) number);
                } catch(Exception f){
                    try {
                        return new StringNode(stringFunctions((FunctionNode) number));
                    } catch(Exception s){
                        throw new Exception("Function does not exist.");
                    }
                }
            }
        }
        //if its a mathopNode
        else if(number.getClass().equals(MathOpNode.class)){
            //recursive calls
            Node left = evaluateMathop(((MathOpNode) number).getLeft(), booleantrue);
            Node right = evaluateMathop(((MathOpNode) number).getRight(), booleantrue);
            //if both sides are ints then call evaluateIntMathOp
            if(left.getClass().equals(IntegerNode.class) && right.getClass().equals(IntegerNode.class) &&
                    booleantrue == 0){
                return evaluateIntMathOp((IntegerNode) left, (IntegerNode) right, ((MathOpNode) number).getOperation());
            }
            else return evaluateFloatMathop(left, right, ((MathOpNode) number).getOperation());
        }
        else throw new Exception("Unable to perform operation.");
    }

    /**
     * A float version of evaluateMathop
     * @param rightSide Can be IntegerNode or FloatNode
     * @param leftSide Can be IntegerNode or FloatNode
     * @param operation The operation to perform
     * @return The simplified answer
     */
    private FloatNode evaluateFloatMathop(Node leftSide, Node rightSide, MathOpNode.operation operation) {
        FloatNode left, right;

        //both left and right can be integers
        if (leftSide.getClass().equals(IntegerNode.class) && rightSide.getClass().equals(IntegerNode.class)) {
            IntegerNode temp1 = (IntegerNode) leftSide;
            IntegerNode temp2 = (IntegerNode) rightSide;
            left = new FloatNode(temp1.getInteger());
            right = new FloatNode(temp2.getInteger());
        } else if (leftSide.getClass().equals(IntegerNode.class)) {
            IntegerNode temp = (IntegerNode) leftSide;
            left = new FloatNode(temp.getInteger());
            right = (FloatNode) rightSide;
        } else if (rightSide.getClass().equals(IntegerNode.class)) {
            IntegerNode temp = (IntegerNode) rightSide;
            right = new FloatNode(temp.getInteger());
            left = (FloatNode) leftSide;
        } else {
            left = (FloatNode) leftSide;
            right = (FloatNode) rightSide;
        }
        //solve
        if (operation == MathOpNode.operation.add){
            return new FloatNode(left.getFloat() + right.getFloat());
        }
        else if(operation == MathOpNode.operation.subtract){
            return new FloatNode(left.getFloat() - right.getFloat());
        }
        else if(operation == MathOpNode.operation.times){
            return new FloatNode(left.getFloat() * right.getFloat());
        } else {
            return new FloatNode(left.getFloat()/right.getFloat());
        }
    }

    /**
     * Evaluates Math Operations with only ints
     * @param left A node that contains IntegerNode
     * @param right A node that contains a IntegerNode
     * @param operation The operation to perform
     * @return a solved math operations
     */
    private IntegerNode evaluateIntMathOp(IntegerNode left, IntegerNode right, MathOpNode.operation operation) {
        FloatNode temp = evaluateFloatMathop(left, right, operation);
        return new IntegerNode((int) temp.getFloat());
    }

    /**
     * Checks if variable is string or float
     * @param node Variable node to check
     * @param type The char type. Should be '%' or '$'
     * @return if they match or not
     */
    private boolean checkVariableType(VariableNode node, char type){
        return node.getName().charAt(node.getName().length() - 1) == type;
    }

    /**
     * Walks through ast node to find label statements
     */
    private void walkforLabel(){
        for(int i = 0; i < astList.size(); i++){
            if(astList.get(i).getClass().equals(LabeledStatementNode.class)){
                LabeledStatementNode label = (LabeledStatementNode)astList.get(i);
                StatementNode statement = label.getStatement();
                if (astList.size() == i + 1) statement.setNext(null);
                else statement.setNext(astList.get(i+1));
                //Populate the map with label statement
                labelData.put(label.getLabel(), statement);
                //replace label Node with child Node
                astList.set(i, label.getStatement());
            }
        }
    }

    /**
     * Walks through the ast to find For and Next Statements
     * @throws Exception When there is no Next Node for a For Node
     */
    private void walkforFor() throws Exception {
        for(int i = 0; i < astList.size(); i++){
            if(astList.get(i).getClass().equals(ForNode.class)){
                ForNode forNode = (ForNode)astList.get(i);
                boolean hasNext = false;
                for(int j = i; j < astList.size(); j++){
                    if (astList.get(j).getClass().equals(NextNode.class)) {
                        hasNext = true;
                        NextNode next = (NextNode)astList.get(j);
                        //add reference to ForNode
                        next.setFor(forNode);
                        //add reference to Node after NextNode
                        if(j + 1 == astList.size()){
                            forNode.nextStatement(null);
                        }
                        else forNode.nextStatement(astList.get(j+1));
                        astList.set(j, next);
                        astList.set(i, forNode);
                        break;
                    }
                }
                if(!hasNext) throw new Exception("No Next statement found after For Statement.");
            }
        }
    }

    /**
     * Walk through AST to find Data Statement
     */
    private void walkforData(){
        globalData = new ArrayList<Node>();
        for(int i = 0; i < astList.size(); i++){
            if(astList.get(i).getClass().equals(DataNode.class)){
                DataNode data = (DataNode)astList.get(i);
                globalData.addAll(data.getList()); //add contents to list
                astList.remove(i); //remove node
            }
        }
    }

    /**
     * Walk through AST and set next Node for each Node
     */
    private void walkforNext(){
        for(int i = 0; i < astList.size(); i++){
            //if last statement set next to null
            if(i+1 == astList.size()) astList.get(i).setNext(null);
            else astList.get(i).setNext(astList.get(i+1));
        }
    }
}
