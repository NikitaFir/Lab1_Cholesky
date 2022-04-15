package com.lab1;

import static com.lab1.MatrixBuilder.*;

/**
* Последовательная реализация метода Холецкого решения СЛАУ
*/
public class SolveByCholesky {

    public static Double[] solveLinearSystem(Double[][] matrixA, Double[] vectorB) {

        int matrixDimension = matrixA.length;
        Double[][] matrixL = new Double[matrixDimension][matrixDimension];

        matrixL[0][0] = Math.sqrt(matrixA[0][0]);

        for (int j = 1; j < matrixDimension; j++) {
            int i = 0;
            matrixL[j][i] = matrixA[j][i] / matrixL[0][0];
        }

        for (int i = 1; i < matrixDimension; i++) {
            double sum = 0.0;

            for (int p1 = 0; p1 <= i - 1; p1++) {
                sum = sum + matrixL[i][p1] * matrixL[i][p1];
            }

            matrixL[i][i] = Math.sqrt(Math.abs(matrixA[i][i] - sum));

            for (int j = i + 1; j < matrixDimension; j++) {
                sum = 0.0;

                for (int p2 = 0; p2 <= i - 1; p2++) {
                    sum = sum + matrixL[i][p2] * matrixL[j][p2];
                }

                matrixL[j][i] = (matrixA[j][i] - sum) / matrixL[i][i];
            }
        }

        Double[][] transMatrixL = transpositionMatrix(matrixL);

        Double[] vectorX = new Double[matrixDimension];
        Double[] vectorY = new Double[matrixDimension];
        double sum1;
        double sum2;

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
}
