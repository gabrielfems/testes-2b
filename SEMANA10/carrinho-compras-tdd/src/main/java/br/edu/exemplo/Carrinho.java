package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private static final double LIMITE_PARA_DESCONTO = 500.00;
    private static final double PERCENTUAL_DESCONTO = 0.10;

    private final List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
        produtos.add(produto);
    }

    public void remover(Produto produto) {
        produtos.remove(produto);
    }

    public double getTotal() {
        double subtotal = calcularSubtotal();
        return temDireitoADesconto(subtotal)
                ? subtotal - (subtotal * PERCENTUAL_DESCONTO)
                : subtotal;
    }

    private double calcularSubtotal() {
        double subtotal = 0;
        for (Produto produto : produtos) {
            subtotal += produto.getPreco();
        }
        return subtotal;
    }

    private boolean temDireitoADesconto(double subtotal) {
        return subtotal >= LIMITE_PARA_DESCONTO;
    }
}
