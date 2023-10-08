import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    
    private int lineaActual = 1;
    private static final Map<String, TipoToken> palabrasReservadas;

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
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        char c;
                
        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            switch (estado){
                case 0:
                    switch (c) {
                        case '(':
                            tokens.add(new Token(TipoToken.LEFT_PAREN, "(",i+1));
                            break;
                        case ')':
                            tokens.add(new Token(TipoToken.RIGHT_PAREN, ")",i+1));
                            break;
                        case '{':
                            tokens.add(new Token(TipoToken.LEFT_BRACE, "{", i+1));
                            break;
                        case '}':
                            tokens.add(new Token(TipoToken.RIGHT_BRACE, "}", i+1));
                            break;
                        case ',':
                            tokens.add(new Token(TipoToken.COMMA, ",", i+1));
                            break;
                        case '.':
                            tokens.add(new Token(TipoToken.DOT, ".", i+1));
                            break;
                        case '-':
                            tokens.add(new Token(TipoToken.MINUS, "-", i+1));
                            break;
                        case '+':
                            tokens.add(new Token(TipoToken.PLUS, "+", i+1));
                            break;
                        case ';':
                            tokens.add(new Token(TipoToken.SEMICOLON, ";",i+1));
                            break;
                        case '!':
                            if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                tokens.add(new Token(TipoToken.BANG_EQUAL, "!=",i+1));
                                i++;  // Avanza para saltar el '='
                            } else {
                                tokens.add(new Token(TipoToken.BANG, "!",i+1));
                            }
                            break;
                        case '=':
                            if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                tokens.add(new Token(TipoToken.EQUAL_EQUAL, "==", i+1));
                                i++;  // Avanza para saltar el '='
                            } else {
                                tokens.add(new Token(TipoToken.EQUAL, "=",i+1));
                            }
                            break;
                        case '>':
                            if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                tokens.add(new Token(TipoToken.GREATER_EQUAL, ">=", i+1));
                                i++;  // Avanza para saltar el '='
                            } else {
                                tokens.add(new Token(TipoToken.GREATER, ">",i+1));
                            }
                            break;

                        case '<':
                            if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                tokens.add(new Token(TipoToken.LESS_EQUAL, "<=", i+1));
                                i++;  // Avanza para saltar el '='
                            } else {
                                tokens.add(new Token(TipoToken.LESS, "<",i+1));
                            }
                            break;

                        /* case '/':
                            tokens.add(new Token(TipoToken.SLASH, "/"));
                            break;
                        case '*':
                            tokens.add(new Token(TipoToken.STAR, "*"));
                            break; */
                    }
                    if(Character.isLetter(c)){
                        estado = 9;
                        lexema += c;
                    } else if(Character.isDigit(c)){
                        estado = 11;
                        lexema += c;

                        /*while(Character.isDigit(c)){
                            lexema += c;
                            i++;
                            c = source.charAt(i);
                        }
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        lexema = "";
                        estado = 0;
                        tokens.add(t);
                        */
                    } else if (c == '"'){
                        estado = 24;
                        //"lexema += c;
                    }
                    lineaActual++;
                    break;

                case 9:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        estado = 9;
                        lexema += c;
                    }
                    else{
                        // Vamos a crear el Token de identificador o palabra reservada
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
                    
                case 11:
                    if (Character.isDigit(c)) {
                        estado = 11;
                        lexema += c;
                    } else if (c == '.') {
                        estado = 12;
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 14;
                        lexema += c;
                    } else if (c == '+' || c == '-') {
                        estado = 12;  // Permitir signos en la parte decimal
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 12:
                    if (Character.isDigit(c)) {
                        estado = 13;
                        lexema += c;
                    } else if ((c == '+' || c == '-') && lexema.charAt(lexema.length() - 1) == 'E') {
                        estado = 12;  // Permitir signos después de 'E'
                        lexema += c;
                    }
                    break;
                    
                case 13:
                    if (Character.isDigit(c)) {
                        estado = 13;
                        lexema += c;
                    } else if (c == 'E') {
                        estado = 14;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 14:
                    if (Character.isDigit(c)) {
                        estado = 16;
                        lexema += c;
                    } else if (c == '+' || c == '-') {
                        estado = 15;
                        lexema += c;
                    }
                    break;
                    
                case 15:
                    if (Character.isDigit(c)) {
                        estado = 16;
                        lexema += c;
                    }
                    break;
                    
                case 16:
                    if (Character.isDigit(c)) {
                        estado = 16;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
         
                case 24:
                   if(!(c == '"')){
                        estado = 24;
                        lexema += c;                                  
                    }
                    
                    else{                      
                        Token t = new Token(TipoToken.STRING, "\"" + lexema + "\"", lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";     
                    }
                    break;
                                  
            }
        }
        
        if (estado == 24) {
        // Error: La cadena no se cerró con comillas
        Interprete.error(lineaActual, "La cadena no se cerro con comillas");
    }
        tokens.add(new Token(TipoToken.EOF, "",  source.length())); //Fin del archivo
        return tokens;
    }
}
