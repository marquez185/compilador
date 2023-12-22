package Interprete;
import java.util.ArrayList;
import java.util.List;

public class IfSolver {
    private final Nodo nodo;

    public IfSolver(Nodo nodo) {
        this.nodo = nodo;
    }

    public void solve() {
        solve(nodo);
    }

    private void solve(Nodo n) {
        SolverAritmetico arithmeticSolver;
        Arbol arbol;

        List<Nodo> children = n.getHijos();
        Nodo condition = children.get(0);

        Nodo parentIfBody = new Nodo(new Token(TipoToken.SEMICOLON, ";"));
        Nodo parentElseBody = new Nodo(new Token(TipoToken.SEMICOLON, ";"));

        if (children.get(children.size() - 1).getValue().getTipo() == TipoToken.ELSE) {
            parentElseBody.insertarHijos(children.get(children.size() - 1).getHijos());
        }
        else {
            parentElseBody = null;
        }

        parentIfBody.insertarHijos(children.subList(1, children.size()));

        arithmeticSolver = new SolverAritmetico(condition);
        Object conditionResult = arithmeticSolver.solve();

        if (conditionResult instanceof Boolean) {
            if ((Boolean) conditionResult) {
                arbol = new Arbol(parentIfBody);
                arbol.recorrer();
            }
            else if (parentElseBody != null) {
                arbol = new Arbol(parentElseBody);
                arbol.recorrer();
            }
        }
        else {
            throw new RuntimeException("If condition must be boolean.");
        }

    }

}
