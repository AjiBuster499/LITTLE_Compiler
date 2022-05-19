import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

class SymbolExtractor extends LittleBaseListener {

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

						        @Override public void enterProgram(LittleParser.ProgramContext ctx) {
							        String scope = "GLOBAL";
								        SymbolTable symTab = new SymbolTable(scope);
									        tableStack.push(symTab);
										        print_table_list.add(symTab);
											    }
							    /**
							     *      * {@inheritDoc}
							     *           *
							     *                * <p>The default implementation does nothing.</p>
							     *                     */

							    @Override public void exitProgram(LittleParser.ProgramContext ctx) {

							        }
							        /**
								 *      * {@inheritDoc}
								 *           *
								 *                * <p>The default implementation does nothing.</p>
								 *                     */
							        @Override public void enterId(LittleParser.IdContext ctx) {
								            name = ctx.IDENTIFIER().getText();
									        }
								    /**
								     *      * {@inheritDoc}
								     *           *
								     *                * <p>The default implementation does nothing.</p>
								     *                     */
								    @Override public void exitId(LittleParser.IdContext ctx) {
								            if((enter_id_list || enter_param_dec_list) && !inRead && !inWrite){
										                tableStack.peek().addSymbol(name, new Symbol(current_type, "0"));
												        }
									        }
								        /**
									 *      * {@inheritDoc}
									 *           *
									 *                * <p>The default implementation does nothing.</p>
									 *                     */
								        @Override public void enterPgm_body(LittleParser.Pgm_bodyContext ctx) {

									    }
									    /**
									     *      * {@inheritDoc}
									     *           *
									     *                * <p>The default implementation does nothing.</p>
									     *                     */
									    @Override public void exitPgm_body(LittleParser.Pgm_bodyContext ctx) {
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
										        }
									        /**
										 *      * {@inheritDoc}
										 *           *
										 *                * <p>The default implementation does nothing.</p>
										 *                     */
									        @Override public void enterDecl(LittleParser.DeclContext ctx) {

										    }
										    /**
										     *      * {@inheritDoc}
										     *           *
										     *                * <p>The default implementation does nothing.</p>
										     *                     */
										    @Override public void exitDecl(LittleParser.DeclContext ctx) { }
										        /**
											 *      * {@inheritDoc}
											 *           *
											 *                * <p>The default implementation does nothing.</p>
											 *                     */
										        @Override public void enterString_decl(LittleParser.String_declContext ctx) {
											        name = ctx.id().IDENTIFIER().getText();
												        value = ctx.str().getText();
													        tableStack.peek().addSymbol(name, new Symbol("STRING", value));
														    }
											    /**
											     *      * {@inheritDoc}
											     *           *
											     *                * <p>The default implementation does nothing.</p>
											     *                     */
											    @Override public void exitString_decl(LittleParser.String_declContext ctx) { }
											        /**
												 *      * {@inheritDoc}
												 *           *
												 *                * <p>The default implementation does nothing.</p>
												 *                     */
											        @Override public void enterStr(LittleParser.StrContext ctx) {

												    }
												    /**
												     *      * {@inheritDoc}
												     *           *
												     *                * <p>The default implementation does nothing.</p>
												     *                     */
												    @Override public void exitStr(LittleParser.StrContext ctx) { }
												        /**
													 *      * {@inheritDoc}
													 *           *
													 *                * <p>The default implementation does nothing.</p>
													 *                     */
												        @Override public void enterVar_decl(LittleParser.Var_declContext ctx) {
													        current_type = ctx.var_type().getText();
														    }
													    /**
													     *      * {@inheritDoc}
													     *           *
													     *                * <p>The default implementation does nothing.</p>
													     *                     */
													    @Override public void exitVar_decl(LittleParser.Var_declContext ctx) {

													        }
													        /**
														 *      * {@inheritDoc}
														 *           *
														 *                * <p>The default implementation does nothing.</p>
														 *                     */
													        @Override public void enterVar_type(LittleParser.Var_typeContext ctx) {
														        current_type = ctx.getText();
															    }
														    /**
														     *      * {@inheritDoc}
														     *           *
														     *                * <p>The default implementation does nothing.</p>
														     *                     */
														    @Override public void exitVar_type(LittleParser.Var_typeContext ctx) { }
														        /**
															 *      * {@inheritDoc}
															 *           *
															 *                * <p>The default implementation does nothing.</p>
															 *                     */
														        @Override public void enterAny_type(LittleParser.Any_typeContext ctx) {

															    }
															    /**
															     *      * {@inheritDoc}
															     *           *
															     *                * <p>The default implementation does nothing.</p>
															     *                     */
															    @Override public void exitAny_type(LittleParser.Any_typeContext ctx) { }
															        /**
																 *      * {@inheritDoc}
																 *           *
																 *                * <p>The default implementation does nothing.</p>
																 *                     */
															        @Override public void enterId_list(LittleParser.Id_listContext ctx) {
																        enter_id_list = true;
																	    }
																    /**
																     *      * {@inheritDoc}
																     *           *
																     *                * <p>The default implementation does nothing.</p>
																     *                     */
																    @Override public void exitId_list(LittleParser.Id_listContext ctx) {
																            enter_id_list = false;
																	        }
																        /**
																	 *      * {@inheritDoc}
																	 *           *
																	 *                * <p>The default implementation does nothing.</p>
																	 *                     */
																        @Override public void enterId_tail(LittleParser.Id_tailContext ctx) { }
																	    /**
																	     *      * {@inheritDoc}
																	     *           *
																	     *                * <p>The default implementation does nothing.</p>
																	     *                     */
																	    @Override public void exitId_tail(LittleParser.Id_tailContext ctx) { }
																	        /**
																		 *      * {@inheritDoc}
																		 *           *
																		 *                * <p>The default implementation does nothing.</p>
																		 *                     */
																	        @Override public void enterParam_decl_list(LittleParser.Param_decl_listContext ctx) {
																		        enter_param_dec_list = true;
																			    }
																		    /**
																		     *      * {@inheritDoc}
																		     *           *
																		     *                * <p>The default implementation does nothing.</p>
																		     *                     */
																		    @Override public void exitParam_decl_list(LittleParser.Param_decl_listContext ctx) {
																		            enter_param_dec_list = false;
																			        }
																		        /**
																			 *      * {@inheritDoc}
																			 *           *
																			 *                * <p>The default implementation does nothing.</p>
																			 *                     */
																		        @Override public void enterParam_decl(LittleParser.Param_declContext ctx) { }
																			    /**
																			     *      * {@inheritDoc}
																			     *           *
																			     *                * <p>The default implementation does nothing.</p>
																			     *                     */
																			    @Override public void exitParam_decl(LittleParser.Param_declContext ctx) { }
																			        /**
																				 *      * {@inheritDoc}
																				 *           *
																				 *                * <p>The default implementation does nothing.</p>
																				 *                     */
																			        @Override public void enterParam_decl_tail(LittleParser.Param_decl_tailContext ctx) {

																				    }
																				    /**
																				     *      * {@inheritDoc}
																				     *           *
																				     *                * <p>The default implementation does nothing.</p>
																				     *                     */
																				    @Override public void exitParam_decl_tail(LittleParser.Param_decl_tailContext ctx) {

																				        }
																				        /**
																					 *      * {@inheritDoc}
																					 *           *
																					 *                * <p>The default implementation does nothing.</p>
																					 *                     */
																				        @Override public void enterFunc_declarations(LittleParser.Func_declarationsContext ctx) {

																					    }
																					    /**
																					     *      * {@inheritDoc}
																					     *           *
																					     *                * <p>The default implementation does nothing.</p>
																					     *                     */
																					    @Override public void exitFunc_declarations(LittleParser.Func_declarationsContext ctx) { }
																					        /**
																						 *      * {@inheritDoc}
																						 *           *
																						 *                * <p>The default implementation does nothing.</p>
																						 *                     */
																					        @Override public void enterFunc_decl(LittleParser.Func_declContext ctx) {
																						        String scope = ctx.id().IDENTIFIER().getText();
																							        SymbolTable symTab = new SymbolTable(scope);
																								        tableStack.push(symTab);
																									        print_table_list.add(symTab);
																										    }
																						    /**
																						     *      * {@inheritDoc}
																						     *           *
																						     *                * <p>The default implementation does nothing.</p>
																						     *                     */
																						    @Override public void exitFunc_decl(LittleParser.Func_declContext ctx) {

																						        }
																						        /**
																							 *      * {@inheritDoc}
																							 *           *
																							 *                * <p>The default implementation does nothing.</p>
																							 *                     */
																						        @Override public void enterFunc_body(LittleParser.Func_bodyContext ctx) {

																							    }
																							    /**
																							     *      * {@inheritDoc}
																							     *           *
																							     *                * <p>The default implementation does nothing.</p>
																							     *                     */
																							    @Override public void exitFunc_body(LittleParser.Func_bodyContext ctx) {
																							            tableStack.pop();
																								        }
																							        /**
																								 *      * {@inheritDoc}
																								 *           *
																								 *                * <p>The default implementation does nothing.</p>
																								 *                     */
																							        @Override public void enterStmt_list(LittleParser.Stmt_listContext ctx) { }
																								    /**
																								     *      * {@inheritDoc}
																								     *           *
																								     *                * <p>The default implementation does nothing.</p>
																								     *                     */
																								    @Override public void exitStmt_list(LittleParser.Stmt_listContext ctx) { }
																								        /**
																									 *      * {@inheritDoc}
																									 *           *
																									 *                * <p>The default implementation does nothing.</p>
																									 *                     */
																								        @Override public void enterStmt(LittleParser.StmtContext ctx) { }
																									    /**
																									     *      * {@inheritDoc}
																									     *           *
																									     *                * <p>The default implementation does nothing.</p>
																									     *                     */
																									    @Override public void exitStmt(LittleParser.StmtContext ctx) { }
																									        /**
																										 *      * {@inheritDoc}
																										 *           *
																										 *                * <p>The default implementation does nothing.</p>
																										 *                     */
																									        @Override public void enterBase_stmt(LittleParser.Base_stmtContext ctx) { }
																										    /**
																										     *      * {@inheritDoc}
																										     *           *
																										     *                * <p>The default implementation does nothing.</p>
																										     *                     */
																										    @Override public void exitBase_stmt(LittleParser.Base_stmtContext ctx) { }
																										        /**
																											 *      * {@inheritDoc}
																											 *           *
																											 *                * <p>The default implementation does nothing.</p>
																											 *                     */
																										        @Override public void enterAssign_stmt(LittleParser.Assign_stmtContext ctx) { }
																											    /**
																											     *      * {@inheritDoc}
																											     *           *
																											     *                * <p>The default implementation does nothing.</p>
																											     *                     */
																											    @Override public void exitAssign_stmt(LittleParser.Assign_stmtContext ctx) { }
																											        /**
																												 *      * {@inheritDoc}
																												 *           *
																												 *                * <p>The default implementation does nothing.</p>
																												 *                     */
																											        @Override public void enterAssign_expr(LittleParser.Assign_exprContext ctx) { }
																												    /**
																												     *      * {@inheritDoc}
																												     *           *
																												     *                * <p>The default implementation does nothing.</p>
																												     *                     */
																												    @Override public void exitAssign_expr(LittleParser.Assign_exprContext ctx) { }
																												        /**
																													 *      * {@inheritDoc}
																													 *           *
																													 *                * <p>The default implementation does nothing.</p>
																													 *                     */
																												        @Override public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
																													        inRead = true;
																														    }
																													    /**
																													     *      * {@inheritDoc}
																													     *           *
																													     *                * <p>The default implementation does nothing.</p>
																													     *                     */
																													    @Override public void exitRead_stmt(LittleParser.Read_stmtContext ctx) {
																													            inRead = false;
																														        }
																													        /**
																														 *      * {@inheritDoc}
																														 *           *
																														 *                * <p>The default implementation does nothing.</p>
																														 *                     */
																													        @Override public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
																														        inWrite = true;
																															    }
																														    /**
																														     *      * {@inheritDoc}
																														     *           *
																														     *                * <p>The default implementation does nothing.</p>
																														     *                     */
																														    @Override public void exitWrite_stmt(LittleParser.Write_stmtContext ctx) {
																														            inWrite = false;
																															        }
																														        /**
																															 *      * {@inheritDoc}
																															 *           *
																															 *                * <p>The default implementation does nothing.</p>
																															 *                     */
																														        @Override public void enterReturn_stmt(LittleParser.Return_stmtContext ctx) {

																															    }
																															    /**
																															     *      * {@inheritDoc}
																															     *           *
																															     *                * <p>The default implementation does nothing.</p>
																															     *                     */
																															    @Override public void exitReturn_stmt(LittleParser.Return_stmtContext ctx) { }
																															        /**
																																 *      * {@inheritDoc}
																																 *           *
																																 *                * <p>The default implementation does nothing.</p>
																																 *                     */
																															        @Override public void enterExpr(LittleParser.ExprContext ctx) { }
																																    /**
																																     *      * {@inheritDoc}
																																     *           *
																																     *                * <p>The default implementation does nothing.</p>
																																     *                     */
																																    @Override public void exitExpr(LittleParser.ExprContext ctx) { }
																																        /**
																																	 *      * {@inheritDoc}
																																	 *           *
																																	 *                * <p>The default implementation does nothing.</p>
																																	 *                     */
																																        @Override public void enterExpr_prefix(LittleParser.Expr_prefixContext ctx) { }
																																	    /**
																																	     *      * {@inheritDoc}
																																	     *           *
																																	     *                * <p>The default implementation does nothing.</p>
																																	     *                     */
																																	    @Override public void exitExpr_prefix(LittleParser.Expr_prefixContext ctx) { }
																																	        /**
																																		 *      * {@inheritDoc}
																																		 *           *
																																		 *                * <p>The default implementation does nothing.</p>
																																		 *                     */
																																	        @Override public void enterFactor(LittleParser.FactorContext ctx) { }
																																		    /**
																																		     *      * {@inheritDoc}
																																		     *           *
																																		     *                * <p>The default implementation does nothing.</p>
																																		     *                     */
																																		    @Override public void exitFactor(LittleParser.FactorContext ctx) { }
																																		        /**
																																			 *      * {@inheritDoc}
																																			 *           *
																																			 *                * <p>The default implementation does nothing.</p>
																																			 *                     */
																																		        @Override public void enterFactor_prefix(LittleParser.Factor_prefixContext ctx) { }
																																			    /**
																																			     *      * {@inheritDoc}
																																			     *           *
																																			     *                * <p>The default implementation does nothing.</p>
																																			     *                     */
																																			    @Override public void exitFactor_prefix(LittleParser.Factor_prefixContext ctx) { }
																																			        /**
																																				 *      * {@inheritDoc}
																																				 *           *
																																				 *                * <p>The default implementation does nothing.</p>
																																				 *                     */
																																			        @Override public void enterPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
																																				    /**
																																				     *      * {@inheritDoc}
																																				     *           *
																																				     *                * <p>The default implementation does nothing.</p>
																																				     *                     */
																																				    @Override public void exitPostfix_expr(LittleParser.Postfix_exprContext ctx) { }
																																				        /**
																																					 *      * {@inheritDoc}
																																					 *           *
																																					 *                * <p>The default implementation does nothing.</p>
																																					 *                     */
																																				        @Override public void enterCall_expr(LittleParser.Call_exprContext ctx) { }
																																					    /**
																																					     *      * {@inheritDoc}
																																					     *           *
																																					     *                * <p>The default implementation does nothing.</p>
																																					     *                     */
																																					    @Override public void exitCall_expr(LittleParser.Call_exprContext ctx) { }
																																					        /**
																																						 *      * {@inheritDoc}
																																						 *           *
																																						 *                * <p>The default implementation does nothing.</p>
																																						 *                     */
																																					        @Override public void enterExpr_list(LittleParser.Expr_listContext ctx) { }
																																						    /**
																																						     *      * {@inheritDoc}
																																						     *           *
																																						     *                * <p>The default implementation does nothing.</p>
																																						     *                     */
																																						    @Override public void exitExpr_list(LittleParser.Expr_listContext ctx) { }
																																						        /**
																																							 *      * {@inheritDoc}
																																							 *           *
																																							 *                * <p>The default implementation does nothing.</p>
																																							 *                     */
																																						        @Override public void enterExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
																																							    /**
																																							     *      * {@inheritDoc}
																																							     *           *
																																							     *                * <p>The default implementation does nothing.</p>
																																							     *                     */
																																							    @Override public void exitExpr_list_tail(LittleParser.Expr_list_tailContext ctx) { }
																																							        /**
																																								 *      * {@inheritDoc}
																																								 *           *
																																								 *                * <p>The default implementation does nothing.</p>
																																								 *                     */
																																							        @Override public void enterPrimary(LittleParser.PrimaryContext ctx) { }
																																								    /**
																																								     *      * {@inheritDoc}
																																								     *           *
																																								     *                * <p>The default implementation does nothing.</p>
																																								     *                     */
																																								    @Override public void exitPrimary(LittleParser.PrimaryContext ctx) { }
																																								        /**
																																									 *      * {@inheritDoc}
																																									 *           *
																																									 *                * <p>The default implementation does nothing.</p>
																																									 *                     */
																																								        @Override public void enterAddop(LittleParser.AddopContext ctx) { }
																																									    /**
																																									     *      * {@inheritDoc}
																																									     *           *
																																									     *                * <p>The default implementation does nothing.</p>
																																									     *                     */
																																									    @Override public void exitAddop(LittleParser.AddopContext ctx) { }
																																									        /**
																																										 *      * {@inheritDoc}
																																										 *           *
																																										 *                * <p>The default implementation does nothing.</p>
																																										 *                     */
																																									        @Override public void enterMulop(LittleParser.MulopContext ctx) { }
																																										    /**
																																										     *      * {@inheritDoc}
																																										     *           *
																																										     *                * <p>The default implementation does nothing.</p>
																																										     *                     */
																																										    @Override public void exitMulop(LittleParser.MulopContext ctx) { }
																																										        /**
																																											 *      * {@inheritDoc}
																																											 *           *
																																											 *                * <p>The default implementation does nothing.</p>
																																											 *                     */
																																										        @Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
																																											        String scope = "BLOCK " + moveIntoNewBlock();
																																												        SymbolTable symTab = new SymbolTable(scope);
																																													        tableStack.push(symTab);
																																														        print_table_list.add(symTab);
																																															    }
																																											    /**
																																											     *      * {@inheritDoc}
																																											     *           *
																																											     *                * <p>The default implementation does nothing.</p>
																																											     *                     */
																																											    @Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
																																											            tableStack.pop();
																																												        }
																																											        /**
																																												 *      * {@inheritDoc}
																																												 *           *
																																												 *                * <p>The default implementation does nothing.</p>
																																												 *                     */
																																											        @Override public void enterElse_part(LittleParser.Else_partContext ctx) {
																																												        if(ctx.decl() != null){
																																														            String scope = "BLOCK " + moveIntoNewBlock();
																																															                SymbolTable symTab = new SymbolTable(scope);
																																																	            tableStack.push(symTab);
																																																		                print_table_list.add(symTab);
																																																				        }
																																													    }
																																												    /**
																																												     *      * {@inheritDoc}
																																												     *           *
																																												     *                * <p>The default implementation does nothing.</p>
																																												     *                     */
																																												    @Override public void exitElse_part(LittleParser.Else_partContext ctx) {
																																												            tableStack.pop();
																																													        }
																																												        /**
																																													 *      * {@inheritDoc}
																																													 *           *
																																													 *                * <p>The default implementation does nothing.</p>
																																													 *                     */
																																												        @Override public void enterCond(LittleParser.CondContext ctx) { }
																																													    /**
																																													     *      * {@inheritDoc}
																																													     *           *
																																													     *                * <p>The default implementation does nothing.</p>
																																													     *                     */
																																													    @Override public void exitCond(LittleParser.CondContext ctx) { }
																																													        /**
																																														 *      * {@inheritDoc}
																																														 *           *
																																														 *                * <p>The default implementation does nothing.</p>
																																														 *                     */
																																													        @Override public void enterCompop(LittleParser.CompopContext ctx) { }
																																														    /**
																																														     *      * {@inheritDoc}
																																														     *           *
																																														     *                * <p>The default implementation does nothing.</p>
																																														     *                     */
																																														    @Override public void exitCompop(LittleParser.CompopContext ctx) { }
																																														        /**
																																															 *      * {@inheritDoc}
																																															 *           *
																																															 *                * <p>The default implementation does nothing.</p>
																																															 *                     */
																																														        @Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
																																															        String scope = "BLOCK " + moveIntoNewBlock();
																																																        SymbolTable symTab = new SymbolTable(scope);
																																																	        tableStack.push(symTab);
																																																		        print_table_list.add(symTab);
																																																			    }
																																															    /**
																																															     *      * {@inheritDoc}
																																															     *           *
																																															     *                * <p>The default implementation does nothing.</p>
																																															     *                     */
																																															    @Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
																																															            tableStack.pop();
																																																        }
																																															        /**
																																																 *      * {@inheritDoc}
																																																 *           *
																																																 *                * <p>The default implementation does nothing.</p>
																																																 *                     */
																																															        @Override public void enterEmpty(LittleParser.EmptyContext ctx) { }
																																																    /**
																																																     *      * {@inheritDoc}
																																																     *           *
																																																     *                * <p>The default implementation does nothing.</p>
																																																     *                     */
																																																    @Override public void exitEmpty(LittleParser.EmptyContext ctx) { }

																																																        /**
																																																	 *      * {@inheritDoc}
																																																	 *           *
																																																	 *                * <p>The default implementation does nothing.</p>
																																																	 *                     */
																																																        @Override public void enterEveryRule(ParserRuleContext ctx) { }
																																																	    /**
																																																	     *      * {@inheritDoc}
																																																	     *           *
																																																	     *                * <p>The default implementation does nothing.</p>
																																																	     *                     */
																																																	    @Override public void exitEveryRule(ParserRuleContext ctx) { }
																																																	        /**
																																																		 *      * {@inheritDoc}
																																																		 *           *
																																																		 *                * <p>The default implementation does nothing.</p>
																																																		 *                     */
																																																	        @Override public void visitTerminal(TerminalNode node) { }
																																																		    /**
																																																		     *      * {@inheritDoc}
																																																		     *           *
																																																		     *                * <p>The default implementation does nothing.</p>
																																																		     *                     */
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

