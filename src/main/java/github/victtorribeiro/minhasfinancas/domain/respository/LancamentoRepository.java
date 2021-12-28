package github.victtorribeiro.minhasfinancas.domain.respository;

import github.victtorribeiro.minhasfinancas.domain.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
