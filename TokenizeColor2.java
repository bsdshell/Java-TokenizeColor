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

    // here
    // Letter,
    // Digit,

    Semicolon,
    DoubleQuote,
    ForwardSlash,
    BackwardSlash,
    Asterisk,
    Plus,
    Minus,
    Question,
    NumSign,
    SingleQuote,
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
}

class Type{
}

public class TokenizeColor2{
    public static void main(String[] args) {
        test0();
        test1();
    }
    public static void plb(String s){
        pl("[" + s + "]");
    }

    public static List<Tuple<String, TokenX>> parse(String s){
            List<Tuple<String, TokenX>> ls = new ArrayList<>();
            // Tuple<String, TokenX> tuple = new Tuple<>("a", TokenX.String);

            // String s = "123 ab22 'ee' \"bef\"zz  abc123 3.1";
            // String s = "3.1 abc22 123";
            // String s = "a 12 3.1";
            // String s = "1 12"; OK
            // String s = "1 3.1";
            int i = 0;
            while(i < len(s)){
                char c = s.charAt(i);
                if(c == '"'){
                    InxStr ss = stringDQ(i, s);
                    if(ss != null){
                        plb("StringDQ => ss=" + ss);
                        ls.add(new Tuple(ss.str, TokenX.StringDQ));
                        i = ss.inx;
                    }
                }else if(c == '\''){
                    InxStr ss = stringSQ(i, s);
                    if(ss != null){
                        plb("StringSQ => ss=" + ss);
                        ls.add(new Tuple(ss.str, TokenX.StringSQ));
                        i = ss.inx;
                    }
                }else if(isLetter(c)){
                    InxStr ss = alphabetDigit(i, s);
                    if(ss != null){
                        plb("alphabetDigit => ss=" + ss);
                        ls.add(new Tuple(ss.str, TokenX.AlphabetDigit));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Space){
                    InxStr ss = checkSymbol(i, s, Symbol.Space);
                    if(ss != null){
                        plb("Space => ss=" + ss);
                        ls.add(new Tuple(ss.str, TokenX.Space));
                        i = ss.inx;
                    }
                }else if(isDigit(c)){
                    InxStr ss = floatNum(i, s);
                    if(ss != null){
                        plb("floatNum => ss=" + ss);
                        ls.add(new Tuple(ss.str, TokenX.FloatNum));
                        i = ss.inx;
                    }else {

                        pl("i=" + i);
                        pl("s=" + s);

                        InxStr s1 = integer(i, s);
                        pl("s1 >>" + s1);
                        if(s1 != null){
                            plb("integer => s1=" + s1);
                            ls.add(new Tuple(s1.str, TokenX.Integer));
                            i = s1.inx;
                        }
                        pl("bottom i=" + i);
                    }
                }else if(c == Symbol.BracketLeft){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketLeft);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Integer));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketRight){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketRight);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Integer));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketRightSq){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketRightSq);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketRightSq));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketLeftSq){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketLeftSq);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketLeftSq));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketLeftAngle){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketLeftAngle);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketLeftAngle));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketRightAngle){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketRightAngle);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketRightAngle));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Equal){
                    InxStr ss = checkSymbol(i, s, Symbol.Equal);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Equal));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Period){
                    InxStr ss = checkSymbol(i, s, Symbol.Period);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Period));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Comma){
                    InxStr ss = checkSymbol(i, s, Symbol.Comma);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Comma));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Colon){
                    InxStr ss = checkSymbol(i, s, Symbol.Colon);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Colon));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Semicolon){
                    InxStr ss = checkSymbol(i, s, Symbol.Semicolon);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Semicolon));
                        i = ss.inx;
                    }
                }else if(c == Symbol.ForwardSlash){
                    InxStr ss = checkSymbol(i, s, Symbol.ForwardSlash);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.ForwardSlash));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BackwardSlash){
                    InxStr ss = checkSymbol(i, s, Symbol.BackwardSlash);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BackwardSlash));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Asterisk){
                    InxStr ss = checkSymbol(i, s, Symbol.Asterisk);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Asterisk));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Plus){
                    InxStr ss = checkSymbol(i, s, Symbol.Plus);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Plus));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Minus){
                    InxStr ss = checkSymbol(i, s, Symbol.Minus);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Minus));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Question){
                    InxStr ss = checkSymbol(i, s, Symbol.Question);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Question));
                        i = ss.inx;
                    }
                }else if(c == Symbol.NumSign){
                    InxStr ss = checkSymbol(i, s, Symbol.NumSign);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.NumSign));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Exclamation){
                    InxStr ss = checkSymbol(i, s, Symbol.Exclamation);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Exclamation));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Dollar){
                    InxStr ss = checkSymbol(i, s, Symbol.Dollar);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Dollar));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Verbar){
                    InxStr ss = checkSymbol(i, s, Symbol.Verbar);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Verbar));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Tilde){
                    InxStr ss = checkSymbol(i, s, Symbol.Tilde);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Tilde));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Lowbar){
                    InxStr ss = checkSymbol(i, s, Symbol.Lowbar);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Lowbar));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketCurlyLeft){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketCurlyLeft);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketCurlyLeft));
                        i = ss.inx;
                    }
                }else if(c == Symbol.BracketCurlyRight){
                    InxStr ss = checkSymbol(i, s, Symbol.BracketCurlyRight);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.BracketCurlyRight));
                        i = ss.inx;
                    }
                }else if(c == Symbol.Percent){
                    InxStr ss = checkSymbol(i, s, Symbol.Percent);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.Percent));
                        i = ss.inx;
                    }
                }else if(c == Symbol.AtSign){
                    InxStr ss = checkSymbol(i, s, Symbol.AtSign);          
                    if(ss != null){
                        ls.add(new Tuple(ss.str, TokenX.AtSign));
                        i = ss.inx;
                    }
                }else{
                    String xs = charToStr(c);
                    ls.add(new Tuple(xs, TokenX.Unknown));
                    i++;
                }
            }
            return ls;
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

    /**

        e.g
        "abc"
        ""

        (0) -> (1) [a-z] ->  (2)
            "            " 
    */
    public static InxStr stringDQ(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            if(state == 0 && s.charAt(i) == '"'){
                state = 1;
                str += charToStr(s.charAt(i));
            }else if(state == 1 && s.charAt(i) != '"'){
                str += charToStr(s.charAt(i));
            }else if(state == 1 && s.charAt(i) == '"'){
                state = 2;
                str += charToStr(s.charAt(i));
                done = true;
            }
            i++;
        }
        if(state == 2){
            two = new InxStr(i, str);
        }
        return two;
    }





    public static InxStr checkSymbol(int inx, String s, char symbol){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            char c = s.charAt(i);
            if(state == 0 && c == symbol){
                state = 1;
                str += charToStr(s.charAt(i));
            }else if(state == 1 && c == symbol){
                state = 1;
                str += charToStr(s.charAt(i));
            }

            // Next char
            if(i + 1 < len(s) && s.charAt(i + 1) != symbol){
                if(state == 1){
                    state = 2;
                    done = true;
                }
            }
            
            i++;
        }
        boolean endOfLine = i == len(s);
        /*
                                                i
                                                + -> End of string
                                                ↓ 
                                   + ->   "abc("x
                                   ↓ 
        */
        if(state == 2 || endOfLine && state == 1){
            two = new InxStr(i, str);
        }
        return two;
    }

    /**
        e.g.
        ''   ✓  
        'ab' ✓  
    */
    public static InxStr stringSQ(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            if(state == 0 && s.charAt(i) == '\''){
                state = 1;
                str += charToStr(s.charAt(i));
            }else if(state == 1 && s.charAt(i) != '\''){
                str += charToStr(s.charAt(i));
            }else if(state == 1 && s.charAt(i) == '\''){
                state = 2;
                str += charToStr(s.charAt(i));
                done = true;
            }
            i++;
        }
        if(state == 2){
            two = new InxStr(i, str);
        }
        return two;
    }


    /**
        ab12 ✓  
        12ab x

        (0) ->       (1)     ->        (2)
           [a-z]  [a-z0-9]  ^[a-z0-9]  
    */
    public static InxStr alphabetDigit(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            char c = s.charAt(i);
            if(state == 0 && isLetter(c)){
                state = 1;
                str += charToStr(c); 
            }else if(state == 1 && (isLetter(c) || isDigit(c)) ){
                state = 1;
                str += charToStr(c);
            }

            if(i + 1 < len(s) && !( isLetter(s.charAt(i + 1)) || isDigit(s.charAt(i + 1)))){
                state = 2;
                done = true;
            }
            i++;
        }
        boolean endOfLine = i == len(s);

        if(state == 2 || endOfLine && state == 1){
            two = new InxStr(i, str);
        }
        return two;
    }

    public static boolean isPositiveDigit(char c){
        return '1' <= c && c <= '9';
    }

    /**
        (0) -> (3)  ->      (2)
             0      ' '
                    endOFLINE

        (0) -> (1)   ->     (2)
            ↑         ↑ 
           [1-9]    ^[0-9]

        0 yes
        123 Yes
        0123 No 
    */
    public static InxStr integer(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            char c = s.charAt(i);

            if(state == 0 && isDigit(c)){
                str += charToStr(c); 
                state = 1;
            }else if(state == 1 && isDigit(c)){
                str += charToStr(c); 
                state = 1;
            }

            if(i + 1 < len(s) && !isDigit(s.charAt(i + 1))){
                if(state == 1){
                   state = 2; 
                   done = true;
               }
            }

            i++;
        }
        boolean endOfLine = i == len(s);
        if(state == 2 || (state == 1 && endOfLine)){
            two = new InxStr(i, str);
        }
        return two;
    }

    /**
        0.01 Yes
        0.13 Yes
        3.14 Yes
        0.001 Yes
        123.0 Yes
        0.0   No
        .123  No
        0.    No
                                           [0-9]
                 - -            --         - -
                \  /           \  /       \   /
        (0) ->  (1)      ->    (2)     ->  (3)  -> (4)
            [0-9]  [0-9]  [.]        [0-9]   
                                 
    */
    public static InxStr floatNum(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i < len(s) && !done){
            char c = s.charAt(i);
            if(state == 0 && isDigit(c)){
                state = 1;
                str += charToStr(c); 
            }else if(state == 1 && isDigit(c)){
                state = 1;
                str += charToStr(c); 
            }else if(state == 1 && c == '.'){
                state = 2;
                str += charToStr(c); 
            }else if(state == 2 && isDigit(c)){
                state = 3;
                str += charToStr(c); 
            }else if(state == 3 && isDigit(c)){
                state = 3;
                str += charToStr(c); 
            }

            if(i + 1 < len(s) && s.charAt(i + 1) != '.' && !isDigit(s.charAt(i + 1) )){
                if(state == 3){
                    state = 4;
                    done = true;
                }else{
                    state = -1;
                    done = true;
                }
            }

            i++;
        }
        boolean endOfLine = i == len(s);

        if(state == 4 || (endOfLine && state == 3)){
            two = new InxStr(i, str);
        }

        return two;
    }

    /**
        0123 Yes
        01   Yes
    */
    public static InxStr digitStr(int inx, String s){
        return null;
    }

} 

