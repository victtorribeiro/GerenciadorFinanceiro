package github.victtorribeiro.minhasfinancas.service;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;

public interface UsuarioService {
    Usuario autenticar(String email, String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
}
