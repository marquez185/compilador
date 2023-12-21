package Interprete;

import parser.*;

import java.util.List;

public class Parser {
    private final List <Token> tokens;
    //Literales
    private final Token IDENTIFIER = new Token(TipoToken.IDENTIFIER, "identificador");
    private final Token STRING = new Token(TipoToken.STRING,"string");
    private final Token NUMBER = new Token(TipoToken.NUMBER, "number");

    // Palabras clave
    private final Token AND = new Token(TipoToken.AND, "and");
    private final Token ELSE = new Token(TipoToken.ELSE,"else");
    private final Token FALSE = new Token(TipoToken.FALSE, "false");
    private final Token FUN = new Token(TipoToken.FUN, "fun");
    private final Token FOR = new Token(TipoToken.FOR,"for");
    private final Token IF = new Token(TipoToken.IF, "if");
    private final Token NULL = new Token(TipoToken.NULL, "null");
    private final Token OR = new Token(TipoToken.OR,"or");
    private final Token PRINT = new Token(TipoToken.PRINT, "print");
    private final Token RETURN = new Token(TipoToken.RETURN, "return");
    private final Token TRUE = new Token(TipoToken.TRUE,"true");
    private final Token VAR = new Token(TipoToken.VAR, "var");
    private final Token WHILE = new Token(TipoToken.WHILE, "while");

    // Tokens de un sólo caracter
    private final Token LEFT_PAREN = new Token(TipoToken.LEFT_PAREN,"(");
    private final Token RIGHT_PAREN = new Token(TipoToken.RIGHT_PAREN, ")");
    private final Token LEFT_BRACE = new Token(TipoToken.LEFT_BRACE, "{");
    private final Token RIGHT_BRACE = new Token(TipoToken.RIGHT_BRACE, "}");
    private final Token COMMA = new Token(TipoToken.COMMA,",");
    private final Token DOT = new Token(TipoToken.DOT, ".");
    private final Token MINUS = new Token(TipoToken.MINUS,"-");
    private final Token PLUS = new Token(TipoToken.PLUS, "+");
    private final Token SEMICOLON = new Token(TipoToken.SEMICOLON,";");
    private final Token SLASH = new Token(TipoToken.SLASH, "/");
    private final Token STAR = new Token(TipoToken.STAR,"*");
    private final Token LEFT_SQUARE = new Token(TipoToken.LEFT_SQUARE,"[");
    private final Token RIGHT_SQUARE = new Token(TipoToken.RIGHT_SQUARE, "]");


    // Tokens para uso logico
    private final Token BANG = new Token(TipoToken.BANG,"!");
    private final Token BANG_EQUAL = new Token(TipoToken.BANG_EQUAL, "!=");
    private final Token EQUAL = new Token(TipoToken.EQUAL, "=");
    private final Token EQUAL_EQUAL = new Token(TipoToken.EQUAL_EQUAL,"==");
    private final Token GREATER = new Token(TipoToken.GREATER, ">");
    private final Token GREATER_EQUAL = new Token(TipoToken.GREATER_EQUAL, ">=");
    private final Token LESS = new Token(TipoToken.LESS,"<");
    private final Token LESS_EQUAL = new Token(TipoToken.LESS_EQUAL, "<=");

    //FIN DE CADENA
    private final Token EOF = new Token(TipoToken.EOF, "");

    private int i = 0; //Numero del token en la lista
    private Token preanalisis; //Analisis de tokens

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);

        program();

        if(!Interprete.existenErrores && !preanalisis.equals(EOF)){
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
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
     //----------------------------------BLOQUE DE STATEMENTS (SEBAS)---------------------------------------------------------------
    private void statement() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                exprSTMT();
        } else {
             Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
     }
    
    private void exprSTMT() {
        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                expression();
          match(SEMICOLON);
        } else {
          Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }
    
    private void expression() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                assignment();
        }
    }
    
    private void assignment() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                assignment();
                logicOr();
                assignmentOpc();
          } else {
            Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
          }
    }

     private void assignmentOpc() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(EQUAL)) {
          match(EQUAL);
          expression();
        }
     }

    private void logicOr() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                logicAnd();
                logicOr2();
        } else {
         Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }

    private void logicOr2() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(OR)) {
          match(OR);
          logicAnd();
          logicOr2();
        }
     }
    
    private void logicAnd() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                equality();
                logicAnd2();
        } else {
           Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }
    
    private void logicAnd2() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(AND)) {
            match(AND);
            equality();
            logicAnd2();
        }
     }
    
    
    private void equality() {
        if (Interprete.existenErrores)
            return;

        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                comparison();
                equality2();
        } else {
             Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }
    
    private void equality2() {
        if (Interprete.existenErrores)
            return;

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
        if (Interprete.existenErrores)
            return;
        
        if (preanalisis.equals(MINUS)||
            preanalisis.equals(MINUS) ||
            preanalisis.equals(TRUE) ||
            preanalisis.equals(FALSE) ||
            preanalisis.equals(NULL) ||
            preanalisis.equals(NUMBER) ||
            preanalisis.equals(STRING) ||
            preanalisis.equals(IDENTIFIER) ||
            preanalisis.equals(LEFT_PAREN)) {
                term();
                comparison2();
        } else {
             Interprete.error(preanalisis.getNumeroLinea(), "Error  No se esperaba el token" + preanalisis.getLexema());
        }
    }
    
    private void comparison2() {
       if (Interprete.existenErrores)
            return;

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
    //----------------------------------------------------------------------------------------------------------------------------------------
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

   /*
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
    private void match(Token terminal) {
        if (Interprete.existenErrores)
          return;
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
}

