<h1 align="center">Testes de API com Rest Assured</h1>

- RestAssured é uma biblioteca para testar API RESTful.
  
- Ele utiliza uma biblioteca chamada Hamcrest para fazer as assertivas.

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
