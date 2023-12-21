package Interprete;

import parser.*;

import java.util.List;

public class Parser {
    private final List <Token> tokens;
    private int i = 0; //Numero del token en la lista
    private Token preanalisis; //Analisis de tokens

    //Literales
    private final Token IDENTIFIER = new Token(TipoToken.IDENTIFIER, "", i);
    private final Token STRING = new Token(TipoToken.STRING,"", i);
    private final Token NUMBER = new Token(TipoToken.NUMBER, "",i);

    // Palabras clave
    private final Token AND = new Token(TipoToken.AND, "", i);
    private final Token ELSE = new Token(TipoToken.ELSE,"", i);
    private final Token FALSE = new Token(TipoToken.FALSE, "",i);
    private final Token FUN = new Token(TipoToken.FUN, "", i);
    private final Token FOR = new Token(TipoToken.FOR,"", i);
    private final Token IF = new Token(TipoToken.IF, "",i);
    private final Token NULL = new Token(TipoToken.NULL, "", i);
    private final Token OR = new Token(TipoToken.OR,"", i);
    private final Token PRINT = new Token(TipoToken.PRINT, "",i);
    private final Token RETURN = new Token(TipoToken.RETURN, "", i);
    private final Token TRUE = new Token(TipoToken.TRUE,"", i);
    private final Token VAR = new Token(TipoToken.VAR, "",i);
    private final Token WHILE = new Token(TipoToken.WHILE, "",i);

    // Tokens de un sólo caracter
    private final Token LEFT_PAREN = new Token(TipoToken.LEFT_PAREN,"", i);
    private final Token RIGHT_PAREN = new Token(TipoToken.RIGHT_PAREN, "",i);
    private final Token LEFT_BRACE = new Token(TipoToken.LEFT_BRACE, "",i);
    private final Token RIGHT_BRACE = new Token(TipoToken.RIGHT_BRACE, "", i);
    private final Token COMMA = new Token(TipoToken.COMMA,"", i);
    private final Token DOT = new Token(TipoToken.DOT, "",i);
    private final Token MINUS = new Token(TipoToken.MINUS,"", i);
    private final Token PLUS = new Token(TipoToken.PLUS, "",i);
    private final Token SEMICOLON = new Token(TipoToken.SEMICOLON,"", i);
    private final Token SLASH = new Token(TipoToken.SLASH, "",i);
    private final Token STAR = new Token(TipoToken.STAR,"", i);
    private final Token LEFT_SQUARE = new Token(TipoToken.LEFT_SQUARE,"", i);
    private final Token RIGHT_SQUARE = new Token(TipoToken.RIGHT_SQUARE, "",i);


    // Tokens para uso logico
    private final Token BANG = new Token(TipoToken.BANG,"", i);
    private final Token BANG_EQUAL = new Token(TipoToken.BANG_EQUAL, "",i);
    private final Token EQUAL = new Token(TipoToken.EQUAL, "",i);
    private final Token EQUAL_EQUAL = new Token(TipoToken.EQUAL_EQUAL,"", i);
    private final Token GREATER = new Token(TipoToken.GREATER, "",i);
    private final Token GREATER_EQUAL = new Token(TipoToken.GREATER_EQUAL, "",i);
    private final Token LESS = new Token(TipoToken.LESS,"", i);
    private final Token LESS_EQUAL = new Token(TipoToken.LESS_EQUAL, "",i);

    //FIN DE CADENA
    private final Token EOF = new Token(TipoToken.EOF, "",i);

    // Modificar para usar la funcion de error de interprete
    private boolean hayErrores = false; //VAR. de errores
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);

        program();

        if(!Interprete.existenErrores && !preanalisis.equals(EOF)){
            System.out.println("Error  No se esperaba el token " + preanalisis.tipo);
            Interprete.error(preanalisis.getNumeroLinea(), "Unexpected token " + preanalisis.getLexema());
        }
    }
    public void program(){
        if(preanalisis.equals(IDENTIFIER) ||
           preanalisis.equals(STRING) ||
           preanalisis.equals(NUMBER) ||
           preanalisis.equals(AND) ||
           preanalisis.equals(ELSE) ||
           preanalisis.equals(FALSE) ||
           preanalisis.equals(FUN) ||
           preanalisis.equals(FOR) ||
           preanalisis.equals(IF) ||
           preanalisis.equals(NULL) ||
           preanalisis.equals(OR) ||
           preanalisis.equals(PRINT) ||
           preanalisis.equals(RETURN) ||
           preanalisis.equals(TRUE) ||
           preanalisis.equals(VAR) ||
           preanalisis.equals(WHILE) ||
           preanalisis.equals(LEFT_PAREN) ||
           preanalisis.equals(RIGHT_PAREN) ||
           preanalisis.equals(LEFT_BRACE) ||
           preanalisis.equals(RIGHT_BRACE) ||
           preanalisis.equals(COMMA) ||
           preanalisis.equals(DOT) ||
           preanalisis.equals(MINUS) ||
           preanalisis.equals(PLUS) ||
           preanalisis.equals(SEMICOLON) ||
           preanalisis.equals(SLASH) ||
           preanalisis.equals(STAR) ||
           preanalisis.equals(LEFT_SQUARE) ||
           preanalisis.equals(RIGHT_SQUARE) ||
           preanalisis.equals(BANG) ||
           preanalisis.equals(BANG_EQUAL) ||
           preanalisis.equals(EQUAL) ||
           preanalisis.equals(EQUAL_EQUAL) ||
           preanalisis.equals(GREATER) ||
           preanalisis.equals(GREATER_EQUAL) ||
           preanalisis.equals(LESS) ||
           preanalisis.equals(LESS_EQUAL))
        Declaration();
        else{
            System.out.println("Error: Expresion incorrecta" + preanalisis.tipo);
        }
    }
    
    private void Declaration() {
        if (Interprete.error())
            return;

        if (preanalisis.equals(CLASS)) {
            classDecl();
            declaration();
        } else if (preanalisis.equals(FUN)) {
            funcDecl();
            declaration();
        } else if (lookahead.equals(VAR)) {
            varDecl();
            declaration();
        } else if (lookahead.equals(MINUS) ||
                lookahead.equals(PLUS) ||
                lookahead.equals(FOR) ||
                lookahead.equals(IF) ||
                lookahead.equals(PRINT) ||
                lookahead.equals(RETURN) ||
                lookahead.equals(WHILE) ||
                lookahead.equals(LEFT_BRACE) ||
                lookahead.equals(TRUE) ||
                lookahead.equals(FALSE) ||
                lookahead.equals(NULL) ||
                lookahead.equals(NUMBER) ||
                lookahead.equals(STRING) ||
                lookahead.equals(IDENTIFIER) ||
                lookahead.equals(LEFT_PAREN){

        }
            StmtExpression();
            Declaration();
        }

    private void term(){
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


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}

