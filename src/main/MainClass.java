/**
 * Created by azadabbasi on 4/10/14.2
 */
package main;
import obfuscator.Obfuscator;
import obfuscator.ControlFlowObfuscator;
import obfuscator.ParameterObfuscator;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import parser.ASTgenerator;
import parser.InputReader;
import parser.LuaLexer;
import parser.LuaParser;

import unparser.TreeConstructor;
import unparser.Unparser;
import org.apache.commons.cli.*;

public class MainClass {

        public static void main(String[] args) throws Exception {
            String obfuName; // used to select obfuscation

            /**
             * The ARGUMENT HANDLER STARTS HERE
             */
            //------------------------------------------------------------------------
            if(args.length < 2){
                System.out.println("\n    This class takes 3 arguments:" +
                        "\n     1:the path to the input file which contains the Lua code" +
                        "\n     2:the path to the desired output path" );
                return;
            }
            Options options = new Options();

            options.addOption("d", false, "Delete records"); // does not have a value
            options.addOption("c", true, "CSV Repository"); // has a value
            /**
             * THE ARGUMENT HANDLER ENDS HERE
             */
            //------------------------------------------------------------------------

            /**
             * THE PARSER STARTS HERE
             */
            //------------------------------------------------------------------------
            LuaLexer lexer = new LuaLexer(new ANTLRFileStream(args[0]));
            LuaParser parser = new LuaParser(new CommonTokenStream(lexer));
            CommonTree tree =  parser.parse().getTree();

            // we can create an unstructured tree using the following method
            String treeString = tree.toStringTree();

            /**now we feed the common root "root" to the createStructuredTree
             and feed the Final string to the input reader to put it in a file.
             **/
            //ASTgenerator reads the tree into a structured tree
            ASTgenerator myAST = new ASTgenerator(tree);
            String treeStructure = myAST.getAST();
//            System.out.println(treeString);

            //static method printToFile prints the tree into a file.
            InputReader.printToFile("output.txt", treeStructure);


            /**
             * PARSER ENDS HERE
             */
            //-------------------------------------------------------------------------

            /**
             * The OBFUSCATOR STARTS HERE
             */
            //-------------------------------------------------------------------------
            //the Obfuscator goes here , whatever level we decide to have

            // Areej : minimum Vocab Obfuscator
                            Obfuscator myOb = new Obfuscator("output.txt","median.txt");
                            //call the fileProcessing Function
                            try{

                  /* Key words{ MinVocab,Reverse,XOR,ILOveOU,Boss,Confusing} = obfuName */

                                myOb.FileProcessing("Confusing");
                            }catch(Exception e){e.printStackTrace();
                            }

//            Areej : ControlFlowObfuscator


            TreeConstructor t = new TreeConstructor("median.txt");
            ControlFlowObfuscator cfo = new ControlFlowObfuscator(t.getRoot());
            //call the Function
            cfo.CFOObfuscate();
            InputReader.printToFile("obfuscatedAST.txt", t.toString());



            ParameterObfuscator o = new ParameterObfuscator(t.getRoot());
            //call the Function
            o.addParams();
            InputReader.printToFile("obfuscatedAST.txt", t.toString());

//
//            Amanda's obfuscator
//            TreeConstructor t = new TreeConstructor(args[1]);
//            ParameterObfuscator o = new ParameterObfuscator(t.getRoot());
//            o.addParams();
//            InputReader.printToFile(args[2], tree.toString());


            /**
             * The OBFUSCATOR ENDS HERE
             */
            //-------------------------------------------------------------------------

            /**
             * The UNPARSER STARTS HERE
             */
            //-------------------------------------------------------------------------
//            read the AST file back to a tree
            TreeConstructor myTree = new TreeConstructor("obfuscatedAST.txt");
            InputReader.printToFile("obfuscatedAST.txt",myTree.toString());
            Unparser myUnparser = new Unparser(myTree.getRoot());
            myUnparser.unparse();
            System.out.println(myUnparser.getCode());
            InputReader.printToFile(args[1],myUnparser.getCode());

//            System.out.println(Obfuscator.confusingString());
//            -------------------------------------------------------------------------
        }
    }