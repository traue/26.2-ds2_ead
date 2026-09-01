package refatorado;

/*
 * =====================================================================================
 *  PASSO 2 - EXTRACT CLASS  (categoria: Moving Features between Objects)
 * =====================================================================================
 *
 *  PROBLEMA (bad smell): Large Class + Data Clump.
 *      Na classe ClienteLegado, sete campos (logradouro, num, compl, bairro, cidade,
 *      estado, cep) andavam sempre juntos. Sempre que um grupo de dados anda junto,
 *      ele está pedindo para virar uma classe.
 *
 *  SOLUÇÃO:
 *      Criar uma classe nova e mover para ela os campos e os métodos responsáveis por
 *      aquela responsabilidade.
 *
 *  GANHO:
 *      - Coesão: Cliente volta a falar só de cliente.
 *      - Reuso: Fornecedor, Filial e Entregador podem usar Endereco sem copiar nada.
 *      - O endereço passa a saber se formatar sozinho (Tell, Don't Ask).
 */
public class Endereco {

    private final String logradouro;
    private final int numero;
    private final String complemento;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String cep;

    public Endereco(String logradouro, int numero, String complemento,
                    String bairro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    /**
     * O conhecimento de COMO um endereço é escrito mora aqui, e não na nota fiscal.
     * Se amanhã a farmácia precisar imprimir endereço em uma etiqueta de entrega,
     * a formatação já está pronta e é a mesma.
     */
    public String formatado() {
        return logradouro + ", " + numero + " - " + complemento + "\n"
             + "Bairro: " + bairro + "\n"
             + cidade + "/" + estado + " - " + cep;
    }
}
