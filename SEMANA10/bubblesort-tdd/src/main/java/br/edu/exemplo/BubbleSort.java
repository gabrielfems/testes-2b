package br.edu.exemplo;

public class BubbleSort {

    private int[] numeros;

    public BubbleSort(int[] numeros) {
        this.numeros = numeros;
    }

    public void ordenar() {
        for (int i = 0; i < numeros.length - 1; i++) {
            boolean houveTroca = false;

            // a cada passada, o maior elemento restante "flutua" para o fim
            for (int j = 0; j < numeros.length - 1 - i; j++) {
                if (numeros[j] > numeros[j + 1]) {
                    trocar(j, j + 1);
                    houveTroca = true;
                }
            }

            // sem trocas, o vetor já está ordenado: encerra antes
            if (!houveTroca) {
                break;
            }
        }
    }

    private void trocar(int a, int b) {
        int temp = numeros[a];
        numeros[a] = numeros[b];
        numeros[b] = temp;
    }

    public int[] getNumeros() {
        return numeros;
    }
}
