import { CadastroPessoaRequest } from './CadastroPessoaRequest.js';

export class CadastroPessoaCompletoRequest extends CadastroPessoaRequest {
    constructor(pessoa, enderecos, documentos) {
        super(pessoa);
        this.enderecos = enderecos;
        this.documentos = documentos;
    }
}
