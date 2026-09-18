package aulas.umc.oo.DTO;

import java.util.UUID;

public class CadastroPessoaResponse {

    private UUID id;
    private String nome;
    private Integer idade;
    private String email;
    private String tipoSanguineo;

    public String getNome()
    {
        return  nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }
}
