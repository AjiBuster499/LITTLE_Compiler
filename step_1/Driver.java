import org.antlr.v4.runtime.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class Driver {
    public static void main(String[] args) throws Exception {

        String inputFile = " ";
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        CharStream charStream = CharStreams.fromStream(is);
        Little lexer = new Little(charStream);
        Vocabulary vocabulary = lexer.getVocabulary();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        for(Token t : tokens.getTokens()){
            if(Objects.equals(vocabulary.getSymbolicName(t.getType()), "EOF")){
                continue;
            }
            System.out.print("Token Type: ");
            System.out.println(vocabulary.getSymbolicName(t.getType()));
            //System.out.println(t.getType());
            System.out.print("Value: ");
            System.out.println(t.getText());
        }
    }
}
