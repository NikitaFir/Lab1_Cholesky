package com.lab1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.lab1.MatrixBuilder.transpositionMatrix;

/**
 * Параллельная реализация метода Холецкого решения СЛАУ
 */
public class SolveByCholeskyParallel {

    public static Double[] solveLinearSystemParallel(Double[][] matrixA, Double[] vectorB, int threads) {

        int matrixDimension = matrixA.length;
        Double[][] matrixL = new Double[matrixDimension][matrixDimension];

        Double[] vectorX = new Double[matrixDimension];
        Double[] vectorY = new Double[matrixDimension];
        double sum1;
        double sum2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < matrixDimension; i++) {
            executorService.execute(new DiagonalElementCalculation(i, matrixA, matrixL));
            executorService.execute(new UnderDiagonalElementCalculation(i, matrixDimension, matrixA, matrixL));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(24L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Double[][] transMatrixL = transpositionMatrix(matrixL);


        for (int i = 0; i < matrixDimension; i++) {
            sum1 = 0;

            for (int j = 0; j < i; j++) {
                sum1 +=  vectorY[j] * matrixL[i][j];
            }
            vectorY[i] = (vectorB[i] - sum1) / matrixL[i][i];
        }

        for (int i = matrixDimension - 1; i >= 0; i--) {

            sum2 = 0;

            for (int j = matrixDimension - 1; j > i; j--) {
                sum2 += vectorX[j] * transMatrixL[i][j];
            }
            vectorX[i] =  (vectorY[i] - sum2) / transMatrixL[i][i];
        }

        return vectorX;
    }
    // Вычисление диагонального элемента
    static class DiagonalElementCalculation extends Thread {

        int i;
        Double[][] a;
        Double[][] L;

        public DiagonalElementCalculation(int i, Double[][] a, Double[][] L) {
            this.i = i;
            this.a = a;
            this.L = L;
        }

        @Override
        public void run() {

            synchronized (L) {
                double sum = 0;
                for (int j = 0; j < i; j++) {

                    while (L[i][j] == null) {

                        try {
                            L.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sum += L[i][j] * L[i][j];
                }

                L[i][i] = Math.sqrt(Math.abs(a[i][i] - sum));
                L.notifyAll();
            }
        }
    }
    // Вычисление поддиагонального элемента
    static class UnderDiagonalElementCalculation extends Thread {

        int i;
        int n;
        Double[][] L;
        Double[][] a;

        public UnderDiagonalElementCalculation(int i, int n, Double[][] a, Double[][] L) {

            this.i = i;
            this.n = n;
            this.a = a;
            this.L = L;
        }

        @Override
        public void run() {

            synchronized (L) {
                for (int j = i + 1; j < n; j++) {
                    double sum = 0.0;
                    for (int p2 = 0; p2 <= i - 1; p2++) {

                        while (L[i][p2] == null
                                && L[j][p2] == null) {

                            try {
                                L.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        sum += L[i][p2] * L[j][p2];
                    }

                    while (L[i][i] == null) {
                        try {
                            L.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    L[j][i] = (a[j][i] - sum) / L[i][i];
                    L.notifyAll();
                }
            }
        }
    }
}
