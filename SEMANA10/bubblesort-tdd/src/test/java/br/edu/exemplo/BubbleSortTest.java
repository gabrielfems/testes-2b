package br.edu.exemplo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BubbleSortTest {

    @Test
    void deveOrdenarVetorDesordenado() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{5, 3, 1, 4, 2});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveManterVetorJaOrdenado() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{1, 2, 3, 4, 5});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveOrdenarVetorComValoresRepetidos() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{3, 1, 3, 2, 1});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 1, 2, 3, 3},
                bubbleSort.getNumeros()
        );
    }

    // ---- Testes adicionais (cobrem bordas que os requisitos não citam) ----

    @Test
    void deveOrdenarVetorEmOrdemDecrescente() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{5, 4, 3, 2, 1});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveOrdenarVetorComNumerosNegativos() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{3, -1, 0, -5, 2});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{-5, -1, 0, 2, 3},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveManterVetorVazio() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{}, bubbleSort.getNumeros());
    }

    @Test
    void deveManterVetorComUmElemento() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{42});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{42}, bubbleSort.getNumeros());
    }
}
