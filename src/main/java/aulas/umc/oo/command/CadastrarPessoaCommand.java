package aulas.umc.oo.command;


import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.UseCase.CadastrarPessoaUseCase;
import model.valueObjects.Email;
import model.valueObjects.NomePessoa;

import java.util.UUID;

public class CadastrarPessoaCommand {

    public UUID id;
    public NomePessoa nomePessoa;
    public Email emailPessoa;

    public NomePessoa getNomePessoa() {
        return nomePessoa;
    }

    public Email getEmailPessoa() {
        return emailPessoa;
    }


    public CadastroPessoaResponse CadastrarPessoaCommand(String nomePessoa, String email) {

        CadastrarPessoaUseCase cadastrarPessoaUseCase = new CadastrarPessoaUseCase();
        this.nomePessoa = new NomePessoa(nomePessoa);
        this.emailPessoa = new Email(email);

        cadastrarPessoaUseCase.executar(this);

        CadastroPessoaResponse cadastroPessoaResponse = new CadastroPessoaResponse();
        cadastroPessoaResponse.setId(this.id);
        cadastroPessoaResponse.setNome(nomePessoa);
        cadastroPessoaResponse.setEmail(email);

        return cadastroPessoaResponse;


    }
}
