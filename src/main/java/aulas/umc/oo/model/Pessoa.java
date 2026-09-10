package aulas.umc.oo.model;

import model.valueObjects.Email;
import model.valueObjects.IdadePessoa;
import model.valueObjects.NomePessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pessoa extends Domain{
    public IdadePessoa idadePessoa;
    public NomePessoa nome;
    public IdadePessoa idade;
    public Email email;
    public String tipoSanguineo;
    private final List<Endereco> enderecos;
    private final List<Documento> documentos;


    public Pessoa(String nome, int idade, String tipoSanguineo, Endereco endereco, Documento documento) {
        if (endereco == null) {
            throw new IllegalArgumentException("Pessoa deve possuir pelo menos um endereco.");
        }

        this.id = UUID.randomUUID();
        this.nome = new NomePessoa(nome);
        this.idade = new  IdadePessoa(idade);
        this.tipoSanguineo = tipoSanguineo;
        this.enderecos = new ArrayList<>();
        this.enderecos.add(endereco);
        this.documentos = new ArrayList<>();
        this.documentos.add(documento);
    }

    public Pessoa (NomePessoa nome,Email email) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.enderecos = new ArrayList<>();
        this.documentos = new ArrayList<>();
    }

    public void adicionarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereco nao pode ser nulo.");
        }
        enderecos.add(endereco);
    }

    public void adicionarDocumento(Documento documento) {
        if (documento == null) {
            throw new IllegalArgumentException("Documento nao pode ser nulo.");
        }
        documentos.add(documento);
    }


    public void exibirNome() {
        System.out.println(nome.getValor() + " ola Mundo!");
    }

    public void exibirIdade() {
        System.out.println("Idade: " + idade);
    }

    public void exibirTipoSanguineo() {
        System.out.println("Tipo Sanguineo: " + tipoSanguineo);
    }

    public void exibirEnderecos() {
        for (Endereco endereco : enderecos) {
            endereco.exibir();
        }
    }
    public void exibirDocumentos() {
        for (Documento documento : documentos) {
            documento.exibir();
        }
    }

    public void fazerAniversario() {
        this.idade.setValor(this.idade.getValor() + 1);
    }
}
