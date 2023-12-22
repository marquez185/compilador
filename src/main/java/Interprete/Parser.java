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

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        i = 0;
        preanalisis = tokens.get(i);

        program();

        if (!Interprete.existenErrores && !preanalisis.equals(EOF)) {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
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
            assignment();
            logicOr();
            assignmentOpc();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void assignmentOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(EQUAL)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void logicOr2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(OR)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void logicAnd2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(AND)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void equality2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(BANG_EQUAL)) {
            match(BANG_EQUAL);
            comparison();
            equality2();
        } else if (preanalisis.equals(EQUAL)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void comparison2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(GREATER)) {
            match(GREATER);
            term();
            comparison2();
        } else if (preanalisis.equals(GREATER_EQUAL)) {
            match(GREATER_EQUAL);
            term();
            comparison2();
        } else if (preanalisis.equals(LESS)) {
            match(LESS);
            term();
            comparison2();
        } else if (preanalisis.equals(LESS_EQUAL)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void term2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(MINUS)) {
            match(MINUS);
            factor();
            term2();
        } else if (preanalisis.equals(PLUS)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void factor2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(SLASH)) {
            match(SLASH);
            unary();
            factor2();
        } else if (preanalisis.equals(STAR)) {
            match(STAR);
            unary();
            factor2();
        }
    }

    private void unary() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(BANG)) {
            match(BANG);
            unary();
        } else if (preanalisis.equals(MINUS)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Inesperado" + preanalisis.getLexema());
        }
    }

    private void call2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(LEFT_PAREN)) {
            match(LEFT_PAREN);
            argumentsOpc();
            match(RIGHT_PAREN);
            call2();
        } else if (preanalisis.equals(DOT)) {
            match(DOT);
            match(IDENTIFIER);
            call2();
        }
    }

    private void primary() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(TRUE)) {
            match(TRUE);
        } else if (preanalisis.equals(FALSE)) {
            match(FALSE);
        } else if (preanalisis.equals(NULL)) {
            match(NULL);
        } else if (preanalisis.equals(NUMBER)) {
            match(NUMBER);
        } else if (preanalisis.equals(STRING)) {
            match(STRING);
        } else if (preanalisis.equals(IDENTIFIER)) {
            match(IDENTIFIER);
        } else if (preanalisis.equals(LEFT_PAREN)) {
            match(LEFT_PAREN);
            expression();
            match(RIGHT_PAREN);
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Inesperado" + preanalisis.getLexema());
        }
    }

    //================================================================== BLOQUE DE OTROS ==================================================================
    private void function() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(IDENTIFIER)) {
            match(IDENTIFIER);
            match(LEFT_PAREN);
            parametersOpc();
            match(RIGHT_PAREN);
            block();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba identificador" + preanalisis.getLexema());
        }
    }

    private void functions() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(FUN)) {
            funcDecl();
            functions();
        }
    }

    private void parametersOpc() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(IDENTIFIER)) {
            parameters();
        }
    }

    private void parameters() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(IDENTIFIER)) {
            match(IDENTIFIER);
            parameters2();
        } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba identificador" + preanalisis.getLexema());
        }
    }

    private void parameters2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(COMMA)) {
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
            Interprete.error(preanalisis.getNumeroLinea(), "Error  Se esperaba argumentos" + preanalisis.getLexema());
        }
    }

    private void arguments2() {
        if (Interprete.existenErrores) {
            return;
        }

        if (preanalisis.equals(COMMA)) {
            match(COMMA);
            expression();
            arguments2();
        }
    }

    private void match(Token terminal) {
        if (Interprete.existenErrores) {
            return;
        }
        if (preanalisis.equals(terminal)) {
            i++;
            preanalisis = tokens.get(i);
        } else {
            Interprete.existenErrores = true;
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }
    /*
    private void term() {
        factor();
        term2();
    }

    private Expression factor(){
        Expression expr = unary();
        expr = factor2(expr);
        return expr;
    }

    private Expression factor2(Expression expr){
        switch (preanalisis.getTipo()){
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = unary();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return factor2(expb);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = unary();
                expb = new ExprBinary(expr, operador, expr2);
                return factor2(expb);
        }
        return expr;
    }

    private Expression unary(){
        switch (preanalisis.getTipo()){
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = unary();
                return new ExprUnary(operador, expr);
            case MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr = unary();
                return new ExprUnary(operador, expr);
            default:
                return call();
        }
    }

    private Expression call(){
        Expression expr = primary();
        expr = call2(expr);
        return expr;
    }

    private Expression call2(Expression expr){
        switch (preanalisis.getTipo()){
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = argumentsOptional();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return call2(ecf);
        }
        return expr;
    }

    private Expression primary(){
        switch (preanalisis.getTipo()){
            case TRUE:
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.getLiteral());
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.getLiteral());
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                Expresion expr = expression();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
        }
        return null;
    }


    private void match(TipoToken tt) throws ParserException {
        if(preanalisis.getTipo() ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.getPosition().getLine() +
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }
     */

}
