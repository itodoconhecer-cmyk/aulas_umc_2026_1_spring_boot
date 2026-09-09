package aulas.umc.oo.mapper;


import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.command.CadastrarPessoaCommand;
import model.valueObjects.Email;
import model.valueObjects.NomePessoa;

public class PessoaRequestMapper {

    public CadastroPessoaResponse toCommand(CadastroPessoaRequest request)
    {

        CadastrarPessoaCommand cadastrarPessoaCommand = new CadastrarPessoaCommand();
        CadastroPessoaResponse cadastroPessoaResponse = cadastrarPessoaCommand.CadastrarPessoaCommand(request.getNome(),request.getEmail());
        return cadastroPessoaResponse;

    }
}
