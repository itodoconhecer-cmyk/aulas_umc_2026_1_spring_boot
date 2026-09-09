package model.valueObjects;

public class IdadePessoa {
    private int valor;
    public IdadePessoa(int valor) {
        if (valor < 18 || valor >= 100) {
            throw new IllegalArgumentException("A idade deve ser entre 1 e 100");
        }
        this.valor = valor;
    }
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        if (valor < 18 || valor >= 100) {
            throw new IllegalArgumentException("A idade deve ser entre 1 e 100");
        }
        this.valor = valor;
    }

}
