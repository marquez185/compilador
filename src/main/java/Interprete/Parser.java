package Interprete;

import java.util.List;
public class Parser {

    private final List<Token> tokens;
    //Literales
    private final Token IDENTIFIER = new Token(TipoToken.IDENTIFIER, "identificador");
    private final Token STRING = new Token(TipoToken.STRING, "string");
    private final Token NUMBER = new Token(TipoToken.NUMBER, "number");

    // Palabras clave
    private final Token AND = new Token(TipoToken.AND, "and");
    private final Token ELSE = new Token(TipoToken.ELSE, "else");
    private final Token FALSE = new Token(TipoToken.FALSE, "false");
    private final Token FUN = new Token(TipoToken.FUN, "fun");
    private final Token FOR = new Token(TipoToken.FOR, "for");
    private final Token IF = new Token(TipoToken.IF, "if");
    private final Token NULL = new Token(TipoToken.NULL, "null");
    private final Token OR = new Token(TipoToken.OR, "or");
    private final Token PRINT = new Token(TipoToken.PRINT, "print");
    private final Token RETURN = new Token(TipoToken.RETURN, "return");
    private final Token TRUE = new Token(TipoToken.TRUE, "true");
    private final Token VAR = new Token(TipoToken.VAR, "var");
    private final Token WHILE = new Token(TipoToken.WHILE, "while");

    // Tokens de un sólo caracter
    private final Token LEFT_PAREN = new Token(TipoToken.LEFT_PAREN, "(");
    private final Token RIGHT_PAREN = new Token(TipoToken.RIGHT_PAREN, ")");
    private final Token LEFT_BRACE = new Token(TipoToken.LEFT_BRACE, "{");
    private final Token RIGHT_BRACE = new Token(TipoToken.RIGHT_BRACE, "}");
    private final Token COMMA = new Token(TipoToken.COMMA, ",");
    private final Token DOT = new Token(TipoToken.DOT, ".");
    private final Token MINUS = new Token(TipoToken.MINUS, "-");
    private final Token PLUS = new Token(TipoToken.PLUS, "+");
    private final Token SEMICOLON = new Token(TipoToken.SEMICOLON, ";");
    private final Token SLASH = new Token(TipoToken.SLASH, "/");
    private final Token STAR = new Token(TipoToken.STAR, "*");
    private final Token LEFT_SQUARE = new Token(TipoToken.LEFT_SQUARE, "[");
    private final Token RIGHT_SQUARE = new Token(TipoToken.RIGHT_SQUARE, "]");

    // Tokens para uso logico
    private final Token BANG = new Token(TipoToken.BANG, "!");
    private final Token BANG_EQUAL = new Token(TipoToken.BANG_EQUAL, "!=");
    private final Token EQUAL = new Token(TipoToken.EQUAL, "=");
    private final Token EQUAL_EQUAL = new Token(TipoToken.EQUAL_EQUAL, "==");
    private final Token GREATER = new Token(TipoToken.GREATER, ">");
    private final Token GREATER_EQUAL = new Token(TipoToken.GREATER_EQUAL, ">=");
    private final Token LESS = new Token(TipoToken.LESS, "<");
    private final Token LESS_EQUAL = new Token(TipoToken.LESS_EQUAL, "<=");

    //FIN DE CADENA
    private final Token EOF = new Token(TipoToken.EOF, "");

    private int i = 0; //Numero del token en la lista
    private Token preanalisis; //Analisis de tokens

    private void declaracion() {
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        i = 0;
        preanalisis = tokens.get(i);

        program();

        if (!Interprete.existenErrores && !(preanalisis.tipo == TipoToken.EOF)) {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (PARSE): " + preanalisis.getLexema());
        }
    }

    private void program() {
        if (preanalisis.tipo == TipoToken.VAR
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.PLUS
                || preanalisis.tipo == TipoToken.FUN
                || preanalisis.tipo == TipoToken.FOR
                || preanalisis.tipo == TipoToken.IF
                || preanalisis.tipo == TipoToken.PRINT
                || preanalisis.tipo == TipoToken.RETURN
                || preanalisis.tipo == TipoToken.WHILE
                || preanalisis.tipo == TipoToken.LEFT_BRACE
                || preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            DECLARATION();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  expresion incorrecta (PROGRAM): " + preanalisis.getLexema());
        }
    }

    private void DECLARATION() {
        if (Interprete.existenErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.FUN) {
            funcDecl();
            DECLARATION();
        } else if (preanalisis.tipo == TipoToken.VAR) {
            varDecl();
            DECLARATION();
        } else if (preanalisis.tipo == TipoToken.VAR
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.PLUS
                || preanalisis.tipo == TipoToken.FUN
                || preanalisis.tipo == TipoToken.FOR
                || preanalisis.tipo == TipoToken.IF
                || preanalisis.tipo == TipoToken.PRINT
                || preanalisis.tipo == TipoToken.RETURN
                || preanalisis.tipo == TipoToken.WHILE
                || preanalisis.tipo == TipoToken.LEFT_BRACE
                || preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            statement();
            DECLARATION();
        }
    }

    private void funcDecl() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.FUN) {
            match(FUN);
            function();
        } else {
            System.out.println("Error: Se esperaba fun" + preanalisis.tipo);
        }
    }

    private void varDecl() {
        if (Interprete.existenErrores) {
            return;
        }
        if (preanalisis.tipo == TipoToken.VAR) {
            match(VAR);
            match(IDENTIFIER);
            varInit();
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error se esperaba variable (VARDECL): ");
        }
    }

    private void varInit() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(EQUAL);
            expression();
        }

    }

    //================================================================== BLOQUE DE STATEMENTS ==================================================================
    private void statement() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            exprSTMT();
        } else if (preanalisis.tipo == TipoToken.FOR) {
            forSTMT();
        } else if (preanalisis.tipo == TipoToken.IF) {
            ifSTMT();
        } else if (preanalisis.tipo == TipoToken.PRINT) {
            printSTMT();
        } else if (preanalisis.tipo == TipoToken.RETURN) {
            returnSTMT();
        } else if (preanalisis.tipo == TipoToken.WHILE) {
            whileSTMT();
        } else if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            block();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (STATEMENT): " + preanalisis.getLexema());
        }
    }

    private void exprSTMT() {
        if (preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            expression();
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (EXPRSTMT): " + preanalisis.getLexema());
        }
    }

    private void forSTMT() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.FOR) {
            match(FOR);
            match(LEFT_PAREN);
            forSTMT1();
            forSTMT2();
            forSTMT3();
            match(RIGHT_PAREN);
            statement();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (FORSTMT): " + preanalisis.getLexema());
        }
    }

    private void forSTMT1() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.VAR) {
            varDecl();
        } else if (preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            exprSTMT();
        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (FORSTMT1): " + preanalisis.getLexema());
        }
    }

    private void forSTMT2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            expression();
            match(SEMICOLON);
        } else if (preanalisis.tipo == TipoToken.SEMICOLON) {
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (FORSTMT2): " + preanalisis.getLexema());
        }
    }

    private void forSTMT3() {
        if (Interprete.existenErrores) {
            return;
        }
        // if (preanalisis.equals(BANG)
        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            expression();
        }
    }

    private void ifSTMT() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.IF) {
            match(IF);
            match(LEFT_PAREN);
            expression();
            match(RIGHT_PAREN);
            statement();
            elseStatement();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (IFSTMT): " + preanalisis.getLexema());
        }
    }

    private void elseStatement() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.ELSE) {
            match(ELSE);
            statement();
        }
    }

    private void printSTMT() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.PRINT) {
            match(PRINT);
            expression();
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (PRINTSTMT): " + preanalisis.getLexema());
        }
    }

    private void returnSTMT() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.RETURN) {
            match(RETURN);
            returnSTMTOpc();
            match(SEMICOLON);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (RETURNSTMT): " + preanalisis.getLexema());
        }
    }

    private void returnSTMTOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            expression();
        }
    }

    private void whileSTMT() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.WHILE) {
            match(WHILE);
            match(LEFT_PAREN);
            expression();
            match(RIGHT_PAREN);
            statement();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (WHILESTMT): " + preanalisis.getLexema());
        }
    }

    private void block() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.LEFT_BRACE) {
            match(LEFT_BRACE);
            blockDecl();
            match(RIGHT_BRACE);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba \"{\" (BLOCK)" + preanalisis.getLexema());
        }
    }

    private void blockDecl() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.VAR
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.PLUS
                || preanalisis.tipo == TipoToken.FOR
                || preanalisis.tipo == TipoToken.IF
                || preanalisis.tipo == TipoToken.FUN
                || preanalisis.tipo == TipoToken.PRINT
                || preanalisis.tipo == TipoToken.RETURN
                || preanalisis.tipo == TipoToken.WHILE
                || preanalisis.tipo == TipoToken.LEFT_BRACE
                || preanalisis.tipo == TipoToken.BANG
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            DECLARATION();
            blockDecl();
        }
    }

    //================================================================== BLOQUE DE EXPRESIONES ==================================================================
    private void expression() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            assignment();
        }
    }

    private void assignment() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            logicOr();
            assignmentOpc();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (ASSIGNMENT): " + preanalisis.getLexema());
        }
    }

    private void assignmentOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.EQUAL) {
            match(EQUAL);
            expression();
        }
    }

    private void logicOr() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            logicAnd();
            logicOr2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (LOGICOR): " + preanalisis.getLexema());
        }
    }

    private void logicOr2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.OR) {
            match(OR);
            logicAnd();
            logicOr2();
        }
    }

    private void logicAnd() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            equality();
            logicAnd2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (LOGICOR2): " + preanalisis.getLexema());
        }
    }

    private void logicAnd2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.AND) {
            match(AND);
            equality();
            logicAnd2();
        }
    }

    private void equality() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            comparison();
            equality2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (EQUALIYT): " + preanalisis.getLexema());
        }
    }

    private void equality2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL) {
            match(BANG_EQUAL);
            comparison();
            equality2();
        } else if (preanalisis.tipo == TipoToken.EQUAL) {
            match(EQUAL);
            comparison();
            equality2();
        }
    }

    private void comparison() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            term();
            comparison2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (COMPARISON): " + preanalisis.getLexema());
        }
    }

    private void comparison2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.GREATER) {
            match(GREATER);
            term();
            comparison2();
        } else if (preanalisis.tipo == TipoToken.GREATER_EQUAL) {
            match(GREATER_EQUAL);
            term();
            comparison2();
        } else if (preanalisis.tipo == TipoToken.LESS) {
            match(LESS);
            term();
            comparison2();
        } else if (preanalisis.tipo == TipoToken.LESS_EQUAL) {
            match(LESS_EQUAL);
            term();
            comparison2();
        }
    }

    private void term() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            factor();
            term2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (TERM): " + preanalisis.getLexema());
        }
    }

    private void term2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.MINUS) {
            match(MINUS);
            factor();
            term2();
        } else if (preanalisis.tipo == TipoToken.PLUS) {
            match(PLUS);
            factor();
            term2();
        }
    }

    private void factor() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            unary();
            factor2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (FACTOR): " + preanalisis.getLexema());
        }
    }

    private void factor2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.SLASH) {
            match(SLASH);
            unary();
            factor2();
        } else if (preanalisis.tipo == TipoToken.STAR) {
            match(STAR);
            unary();
            factor2();
        }
    }

    private void unary() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG) {
            match(BANG);
            unary();
        } else if (preanalisis.tipo == TipoToken.MINUS) {
            match(MINUS);
            unary();
        } else if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            call();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (UNARY): " + preanalisis.getLexema());
        }
    }

    private void call() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            primary();
            call2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Inesperado (CALL): " + preanalisis.getLexema());
        }
    }

    private void call2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
            match(LEFT_PAREN);
            argumentsOpc();
            match(RIGHT_PAREN);
            call2();
        } else if (preanalisis.tipo == TipoToken.DOT) {
            match(DOT);
            match(IDENTIFIER);
            call2();
        }
    }

    private void primary() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.TRUE) {
            match(TRUE);
        } else if (preanalisis.tipo == TipoToken.FALSE) {
            match(FALSE);
        } else if (preanalisis.tipo == TipoToken.NULL) {
            match(NULL);
        } else if (preanalisis.tipo == TipoToken.NUMBER) {
            match(NUMBER);
        } else if (preanalisis.tipo == TipoToken.STRING) {
            match(STRING);
        } else if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(IDENTIFIER);
        } else if (preanalisis.tipo == TipoToken.LEFT_PAREN) {
            match(LEFT_PAREN);
            expression();
            match(RIGHT_PAREN);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Inesperado (PRIMARY): " + preanalisis.getLexema());
        }
    }

    //================================================================== BLOQUE DE OTROS ==================================================================
    private void function() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(IDENTIFIER);
            match(LEFT_PAREN);
            parametersOpc();
            match(RIGHT_PAREN);
            block();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba identificador (FUNCTION): " + preanalisis.getLexema());
        }
    }

    private void functions() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.FUN) {
            funcDecl();
            functions();
        }
    }

    private void parametersOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            parameters();
        }
    }

    private void parameters() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.IDENTIFIER) {
            match(IDENTIFIER);
            parameters2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba identificador (PARAMETERS)" + preanalisis.getLexema());
        }
    }

    private void parameters2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.COMMA) {
            match(COMMA);
            match(IDENTIFIER);
            parameters2();
        }
    }

    private void argumentsOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            arguments();
        }
    }

    private void arguments() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.BANG_EQUAL
                || preanalisis.tipo == TipoToken.MINUS
                || preanalisis.tipo == TipoToken.TRUE
                || preanalisis.tipo == TipoToken.STAR
                || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER
                || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFIER
                || preanalisis.tipo == TipoToken.LEFT_PAREN) {
            expression();
            arguments2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba argumentos (ARGUMENTS)" + preanalisis.getLexema());
        }
    }

    private void arguments2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.tipo == TipoToken.COMMA) {
            match(COMMA);
            expression();
            arguments2();
        }
    }

    private void match(Token terminal) {
        if (Interprete.existenErrores) {
            return;
        }

        // Verificar si el tipo del token preanalisis coincide con el tipo del token terminal.
        if (preanalisis.getTipo() == terminal.getTipo()) {
            i++;
            // Asegurar que i no exceda el tamaño de la lista de tokens.
            if (i < tokens.size()) {
                preanalisis = tokens.get(i);
            } else {
                // Manejar el caso de fin de lista de tokens.
                preanalisis = new Token(TipoToken.EOF, "");
            }
        } else {
            Interprete.existenErrores = true;
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token (match) " + preanalisis.getLexema());
        }
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }
}