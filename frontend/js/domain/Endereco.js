export class Endereco {
    constructor(rua, numero, cidade) {
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
    }

    estaPreenchido() {
        return Boolean(this.rua || this.numero || this.cidade);
    }
}
