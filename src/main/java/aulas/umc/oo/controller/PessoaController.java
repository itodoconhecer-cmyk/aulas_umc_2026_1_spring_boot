package aulas.umc.oo.controller;


import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.mapper.PessoaRequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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



    }

