package github.victtorribeiro.minhasfinancas.service.impl;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.domain.respository.UsuarioRepository;
import github.victtorribeiro.minhasfinancas.exception.RegraNegocioException;
import github.victtorribeiro.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Usuário cadastrado com esse email.");
        }
    }
}
