package github.victtorribeiro.minhasfinancas.api.controller;

import github.victtorribeiro.minhasfinancas.api.dto.AtualizaStatusDTO;
import github.victtorribeiro.minhasfinancas.api.dto.LancamentoDTO;
import github.victtorribeiro.minhasfinancas.domain.entity.Lancamento;
import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.domain.enums.StatusLancamento;
import github.victtorribeiro.minhasfinancas.domain.enums.TipoLancamento;
import github.victtorribeiro.minhasfinancas.exception.RegraNegocioException;
import github.victtorribeiro.minhasfinancas.service.LancamentoService;
import github.victtorribeiro.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService service;
    private final UsuarioService usuarioService;

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
        try {
            Lancamento entidade = converter(dto);

            service.salvar(entidade);

            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar-id/{id}")
    public ResponseEntity obterLancamentoPorId(@PathVariable("id") Long id){
        return service.obterPorId(id)
                .map( lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
                .orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
        return service.obterPorId(id).map( entity -> {
                try {
                    Lancamento lancamento = converter(dto);
                    lancamento.setId(entity.getId());
                    service.atualizar(lancamento);
                    return ResponseEntity.ok(lancamento);
                }catch (RegraNegocioException e){
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }).orElseGet(() -> new ResponseEntity("Lan??amento n??o encontrado na base de dados.", HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable("id") long id){
        return service.obterPorId(id).map( entity ->{
            service.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lan??amento n??o encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/buscar")
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
            ){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()){
            return ResponseEntity.badRequest().body("N??o foi possivel realizar a consulta. Usu??rio n??o encontrado para esse ID informado");
        }else {
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PutMapping("/atualizar-status/{id}")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto){
        return service.obterPorId(id).map( entity ->{
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null){
                return ResponseEntity.badRequest().body("N??o foi possivel atualizar status de lan??amento. Status inv??lido");
            }
            try {
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
            new ResponseEntity("Lancamento n??o encontrado na base de Dados.", HttpStatus.BAD_REQUEST)
        );

    }

    private LancamentoDTO converter(Lancamento lancamento){
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .mes(lancamento.getMes())
                .ano(lancamento.getAno())
                .status(lancamento.getStatus().name())
                .tipo(lancamento.getTipo().name())
                .usuario(lancamento.getUsuario().getId())
                .build();
    }

    private Lancamento converter(LancamentoDTO dto){
        Lancamento lancamento =  new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(dto.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usu??rio n??o encontrado para o Id informado."));

        lancamento.setUsuario(usuario);
        if (dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }

}
