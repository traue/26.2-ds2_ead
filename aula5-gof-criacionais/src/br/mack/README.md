## Padrões GoF Criacionais: Singleton, Factory Method e Abstract Factory

---

## Parte 1: onde estamos no curso?

Nas aulas anteriores vimos os **padrões GRASP** — um conjunto de nove diretrizes para atribuição de responsabilidades no projeto orientado a objetos: Information Expert, Creator, Controller, Low Coupling, High Cohesion, Polymorphism, Pure Fabrication, Indirection e Protected Variations.

Hoje começamos um catálogo diferente: os **padrões GoF** (Gang of Four).

O nome vem do livro *Design Patterns: Elements of Reusable Object-Oriented Software*, publicado em 1994 por Erich Gamma, Richard Helm, Ralph Johnson e John Vlissides. É uma referência central da Engenharia de Software — e o apelido "turma dos quatro" ficou.

O catálogo traz **23 padrões**, divididos em três grupos:

| Grupo | O que resolve |
|---|---|
| **Criacionais** | Situações envolvendo criacao de objetos |
| **Estruturais** | Reorganizacao de estruturas no diagrama de classes |
| **Comportamentais** | Modelagem de comportamentos no projeto de software |

Hoje vamos focar nos **criacionais**. São cinco no total: Singleton, Factory Method, Abstract Factory, Builder e Prototype. Veremos os três primeiros.

> **Nota sobre nomenclatura:** assim como fizemos com GRASP, manteremos os nomes originais em inglês. Parte do valor dos padrões é criar um vocabulário comum entre desenvolvedores — e esse vocabulário é em inglês.

---

## Parte 2 — Singleton

### O problema

Em algumas situações precisamos garantir que uma classe tenha **uma, e apenas uma, instância** durante toda a execução do programa. Exemplos: conexão com banco de dados, gerenciador de configurações, logger de sistema.

Se qualquer trecho do código puder fazer `new MinhaClasse()`, não há como controlar quantas instâncias existem.

### A solução

Implementar a classe como um **singleton**: a instanciação é feita de forma restrita e controlada, de modo que sempre exista apenas uma instância.

Isso se consegue com:
- construtor `private` — impede que outras classes usem `new`;
- atributo estático que guarda a instância;
- método estático público `getInstance()` que devolve sempre a mesma instância.

### Estrutura (diagrama UML simplificado)

```
+-------------------------+
|       Singleton         |
+-------------------------+
| - instance: Singleton   |
+-------------------------+
| - Singleton()           |
| + getInstance():Singleton|
+-------------------------+
```

### Código — implementação básica em Java

```java
// A classe é final para evitar que seja herdada e a restrição seja burlada
public final class Singleton {

    // O atributo é privado, estático e final.
    // É inicializado uma única vez quando a classe é carregada pela JVM.
    private static final Singleton INSTANCE = new Singleton();

    // Construtor privado: ninguém de fora pode chamar "new Singleton()"
    private Singleton() {}

    // Único ponto de acesso à instância
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

```java
// Exemplo de uso — em qualquer lugar do sistema
Singleton s1 = Singleton.getInstance();
Singleton s2 = Singleton.getInstance();

// s1 e s2 apontam para o mesmo objeto na memória
System.out.println(s1 == s2); // true
```

### Exemplo do mundo real — classe Presidente

```java
// Antes do padrão: qualquer código poderia criar dois presidentes
Presidente p1 = new Presidente("Alice", 50000.0);
Presidente p2 = new Presidente("Bob",   60000.0); // problema: dois presidentes!

// Com Singleton:
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
```

```java
// Uso
Presidente p = Presidente.getInstance();
System.out.println(p.getNome()); // Alice
```

### Pontos de atencao

- No diagrama UML, basta colocar o estereótipo `<<singleton>>` sobre o nome da classe. Não é preciso detalhar o atributo INSTANCE nem o construtor privado — por convenção todos sabem que eles existem.
- O código acima é a forma mais simples (eager initialization). Ela **não é thread-safe** para o caso de inicialização tardia. Em sistemas concorrentes, existem variações mais robustas (double-checked locking, holder idiom).
- Singleton é considerado por muitos um **antipattern** quando usado de forma indiscriminada — ele introduz estado global e dificulta testes automatizados. Use com critério.

---

## Parte 3 — Factory Method

### O problema

Como criar objetos sem precisar especificar, no código cliente, a classe concreta à qual eles pertencem?

Imagine um sistema que hoje cria objetos `SucoMaca`. Se amanhã precisarmos também de `SucoLaranja`, teremos que alterar todos os pontos do código onde fazemos `new SucoMaca()`. Isso viola o princípio Open/Closed (aberto para extensão, fechado para modificação).

### A solução

Criamos uma **classe de fábrica** com um método de fabricação (`factoryMethod()`). Cada tipo de objeto terá sua própria fábrica, responsável unicamente por criar aquele tipo.

O código cliente fala com a fábrica — não precisa conhecer a classe concreta do produto.

### Estrutura (papéis no padrão)

```
Creator (abstrato)              Product (abstrato)
- factoryMethod() : Product     |
        ^                       ^
        |                       |
Creator1                      Product1
- factoryMethod() : Product   (criado por Creator1)
```

### Código — estrutura genérica em Java

```java
// --- Hierarquia de produtos ---

// Produto abstrato: define o tipo que o código cliente conhece
abstract class Product {
    // métodos comuns a todos os produtos podem vir aqui
    public abstract void descricao();
}

// Produto concreto 1
class Product1 extends Product {
    @Override
    public void descricao() {
        System.out.println("Sou o Product1");
    }
}

// Produto concreto 2
class Product2 extends Product {
    @Override
    public void descricao() {
        System.out.println("Sou o Product2");
    }
}

// --- Hierarquia de criadores (fábricas) ---

// Creator abstrato: declara o método de fábrica
abstract class Creator {
    // O método retorna Product (tipo abstrato), não a classe concreta
    protected abstract Product factoryMethod();
}

// Creator concreto para Product1
class Creator1 extends Creator {
    @Override
    protected Product factoryMethod() {
        return new Product1(); // única linha que "sabe" qual classe concreta criar
    }
}

// Creator concreto para Product2
class Creator2 extends Creator {
    @Override
    protected Product factoryMethod() {
        return new Product2();
    }
}
```

```java
// Exemplo de uso — o cliente só conhece Creator e Product (tipos abstratos)
Creator c = new Creator1();      // poderia ser Creator2 sem mudar o resto
Product p = c.factoryMethod();   // devolve um Product1, mas o cliente não precisa saber
p.descricao();                   // "Sou o Product1"
```

### Exemplo do mundo real — fábrica de sucos

```java
// Produto abstrato
abstract class Suco {
    public abstract String getSabor();
}

// Produtos concretos
class SucoMaca extends Suco {
    @Override
    public String getSabor() { return "Maca"; }
}

class SucoLaranja extends Suco {
    @Override
    public String getSabor() { return "Laranja"; }
}

// Creator abstrato
abstract class FabricaDeSuco {
    public abstract Suco fabricaSuco();
}

// Fábricas concretas
class FabricaDeSucoMaca extends FabricaDeSuco {
    @Override
    public Suco fabricaSuco() {
        return new SucoMaca();
    }
}

class FabricaDeSucoLaranja extends FabricaDeSuco {
    @Override
    public Suco fabricaSuco() {
        return new SucoLaranja();
    }
}
```

```java
// Uso
FabricaDeSuco fabrica = new FabricaDeSucoMaca();
Suco s = fabrica.fabricaSuco();
System.out.println("Sabor: " + s.getSabor()); // Sabor: Maca

// Para adicionar suco de uva: criamos FabricaDeSucoUva extends FabricaDeSuco
// e SucoUva extends Suco. Nenhuma linha do código existente precisa mudar.
```

### Pontos de atencao

- `Product` e `Creator` são abstratos — podem ser `abstract class` ou `interface` em Java.
- Cada novo tipo de produto exige uma nova fábrica. Se o sistema tiver muitos tipos, pode ocorrer uma "explosão de classes". Nesse caso, o próximo padrão (Abstract Factory) pode ser mais adequado.
- As próprias fábricas costumam ser implementadas como **Singleton**, já que não precisam de múltiplas instâncias.

---

## Parte 4 — Abstract Factory

### O problema

Como criar **famílias** de objetos relacionados sem especificar as classes concretas?

O Factory Method cria um único tipo de produto por fábrica. Quando os produtos vêm em grupos que precisam ser consistentes entre si (ex.: produtos de uma mesma "linha"), precisamos de algo mais.

### A solução

Criamos uma **fábrica abstrata** com um método de fabricação para cada tipo de produto da família. Cada fábrica concreta produz uma versão coerente da família inteira.

### Estrutura

```
AbstractFactory (abstrato)          ProductA (abstrato)   ProductB (abstrato)
- createProductA() : ProductA            ^                     ^
- createProductB() : ProductB            |                     |
        ^                            ProductA1             ProductB1
        |
    Factory1
    - createProductA() -> ProductA1
    - createProductB() -> ProductB1
```

### Código — estrutura genérica em Java

```java
// --- Interfaces dos produtos ---

// Família tem dois tipos de produto: ProductA e ProductB
interface ProductA {
    void exibir();
}

interface ProductB {
    void exibir();
}

// --- Implementações concretas dos produtos ---

class ProductA1 implements ProductA {
    @Override
    public void exibir() { System.out.println("ProductA — versao 1"); }
}

class ProductB1 implements ProductB {
    @Override
    public void exibir() { System.out.println("ProductB — versao 1"); }
}

class ProductA2 implements ProductA {
    @Override
    public void exibir() { System.out.println("ProductA — versao 2"); }
}

class ProductB2 implements ProductB {
    @Override
    public void exibir() { System.out.println("ProductB — versao 2"); }
}

// --- Fábrica abstrata ---

abstract class AbstractFactory {
    public abstract ProductA createProductA();
    public abstract ProductB createProductB();
}

// --- Fábricas concretas: cada uma produz uma "família" coerente ---

class Factory1 extends AbstractFactory {
    @Override
    public ProductA createProductA() { return new ProductA1(); }

    @Override
    public ProductB createProductB() { return new ProductB1(); }
}

class Factory2 extends AbstractFactory {
    @Override
    public ProductA createProductA() { return new ProductA2(); }

    @Override
    public ProductB createProductB() { return new ProductB2(); }
}
```

```java
// Uso — o cliente trabalha só com AbstractFactory, ProductA e ProductB
AbstractFactory fabrica = new Factory1(); // troque por Factory2 sem mudar o resto

ProductA pa = fabrica.createProductA();
ProductB pb = fabrica.createProductB();

pa.exibir(); // "ProductA — versao 1"
pb.exibir(); // "ProductB — versao 1"
```

### Exemplo do mundo real — restaurantes

```java
// Família: todo restaurante produz um FastFood e uma Sobremesa

interface FastFood {
    String getNome();
}

interface Sobremesa {
    String getNome();
}

// Produtos do restaurante árabe
class Kibe implements FastFood {
    @Override public String getNome() { return "Kibe"; }
}

class Ataif implements Sobremesa {
    @Override public String getNome() { return "Ataif"; }
}

// Produtos do restaurante italiano
class Pizza implements FastFood {
    @Override public String getNome() { return "Pizza"; }
}

class Cannoli implements Sobremesa {
    @Override public String getNome() { return "Cannoli"; }
}

// Fábrica abstrata (pode ser abstract class ou interface)
abstract class Restaurante {
    public abstract FastFood criaFastFood();
    public abstract Sobremesa criaSobremesa();
}

// Restaurante árabe cria sua família
class RestauranteArabe extends Restaurante {
    @Override
    public FastFood criaFastFood()   { return new Kibe(); }

    @Override
    public Sobremesa criaSobremesa() { return new Ataif(); }
}

// Restaurante italiano cria sua família
class RestauranteItaliano extends Restaurante {
    @Override
    public FastFood criaFastFood()   { return new Pizza(); }

    @Override
    public Sobremesa criaSobremesa() { return new Cannoli(); }
}
```

```java
// Uso
Restaurante r = new RestauranteArabe(); // troque por RestauranteItaliano à vontade

FastFood   ff = r.criaFastFood();
Sobremesa  sb = r.criaSobremesa();

System.out.println(ff.getNome() + " + " + sb.getNome()); // "Kibe + Ataif"

// Para adicionar restaurante japonês: cria RestauranteJapones extends Restaurante,
// cria Temaki implements FastFood e Manju implements Sobremesa.
// O código cliente acima não precisa mudar nada.
```

### Pontos de atencao

- A definição da família (quais tipos de produto cada fábrica deve criar) é central e não deve mudar ao longo do projeto — se mudar, todas as fábricas terão que ser alteradas.
- As fábricas concretas devem implementar **todos** os métodos previstos na fábrica abstrata. A herança não é seletiva.
- As fábricas concretas também costumam ser implementadas como **Singleton**.

---

## Parte 5 — Comparacao entre os tres padroes

| Padrão | Cria... | Como? | Exemplo |
|---|---|---|---|
| **Singleton** | Uma única instância de uma classe | Construtor privado + `getInstance()` | Logger, conexão com banco |
| **Factory Method** | Um objeto de um tipo, sem especificar a classe concreta | Uma fábrica por tipo de produto | Fábrica de sucos |
| **Abstract Factory** | Uma família de objetos relacionados | Uma fábrica abstrata com um método por tipo | Restaurante com fastfood + sobremesa |

Os três padrões se complementam: fábricas criadas com Factory Method ou Abstract Factory costumam ser implementadas como Singleton.

---

## Parte 6 — Para ir mais fundo (referencia rapida)

- Singleton: https://refactoring.guru/design-patterns/singleton
- Factory Method: https://refactoring.guru/design-patterns/factory-method
- Abstract Factory: https://refactoring.guru/design-patterns/abstract-factory