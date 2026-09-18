export class Documento {
    constructor(tipo, valor, demaisDados) {
        this.tipo = tipo;
        this.valor = valor;
        this.demaisDados = demaisDados;
    }

    estaPreenchido() {
        return Boolean(this.tipo || this.valor || this.demaisDados);
    }
}
