package aulas.umc.oo.DTO;

import java.util.ArrayList;
import java.util.List;

public class CadastroPessoaCompletoRequest extends CadastroPessoaRequest {
    private List<EnderecoRequest> enderecos = new ArrayList<>();
    private List<DocumentoRequest> documentos = new ArrayList<>();

    public List<EnderecoRequest> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoRequest> enderecos) {
        this.enderecos = enderecos;
    }

    public List<DocumentoRequest> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoRequest> documentos) {
        this.documentos = documentos;
    }
}
