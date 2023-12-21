package Interprete;

public class Arbol {
    private final Nodo raiz;

    public Arbol(Nodo raiz){
        this.raiz = raiz;
    }

    public void recorrer(){
        recorrerAux(raiz, 0); // Iniciar el recorrido desde la raíz
    }

    private void recorrerAux(Nodo nodo, int nivel) {
        if (nodo == null) return;

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

