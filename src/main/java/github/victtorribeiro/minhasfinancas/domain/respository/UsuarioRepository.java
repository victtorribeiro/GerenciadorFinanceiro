package github.victtorribeiro.minhasfinancas.domain.respository;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail (String email);



    //Optional<Usuario> findByEmail(String email);
    // EXEMPLO DE CONCATENAÇÃO
    //Optional<Usuario> findByEmailAndNome(String email, String nome);

}
