package aulas.umc.oo.model;

import aulas.umc.oo.model.valueObjects.ValorDoc;
import model.valueObjects.DemaisDados;
import model.valueObjects.Tipo;


public class Documento extends Domain{
    private Tipo tipo;
    private ValorDoc valor;
    private DemaisDados demaisDados;

    public Documento(Tipo tipo, ValorDoc valor, DemaisDados demaisDados) {
        this.tipo = tipo;
        this.valor = valor;
        this.demaisDados = demaisDados;
    }

    public Documento() {
    }

    public void exibir() {
        System.out.println(this.tipo.getValor() + ", " + this.valor.getValor() + " - " + this.demaisDados.getValor());
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public ValorDoc getValor() {
        return valor;
    }

    public void setValor(ValorDoc valor) {
        this.valor = valor;
    }

    public DemaisDados getDemaisDados() {
        return demaisDados;
    }

    public void setDemaisDados(DemaisDados demaisDados) {
        this.demaisDados = demaisDados;
    }
}
