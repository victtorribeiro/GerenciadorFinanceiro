package github.victtorribeiro.minhasfinancas.service.impl;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.domain.repository.UsuarioRepository;
import github.victtorribeiro.minhasfinancas.exception.ErroAutenticacao;
import github.victtorribeiro.minhasfinancas.exception.RegraNegocioException;
import github.victtorribeiro.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    private PasswordEncoder encoder;

    public UsuarioServiceImpl( UsuarioRepository repository, PasswordEncoder encoder ) {
        super();
        this.repository = repository;
        this.encoder = encoder;

    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()){
            throw new ErroAutenticacao("Usuário não encontrado com esse email.");
        }

        boolean senhasBatem = encoder.matches(senha, usuario.get().getSenha());

        if (!senhasBatem){
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        criptogradarSenha(usuario);
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Usuário cadastrado com esse email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }

    private void criptogradarSenha(Usuario usuario){
        String senha = usuario.getSenha();
        String senhaCripto = encoder.encode(senha);
        usuario.setSenha(senhaCripto);
    }


}
