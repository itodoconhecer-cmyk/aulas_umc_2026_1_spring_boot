package aulas.umc.oo.command;


import aulas.umc.oo.UseCase.CadastrarPessoaUseCase;
import model.valueObjects.Email;
import model.valueObjects.NomePessoa;

public class CadastrarPessoaCommand {

    private final NomePessoa nomePessoa;
    private final Email emailPessoa;

    public NomePessoa getNomePessoa() {
        return nomePessoa;
    }

    public Email getEmailPessoa() {
        return emailPessoa;
    }

    public CadastrarPessoaCommand(String nomePessoa, String email) {

        CadastrarPessoaUseCase cadastrarPessoaUseCase = new CadastrarPessoaUseCase();
        this.nomePessoa = new NomePessoa(nomePessoa);
        this.emailPessoa = new Email(email);

        cadastrarPessoaUseCase.executar(this);

    }
}
