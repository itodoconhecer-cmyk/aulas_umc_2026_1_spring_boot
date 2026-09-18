package aulas.umc.oo.mapper;


import aulas.umc.oo.DTO.CadastroPessoaRequest;
import aulas.umc.oo.DTO.CadastroPessoaResponse;
import aulas.umc.oo.command.CadastrarPessoaCommand;


import java.io.IOException;

public class PessoaRequestMapper {

    public CadastroPessoaResponse toCommand(CadastroPessoaRequest request) throws IOException {

        CadastrarPessoaCommand cadastrarPessoaCommand = new CadastrarPessoaCommand();
        CadastroPessoaResponse cadastroPessoaResponse = cadastrarPessoaCommand.CadastrarPessoaCommand(
                request.getNome(),
                request.getIdade(),
                request.getEmail(),
                request.getTipoSanguineo());
        return cadastroPessoaResponse;

    }
}
