package com.os.project;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        System.out.println("The matrix will use A{MxP} and B{PxN}");
//        System.out.println("Enter the size of Matrix:");
//        System.out.print("Enter M: ");
//        int m = sc.nextInt();
//        System.out.print("\nEnter P: ");
//        int p = sc.nextInt();
//        int matrix_1 [][] = new int[m][p];
//        System.out.print("Enter N: ");
//        int n = sc.nextInt();
//        int matrix_2 [][] = new int[p][n];
//        int m_size = matrix_1.length;
//        int n_size = matrix_1[1].length;
//        System.out.println(m_size+" "+n_size+" "+matrix_1[0].length);
//        get_matrix(matrix_1,m,p);
//        display_matrix(matrix_1);



        //test
        System.out.println("Enter the order N of the matrix");
        int n = sc.nextInt();
        int[][] matrix_1 = new int[n][n];
        int[][] matrix_2 = new int[n][n];
        get_matrix(matrix_1,n,n);
        get_matrix(matrix_2,n,n);
        display_matrix(matrix_1);
        System.out.println("");
        display_matrix(matrix_2);


        //strassen test for n
        Strassen s = new Strassen();
        int R[][] = s.multiply(matrix_1,matrix_2);
        display_matrix(R);


        //TODO: Matrix validation
    }

    static void display_matrix(int matrix[][]) {
        for (int mat[]: matrix) {
            for(int ele: mat) {
                System.out.print(ele+"   ");
            }
            System.out.println("");

        }
    }
    static void get_matrix(int matrix[][],int m,int n) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter data:");
        for (int i = 0; i < m ; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }
    }
}
