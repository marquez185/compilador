package Interprete;
import java.util.List;

public class WhileSolver {
    private final Nodo nodo;

    public WhileSolver(Nodo nodo) {
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
        children.remove(0);

        Nodo parentBody = new Nodo(new Token(TipoToken.SEMICOLON, ";"));
        parentBody.insertarHijos(children);

        arithmeticSolver = new SolverAritmetico(condition);
        Object conditionResult = arithmeticSolver.solve();

        arbol = new Arbol(parentBody);

        if (conditionResult instanceof Boolean) {
            while ((Boolean) conditionResult) {
                arbol.recorrer();
                conditionResult = arithmeticSolver.solve();
            }
        }
        else {
            throw new RuntimeException("While condition must be boolean.");
        }

    }
}
