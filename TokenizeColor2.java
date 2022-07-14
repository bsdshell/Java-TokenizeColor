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

public class TokenizeColor2{
    public static void main(String[] args) {
        test0();
    }
    public static void test0(){
        beg();
        {
            String s = "123  ab22 'ee'\"bef\"zz  abc123 3.14159 2.7 3.33 2 ab123";
            int i = 0;
            while(i < len(s)){
                char c = s.charAt(i);
                if(c == '"'){
                    InxStr s1 = string(i, s);
                    pl("\"ab\"    => s1=" + s1);
                    if(s1 != null){
                        i = s1.inx;
                    }
                }else if(c == '\''){
                    InxStr s2 = string2(i, s);
                    pl("'ab'      => s2=" + s2);
                    if(s2 != null){
                        i = s2.inx;
                    }
                }else if(isLetter(c)){
                    InxStr s3 = alphabetDigit(i, s);
                    pl("abc123     => s3=" + s3);
                    if(s3 != null){
                        i = s3.inx;
                    }
                }else if(isDigit(c)){
                    InxStr s3 = floatNum(i, s);
                    pl("3.14159     => s3=" + s3);
                    if(s3 != null){
                        i = s3.inx;
                    }else {
                        InxStr s4 = integer(i, s);
                        pl("123        => s3=" + s4);
                        if(s4 != null){
                            i = s4.inx;
                        }
                    }
                }else{
                    i++;
                }
            }
        }
        end();
    }
    public static void test1(){
        beg();
        String fname = "/Users/aaa/myfile/bitbucket/testfile/test.txt";
        StopWatch sw = new StopWatch();
        sw.start();



        sw.printTime();
        end();
    }

    /**

        (0) -> (1) [a-z] ->  (2)
            "            " 
    */
    public static InxStr string(int inx, String s){
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

    public static InxStr string2(int inx, String s){
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
        while(i <= len(s) && !done){
            if(i == len(s)){
                state = 2;
                done = true;
            }else{
                if(state == 0 && isLetter(s.charAt(i))){
                    state = 1;
                    str += s.charAt(i);
                }else if(state == 1 && (isLetter(s.charAt(i)) || isDigit(s.charAt(i))) ){
                    str += charToStr(s.charAt(i));
                }else if(state == 1 && !( isLetter(s.charAt(i)) || isDigit(s.charAt(i)))){
                    state = 2;
                    done = true;
                }
            }
            i++;
        }
        if(state == 2){
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
        while(i <= len(s) && !done){
            if(state == 1 && i == len(s)){
                state = 2;
                done = true;
            }else if(state == 3 && i == len(s)){
                state = 2;
                done = true;
            }else{
                char c = s.charAt(i);
                if(state == 0 && c == '0'){
                    str += charToStr(c); 
                    state = 3;
                }else if(state == 3 && c == ' '){
                    state = 2;
                    done = true;
                }else if(state == 3 && c != ' '){
                    // Error number format
                    state = 0;
                    done = true;
                }else if(state == 0 && isPositiveDigit(c)){
                    state = 1;
                    str += charToStr(c); 
                }else if(state == 1 && isDigit(c)){
                    state = 1;
                    str += charToStr(c); 
                }else if(state == 1 && !isDigit(c)){
                    state = 2;
                    done = true;
                }
            }
            i++;
        }
        if(state == 2){
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

                 - -            --
                \  /           \  / 
        (0) ->  (1)      ->    (2)        ->  (3)
            [0-9]  [0-9]  [.]     [0-9]   [\s EOL]
                                 
    */
    public static InxStr floatNum(int inx, String s){
        InxStr two = null;
        int state = 0;
        String str = "";
        boolean done = false;
        int i = inx;
        while(i <= len(s) && !done){
            if(i == len(s)){
                if(state == 2){
                    state = 3;
                    done = true;
                }else{
                    state = 0;
                    done = true;
                }
            }else{
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
                    state = 2;
                    str += charToStr(c); 
                }else if(state == 2 && c == ' '){
                    state = 3;
                    done = true;
                }else{
                    state = 0;
                    done = true;
                }
            }
            i++;
        }
        if(state == 3){
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

