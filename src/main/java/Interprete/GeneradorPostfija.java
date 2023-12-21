package Interprete;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPostfija {

    private final List<Token> infija;
    private final Stack<Token> stack;
    private final List<Token> postfija;

    public GeneradorPostfija(List<Token> infija) {
        this.infija = infija;
        this.stack = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir() {
        boolean controlStructure = false;
        Stack<Token> stackControlStructure = new Stack<>();

        for (int i = 0; i < infija.size(); i++) {
            Token t = infija.get(i);

            if (t.getType() == TokenType.EOF) {
                break;
            }

            if (t.isKeyword()) {
                postfija.add(t);
                if (t.isControlStructure()) {
                    controlStructure = true;
                    stackControlStructure.push(t);
                }
            }
            else if (t.isOperand()) {
                postfija.add(t);
            }
            else if (t.getType() == TokenType.LEFT_PAREN) {
                stack.push(t);
            }
            else if (t.getType() == TokenType.RIGHT_PAREN) {
                while (!stack.isEmpty() && stack.peek().getType() != TokenType.LEFT_PAREN) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                if (!stack.isEmpty()) {
                    if (stack.peek().getType() == TokenType.LEFT_PAREN) {
                        stack.pop();
                    }
                }
                if (controlStructure && infija.get(i + 1).getType() == TokenType.LEFT_BRACE) {
                    postfija.add(new Token(TokenType.SEMICOLON, ";"));
                }
            }
            else if (t.isOperator()) {
                while (!stack.isEmpty() && stack.peek().greaterEqualPrecedence(t)) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                stack.push(t);
            }
            else if (t.getType() == TokenType.SEMICOLON) {
                while (!stack.isEmpty() && stack.peek().getType() != TokenType.LEFT_BRACE) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            }
            else if (t.getType() == TokenType.LEFT_BRACE) {
                // Se mete a la pila, tal como el parentesis. Este paso
                // pudiera omitirse, sólo hay que tener cuidado en el manejo
                // del "}".
                stack.push(t);
            }
            else if (t.getType() == TokenType.RIGHT_BRACE && controlStructure) {

                // Primero verificar si hay un else:
                if (infija.get(i + 1).getType() == TokenType.ELSE) {
                    // Sacar el "{" de la pila
                    stack.pop();
                }
                else {
                    // En este punto, en la pila sólo hay un token: "{"
                    // El cual se extrae y se añade un ";" a cadena postfija,
                    // El cual servirá para indicar que se finaliza la estructura
                    // de control.
                    stack.pop();
                    postfija.add(new Token(TokenType.SEMICOLON, ";"));

                    // Se extrae de la pila de estrucuras de control, el elemento en el tope
                    Token aux = stackControlStructure.pop();

                    if (aux.getType() == TokenType.ELSE) {
                        stackControlStructure.pop();
                        postfija.add(new Token(TokenType.SEMICOLON, ";"));
                    }

                    if (stackControlStructure.isEmpty()) {
                        controlStructure = false;
                    }
                }


            }
        }
        while (!stack.isEmpty()) {
            Token temp = stack.pop();
            postfija.add(temp);
        }

        while (!stackControlStructure.isEmpty()) {
            stackControlStructure.pop();
            postfija.add(new Token(TokenType.SEMICOLON, ";"));
        }

        return postfija;
    }

}
