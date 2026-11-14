package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProdutoServiceIntegrationTest {

    @Autowired ProdutoService service;
    @Autowired ProdutoRepository repository;
    @Autowired EntityManager entityManager;

    /**
     * Envia as alterações pendentes ao H2 e limpa o cache de 1º nível (persistence context).
     * Sem o clear(), findById() devolveria a MESMA instância que já está em memória
     * e o teste não provaria que o dado foi realmente lido do banco.
     */
    private void sincronizarComBanco() {
        repository.flush();
        entityManager.clear();
    }

    // CT01 — Cadastro válido
    @Test
    void deveCadastrarERecuperarProduto() {
        Produto salvo = service.cadastrar("Teclado", 150.0);

        assertNotNull(salvo.getId());

        sincronizarComBanco();

        Produto encontrado = repository.findById(salvo.getId()).orElseThrow();
        assertNotSame(salvo, encontrado, "deve ter sido relido do banco, não do cache");
        assertEquals("Teclado", encontrado.getNome());
        assertEquals(150.0, encontrado.getPreco());
    }

    // CT02 — Nome inválido
    @Test
    void naoDevePersistirProdutoComNomeEmBranco() {
        long antes = repository.count();

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar("   ", 80.0));

        assertEquals("Nome obrigatório", erro.getMessage());
        assertEquals(antes, repository.count());
    }

    // CT03 — Atualização (desafio)
    @Test
    void deveAtualizarPrecoPersistido() {
        Produto salvo = service.cadastrar("Mouse", 80.0);

        service.atualizarPreco(salvo.getId(), 95.0);
        sincronizarComBanco();

        Produto encontrado = repository.findById(salvo.getId()).orElseThrow();
        assertEquals("Mouse", encontrado.getNome());
        assertEquals(95.0, encontrado.getPreco());
    }

    // ---- Testes adicionais (regras do service que o enunciado não cobre) ----

    @Test
    void naoDevePersistirProdutoComNomeNulo() {
        long antes = repository.count();

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(null, 80.0));

        assertEquals(antes, repository.count());
    }

    @Test
    void naoDevePersistirProdutoComPrecoZeroOuNegativo() {
        long antes = repository.count();

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Teclado", 0));
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Teclado", -10.0));

        assertEquals(antes, repository.count());
    }

    @Test
    void naoDeveAtualizarParaPrecoInvalidoEManterPrecoOriginal() {
        Produto salvo = service.cadastrar("Mouse", 80.0);
        sincronizarComBanco();

        assertThrows(IllegalArgumentException.class,
                () -> service.atualizarPreco(salvo.getId(), -1.0));
        sincronizarComBanco();

        assertEquals(80.0, repository.findById(salvo.getId()).orElseThrow().getPreco());
    }

    @Test
    void deveFalharAoBuscarProdutoInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscar(999_999L));
    }
}
