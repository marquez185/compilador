package parser;

import Interprete.Token;

class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }
}