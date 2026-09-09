package aulas.umc.oo.mapper;


import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.command.CadastrarPessoaCommand;
import model.valueObjects.Email;
import model.valueObjects.NomePessoa;

public class PessoaRequestMapper {

    public CadastrarPessoaCommand toCommand(CadastroPessoaRequest request)
    {

        return new CadastrarPessoaCommand(request.getNome(),request.getEmail());

    }
}
