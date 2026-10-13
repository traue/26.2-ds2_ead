package br.mack.singleton;

public final class Presidente {

    private static final Presidente INSTANCE = new Presidente("Alice", 50000.0);

    private String nome;
    private double salario;

    // Construtor privado com os dados iniciais
    private Presidente(String nome, double salario) {
        this.nome   = nome;
        this.salario = salario;
    }

    public static Presidente getInstance() {
        return INSTANCE;
    }

    public String getNome()    { return nome; }
    public double getSalario() { return salario; }

    public void set(String novoNome, double novoSalario) {
        this.nome    = novoNome;
        this.salario = novoSalario;
    }
}
