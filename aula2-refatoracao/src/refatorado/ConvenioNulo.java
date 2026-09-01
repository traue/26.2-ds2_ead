package refatorado;

/*
 * =====================================================================================
 *  PASSO 4 - INTRODUCE NULL OBJECT (parte 2 de 2)
 *            (categoria: Simplifying Conditional Expressions)
 * =====================================================================================
 *
 *  PROBLEMA (bad smell): verificações de null espalhadas.
 *      No legado, o mesmo "if (cliente.convenioNome != null)" aparecia duas vezes no
 *      mesmo método - e apareceria de novo em todo lugar novo que precisasse do desconto.
 *      Esquecer um desses ifs = NullPointerException em produção.
 *
 *  SOLUÇÃO:
 *      Criar uma subclasse que representa "a ausência de convênio" e que responde às
 *      mesmas mensagens, mas com o comportamento neutro/padrão. O null desaparece do
 *      sistema: nunca existe um Cliente sem convênio, existe um Cliente com ConvenioNulo.
 *
 *  GANHO:
 *      - As condicionais somem do código que USA o convênio.
 *      - O comportamento padrão fica declarado em UM lugar só, com nome próprio.
 *      - Chamadas ficam uniformes: cliente.getConvenio().getPercentualDesconto()
 *        funciona sempre, com ou sem convênio.
 *
 *  ATENÇÃO (todo padrão tem trade-off):
 *      Null Object é ótimo quando "ausente" tem um comportamento neutro bem definido.
 *      Se a ausência do dado for um ERRO que precisa ser tratado, esconder isso atrás de
 *      um objeto neutro pode mascarar o problema. Nesse caso, Optional ou uma exceção
 *      são escolhas melhores.
 */
public class ConvenioNulo extends Convenio {

    public ConvenioNulo() {
        // Nome vazio e desconto zero: o comportamento neutro do conceito "sem convênio".
        super("", 0.0);
    }

    /**
     * Aqui está o pulo do gato: multiplicar por 0.0 é matematicamente inofensivo,
     * então o cálculo do total funciona sem nenhum "if".
     */
    @Override
    public double getPercentualDesconto() {
        return 0.0;
    }

    /** E a nota fiscal simplesmente não imprime a linha de convênio - sem testar null. */
    @Override
    public boolean deveAparecerNaNota() {
        return false;
    }
}
