package Interprete;

public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int numeroLinea;


    public Token(TipoToken tipo, String lexema, Object literal, int numeroLinea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.numeroLinea = numeroLinea;
    }

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.numeroLinea = 0;

    }
    
    public int getNumeroLinea() {
        return this.numeroLinea;
    }

    public TipoToken getTipo() {
      return tipo;
    }

    public String getLexema() {
      return lexema;
    }

    public Object getLiteral() {
      return literal;
    }

    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + ">";
    }
}