package aulas.umc.oo.command;


import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.UseCase.CadastrarPessoaUseCase;
import model.valueObjects.Email;
import model.valueObjects.IdadePessoa;
import model.valueObjects.NomePessoa;

import java.io.IOException;
import java.util.UUID;

public class CadastrarPessoaCommand {

    public UUID id;
    public NomePessoa nomePessoa;
    public IdadePessoa idadePessoa;
    public Email emailPessoa;
    public String tipoSanguineo;

    public NomePessoa getNomePessoa() {
        return nomePessoa;
    }

    public Email getEmailPessoa() {
        return emailPessoa;
    }

    public IdadePessoa getIdadePessoa() {
        return idadePessoa;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public CadastroPessoaResponse CadastrarPessoaCommand(String nomePessoa, Integer idade, String email, String tipoSanguineo) throws IOException {

        CadastrarPessoaUseCase cadastrarPessoaUseCase = new CadastrarPessoaUseCase();
        this.nomePessoa = new NomePessoa(nomePessoa);
        this.idadePessoa = new IdadePessoa(idade != null ? idade : 18);
        this.emailPessoa = new Email(email);
        this.tipoSanguineo = tipoSanguineo;

        cadastrarPessoaUseCase.executar(this);

        CadastroPessoaResponse cadastroPessoaResponse = new CadastroPessoaResponse();
        cadastroPessoaResponse.setId(this.id);
        cadastroPessoaResponse.setNome(nomePessoa);
        cadastroPessoaResponse.setIdade(this.idadePessoa.getValor());
        cadastroPessoaResponse.setEmail(email);
        cadastroPessoaResponse.setTipoSanguineo(tipoSanguineo);

        return cadastroPessoaResponse;


    }
}
