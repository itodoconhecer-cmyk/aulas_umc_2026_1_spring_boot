package aulas.umc.oo.controller;


import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.command.CadastrarPessoaCommand;
import aulas.umc.oo.mapper.PessoaRequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


    @RestController
    @RequestMapping("/api/pessoas")
    public class PessoaController {


        @PostMapping
        public ResponseEntity<CadastroPessoaResponse> cadastrar(@RequestBody CadastroPessoaRequest cadastroPessoaRequest) {


            CadastrarPessoaCommand command = new PessoaRequestMapper().toCommand(cadastroPessoaRequest);



        }



    }

