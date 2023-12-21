package Interprete;
import java
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import.* inteprete;
public class Arbol {
    private final Nodo raiz;

    public Arbol(Nodo raiz){
        this.raiz = raiz;
        for(Nodo n : raiz.getHijos()){
            Token t = n.getValue();
            switch (t.tipo)
            {

    }

    public void recorrer(){
        recorrerAux(raiz, 0); // Iniciar el recorrido desde la raíz
    }

    private void recorrerAux(Nodo nodo, int nivel) {
        if (nodo == null) return;
        for( nodo: raiz.getHijos()){
            Token nodo = n.getValue();
            switch (t.tipo) {

                case PLUS:
                case MINUS:
                case STAR:
                case SLASH:
                case LESS:
                case LESS_EQUAL:
                case GREATER_EQUAL:
                case GREATER:
                case EQUAL_EQUAL:
                case BANG_EQUAL:
                case AND:
                case OR:
                    SolverAritmetico solver = new SolverAritmetico(n);
                    solver.solve();
                    break;

                case VAR:
                    VarSolver varSolver = new VarSolver(n);
                    varSolver.solve();
                    break;
                case IF:
                    IfSolver ifSolver = new IfSolver(n);
                    ifSolver.solve();
                    break;
                case FOR:
                    ForSolver forSolver = new ForSolver(n);
                    forSolver.solve();
                    break;
                case WHILE:
                    WhileSolver whileSolver = new WhileSolver(n);
                    whileSolver.solve();
                    break;
                case PRINT:
                    PrintSolver printSolver = new PrintSolver(n);
                    printSolver.solve();
                    break;
            }

        }

        // Aquí puedes procesar el nodo, por ejemplo, imprimir su valor
        imprimirNodo(nodo, nivel);

        // Si el nodo tiene hijos, recorrer cada uno de ellos
        if (nodo.getHijos() != null) {
            for (Nodo hijo : nodo.getHijos()) {
                recorrerAux(hijo, nivel + 1); // Aumentar el nivel para los hijos
            }
        }
    }

    private void imprimirNodo(Nodo nodo, int nivel) {
        // Crear un prefijo basado en el nivel para la indentación
        String prefijo = new String(new char[nivel]).replace("\0", "  ");

        // Obtener el valor del token en el nodo
        Token t = nodo.getValue();

        // Imprimir el token con la indentación apropiada
        System.out.println(prefijo + "Token: " + t.getLexema() + " (Tipo: " + t.getTipo() + ")");
    }
}

