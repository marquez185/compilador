package Interprete;

public class assign {
    mport java.util.List;

    public class AssingSolver {
        private Nodo nodo;

        public AssingSolver(Nodo nodo) {
            this.nodo = nodo;
        }

        public void solve() {
            solve(nodo);
        }

        private void solve(Nodo n) {
            SymbolTable symbolTable = SymbolTable.getInstance();
            SolverAritmetico arithmeticSolver;
            Arbol arbol;

            List<Nodo> children = n.getHijos();
            Nodo variable = children.get(0);
            Nodo value = children.get(1);

            arithmeticSolver = new SolverAritmetico(value);
            Object valueResult = arithmeticSolver.solve();

            symbolTable.setValue(variable.getValue().getLexeme(), valueResult);

        }
    }
}
