package Interprete;
import java.util.Objects;

public class SolverAritmetico {
    private final Nodo nodo;

    public SolverAritmetico(Nodo nodo) {
        this.nodo = nodo;
    }

    public Object solve() {
        return solve(nodo);
    }

    private Object solve(Nodo n) {
      //  SymbolTable symbolTable = SymbolTable.getInstance();
        // No tiene hijos, es un operando
        if (n.getHijos() == null) {
            if(n.getValue().getTipo() == TipoToken.NUMBER || n.getValue().getTipo() == TipoToken.STRING){
                return n.getValue().getLiteral();
            }
            else if(n.getValue().getTipo() == TipoToken.IDENTIFIER){
               // return symbolTable.getValue(n.getValue().getLexema());
            }
            else if (n.getValue().getTipo() == TipoToken.TRUE || n.getValue().getTipo() == TipoToken.FALSE) {
                return Boolean.parseBoolean(n.getValue().getLexema());
            }
        }

        // Por simplicidad se asume que la lista de hijos del nodo tiene dos elementos
        Nodo izq = n.getHijos().get(0);
        Nodo der = n.getHijos().get(1);

        Object resultadoIzquierdo = solve(izq);
        Object resultadoDerecho = solve(der);

        // Operaciones con double
        if(resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double) {
            switch (n.getValue().getTipo()) {
                case PLUS:
                    return ((Double) resultadoIzquierdo + (Double) resultadoDerecho);
                case MINUS:
                    return ((Double) resultadoIzquierdo - (Double) resultadoDerecho);
                case STAR:
                    return ((Double) resultadoIzquierdo * (Double) resultadoDerecho);
                case SLASH:
                    return ((Double) resultadoIzquierdo / (Double) resultadoDerecho);
                case EQUAL:
                    return resultadoIzquierdo.equals(resultadoDerecho);
                case BANG_EQUAL:
                    return !resultadoIzquierdo.equals(resultadoDerecho);
                case LESS:
                    return ((Double) resultadoIzquierdo < (Double) resultadoDerecho);
                case LESS_EQUAL:
                    return ((Double) resultadoIzquierdo <= (Double) resultadoDerecho);
                case GREATER:
                    return ((Double) resultadoIzquierdo > (Double) resultadoDerecho);
                case GREATER_EQUAL:
                    return ((Double) resultadoIzquierdo >= (Double) resultadoDerecho);
            }
        }
        else if(resultadoIzquierdo instanceof String && resultadoDerecho instanceof String) {
            boolean equals = String.valueOf(resultadoIzquierdo).equals(String.valueOf(resultadoDerecho));
            switch (n.getValue().getTipo()) {
                case PLUS:
                    return String.valueOf(resultadoIzquierdo).concat(String.valueOf(resultadoDerecho));
                case EQUAL:
                    return equals;
                case BANG_EQUAL:
                    return !equals;
            }
        }
        else if (resultadoIzquierdo instanceof Boolean && resultadoDerecho instanceof Boolean) {
            switch (n.getValue().getTipo()) {
                case AND:
                    return ((Boolean) resultadoIzquierdo && (Boolean) resultadoDerecho);
                case OR:
                    return ((Boolean) resultadoIzquierdo || (Boolean) resultadoDerecho);
                case EQUAL:
                    return resultadoIzquierdo == resultadoDerecho;
                case BANG_EQUAL:
                    return resultadoIzquierdo != resultadoDerecho;
            }
        }
        else if (resultadoIzquierdo instanceof Double && resultadoDerecho instanceof String ||
                resultadoIzquierdo instanceof String && resultadoDerecho instanceof Double) {
            if (Objects.requireNonNull(n.getValue().getTipo()) == TipoToken.PLUS) {
                return resultadoIzquierdo + resultadoDerecho.toString();
            }
        }
        else {
            throw new RuntimeException("Type mismatch");
        }

        return null;
    }
}
