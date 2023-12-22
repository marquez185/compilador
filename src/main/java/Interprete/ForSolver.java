package Interprete;


import java.util.List;

public class ForSolver {
    private Nodo nodo;

    public ForSolver(Nodo nodo) {
        this.nodo = nodo;
    }

    public void solve() {
        solve(nodo);
    }

    private void solve(Nodo n) {
        SolverAritmetico arithmeticSolver;
        Arbol arbol;

        List<Nodo> children = n.getHijos();
        Nodo assignDeclaration = children.get(0);
        Nodo condition = children.get(1);
        Nodo steps = children.get(2);

        Nodo parentBody = new Nodo(new Token(TipoToken.SEMICOLON, ";"));
        parentBody.insertarHijos(children.subList(3, children.size()));
        parentBody.insertarHijo(steps);

        arbol = new Arbol(parentBody);

        // Es una declaración
        if (assignDeclaration.getValue().getTipo() == TipoToken.VAR) {
            VarSolver varSolver = new VarSolver(assignDeclaration);
            varSolver.solve();
        }
        // Es una asignación, la variable ya ha sido declarada
        else if (assignDeclaration.getValue().getTipo() == TipoToken.IDENTIFIER) {
            AssingSolver assingSolver = new AssingSolver(assignDeclaration);
            assingSolver.solve();
        }

        arithmeticSolver = new SolverAritmetico(condition);
        Object conditionResult = arithmeticSolver.solve();

        if (conditionResult instanceof Boolean) {
            while ((Boolean) conditionResult) {
                arbol.recorrer();
                conditionResult = arithmeticSolver.solve();
            }
        }

    }
}
