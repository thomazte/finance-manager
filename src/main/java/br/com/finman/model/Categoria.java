package br.com.finman.model;

public class Categoria {
    private int id;
    private String nome;
    private String tipo; // RECEITA ou DESPESA
    private int usuarioId;

    public Categoria() {}

    public Categoria(int id, String nome, String tipo, int usuarioId) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", usuarioId=" + usuarioId +
                '}';
    }
}