export class CadastroPessoaResponse {
    constructor(data) {
        this.id = data.id;
        this.nome = data.nome;
        this.idade = data.idade;
        this.email = data.email;
        this.tipoSanguineo = data.tipoSanguineo;
    }
}
