import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

class SymbolExtractor extends LittleBaseListener {

    ArrayList<Node> nodes = new ArrayList<>();
    Node currentNode = new Node();
    boolean inAssignmentExpression = false;
    int t = 1;

    Stack<SymbolTable> tableStack = new Stack<>();

    ArrayList<SymbolTable> print_table_list = new ArrayList<>();

    ArrayList<String> p_types = new ArrayList<>();
    ArrayList<String> p_names = new ArrayList<>();

    String current_type;
    String name;
    String value;
    boolean enter_id_list = false;
    boolean enter_param_dec_list = false;
    boolean inRead = false;
    boolean inWrite = false;
    int register = 0;
    HashMap<String, Integer> regs = new HashMap<>();

    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        String scope = "GLOBAL";
        SymbolTable symTab = new SymbolTable(scope);
        tableStack.push(symTab);
        print_table_list.add(symTab);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */

    @Override public void exitProgram(LittleParser.ProgramContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId(LittleParser.IdContext ctx) {
            name = ctx.IDENTIFIER().getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId(LittleParser.IdContext ctx) {
        if((enter_id_list || enter_param_dec_list) && !inRead && !inWrite){
            tableStack.peek().addSymbol(name, new Symbol(current_type, "0"));
        }
        if(inRead || inWrite){
            Operands op = new Operands();
            op.sval = name;
            op.type = print_table_list.get(0).symbolTable.get(op.sval).type;
            currentNode.ops.add(op);
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPgm_body(LittleParser.Pgm_bodyContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPgm_body(LittleParser.Pgm_bodyContext ctx) {
        /*
        for(int i = 0; i < print_table_list.size(); ++i){
            SymbolTable cur = print_table_list.get(i);
            System.out.println("Symbol table " + cur.getScope());
            for(int j = 0; j < cur.symbolNames.size(); ++j){
                if(Objects.equals(cur.symbolTable.get(cur.symbolNames.get(j)).type, "STRING")){
                    System.out.println("name " + cur.symbolNames.get(j) + " type STRING value " + cur.symbolTable.get(cur.symbolNames.get(j)).value);
                }else{
                    System.out.println("name " + cur.symbolNames.get(j) + " type " + cur.symbolTable.get(cur.symbolNames.get(j)).type);
                }
            }
            System.out.println();
        }
         */
        /*
        for(int i = 0; i < nodes.size(); ++i){
            System.out.print(nodes.get(i).ops.get(0).sval);
            System.out.print(" ");
            System.out.print(nodes.get(i).fnct);
            System.out.print(" ---- ");
            for(int j = 1; j < nodes.get(i).ops.size(); ++j){
                System.out.print(nodes.get(i).ops.get(j).sval);
                System.out.print(" ");
            }
            System.out.println();
        }
        */

        System.out.println(";IR CODE");
        System.out.println(";LABEL main");
        System.out.println(";LINK");

        for(int i = 0; i < nodes.size(); ++i){
            if(Objects.equals(nodes.get(i).fnct, ";ASSIGNMENT")){
                if(nodes.get(i).ops.get(1).isInt){
                    System.out.print("STOREI ");
                    System.out.print(nodes.get(i).ops.get(1).ival);
                    System.out.print(" ");
                    System.out.print("$T" + t);
                    System.out.println();
                    System.out.print(";STOREI $T" + t++ + " " + nodes.get(i).ops.get(0).sval);
                    System.out.println();
                }else{
                    System.out.print(";STOREF ");
                    System.out.print(nodes.get(i).ops.get(1).fval);
                    System.out.print(" ");
                    System.out.print("$T" + t);
                    System.out.println();
                    System.out.print(";STOREF $T" + t++ + " " + nodes.get(i).ops.get(0).sval);
                    System.out.println();
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "READ")){
                if(Objects.equals(nodes.get(i).ops.get(1).type, "INT")){
                    for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                        System.out.print(";READI ");
                        System.out.print(nodes.get(i).ops.get(j).sval);
                        System.out.println();
                    }
                }else{
                    for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                        System.out.print(";READF ");
                        System.out.print(nodes.get(i).ops.get(j).sval);
                        System.out.println();
                    }
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "*")){
                boolean litFound = false;
                int litCount = 0;
                int litPos = 0;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(nodes.get(i).ops.get(j).isLit){
                        litFound = true;
                        ++litCount;
                        litPos = j;
                    }
                }
                if(litFound){
                    if(nodes.get(i).ops.get(1).isLit){
                        if(nodes.get(i).ops.get(1).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(1).ival + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(1).fval + " $T" + t++);
                        }
                    }
                    if(nodes.get(i).ops.get(2).isLit){
                        if(nodes.get(i).ops.get(2).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }
                    }
                    if(litCount == 1){
                        if(litPos == 1){
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";MULTI " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }else{
                                System.out.println(";MULTF " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }
                        }else{
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";MULTI " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }else{
                                System.out.println(";MULTF " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }
                        }

                    }else{
                        if(nodes.get(i).ops.get(0).isInt){
                            System.out.println(";MULTI " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }else{
                            System.out.println(";MULTF " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }
                    }

                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println(";STOREI $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.println(";STOREF $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }

                }else{
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";MULTI ");
                    }else{
                        System.out.print(";MULTF ");
                    }
                    System.out.print(nodes.get(i).ops.get(1).sval);
                    System.out.print(" ");
                    System.out.print(nodes.get(i).ops.get(2).sval);
                    System.out.print(" ");
                    String cur = "$T" + t++;
                    System.out.print(cur + "\n");
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";STOREI " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.print(";STOREF " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }
                    System.out.println();
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "+")){
                boolean litFound = false;
                int litCount = 0;
                int litPos = 0;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(nodes.get(i).ops.get(j).isLit){
                        litFound = true;
                        ++litCount;
                        litPos = j;
                    }
                }
                if(litFound){
                    if(nodes.get(i).ops.get(1).isLit){
                        if(nodes.get(i).ops.get(1).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(1).ival + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(1).fval + " $T" + t++);
                        }
                    }
                    if(nodes.get(i).ops.get(2).isLit){
                        if(nodes.get(i).ops.get(2).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }
                    }
                    if(litCount == 1){
                        if(litPos == 1){
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";ADDI " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }else{
                                System.out.println(";ADDF " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }
                        }else{
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";ADDI " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }else{
                                System.out.println(";ADDF " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }
                        }

                    }else{
                        if(nodes.get(i).ops.get(0).isInt){
                            System.out.println(";ADDI " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }else{
                            System.out.println(";ADDF " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }
                    }

                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println(";STOREI $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.println(";STOREF $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }

                }else{
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";ADDI ");
                    }else{
                        System.out.print(";ADDF ");
                    }
                    System.out.print(nodes.get(i).ops.get(1).sval);
                    System.out.print(" ");
                    System.out.print(nodes.get(i).ops.get(2).sval);
                    System.out.print(" ");
                    String cur = "$T" + t++;
                    System.out.print(cur + "\n");
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";STOREI " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.print(";STOREF " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }
                    System.out.println();
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "-")){
                boolean litFound = false;
                int litCount = 0;
                int litPos = 0;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(nodes.get(i).ops.get(j).isLit){
                        litFound = true;
                        ++litCount;
                        litPos = j;
                    }
                }
                if(litFound){
                    if(nodes.get(i).ops.get(1).isLit){
                        if(nodes.get(i).ops.get(1).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(1).ival + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(1).fval + " $T" + t++);
                        }
                    }
                    if(nodes.get(i).ops.get(2).isLit){
                        if(nodes.get(i).ops.get(2).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }
                    }
                    if(litCount == 1){
                        if(litPos == 1){
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";SUBI " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }else{
                                System.out.println(";SUBF " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }
                        }else{
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";SUBI " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }else{
                                System.out.println(";SUBF " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }
                        }

                    }else{
                        if(nodes.get(i).ops.get(0).isInt){
                            System.out.println(";SUBI " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }else{
                            System.out.println(";SUBF " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }
                    }

                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println(";STOREI $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.println(";STOREF $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }

                }else{
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";SUBI ");
                    }else{
                        System.out.print(";SUBF ");
                    }
                    System.out.print(nodes.get(i).ops.get(1).sval);
                    System.out.print(" ");
                    System.out.print(nodes.get(i).ops.get(2).sval);
                    System.out.print(" ");
                    String cur = "$T" + t++;
                    System.out.print(cur + "\n");
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";STOREI " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.print(";STOREF " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }
                    System.out.println();
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "/")){
                boolean litFound = false;
                int litCount = 0;
                int litPos = 0;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(nodes.get(i).ops.get(j).isLit){
                        litFound = true;
                        ++litCount;
                        litPos = j;
                    }
                }
                if(litFound){
                    if(nodes.get(i).ops.get(1).isLit){
                        if(nodes.get(i).ops.get(1).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(1).ival + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(1).fval + " $T" + t++);
                        }
                    }
                    if(nodes.get(i).ops.get(2).isLit){
                        if(nodes.get(i).ops.get(2).isInt){
                            System.out.println(";STOREI " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }else{
                            System.out.println(";STOREF " + nodes.get(i).ops.get(2).fval + " $T" + t++);
                        }
                    }
                    if(litCount == 1){
                        if(litPos == 1){
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";DIVI " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }else{
                                System.out.println(";DIVF " + "$T" + (t - 1) + " " + nodes.get(i).ops.get(2).sval  + " $T" + t++);
                            }
                        }else{
                            if(nodes.get(i).ops.get(0).isInt){
                                System.out.println(";DIVI " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }else{
                                System.out.println(";DIVF " + nodes.get(i).ops.get(1).sval + " $T" + (t - 1) + " $T" + t++);
                            }
                        }
                    }else{
                        if(nodes.get(i).ops.get(0).isInt){
                            System.out.println(";DIVI " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }else{
                            System.out.println(";DIVF " + "$T" + (t - 2) + " $T" + (t - 1) + " $T" + t++);
                        }
                    }

                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println(";STOREI $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.println(";STOREF $T" + (t - 1) + " " + nodes.get(i).ops.get(0).sval);
                    }


                }else{
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";DIVI ");
                    }else{
                        System.out.print(";DIVF ");
                    }
                    System.out.print(nodes.get(i).ops.get(1).sval);
                    System.out.print(" ");
                    System.out.print(nodes.get(i).ops.get(2).sval);
                    System.out.print(" ");
                    String cur = "$T" + t++;
                    System.out.print(cur + "\n");
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.print(";STOREI " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }else{
                        System.out.print(";STOREF " + cur + " " + nodes.get(i).ops.get(0).sval);
                    }
                    System.out.println();
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "WRITE")){
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(Objects.equals(nodes.get(i).ops.get(j).type, "INT")){
                        System.out.println(";WRITEI " + nodes.get(i).ops.get(j).sval);
                    }else if(Objects.equals(nodes.get(i).ops.get(j).type, "FLOAT")){
                        System.out.println(";WRITEF " + nodes.get(i).ops.get(j).sval);
                    }else{
                        System.out.println(";WRITES " + nodes.get(i).ops.get(j).sval);
                    }
                }
            }

        }
        System.out.println(";RET");
        System.out.println(";tiny code");


        for(int j = 0; j < print_table_list.get(0).symbolNames.size(); ++j) {
            String key = print_table_list.get(0).symbolNames.get(j);
            if (Objects.equals(print_table_list.get(0).symbolTable.get(key).type, "STRING")) {
                System.out.println("str " + key + " " + print_table_list.get(0).symbolTable.get(key).value);
            } else {
                System.out.println("var " + key);
            }
        }

        for(int i = 0; i < nodes.size(); ++i){
            if(Objects.equals(nodes.get(i).fnct, "ASSIGNMENT")){
                System.out.print("move ");
                System.out.print(nodes.get(i).ops.get(1).sval);
                System.out.print(" ");
                System.out.print("r" + register);
                System.out.println();
                System.out.print("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                if(regs.containsKey(nodes.get(i).ops.get(0).sval)){
                    regs.replace(nodes.get(i).ops.get(0).sval, register);
                }else{
                    regs.put(nodes.get(i).ops.get(0).sval, register);
                }
                register++;
                System.out.println();
            }

            if(Objects.equals(nodes.get(i).fnct, "READ")){
                if(Objects.equals(nodes.get(i).ops.get(1).type, "INT")){
                    for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                        System.out.print("sys readi ");
                        System.out.print(nodes.get(i).ops.get(j).sval);
                        System.out.println();
                    }
                }else{
                    for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                        System.out.print("sys readr ");
                        System.out.print(nodes.get(i).ops.get(j).sval);
                        System.out.println();
                    }
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "*")){
                boolean litFound = false;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if (nodes.get(i).ops.get(j).isLit) {
                        litFound = true;
                        break;
                    }
                }
                if(litFound){
                    System.out.println("move " + nodes.get(i).ops.get(2).sval + " r" + register++);
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("muli r" + (register - 1) + " r" + register);
                    }else{
                        System.out.println("mulr r" + (register - 1) + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }else{
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("muli " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }else{
                        System.out.println("mulr " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "+")){
                boolean litFound = false;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if (nodes.get(i).ops.get(j).isLit) {
                        litFound = true;
                        break;
                    }
                }
                if(litFound){
                    System.out.println("move " + nodes.get(i).ops.get(2).sval + " r" + register++);
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("addi r" + (register - 1) + " r" + register);
                    }else{
                        System.out.println("addr r" + (register - 1) + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }else{
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("addi " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }else{
                        System.out.println("addr " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "-")){
                boolean litFound = false;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if (nodes.get(i).ops.get(j).isLit) {
                        litFound = true;
                        break;
                    }
                }
                if(litFound){
                    System.out.println("move " + nodes.get(i).ops.get(2).sval + " r" + register++);
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("subi r" + (register - 1) + " r" + register);
                    }else{
                        System.out.println("subr r" + (register - 1) + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }else{
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("subi " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }else{
                        System.out.println("subr " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "/")){
                boolean litFound = false;
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if (nodes.get(i).ops.get(j).isLit) {
                        litFound = true;
                        break;
                    }
                }
                if(litFound){
                    System.out.println("move " + nodes.get(i).ops.get(2).sval + " r" + register++);
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("divi r" + (register - 1) + " r" + register);
                    }else{
                        System.out.println("divr r" + (register - 1) + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }else{
                    System.out.println("move " + nodes.get(i).ops.get(1).sval + " r" + register);
                    if(nodes.get(i).ops.get(0).isInt){
                        System.out.println("divi " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }else{
                        System.out.println("divr " + nodes.get(i).ops.get(2).sval + " r" + register);
                    }
                    System.out.println("move r" + register + " " + nodes.get(i).ops.get(0).sval);
                    register++;
                }
            }

            if(Objects.equals(nodes.get(i).fnct, "WRITE")){
                for(int j = 0; j < nodes.get(i).ops.size(); ++j){
                    if(Objects.equals(nodes.get(i).ops.get(j).type, "STRING")){
                        System.out.println("sys writes " + nodes.get(i).ops.get(j).sval);
                    }else if(Objects.equals(nodes.get(i).ops.get(j).type, "FLOAT")){
                        System.out.println("sys writer " + nodes.get(i).ops.get(j).sval);
                    }else{
                        System.out.println("sys writei " + nodes.get(i).ops.get(j).sval);
                    }
                }
            }

        }

        System.out.println("sys halt");

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterDecl(LittleParser.DeclContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitDecl(LittleParser.DeclContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterString_decl(LittleParser.String_declContext ctx) {
        name = ctx.id().IDENTIFIER().getText();
        value = ctx.str().getText();
        tableStack.peek().addSymbol(name, new Symbol("STRING", value));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitString_decl(LittleParser.String_declContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStr(LittleParser.StrContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStr(LittleParser.StrContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterVar_decl(LittleParser.Var_declContext ctx) {
        current_type = ctx.var_type().getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterVar_type(LittleParser.Var_typeContext ctx) {
        current_type = ctx.getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitVar_type(LittleParser.Var_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAny_type(LittleParser.Any_typeContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAny_type(LittleParser.Any_typeContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId_list(LittleParser.Id_listContext ctx) {
        enter_id_list = true;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId_list(LittleParser.Id_listContext ctx) {
        enter_id_list = false;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterId_tail(LittleParser.Id_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitId_tail(LittleParser.Id_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl_list(LittleParser.Param_decl_listContext ctx) {
        enter_param_dec_list = true;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl_list(LittleParser.Param_decl_listContext ctx) {
        enter_param_dec_list = false;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl(LittleParser.Param_declContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterParam_decl_tail(LittleParser.Param_decl_tailContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitParam_decl_tail(LittleParser.Param_decl_tailContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_declarations(LittleParser.Func_declarationsContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_declarations(LittleParser.Func_declarationsContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
        String scope = ctx.id().IDENTIFIER().getText();
        SymbolTable symTab = new SymbolTable(scope);
        tableStack.push(symTab);
        print_table_list.add(symTab);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFunc_body(LittleParser.Func_bodyContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFunc_body(LittleParser.Func_bodyContext ctx) {
        tableStack.pop();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStmt_list(LittleParser.Stmt_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStmt_list(LittleParser.Stmt_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterStmt(LittleParser.StmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitStmt(LittleParser.StmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterBase_stmt(LittleParser.Base_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitBase_stmt(LittleParser.Base_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        inAssignmentExpression = true;
        currentNode.fnct = "ASSIGNMENT";
        currentNode.assignment = true;
        Operands op = new Operands();
        op.sval = ctx.start.getText();
        if(Objects.equals(print_table_list.get(0).symbolTable.get(op.sval).type, "INT")){
            op.type = "INT";
            op.isInt = true;
        }else if(Objects.equals(print_table_list.get(0).symbolTable.get(op.sval).type, "FLOAT")){
            op.type = "FLOAT";
            op.isInt = false;
        }else{
            op.type = "STRING";
        }
        currentNode.ops.add(op);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        nodes.add(currentNode);
        currentNode = new Node();
        inAssignmentExpression = false;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAssign_expr(LittleParser.Assign_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAssign_expr(LittleParser.Assign_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
        inRead = true;
        currentNode.fnct = "READ";
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitRead_stmt(LittleParser.Read_stmtContext ctx) {
        inRead = false;
        nodes.add(currentNode);
        currentNode = new Node();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
        inWrite = true;
        currentNode.fnct = "WRITE";
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitWrite_stmt(LittleParser.Write_stmtContext ctx) {
        inWrite = false;
        nodes.add(currentNode);
        currentNode = new Node();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterReturn_stmt(LittleParser.Return_stmtContext ctx) {

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitReturn_stmt(LittleParser.Return_stmtContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr(LittleParser.ExprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr(LittleParser.ExprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_prefix(LittleParser.Expr_prefixContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_prefix(LittleParser.Expr_prefixContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFactor(LittleParser.FactorContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFactor(LittleParser.FactorContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterFactor_prefix(LittleParser.Factor_prefixContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitFactor_prefix(LittleParser.Factor_prefixContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterCall_expr(LittleParser.Call_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCall_expr(LittleParser.Call_exprContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_list(LittleParser.Expr_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_list(LittleParser.Expr_listContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterPrimary(LittleParser.PrimaryContext ctx) {
        if(Objects.equals(ctx.stop.getText(), ")")) return;
        if(Objects.equals(ctx.stop.getText(), "(")) return;
        Operands op = new Operands();
        op.sval = ctx.stop.getText();
        op.type = current_type;
        boolean cont = true;
        for(int i = 0; i < op.sval.length(); ++i){
            if(Character.isLetter(op.sval.charAt(i))){
                cont = false;
                break;
            }
        }
        if(cont){
            op.isLit = true;
            op.isInt = true;
            for(int i = 0; i < op.sval.length(); ++i){
                if(op.sval.charAt(i) == '.'){
                    op.isInt = false;
                    break;
                }
            }
            if(op.isInt){
                op.ival = Integer.parseInt(op.sval);
            }else{
                op.fval = Float.parseFloat(op.sval);
            }
        }
        currentNode.ops.add(op);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitPrimary(LittleParser.PrimaryContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterAddop(LittleParser.AddopContext ctx) {
        if(inAssignmentExpression){
            currentNode.fnct = ctx.stop.getText();
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitAddop(LittleParser.AddopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterMulop(LittleParser.MulopContext ctx) {
        if(inAssignmentExpression){
            currentNode.fnct = ctx.stop.getText();
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMulop(LittleParser.MulopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
        String scope = "BLOCK " + moveIntoNewBlock();
        SymbolTable symTab = new SymbolTable(scope);
        tableStack.push(symTab);
        print_table_list.add(symTab);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
        tableStack.pop();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterElse_part(LittleParser.Else_partContext ctx) {
        if(ctx.decl() != null){
            String scope = "BLOCK " + moveIntoNewBlock();
            SymbolTable symTab = new SymbolTable(scope);
            tableStack.push(symTab);
            print_table_list.add(symTab);
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitElse_part(LittleParser.Else_partContext ctx) {
        tableStack.pop();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterCond(LittleParser.CondContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCond(LittleParser.CondContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterCompop(LittleParser.CompopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitCompop(LittleParser.CompopContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
        String scope = "BLOCK " + moveIntoNewBlock();
        SymbolTable symTab = new SymbolTable(scope);
        tableStack.push(symTab);
        print_table_list.add(symTab);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
        tableStack.pop();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterEmpty(LittleParser.EmptyContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitEmpty(LittleParser.EmptyContext ctx) { }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void enterEveryRule(ParserRuleContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void visitTerminal(TerminalNode node) { }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void visitErrorNode(ErrorNode node) { }

    private SymbolTable currentTable;
    private SymbolTable globalTable;

    private int BlockCount = 1;

    public int moveIntoNewBlock(){
        return this.BlockCount++;
    }

    public SymbolExtractor(){
        this.tableStack = new Stack<SymbolTable>();
        this.currentTable = null;
    }


}

class SymbolTable{

    private String scope;

    public String getScope(){return this.scope;}

    public HashMap<String, Symbol> symbolTable;

    public ArrayList<String> symbolNames;

    public SymbolTable(String scope){
        this.scope = scope;
        this.symbolTable = new HashMap<String, Symbol>();
        this.symbolNames = new ArrayList<String>();
    }

    public void addSymbol(String symbolName, Symbol attributes){
        if(this.symbolTable.containsKey(symbolName)){
            System.out.printf("DECLARATION ERROR %s\n", symbolName);
            System.exit(0);
        }
        this.symbolTable.put(symbolName, attributes);
        this.symbolNames.add(symbolName);
    }

}

class Symbol{

    String type;

    String value;

    public Symbol(String type, String value){
        this.type = type;
        this.value = value;
    }

    public String getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

}
