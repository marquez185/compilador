package Interprete;

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
        palabrasReservadas.put("less", TipoToken.LESS);
        palabrasReservadas.put("greater", TipoToken.GREATER);
        palabrasReservadas.put("equal", TipoToken.EQUAL);
        palabrasReservadas.put("less_equal", TipoToken.LESS_EQUAL);
        palabrasReservadas.put("greater_equal", TipoToken.GREATER_EQUAL);
        palabrasReservadas.put("equal_equal", TipoToken.EQUAL);
        palabrasReservadas.put("bang", TipoToken.BANG);
        palabrasReservadas.put("bang_equal", TipoToken.BANG_EQUAL);
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
                        case '[':
                            tokens.add(new Token(TipoToken.LEFT_SQUARE, "[",i+1));
                            break;
                        case ']':
                            tokens.add(new Token(TipoToken.RIGHT_SQUARE, "]",i+1));
                            break;
                      /*  case '!':
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
                            break; */

                        case '/':
                            if (i + 1 < source.length()) {
                                char nextChar = source.charAt(i + 1);
                                if (nextChar == '*' && estado == 0) {
                                    i++; // Avanzar sobre el '*'
                                    estado = 27;
                                } else if (nextChar == '/' && estado == 0) {
                                    i++; // Avanzar sobre el segundo '/'
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
                    } else if (c == '"') {
                        estado = 24;
                        // "lexema += c;
                    } else if (c == '>') {
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
                    lineaActual++;
                    break;
                case 1:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.GREATER_EQUAL, lexema,i));
                        i++;
                    } else {
                        // No añadas el carácter actual al lexema aquí.
                        tokens.add(new Token(TipoToken.GREATER, ">", i));
                    }
                    estado=0;
                    lexema ="";
                    i--;
                    break;

                case 4:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.LESS_EQUAL, lexema, i));
                        i++; // Avanza al siguiente carácter para no procesar el '=' nuevamente
                    } else {
                        tokens.add(new Token(TipoToken.LESS, "<", i));
                        // i--; // Retrocede para volver a procesar el carácter actual en la siguiente iteración
                    }
                    i--;
                    estado = 0;
                    lexema = "";
                    break;
                case 7:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL_EQUAL, lexema, i));
                    } else {
                        tokens.add(new Token(TipoToken.EQUAL, "=", i)); // Solo usa "="
                        i--; // retrocede para procesar el carácter actual en el próximo ciclo
                    }
                    estado=0;
                    lexema ="";
                    break;

                case 10:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG_EQUAL, lexema, i));
                    } else {
                        tokens.add(new Token(TipoToken.BANG, "!", i)); // Solo usa "!"
                        i--; // retrocede para procesar el carácter actual en el próximo ciclo
                    }
                    estado=0;
                    lexema ="";
                    break;
            /*    case 7:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL_EQUAL, lexema,i+1));
                    } else {
                        lexema += c;
                        tokens.add(new Token(TipoToken.EQUAL, lexema,i+1));
                    }
                    estado=0;
                    lexema ="";
                    break;
                case 10:
                    if (c == '=') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG_EQUAL, lexema, i+1));
                    } else {
                        lexema += c;
                        tokens.add(new Token(TipoToken.BANG, lexema, i+1));
                    }
                    estado=0;
                    lexema ="";
                    break; */
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

                // INICIO DEL NUMERO DE PUNTO FLOTANTE
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

                // PARTE DECIMAL DEL NUMERO
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

                // PARTE EXPONENCIAL DEL NUMERO
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

                // FINALIZAR ULTIMOS DIGITOS Y CREAMOS TOKEN NUMBER
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


                // CREAR STRINGS
                case 24:
                    if(c=='\n'){
                        Interprete.error(lineaActual, "Error en la cadena ");
                        estado=0;
                    }
                    else{
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
                        estado = 0; // estado 0}
                    }

                    break;

                case 27:
                    if (c == '*') {
                        estado = 28;
                    }
                    break;

                case 28:
                    if (c == '/') {
                        estado = 0; // Terminó el comentario de bloque
                    } else if (c != '*') {
                        estado = 27; // Regresar al estado anterior
                    }
                    break;

                case 29:
                    // Aquí simplemente hemos terminado el comentario, volvemos al estado inicial.
                    i--;
                    estado = 0;
                    break;

                case 30:
                    if (c == '\n') {
                        estado = 0; // Terminó el comentario de línea
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
            Interprete.error(lineaActual, "La cadena no se cerro correctamente");
        }
        tokens.add(new Token(TipoToken.EOF, "",  source.length())); //Fin del archivo
        return tokens;
    }
}