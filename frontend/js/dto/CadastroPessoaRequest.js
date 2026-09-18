export class CadastroPessoaRequest {
    constructor(pessoa) {
        this.nome = pessoa.nome;
        this.idade = pessoa.idade;
        this.email = pessoa.email;
        this.tipoSanguineo = pessoa.tipoSanguineo;
    }
}
