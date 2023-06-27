<h1 align="center">Testes de API com REST Assured (Tutorial)</h1>

- REST Assured é uma biblioteca para testar API RESTful.
  
- Ele utiliza uma biblioteca chamada Hamcrest para fazer as assertivas.
  
- Você pode utilizar a linguagem do BDD para deixar as coisas mais legais.

### Documentação
- [Documentação Oficial do REST Assured](https://github.com/rest-assured/rest-assured/wiki/Usage)

### O que você precisa para recriar este projeto:

- Aconselho o uso de uma IDE. A utilizada aqui foi o IntelliJ IDEA Communinity Edition 2023.1.1
- Java JDK (Foi utilizado o 15 nesse projeto)
- RestAssured 5.3.0
- JUnit 5.9.3
- Jackson-databind 2.12.7.1

Além disso, você vai precisar baixar o projeto do [Vinicius Pessoni](https://github.com/vinnypessoni/api-clientes-exemplo-microservico) para criar um servidor local.
 
<h1 align="center">Pronto, agora vejamos passo a passo o que deve ser feito</h1>

Abra o IntelliJ e clique em New Project, coloque um nome e um local para o projeto e utilize as seguintes configurações:

```sh
Language: Java
Build System: Gradle
JDK: 15 (caso não tenha, o IntelliJ permite baixar por ele mesmo)
Gradle DSL: Kotlin
```
Pode deixar o Add sample code marcado, para agilizar o processo. Depois disso aguarde a instalação das coisas e, caso você utilize o Windows Defender, poderá ser necessário adicionar o IntelliJ nas exceções do firewall.

Agora abra o arquivo `build.gradle.kts` e substitua o que tiver em dependencies e tasks.test pelo seguinte:

```sh
dependencies {
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
```

Fez isso? Pronto. Agora próximo passo é ir na pastar src/test e, dentro da pasta java, cria um arquivo(java class) chamado TestaCliente.
NOTA: Caso queira, pode deletar a pasta src/main, já que ela não será utilizada aqui.

<h1 align="center">E agora é a hora de se divertir criando testes</h1>

Antes de qualquer coisa, vamos deixar logo nosso servidor rodando. Abra o terminal e vá na pasta onde o projeto do Pessoni esteja (você verá um arquivo chamado `build.gradle` nele) e então digite o seguinte comando:

```sh
./gradlew bootRun
```

Isso vai iniciar o server utilizando a versão do gradle que tiver no wrapper(por isso o w no gradleW). Caso queira ter certeza que o servidor local estará rodando, é só acessar no seu browser:

```sh
http://localhost:8080/
```

Agora que o servidor local está on, vamos para o arquivo TestaCliente que criamos e vamos fazer os seguintes imports:

```sh
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
```
NOTA: Pela IDE, tu pode só ir codando normalmente e, quando surgir algo que não foi importado, ela vai acusar e mandar tu clicar para importar automaticamente (tipo, se tu por um @Test ele vai importar o api.Test do junit).

Você vai ver que já tem uma classe criada automaticamente, a TestaCliente. Dentro dela, coloque o seguinte código e depois clique no botão de play que aparecerá ao lado para executar o teste:

```sh
    @Test
    @DisplayName("Primeiro teste que testa o teste testando o que deve ser testado")
    public void TestandoTeste() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("http://localhost:8080/")
        .then()
                .statusCode(200);
    }
```

Com muita fé não deve ter dado nenhum erro, porém agora precisamos entender o que foi que aconteceu aí em cima, o que nos leva ao nosso quadro:
### Dissecando o código

- @Test → Utilizado para informar "ei, a função aí de baixo é um teste, beleza?".
- @DisplayName → Quando teste é executado, no terminal normalmente mostra o nome do teste. Com o @DisplayName, será mostrado seja lá o que tu por aí dentro, deixando mais fácil de ler seus testes.
- public void TestandoTeste() → Maneira de declarar nossa função de teste. O void significa que a função não terá retorno.
- given() when() then() → BDDzim, leia como "Dado, Quando, Então", ou seja, Dado que tu tenha tal coisa, Quando você fizer isso, Então isso deverá acontecer.
- given().contentType(ContentType.JSON) → Dado que tu tenha um conteúdo JSON;
- .when().get("http://localhost:8080/") → Quando você utilizar o método GET para acessar essa url;
- .then().statusCode(200) → Então o servidor deve retornar o Status Code 200 (OK).

Pronto. Obviamente isso foi criado apenas para mostrar como iremos criar os testes e dando uma olhada bem por cima em como deve ser escrito nossos testes. Agora você deleta isso daí porque vamos começar a criar EXATAMENTE o que tem nesse projetinho.

<h1 align="center">Passo a Passo para criar nosso projetinho</h1>

### Passo 1: Crie o arquivo Cliente.java e copie o conteúdo dele que está neste repositório

Iremos utilizar uma classe para guardarmos as informações dos clientes criados, evitando repetição de código.

### Passo 2: Crie variáveis para guardar os endereços que serão utilizados nessa API

Fazendo isso, caso mudem um endpoint ou a própria url, você só vai precisar fazer a mudança em um local, no lugar de ir mudando manualmente em todos os testes. Você consegue pegar os endpoints facilmente utilizando o swagger no seguinte endereço (o server deverá estar rodando para poder acessar isso):
```sh
http://localhost:8080/swagger-ui.html#/cliente45controller
```

```sh
    private static final String MAIN_ADDRESS = "http://localhost:8080/";
    private static final String CLIENTE = MAIN_ADDRESS + "cliente/";
    private static final String APAGA_CLIENTES = CLIENTE + "apagaTodos";
    private static final String RISCO = MAIN_ADDRESS + "risco/";
```

### Dissecando o código

- private → Apenas a própria classe terá acesso a essa variável.
- static final String → Utilizado para criar uma constante, ou seja, algo que não será mudado. Por padrão, a gente deixa o nome da variável totalmente maiúscula quando ela for uma constante.

### Passo 3: Vamos criar as funções de testes, ao menos uma para cada método e/ou endpoint da API

Para gerar uma boa cobertura de testes, é legal que tu teste os métodos e seus endpoints para saber se estão funcionando como deveriam. Aqui não iremos procurar por exceções ou maneiras de espancar a coitada da API até achar uma maneira de quebrá-la, mas sim se todos os métodos estão dando os retornos corretos caso enviemos exatamente o que estão pedindo. Abaixo estão os 8 testes que foram criados nesse projeto:

```sh
public void pegaTodosClientesSemClientesCadastrados()
public void pegaTodosClientesCadastrados()
public void pegaClienteCadastrado()
public void cadastraCliente()
public void pegaRiscoCliente()
public void pegaRiscoClienteSemAutorizacao()
public void atualizaCliente()
public void deletaCliente()
```

### Dissecando o código

- pegaTodosClientesSemClientesCadastrados() → Vamos utilizar o método GET na url base quando não houver clientes cadastrados.
- pegaTodosClientesCadastrados() → Se você olhar no swagger, vai ver que existem duas maneiras de pegar todos os clientes. Esse teste utilizará o endpoint /clientes para pegar todos os clientes, tendo algum já cadastrado (para diferenciar mais do teste anterior).
- cadastraCliente() → Vamos utilizar o método POST no endpoint /cliente para cadastar um cliente no servidor.
- pegaClienteCadastrado() → Vamos utilizar o método GET no endpoint /cliente/{id} para pegar apenas um cliente específico.
- pegaRiscoCliente() → Vamos utilizar o método GET no endpoint /risco/{id} para ver o risco do cliente cadastrado.
- pegaRiscoClienteSemAutorizacao() → Vamos utilizar o método GET no endpoint /risco/{id} sem enviar as credenciais necessárias para acessá-lo.
- atualizaCliente() → Vamos utilizar o método PUT no endpoint /cliente/{id} para atualizar o cliente que foi criado.
- deletaCliente() → Vamos utilizar o método DELETE no endpoint /cliente/{id} para deletar o cliente que foi criado.

### Antes do passo 4, vamos pensar um pouquinho

Se você olhar o nome dos testes, você poderá perceber algumas coisas, tipo:
- Existem dois testes que pega todos os clientes, cada um usando um endpoint diferente;
- Em diversos testes você vai precisar criar um cliente;
- Como todos os testes devem ser independentes um do outro, então você vai precisar deletar os clientes criados após cada teste.

Com isso em mente, e visando a não repetição de código, podemos fazer algumas coisas antes de começar a realmente criar os testes.

### Passo 4: Criando as funções que serão utilizadas mais de uma vez nos testes

Iremos criar 3 funções que serão utilizadas pelos testes, sendo que duas delas serão usadas em todos eles. Além das funções, também criaremos mais duas variáveis, uma constante e uma instância de objeto:
```sh
    private static final String LISTA_VAZIA = "{}";
    Cliente clienteParaCadastro = new Cliente("Jeovanio", 43, 1001);
```

A primeira função será a de criar um cliente:
```sh
    private ValidatableResponse criarCliente() {
        return  given()
                    .contentType(ContentType.JSON)
                    .body(clienteParaCadastro)
                .when()
                    .post(CLIENTE)
                .then();
    }
```

A segunda irá deletar todos os clientes que estiverem cadastrados:
```sh
    private ValidatableResponse deletaTodosClientes() {
        return  when()
                    .delete(APAGA_CLIENTES)
                .then()
                    .statusCode(200)
                    .assertThat()
                    .body(new IsEqual<>(LISTA_VAZIA));
    }
```

A terceira irá pegar todos os clientes, e vai receber um parâmetro caso você queira utilizar o endpoint ao invés de apenas a url base:
```sh
    private ValidatableResponse pegaTodosClientes(String endpoint) {
        return  given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(MAIN_ADDRESS + endpoint)
                .then()
                    .statusCode(200);
    }
```

### Dissecando o código

- LISTA_VAZIA = "{}" → Isso é meio que um spoiler, mas você verá que será utilizado mais de uma vez o "{}".
- Cliente clienteParaCadastro = new Cliente("Jeovanio", 43, 1001); → cria uma variável que é uma instância da classe Cliente. Os atributos você poderá colocar o que quiser, sabendo que é (nome, idade, id).
- ValidatableResponse → Importado do REST Assured e necessário para enviar os returns das funções para nossos testes.
- .body(clienteParaCadastro) → Coloca dentro do corpo da requisição a variável declarada anteriormente.
- get/post/put/delete → Métodos http. GET pega algo, POST "posta" algo, PUT atualiza algo (esse aparecerá mais na frente) e DELETE deleta algo.
- .assertThat() → Assertiva. Tente ler como algo tipo "garanta isso:". Logo em seguida vem o que você quer que garanta que seja verdade.
- .body(new IsEqual<>(LISTA_VAZIA)) → Isso ta logo após o assertThat, ou seja, "garanta que: o conteúdo do corpo da requisição IsEqual (é igual) a "{}" (O valor de LISTA_VAZIA que declaramos ali em cima)".

Pronto, agora nós finalmente chegamos no passo mais emocionante do nosso código, que é o:

### Passo 5: Hora de criar nossos testes!!!

Levamos um bom tempo para chegar até aqui, porém daqui para baixo é basicamente apenas códigos e assertivas, começando por duas etapas bem importantes em muitos casos de testes:
```sh
    @BeforeEach
    public void setUp() {
        criarCliente();
    }

    @AfterEach
    public void tearDown() {
        deletaTodosClientes();
    }
```

### Dissecando o código

-  @BeforeEach → Execute esse treco aqui embaixo ANTES de todos os testes.
-  @AfterEach → Execute esse treco aqui embaixo DEPOIS de todos os testes

Para quê precisamos disso? Lembra que comentei que praticamente todos os testes precisariam de um cliente criado, e, como todos os testes precisam ser independentes, logo não pode ter nenhum cliente criado? Então... utilizando o `@BeforeEach` vai garantir que antes de cada teste, um cliente será criado e, com o `@AfterEach`, garante que após cada teste o cliente será apagado.

### pegaTodosClientesSemClientesCadastrados()

Como é necessário pegar uma lista vazia, e nosso setUp ta criando um cliente, primeiro precisamos deletar os clientes cadastrados para depois fazer a consulta. Como estamos utilizando a url base da API, passaremos como endpoint apenas uma string vazia.
```sh
    @Test
    @DisplayName("Quando pegar todos os clientes sem cliente cadastrado, então a lista deve estar vazia.")
    public void pegaTodosClientesSemClientesCadastrados() {
        deletaTodosClientes();

        pegaTodosClientes("")
                .assertThat()
                .body(new IsEqual<>(LISTA_VAZIA));
    }
```

### pegaTodosClientesCadastrados()

Basicamente a mesma situação anterior, porém dessa vez estaremos testando em um endpoint diferente e garantindo que a lista não estará vazia.
```sh
    @Test
    @DisplayName("Quando pegar todos os clientes com algum cliente cadastrado, então a resposta não poderá estar vazia.")
    public void pegaTodosClientesCadastrados() {

        pegaTodosClientes("clientes/")
                .assertThat()
                .body(not(LISTA_VAZIA));
    }
```

### cadastraCliente()

```sh
    @Test
    @DisplayName("Quando cadastrar um cliente, então ele deve estar disponível no resultado.")
    public void cadastraCliente() {
        criarCliente()
                .statusCode(201)
                .body("1001.id", equalTo(1001))
                .body("1001.nome", equalTo("Jeovanio"))
                .body("1001.idade", equalTo(43));
    }
```

### pegaClienteCadastrado()
```sh
    @Test
    @DisplayName("Quando solicitado um cliente específico, deverá ser retornado seus dados cadastrados.")
    public void pegaClienteCadastrado() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(CLIENTE + 1001)
        .then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(1001))
                .body("nome", equalTo("Jeovanio"))
                .body("idade", equalTo(43));
    }
```

### pegaRiscoCliente()
```sh
    @Test
    @DisplayName("Quando solicitar o risco de um cliente com credenciais válidas, o valor correto deve ser retornado.")
    public void pegaRiscoCliente() {
        given()
                .auth()
                .basic("aluno", "senha")
        .when()
                .get(RISCO + 1001)
        .then()
                .statusCode(200)
                .assertThat().body("risco", equalTo(-105));
    }
```

### pegaRiscoClienteSemAutorizacao()
```sh
    @Test
    @DisplayName("Quando solicitar o risco de um cliente sem credenciais válidas, o retorno deverá ser null.")
    public void pegaRiscoClienteSemAutorizacao() {
        when()
                .get(RISCO + 1001)
        .then()
                .statusCode(401)
                .assertThat().body("risco", equalTo(null));
    }
```

### atualizaCliente()
```sh
    @Test
    @DisplayName("Quando atualizar um cliente, então os dados dele devem ser alterados.")
    public void atualizaCliente() {
        clienteParaCadastro.setIdade(38);

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastro)
        .when()
                .put(CLIENTE)
        .then()
                .statusCode(200)
                .body("1001.idade", equalTo(38));
    }
```

### deletaCliente()
```sh
    @Test
    @DisplayName("Quando deletar um cliente, então os seus dados devem ser apagados.")
    public void deletaCliente() {
        when()
                .delete(CLIENTE + 1001)
        .then()
                .statusCode(200)
                .assertThat()
                .body(not(contains("Jeovanio")));
    }
```
