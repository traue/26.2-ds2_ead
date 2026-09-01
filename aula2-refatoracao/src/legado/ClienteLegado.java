package legado;

/*
 * [SMELL 2] LARGE CLASS / baixa coesão  ->  Passo 2: EXTRACT CLASS
 *
 * Esta classe acumula responsabilidades de TRÊS conceitos diferentes:
 *
 *   1. a PESSOA          -> nome, cpf
 *   2. o ENDEREÇO        -> logradouro, num, compl, bairro, cidade, estado, cep
 *   3. o CONVÊNIO        -> convenioNome, convenioDesconto
 *
 * Sintomas de que isso é um problema:
 *   - "Data Clump": aqueles 7 campos de endereço andam SEMPRE juntos. Quando um grupo
 *     de campos anda sempre junto, ele quase sempre é uma classe esperando para nascer.
 *   - Baixa reusabilidade: um Fornecedor também tem endereço, mas não pode aproveitar nada.
 *   - Os dois campos de convênio têm uma regra invisível: convenioDesconto só vale se
 *     convenioNome != null. Essa regra não está escrita em lugar nenhum - está na cabeça
 *     de quem escreveu o código.
 *
 * Bônus de bad smell: os atributos são todos package-private (sem private, sem getters),
 * então qualquer classe do pacote pode escrever neles. Encapsulamento zero.
 */
class ClienteLegado {
    String nome;
    String cpf;

    // --- bloco que vai virar a classe Endereco (Passo 2) ---
    String logradouro;
    int num;
    String compl;
    String bairro;
    String cidade;
    String estado;
    String cep;

    // --- bloco que vai virar Convenio / ConvenioNulo (Passo 4) ---
    String convenioNome;      // pode ser null!
    double convenioDesconto;  // só faz sentido se convenioNome != null
}
