package com.lab1;

import java.util.Random;
/**
 * Вспомогательный класс
 * 1. Функция создания симметричной матрицы для теста
 * 2. Функция транспонирования матрицы
 */
public class MatrixBuilder {


    public static Double[][] transpositionMatrix(Double[][] matrix) {

        int rowsMatrix = matrix.length;
        int columnsMatrix = matrix.length;
        Double[][] newMatrix = new Double[rowsMatrix][columnsMatrix];

        for (int i = 0; i < rowsMatrix; i++) {
            for (int j = 0; j < columnsMatrix; j++) {
                newMatrix[j][i] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    public static Double[][] createSymmetricMatrix(int n) {

        Double[][] outputMatrix = new Double[n][n];

        int rangeMin = 1;
        int rangeMax = 100;

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {

                Random r = new Random();
                double randomValue = (int)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
                outputMatrix[i][j] = randomValue;
            }
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                outputMatrix[i][j] = outputMatrix[j][i];
            }
        }

        return outputMatrix;
    }
}
