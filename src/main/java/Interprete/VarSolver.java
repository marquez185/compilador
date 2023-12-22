package Interprete;

public class VarSolver {
    private final Nodo nodo;

    public VarSolver(Nodo nodo) {
        this.nodo = nodo;
    }

    public void solve() {
        solve(nodo);
    }

    private void solve(Nodo n) {
        //SymbolTable symbolTable = SymbolTable.getInstance();
        SolverAritmetico arithmeticSolver;

        Nodo izq = n.getHijos().get(0);
        Nodo der = n.getHijos().size() > 1 ? n.getHijos().get(1) : null;

        arithmeticSolver = new SolverAritmetico(der);

        String resultadoIzq = izq.getValue().getLexema();
        Object resultadoDer = der == null ? null : arithmeticSolver.solve();

        //symbolTable.addVar(resultadoIzq, resultadoDer);

    }

}
