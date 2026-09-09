package model.valueObjects;

public class Email {
    private final String valor;

    public Email(String valor) {

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }

        this.valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}
