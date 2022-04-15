package com.lab1;

import static com.lab1.MatrixBuilder.createSymmetricMatrix;

public class Main {

    public static void main(String[] args) {

        int[] threadsNum = {2, 4, 6, 8};

        int[] matrixDimension = {10, 100, 1000, 3000, 6000};

        for (int i : matrixDimension) {

            System.out.println( "Размерность матрицы: " + i);

            Double[][] matrixA = createSymmetricMatrix(i);
            Double[] vectorB = matrixA[0];

            long startTime = System.currentTimeMillis();

            Double[] res1 = SolveByCholesky.solveLinearSystem(matrixA, vectorB);

            System.out.println("Время выполнения последовательного алгоритма: "
                    + (double) (System.currentTimeMillis() - startTime));

            for (int j : threadsNum) {
                startTime = System.currentTimeMillis();
                Double[] res2 = SolveByCholeskyParallel.solveLinearSystemParallel(matrixA, vectorB, j);
                System.out.println("Время выполнения параллельного алгоритма при " + j + " потоках(е)" + ": "
                        + (double) (System.currentTimeMillis() - startTime) + " милисек.");
            }
            System.out.println("\n");
        }
    }
}
