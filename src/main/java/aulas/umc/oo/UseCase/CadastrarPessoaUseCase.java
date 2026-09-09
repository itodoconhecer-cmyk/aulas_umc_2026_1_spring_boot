package aulas.umc.oo.UseCase;

import aulas.umc.oo.command.CadastrarPessoaCommand;
import aulas.umc.oo.model.Pessoa;



public class CadastrarPessoaUseCase {

    public Pessoa executar(CadastrarPessoaCommand command) {

        Pessoa pessoa = new Pessoa(command.getNomePessoa(), command.getEmailPessoa());

        return pessoa;
    }
}
