import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.*;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


public class TestaCliente {

    private static final String MAIN_ADDRESS = "http://localhost:8080/";
    private static final String CLIENTE = MAIN_ADDRESS + "cliente/";
    private static final String APAGA_CLIENTES = CLIENTE + "apagaTodos";
    private static final String RISCO = MAIN_ADDRESS + "risco/";

    private static final String LISTA_VAZIA = "{}";

    Cliente clienteParaCadastro = new Cliente("Janio", 35, 1001);

    private ValidatableResponse criarCliente() {
        return  given()
                    .contentType(ContentType.JSON)
                    .body(clienteParaCadastro)
                .when()
                    .post(CLIENTE)
                .then();
    }

    private ValidatableResponse deletaTodosClientes() {
        return  when()
                    .delete(APAGA_CLIENTES)
                .then()
                    .statusCode(200)
                    .assertThat().body(new IsEqual<>(LISTA_VAZIA));
    }

    private ValidatableResponse pegaTodosClientes() {
        return  given()
                    .contentType(ContentType.JSON)
                .when()
                    .get(MAIN_ADDRESS)
                    .then()
                .statusCode(200);
    }

    @BeforeEach
    public void setUp() {
        criarCliente();
    }

    @Test
    @DisplayName("Quando pegar todos os clientes sem cliente cadastrado, então a lista deve estar vazia.")
    public void pegaTodosClientesSemClientesCadastrados () {
        deletaTodosClientes();

        pegaTodosClientes()
                .assertThat().body(new IsEqual<>(LISTA_VAZIA));
    }

    @Test
    @DisplayName("Quando pegar todos os clientes com algum cliente cadastrado, então a resposta não poderá estar vazia.")
    public void pegaTodosClientesCadastrados () {

        pegaTodosClientes()
                .assertThat().body(not(LISTA_VAZIA));
    }

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
                .body("nome", equalTo("Janio"))
                .body("idade", equalTo(35));
    }

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
                .assertThat().body("risco", equalTo(-65));
    }

    @Test
    @DisplayName("Quando solicitar o risco de um cliente sem credenciais válidas, o retorno deverá ser null.")
    public void pegaRiscoClienteSemAutorizacao() {
        when()
                .get(RISCO + 1001)
        .then()
                .statusCode(401)
                .assertThat().body("risco", equalTo(null));
    }

    @Test
    @DisplayName("Quando cadastrar um cliente, então ele deve estar disponível no resultado.")
    public void cadastraCliente() {
        criarCliente()
                .statusCode(201)
                .body("1001.id", equalTo(1001))
                .body("1001.nome", equalTo("Janio"))
                .body("1001.idade", equalTo(35));
    }

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

    @Test
    @DisplayName("Quando deletar um cliente, então os seus dados devem ser apagados.")
    public void deletaCliente() {
        when()
                .delete(CLIENTE + 1001)
        .then()
                .statusCode(200)
                .assertThat().body(not(contains("Janio")));
    }

    @AfterEach
    public void tearDown() {
        deletaTodosClientes();
    }
}