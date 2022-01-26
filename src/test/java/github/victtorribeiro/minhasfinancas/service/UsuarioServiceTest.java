package github.victtorribeiro.minhasfinancas.service;

import com.sun.source.tree.ModuleTree;
import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.domain.respository.UsuarioRepository;
import github.victtorribeiro.minhasfinancas.exception.ErroAutenticacao;
import github.victtorribeiro.minhasfinancas.exception.RegraNegocioException;
import github.victtorribeiro.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @Before
    public void setUp(){
        service = new UsuarioServiceImpl(repository);
    }

    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso(){
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado(){
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));

        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado com esse email.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@emai.com","123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");




    }



    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //ação
        service.validarEmail("email@email.com");

        //verificação
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //ação
        service.validarEmail("email@email.com");

        //verificação

    }
}
