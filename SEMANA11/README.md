# Testes de integração com Spring Boot — Produto

Projeto Spring Boot 3.3 (Java 17) com Spring Data JPA e banco H2 em memória.
O enunciado original está em [`ENUNCIADO.md`](ENUNCIADO.md).

## Como executar

```bash
mvn test
```

## Testes implementados

### Exercício 1 — Service + Repository + H2 (`ProdutoServiceIntegrationTest`)

| Caso | Método | O que verifica |
|---|---|---|
| CT01 | `deveCadastrarERecuperarProduto` | Produto recebe ID e, após `flush()` + `clear()`, é relido do banco com nome e preço corretos. |
| CT02 | `naoDevePersistirProdutoComNomeEmBranco` | Nome em branco lança `IllegalArgumentException` e `count()` não muda. |
| CT03 | `deveAtualizarPrecoPersistido` | Após `atualizarPreco(id, 95.0)`, a consulta ao repositório retorna `95.0`. |
| extra | `naoDevePersistirProdutoComNomeNulo` | Nome nulo é rejeitado e nada é gravado. |
| extra | `naoDevePersistirProdutoComPrecoZeroOuNegativo` | Preço `0` e `-10` são rejeitados e nada é gravado. |
| extra | `naoDeveAtualizarParaPrecoInvalidoEManterPrecoOriginal` | Atualização com preço inválido falha e o preço no banco continua `80.0`. |
| extra | `deveFalharAoBuscarProdutoInexistente` | `buscar` de ID inexistente lança `IllegalArgumentException`. |

### Exercício 2 — HTTP com MockMvc (`ProdutoControllerIntegrationTest`)

| Caso | Método | O que verifica |
|---|---|---|
| CT04 | `deveCadastrarProdutoPelaApi` | `POST /produtos` → HTTP 200, `id` numérico, `nome` = `Mouse`, `preco` = `80.0`. |
| CT05 | `deveBuscarProdutoPelaApi` | `POST` captura o ID da resposta (via `ObjectMapper`); `GET /produtos/{id}` → HTTP 200 e JSON idêntico ao do cadastro. |

## Isolamento dos testes

- **Sem mocks:** `ProdutoService` e `ProdutoRepository` são os beans reais, injetados com `@Autowired`.
- **Banco H2 em memória** (`jdbc:h2:mem:produtos`), recriado a cada execução (`ddl-auto=create-drop`).
- **`@Transactional` na classe de teste:** o Spring abre uma transação antes de cada método e faz **rollback** ao final.
  Nada é efetivamente gravado, então um teste não enxerga os dados do outro e a ordem de execução não importa.
- O `MockMvc` roda na mesma thread do teste, por isso participa da mesma transação e também é desfeito pelo rollback.

## Discussão

**Por que `repository.findById()` dá uma evidência diferente de olhar só o objeto retornado pelo service?**
O objeto devolvido por `cadastrar()` é a instância que o próprio Hibernate já mantém em memória
(cache de 1º nível). Conferir esse objeto só prova que o código de aplicação o preencheu, não que o banco o recebeu.
`findById()` consulta o repositório; mas, dentro da mesma transação, ele devolveria essa mesma instância
sem ir ao banco. Por isso os testes chamam `flush()` (envia o SQL ao H2) e `clear()` (descarta o cache):
só então o `findById()` executa um `SELECT` de verdade, e o `assertNotSame` confirma que o dado veio do banco.

**O que o `@Transactional` faz ao término de cada teste?**
Por padrão, o Spring TestContext executa **rollback** da transação do teste. Os dados inseridos/alterados
são descartados, mantendo cada teste independente. Efeito colateral: como o commit nunca acontece,
o `flush()` explícito é necessário para forçar a execução do SQL durante o teste.

**Quais componentes são exercitados no Exercício 2?**
Roteamento do Spring MVC (`DispatcherServlet` e mapeamento de URL), conversão JSON ⇄ objeto (Jackson),
`ProdutoController`, `ProdutoService`, `ProdutoRepository`, JPA/Hibernate e o H2.
Ficam de fora apenas a camada de rede e o servidor HTTP (Tomcat).

**Por que o `MockMvc` não precisa de um servidor HTTP real?**
Ele monta objetos de requisição/resposta simulados e os entrega diretamente ao `DispatcherServlet`,
dentro da JVM do teste, sem abrir porta nem socket.

**Qual a diferença para um teste unitário do controller com dependências simuladas?**
No teste unitário com mocks, só a lógica do controller é verificada: o service é falso e nada é persistido.
No teste de integração, o fluxo real Controller → Service → Repository → H2 é percorrido; assim,
falhas de configuração, serialização JSON, mapeamento JPA ou validações no service aparecem, e o mock as esconderia.

## Evidência da execução

> Preencher após rodar `mvn test` na sua máquina: cole aqui o trecho final da saída
> (linhas `Tests run: ..., Failures: 0, Errors: 0`) e `BUILD SUCCESS`.

```text
(cole a saída do mvn test aqui)
```
