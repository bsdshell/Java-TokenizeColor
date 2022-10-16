import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;
import classfile.*;
import static classfile.Aron.*;
import static classfile.Print.*;
import static classfile.Test.*;
import java.util.stream.*;
import java.util.stream.Collectors;
import static classfile.Token.*;
import static classfile.NameType.*;
import static classfile.Tuple.*;
import static classfile.AnsiColor.*;
import static classfile.Lexer.*;
import static classfile.Symbol.*;
import static classfile.InxStr.*;

// import static classfile.AronDev.*;

/*
class InxStr{
    int inx;
    String str;
    public InxStr(int inx, String str){
        this.inx = inx;
        this.str = str;
    }

    @Override
    public String toString(){
        String ret = "inx=" + inx + " str=" + str;
        return ret;
    }
}
*/


/*
enum TokenX{
    Unknown,
    StringSQ,
    StringDQ,
    Integer,
    FloatNum,
    AlphabetDigit,
    Space,

    BracketLeft,
    BracketRight,
    
    BracketLeftSq,
    BracketRightSq,

    BracketLeftAngle,
    BracketRightAngle,

    Equal,
    Period,
    Comma,
    Colon,

    Letter,
    // here
    // Digit,

    Semicolon,
    DoubleQuote,
    SingleQuote,
    ForwardSlash,
    BackwardSlash,
    Asterisk,
    Plus,
    Minus,
    Question,
    NumSign,
    Exclamation,
    Dollar,
    Verbar,
    Tilde,
    Lowbar,
    BracketCurlyLeft,
    BracketCurlyRight,
    Percent,
    AtSign
}
*/



/*
public class ColorToken{
    public String s;
    public Token type;
    public ColorToken(String color, String s, Token type){
        this.s = color + s + RESET;
        this.type = type;
    }

    @Override
    public boolean equals(Object that){
       if(this == that) return true; 

       if(!(that instanceof NameType)) return false;

       NameType nameType = (NameType)that;
       return this.s.equals(nameType.s) && this.type == nameType.type;
    }

    @Override
    public String toString(){
        return "(s=" + s + " " + "type=" + type + ")";
    }
}
*/

/*
final class Symbol{
    static char Space = ' ';
    static char BracketLeft = '(';
    static char BracketRight = ')';

    static char BracketLeftSq = '[';
    static char BracketRightSq = ']';

    static char BracketLeftAngle = '<';
    static char BracketRightAngle = '>';

    static char Equal = '=';
    static char Period = '.';
    static char Comma = ',';
    static char Colon = ':';
    
    static char Semicolon = ';';
    // DoubleQuote,
    // SingleQuote,
    static char ForwardSlash = '/';
    static char BackwardSlash = '\\';
    static char Asterisk = '*';
    static char Plus = '+';
    static char Minus = '-';
    static char Question = '?';
    static char NumSign = '#';
    static char Exclamation = '!';
    static char Dollar = '$';
    static char Verbar = '|';
    static char Tilde = '~';
    static char Lowbar = '_';
    static char BracketCurlyLeft = '{';
    static char BracketCurlyRight = '}';
    static char Percent = '%';
    static char AtSign = '@';
    static char DoubleQuote = '"';
    static char SingleQuote = '\'';
}
*/



public class TokenizeColor2{
    public static void main(String[] args) {
        // test0();
        // test1();

        // fl("mockStdin");
        // List<String> ls = mockStdin(); 
        List<String> ls = getStdin(); 
        colorToken(ls);
    }


    public static void test0(){
        beg();
        end();
    }
    public static void test1(){
        beg();
        {
            {
                int i = 0;
                String s = "3.14159";
                InxStr two = floatNum(i, s);
                pl("two.str=" + two.str);
            }
            {
                int i = 0;
                String s = "3333";
                InxStr two = floatNum(i, s);
                t(two, null);
            }
            {
                int i = 0;
                String s = "3333.";
                InxStr two = floatNum(i, s);
                t(two, null);
            }
            {
                int i = 0;
                String s = "0";
                InxStr two = floatNum(i, s);
                t(two, null);
            }
        }
        {
            {
                fl("parse 1");
                String s = "abc";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 2");
                String s = "(";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 3");
                String s = ")";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 4");
                String s = "()";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 5");
                String s = "(abc)";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 6");
                String s = "(123)";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 7");
                String s = "(3.14159)";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("parse 8");
                String s = " ( 3.14159 ) ";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
        }
        {
            {
                fl("BracketLeftSq 1");
                String s = "[";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 2");
                String s = "]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 3");
                String s = "[]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 4");
                String s = "[abc]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 5");
                String s = "[3.14159]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 6");
                String s = " [ 3.14159 ] ";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 7");
                String s = " [ (3.14159) ] ";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftSq 8");
                String s = "[[[]]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
        }
        {
            {
                fl("BracketLeftAngle 00");
                String s = "><";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftAngle 01");
                String s = "[><[[<>]>]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("BracketLeftAngle 02");
                String s = "[3.14159><[[<abc>]>]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
        }
        {
            {
                fl("Equal 001");
                String s = "=";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("Equal 002");
                String s = " = ";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("Equal 002");
                String s = "[ =3.14159><[=[<abc>]>]";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
        }
        {
            {
                fl("Period 111");
                String s = "0.0 .[.. =3.14159><[=[<abc.>]>]a0.";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
            }
            {
                fl("Period 112");
                String s = "//<:;>/\\   → [|] ⟹  'abc'\"efghijklmnopqrstuvwxyz \"";
                List<Tuple<String, TokenX>> ls = parse(s); 
                var lr = map(x -> x.x, ls);
                String rstr = concat("", lr);
                t(rstr, s);
                pl(ls);
            }
        }
        end();
    }

} 

