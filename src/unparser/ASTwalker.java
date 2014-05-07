/**
 * Created by Azad on 5/5/2014.
 * this file will walk through an AST
 * file and regenerates the tree
 */
package unparser;
import parser.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

public class ASTwalker {
    public static StringTokenizer st ;
    public static Node treeRoot;
    public static ArrayList<String> Keywords = new ArrayList<String>(
                Arrays.asList("ASSIGNMENT", "ASSIGNMENT_VAR","CALL",
                        "CHUNK", "COL_CALL","CONDITION", "EXPR_LIST",
                        "FIELD", "FIELD_LIST", "FOR_IN", "FUNCTION",
                        "INDEX", "LABEL","LOCAL_ASSIGNMENT", "NAME_LIST",
                        "PARAM_LIST", "TABLE", "UNARY_MINUS","VAR",
                        "VAR_LIST","FUNCTION_FUNCTION"));
    public static Stack<String> myNodeStack = new Stack<String>();
    public static String finalTreeString = "";

    public ASTwalker(String path){
        InputReader read = new InputReader(path);
        String ast = read.getString();
        st = new StringTokenizer(ast);
    }


    //recursive function to create a tree.
    public static Node treeConstructor(String root){
        Node thisNode = new Node(root);
        String currentToken;
        int index = 0 ;
        while(st.hasMoreTokens()){
            currentToken = st.nextToken();

            //if the current token is not a "(" or ")" then it's either a string or another child
            //with no furthur children

            if(!(currentToken.equals(")") || currentToken.equals("("))){
                if(!currentToken.equals("'")){
                    thisNode.addChild(new Node(currentToken));

                }
                else{
                    thisNode.addChild(treeConstructor(getStringToken()));
                }
//                currentToken = st.nextToken();
            }
            //what happens if we see a closing parenthesis here
            else if(currentToken.equals("(")){
                thisNode.addChild(treeConstructor(st.nextToken()));

            }

            //what if we see an opening parenthesis here
            else if(currentToken.equals(")")){
                return thisNode;
            }
        }

        return thisNode;
    }


    //when we see a "'" token, we wait for the next one for them to be the next token all together
    //it means it's a string
    public static String getStringToken(){
        String currentToken = st.nextToken();
        String finalString="";
        while(!currentToken.equals("'")){
            finalString+=currentToken;
            currentToken=st.nextToken();
        }
        st.nextToken();
        return finalString;
    }

    // this function returns true if the node is
    //ending in ")"
    public static boolean handleStack(String str){
        if(str.equals(")")){
            myNodeStack.pop();
            return false;
        }
        else {
            myNodeStack.push("(");
            return true;
        }
    }

    // print the tokens of a file given in the path
    public static void printTokens(String path){
        InputReader read = new InputReader(path);
        String ast = read.getString();
        StringTokenizer wb = new StringTokenizer(ast);
        while(wb.hasMoreTokens()){
            System.out.println(wb.nextToken());
        }
    }
    private static void printStructuredTree(Node tree, int Indention){
        //Create the indention first
        String indent="";
        if (Indention > 0 ) {
            indent = new String(new char[Indention]).replace('\0', '\t');
        }
        //add indents to the final tree
        finalTreeString += indent;
        //add the beginning parenthesis
        finalTreeString += "(";
        //now we add the current Tree's name
        finalTreeString += tree.getName();


        //now we will have to print the children
        if(tree.getChildCount() > 0 ) {
            for (int i = 0 ; i<tree.getChildCount() ; i++){
                //if this child didn't have any children
                if(tree.getChild(i).getChildCount() == 0 ){
                    finalTreeString = finalTreeString + " " + tree.getChild(i).getName();
                }
                else{
                    finalTreeString += "\n";
                    for(int j = i ; j<tree.getChildCount(); j++){
                        printStructuredTree(tree.getChild(j),Indention+1);
                    }
                    finalTreeString = finalTreeString + indent + ")\n";
                    break;
                }
                if(i == tree.getChildCount()-1)
                    finalTreeString += ")\n";
            }
        }
        //else if you don't have any children
        else{
            finalTreeString += ")\n";
        }
    }

    public static void main(String[] args) {
        TreeConstructor tree = new TreeConstructor(args[0]);
        tree.printStructuredTree();
    }
}
