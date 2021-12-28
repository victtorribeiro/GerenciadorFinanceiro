package github.victtorribeiro.minhasfinancas.domain.entity;

import github.victtorribeiro.minhasfinancas.domain.enums.StatusLancamento;
import github.victtorribeiro.minhasfinancas.domain.enums.TipoLancamento;
import lombok.*;
import org.springframework.data.convert.Jsr310Converters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Table( name = "lancamento", schema = "financas")
public class Lancamento {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descricao;

    @Column
    private Integer mes;

    @Column
    private Integer ano;

    @ManyToOne
    @JoinColumn( name = "id_usuario")
    private Usuario usuario;

    @Column
    private BigDecimal valor;

    @Column( name = "data_cadastro")
    //@Convert(converter = Jsr310Converters.LocalDateTimeToDateConverter.class)
    private LocalDate dataCadastro;

    @Column
    @Enumerated( value = EnumType.STRING)
    private TipoLancamento tipo;

    @Column
    @Enumerated( value = EnumType.STRING)
    private StatusLancamento status;
}
