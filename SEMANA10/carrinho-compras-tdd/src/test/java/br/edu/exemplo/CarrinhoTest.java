package br.edu.exemplo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarrinhoTest {

    @Test
    void carrinhoNovoDeveTerTotalZero() {
        Carrinho carrinho = new Carrinho();

        assertEquals(0, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveAdicionarUmProduto() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Teclado", 100.00));

        assertEquals(100.00, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveSomarVariosProdutos() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Teclado", 100.00));
        carrinho.adicionar(new Produto("Mouse", 50.00));

        assertEquals(150.00, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveRemoverProdutoDoCarrinho() {
        Carrinho carrinho = new Carrinho();
        Produto teclado = new Produto("Teclado", 100.00);
        Produto mouse = new Produto("Mouse", 50.00);
        carrinho.adicionar(teclado);
        carrinho.adicionar(mouse);

        carrinho.remover(mouse);

        assertEquals(100.00, carrinho.getTotal(), 0.001);
    }

    @Test
    void naoDevePermitirProdutoComPrecoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("Teclado", -100.00));
    }

    @Test
    void naoDeveAplicarDescontoAbaixoDoLimite() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 499.99));

        assertEquals(499.99, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveAplicarDescontoQuandoTotalForExatamenteQuinhentos() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 500.00));

        assertEquals(450.00, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveAplicarDescontoAcimaDoLimite() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 500.01));

        assertEquals(450.009, carrinho.getTotal(), 0.001);
    }

    @Test
    void deveAplicarDescontoSobreSubtotalDeVariosProdutos() {
        Carrinho carrinho = new Carrinho();

        carrinho.adicionar(new Produto("Monitor", 400.00));
        carrinho.adicionar(new Produto("Teclado", 200.00));

        assertEquals(540.00, carrinho.getTotal(), 0.001);
    }

    @Test
    void naoDeveManterDescontoAposRemoverProdutoQueFezCruzarOLimite() {
        Carrinho carrinho = new Carrinho();
        Produto monitor = new Produto("Monitor", 450.00);
        Produto mouse = new Produto("Mouse", 100.00);
        carrinho.adicionar(monitor);
        carrinho.adicionar(mouse);

        carrinho.remover(mouse);

        assertEquals(450.00, carrinho.getTotal(), 0.001);
    }
}
