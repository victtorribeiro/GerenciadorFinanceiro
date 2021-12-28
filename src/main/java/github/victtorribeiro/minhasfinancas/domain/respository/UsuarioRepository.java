package github.victtorribeiro.minhasfinancas.domain.respository;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
