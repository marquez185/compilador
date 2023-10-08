import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

   // private int linea = 1;
    private static final Map<String, TipoToken> palabrasReservadas;
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
        palabrasReservadas.put("less", TipoToken.LESS);
        palabrasReservadas.put("greater", TipoToken.GREATER);
        palabrasReservadas.put("equal", TipoToken.EQUAL);
        palabrasReservadas.put("less_equal", TipoToken.LESS_EQUAL);
        palabrasReservadas.put("greater_equal", TipoToken.GREATER_EQUAL);
        palabrasReservadas.put("equal_equal", TipoToken.EQUAL);
        palabrasReservadas.put("bang", TipoToken.BANG);
        palabrasReservadas.put("bang_equal", TipoToken.BANG_EQUAL);
    }


    public Scanner(String source) {
        this.source = source + " ";
    }


    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);

            switch (estado) {
                case 0:
                    if (c == '>') {
                        estado = 1;
                        lexema += c;
                    } else if (c == '<') {
                        lexema += c;
                        estado = 4;
                    } else if (c == '=') {
                        lexema += c;
                        estado = 7;
                    } else if (c == '!') {
                        lexema += c;
                        estado = 10;
                    }

                    if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 14;
                        lexema += c;

                        while(Character.isDigit(c)){
                            lexema += c;
                            i++;
                            c = source.charAt(i);
                        }
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        lexema = "";
                        estado = 0;
                        tokens.add(t);

                    }
                    break;

                case 1:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.LESS_EQUAL, lexema));
                    } else {
                        lexema += c;
                        tokens.add(new Token(TipoToken.LESS, lexema));
                    }
                    estado=0;
                    lexema ="";
                case 4:
                    if(c== '='){
                        lexema+=c;
                        tokens.add(new Token(TipoToken.GREATER_EQUAL,lexema));
                    }
                    else{
                        lexema+=c;
                        tokens.add(new Token(TipoToken.GREATER,lexema));
                    }
                    estado=0;
                    lexema ="";
                    break;
                case 7:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL_EQUAL, lexema));
                    } else {
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL, lexema));
                    }
                    estado=0;
                    lexema ="";
                    break;
                case 10:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG_EQUAL, lexema));
                    } else {
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG, lexema));
                    }
                    estado=0;
                    lexema ="";
                    break;
                case 13:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{

                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 14:
                    if(Character.isDigit(c)){
                        estado = 14;
                        lexema += c;
                    }
                    else if(c == '.'){

                    }
                    else if(c == 'E'){

                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    break;

            }

        }


        return tokens;
    }
}

