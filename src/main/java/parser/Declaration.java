package parser;


public class Declaration {
  //declaration
    void DECLARATION(){
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.CLASS)
        {
            CLASS_DECL();
            DECLARATION();
        }
        else if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        }
        else if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
            DECLARATION();
        }
        //Statement
        else if(preanalisis.tipo == TipoToken.ADMIRACION_CIERRA || preanalisis.tipo == TipoToken.RESTA
                || preanalisis.tipo == TipoToken.TRUE || preanalisis.tipo == TipoToken.FALSE
                || preanalisis.tipo == TipoToken.NULL || preanalisis.tipo == TipoToken.THIS
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING
                || preanalisis.tipo == TipoToken.IDENTIFICADOR || preanalisis.tipo == TipoToken.PARENTESIS_ABRE
                || preanalisis.tipo == TipoToken.SUPER
                || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF
                || preanalisis.tipo == TipoToken.PRINT || preanalisis.tipo == TipoToken.RETURN
                || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.CORCHETE_ABRE
                || preanalisis.tipo == TipoToken.SUMA){
            //System.out.println("statement");
            STATEMENT();
            DECLARATION();
        }

    }
    void FUN_DECL(){
        //FUN_DECL -> fun FUNCTION
        if(hayerrores) return;
        if(TipoToken.FUN == preanalisis.tipo)
        {
            coincidir(TipoToken.FUN);
            FUNCTION();
        }
        else{
            hayerrores = true;
            System.out.println("FUN_DECL Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
    }
    void VAR_DECL() {
        //VAR_DECL -> var id VAR_INIT ;
        if(hayerrores) return;
        if(TipoToken.VAR == preanalisis.tipo)
        {
            coincidir(TipoToken.VAR);
            coincidir(TipoToken.IDENTIFICADOR);
            VAR_INIT();
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores = true;
            System.out.println("VAR_DECL Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void VAR_INIT(){
        //VAR_INIT -> = EXPRESSION
        //-> Ɛ
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.IGUAL){
            coincidir(TipoToken.IGUAL);
            EXPRESSION();
        }
    }


}
