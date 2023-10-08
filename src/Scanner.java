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

                        case '/':
                            if (i + 1 < source.length()) { // Asegurar que no estamos al final de la cadena
                                char nextChar = source.charAt(i + 1); // Mirar el siguiente carácter
                                if (nextChar == '*' && estado == 0) { // Si es un comentario de bloque
                                    estado = 26;
                                } else if (nextChar == '/' && estado == 0) { // Si es un comentario de línea
                                    estado = 30;
                                } else {
                                    tokens.add(new Token(TipoToken.SLASH, "/", i+1));
                                }
                            } else {
                                tokens.add(new Token(TipoToken.SLASH, "/", i+1));
                            }
                            break;

                        case '*':
                           if (estado != 27 && estado != 28) {
                               tokens.add(new Token(TipoToken.STAR, "*", i+1));
                           }
                           break;
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

                // RECONOCIMIENTO DE COMENTARIOS

                case 26:
                    if (c == '*') {
                        estado = 27;
                    } else if (c == '/') {
                        estado = 30;
                    } else {
                        tokens.add(new Token(TipoToken.SLASH, "/", i+1));
                        estado = 0; // estado 0
                    }
                    break;

                case 27:
                    if (c == '*') {
                        estado = 28;
                    }
                    // No generamos un token, seguimos dentro del comentario.
                    break;

                case 28:
                    if (c == '/') {
                        estado = 29;
                    } else if (c != '*') {
                        estado = 27;
                    }
                    // No generamos un token, seguimos dentro del comentario.
                    break;

                case 29:
                    // Aquí simplemente hemos terminado el comentario, volvemos al estado inicial.
                    estado = 0;
                    break;

                case 30:
                    if (c == '\n') {
                        estado = 31;
                    }
                    // No generamos un token, seguimos dentro del comentario.
                    break;

                case 31:
                    // Aquí simplemente hemos terminado el comentario, volvemos al estado inicial.
                    estado = 0;
                    break;

                case 32:
                    tokens.add(new Token(TipoToken.SLASH, "/", i+1));
                    i--;  // Retrocede el puntero para no saltarse el carácter que sigue a `/`.
                    estado = 0;
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
