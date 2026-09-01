package refatorado;

/*
 * PASSO 4 - INTRODUCE NULL OBJECT (parte 1 de 2)
 *
 * Primeiro damos identidade ao conceito que estava disperso em dois campos soltos do
 * ClienteLegado (convenioNome + convenioDesconto) e cuja regra de consistência
 * ("o desconto só vale se o nome não for null") não estava escrita em lugar nenhum.
 *
 * Agora existe uma classe Convenio. Um convênio sempre tem nome e sempre tem percentual.
 * O estado inconsistente deixou de ser representável.
 */
public class Convenio {

    private final String nome;
    private final double percentualDesconto; // 0.15 = 15%

    public Convenio(String nome, double percentualDesconto) {
        this.nome = nome;
        this.percentualDesconto = percentualDesconto;
    }

    public String getNome() {
        return nome;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    /**
     * Este é o método-chave do padrão Null Object.
     *
     * Em vez de o resto do sistema perguntar "convenio != null?", ele pergunta ao próprio
     * objeto: "você deve aparecer na nota fiscal?". A pergunta sobre o NULO vira uma
     * pergunta sobre o COMPORTAMENTO - e a resposta é polimórfica.
     */
    public boolean deveAparecerNaNota() {
        return true;
    }
}
