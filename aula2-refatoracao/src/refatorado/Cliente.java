package refatorado;

/*
 * Resultado do PASSO 2 (Extract Class) e do PASSO 4 (Introduce Null Object).
 *
 * Compare com ClienteLegado:
 *   - 11 atributos primitivos  ->  2 atributos primitivos + 2 objetos com significado
 *   - campos abertos ao pacote ->  private + getters (Encapsulate Field)
 *   - convênio podendo ser null->  convênio NUNCA é null
 *
 * A classe agora tem uma responsabilidade só: representar o cliente e delegar aos
 * colaboradores certos aquilo que não é assunto dela (endereço e convênio).
 */
public class Cliente {

    private final String nome;
    private final String cpf;
    private final Endereco endereco;
    private final Convenio convenio; // nunca null - no máximo, um ConvenioNulo

    /** Construtor para o cliente COM convênio. */
    public Cliente(String nome, String cpf, Endereco endereco, Convenio convenio) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        // Cinto de segurança: mesmo que alguém passe null, o objeto nulo entra no lugar.
        this.convenio = (convenio == null) ? new ConvenioNulo() : convenio;
    }

    /**
     * Construtor para o cliente SEM convênio.
     * Repare que o "sem convênio" agora é explícito na assinatura do construtor,
     * em vez de ser um null que o leitor precisa adivinhar.
     */
    public Cliente(String nome, String cpf, Endereco endereco) {
        this(nome, cpf, endereco, new ConvenioNulo());
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Convenio getConvenio() {
        return convenio;
    }
}
