package parser;

import Interprete.Token;
public class ExprGrouping extends Expression {
    final Expression expression;

    ExprGrouping(Expression expression) {
        this.expression = expression;
    }
}
