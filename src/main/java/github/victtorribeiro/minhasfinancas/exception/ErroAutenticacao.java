package github.victtorribeiro.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException{

    public ErroAutenticacao(String mensagem){
        super(mensagem);
    }
}
