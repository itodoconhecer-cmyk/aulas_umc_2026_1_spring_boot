package aulas.umc.oo.DTO;

public class DocumentoRequest {
    private String tipo;
    private String valor;
    private String demaisDados;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDemaisDados() {
        return demaisDados;
    }

    public void setDemaisDados(String demaisDados) {
        this.demaisDados = demaisDados;
    }
}
