package github.victtorribeiro.minhasfinancas.service;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.domain.respository.UsuarioRepository;
import github.victtorribeiro.minhasfinancas.exception.RegraNegocioException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenário
        repository.deleteAll();

        //ação
        service.validarEmail("email@email.com");

        //verificação
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        Usuario usuario = Usuario.builder().nome("Victor").email("email@email.com").build();
        repository.save(usuario);

        //ação
        service.validarEmail("email@email.com");

        //verificação

    }
}
