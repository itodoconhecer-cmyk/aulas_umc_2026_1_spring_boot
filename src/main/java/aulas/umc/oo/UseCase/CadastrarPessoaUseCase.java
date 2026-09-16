package aulas.umc.oo.UseCase;

import aulas.umc.oo.command.CadastrarPessoaCommand;
import aulas.umc.oo.model.Pessoa;
import aulas.umc.oo.repository.PessoaJdbcRepository;

import java.io.IOException;


public class CadastrarPessoaUseCase {

    public Pessoa executar(CadastrarPessoaCommand command) throws IOException {

        Pessoa pessoa = new Pessoa(command.getNomePessoa(), command.getEmailPessoa());
        PessoaJdbcRepository pessoaRepository = new PessoaJdbcRepository();
        pessoaRepository.insert(pessoa);
        command.id = pessoa.id;

        return pessoa;
    }
}
