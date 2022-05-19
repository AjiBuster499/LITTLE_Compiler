import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class Driver {
    public static void main(String[] args) throws Exception {

        InputStream is = System.in;
        CharStream charStream = CharStreams.fromStream(is);
        LittleLexer lexer = new LittleLexer(charStream);
        Vocabulary vocabulary = lexer.getVocabulary();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        LittleParser parser = new LittleParser(tokens);
        SymbolExtractor symbolExtractor = new SymbolExtractor();
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(symbolExtractor, tree);
    }
}
