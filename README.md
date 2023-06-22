<h1 align="center">Testes de API com Rest Assured</h1>

- RestAssured é uma biblioteca para testar API RESTful.
  
- Ele utiliza uma biblioteca chamada Hamcrest para fazer as assertivas.
  
- Você pode utilizar a linguagem do BDD para deixar as coisas mais legais.

### Documentação
- [Documentação Oficial do RestAssured](https://github.com/rest-assured/rest-assured/wiki/Usage)

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

- @Test -> Utilizado para informar "ei, a função aí de baixo é um teste, beleza?".
- @DisplayName -> Quando teste é executado, no terminal normalmente mostra o nome do teste. Com o @DisplayName, será mostrado seja lá o que tu por aí dentro, deixando mais fácil de ler seus testes.
- public void TestandoTeste() -> Maneira de declarar nossa função de teste. O void significa que a função não terá retorno.
- given() when() then() -> BDDzim, leia como "Dado, Quando, Então", ou seja, Dado que tu tenha tal coisa, Quando você fizer isso, Então isso deverá acontecer.
- given().contentType(ContentType.JSON) -> Dado que tu tenha um conteúdo JSON;
- .when().get("http://localhost:8080/") -> Quando você utilizar o método GET para acessar essa url;
- .then().statusCode(200) -> Então o servidor deve retornar o Status Code 200 (OK).

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

- private -> Apenas a própria classe terá acesso a essa variável.
- static final String -> Utilizado para criar uma constante, ou seja, algo que não será mudado. Por padrão, a gente deixa o nome da variável totalmente maiúscula quando ela for uma constante.

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

- pegaTodosClientesSemClientesCadastrados() -> Vamos utilizar o método GET na url base quando não houver clientes cadastrados.
- pegaTodosClientesCadastrados() -> Se você olhar no swagger, vai ver que existem duas maneiras de pegar todos os clientes. Esse teste utilizará o endpoint /clientes para pegar todos os clientes, tendo algum já cadastrado (para diferenciar mais do teste anterior).
- cadastraCliente() -> Vamos utilizar o método POST no endpoint /cliente para cadastar um cliente no servidor.
- pegaClienteCadastrado() -> Vamos utilizar o método GET no endpoint /cliente/{id} para pegar apenas um cliente específico.
- pegaRiscoCliente() -> Vamos utilizar o método GET no endpoint /risco/{id} para ver o risco do cliente cadastrado.
- pegaRiscoClienteSemAutorizacao() -> Vamos utilizar o método GET no endpoint /risco/{id} sem enviar as credenciais necessárias para acessá-lo.
- atualizaCliente() -> Vamos utilizar o método PUT no endpoint /cliente/{id} para atualizar o cliente que foi criado.
- deletaCliente() -> Vamos utilizar o método DELETE no endpoint /cliente/{id} para deletar o cliente que foi criado.
