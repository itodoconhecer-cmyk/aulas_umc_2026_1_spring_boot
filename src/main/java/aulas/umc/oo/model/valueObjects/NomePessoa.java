package model.valueObjects;

public class NomePessoa {

    private final String valor;

    public NomePessoa(String valor) {

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }

        this.valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}
