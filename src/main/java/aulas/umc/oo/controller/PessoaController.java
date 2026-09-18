package aulas.umc.oo.controller;


import aulas.umc.oo.DTO.CadastroPessoaCompletoRequest;
import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.DTO.DocumentoRequest;
import aulas.umc.oo.DTO.EnderecoRequest;
import aulas.umc.oo.mapper.PessoaRequestMapper;
import aulas.umc.oo.model.Documento;
import aulas.umc.oo.model.Endereco;
import aulas.umc.oo.model.Pessoa;
import aulas.umc.oo.model.valueObjects.ValorDoc;
import aulas.umc.oo.repository.PessoaJdbcRepository;
import model.valueObjects.DemaisDados;
import model.valueObjects.Email;
import model.valueObjects.IdadePessoa;
import model.valueObjects.NomePessoa;
import model.valueObjects.Tipo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


    @RestController
    @CrossOrigin(origins = "*")
    @RequestMapping("/api/pessoas")
    public class PessoaController {


        @PostMapping
        public ResponseEntity<CadastroPessoaResponse> cadastrar(@RequestBody CadastroPessoaRequest cadastroPessoaRequest) {

            try {
                CadastroPessoaResponse cadastroPessoaResponse= new PessoaRequestMapper().toCommand(cadastroPessoaRequest);

                return ResponseEntity.ok(cadastroPessoaResponse);

            }catch (Exception ex){

                return ResponseEntity.badRequest().build();

            }


        }

        @PostMapping("/completo")
        public ResponseEntity<CadastroPessoaResponse> cadastrarCompleto(@RequestBody CadastroPessoaCompletoRequest request) {
            try {
                Pessoa pessoa = criarPessoa(request);
                List<Endereco> enderecos = criarEnderecos(request.getEnderecos());
                List<Documento> documentos = criarDocumentos(request.getDocumentos());

                PessoaJdbcRepository repository = new PessoaJdbcRepository();
                repository.insertWithRelations(pessoa, enderecos, documentos);

                return ResponseEntity.ok(toResponse(pessoa));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }

        @GetMapping
        public ResponseEntity<List<CadastroPessoaResponse>> listar() {
            try {
                PessoaJdbcRepository repository = new PessoaJdbcRepository();
                List<Pessoa> pessoas = repository.findAll();
                List<CadastroPessoaResponse> response = new ArrayList<>();

                for (Pessoa pessoa : pessoas) {
                    response.add(toResponse(pessoa));
                }

                return ResponseEntity.ok(response);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }

        @GetMapping("/{id}")
        public ResponseEntity<CadastroPessoaResponse> buscarPorId(@PathVariable UUID id) {
            try {
                PessoaJdbcRepository repository = new PessoaJdbcRepository();
                Optional<Pessoa> pessoa = repository.findById(id);

                if (pessoa.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(toResponse(pessoa.get()));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<CadastroPessoaResponse> atualizar(@PathVariable UUID id, @RequestBody CadastroPessoaRequest request) {
            try {
                Pessoa pessoa = criarPessoa(request);
                pessoa.id = id;

                PessoaJdbcRepository repository = new PessoaJdbcRepository();
                repository.update(pessoa);

                return ResponseEntity.ok(toResponse(pessoa));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> excluir(@PathVariable UUID id) {
            try {
                PessoaJdbcRepository repository = new PessoaJdbcRepository();
                repository.deleteLogical(id);

                return ResponseEntity.noContent().build();
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }

        private Pessoa criarPessoa(CadastroPessoaRequest request) {
            Pessoa pessoa = new Pessoa(new NomePessoa(request.getNome()), new Email(request.getEmail()));
            pessoa.idade = new IdadePessoa(request.getIdade() != null ? request.getIdade() : 18);
            pessoa.tipoSanguineo = request.getTipoSanguineo();

            return pessoa;
        }

        private List<Endereco> criarEnderecos(List<EnderecoRequest> requests) {
            List<Endereco> enderecos = new ArrayList<>();
            if (requests == null) {
                return enderecos;
            }

            for (EnderecoRequest request : requests) {
                Endereco endereco = new Endereco(request.getRua(), request.getNumero(), request.getCidade());
                endereco.id = UUID.randomUUID();
                enderecos.add(endereco);
            }

            return enderecos;
        }

        private List<Documento> criarDocumentos(List<DocumentoRequest> requests) {
            List<Documento> documentos = new ArrayList<>();
            if (requests == null) {
                return documentos;
            }

            for (DocumentoRequest request : requests) {
                Documento documento = new Documento();
                documento.id = UUID.randomUUID();
                documento.setTipo(request.getTipo() != null ? new Tipo(request.getTipo()) : null);
                documento.setValor(request.getValor() != null ? new ValorDoc(request.getValor()) : null);
                documento.setDemaisDados(request.getDemaisDados() != null ? new DemaisDados(request.getDemaisDados()) : null);
                documentos.add(documento);
            }

            return documentos;
        }

        private CadastroPessoaResponse toResponse(Pessoa pessoa) {
            CadastroPessoaResponse response = new CadastroPessoaResponse();
            response.setId(pessoa.id);
            response.setNome(pessoa.nome != null ? pessoa.nome.getValor() : null);
            response.setIdade(pessoa.idade != null ? pessoa.idade.getValor() : null);
            response.setEmail(pessoa.email != null ? pessoa.email.getValor() : null);
            response.setTipoSanguineo(pessoa.tipoSanguineo);

            return response;
        }



    }

