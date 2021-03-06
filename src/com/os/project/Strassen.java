package com.os.project;

import java.util.concurrent.*;

public class Strassen implements Callable<int[][]>{

    private int[][] A;
    private int[][] B;
    private int[][] M;
    private int n;
    public int[][] C;


    public Strassen (int[][] A) {
        this.M = A;
    }

    public Strassen (int[][] A,int[][] B){
        this.A = A;
        this.B = B;
        this.n = A.length;
    }

    @Override
    public int[][] call() throws Exception {
            C = multiply(A,B);
        return C;
    }

    ExecutorService executor = Executors.newFixedThreadPool (4);

    public int[][] multiply(int[][] A, int[][] B) throws ExecutionException, InterruptedException {
        int n = A.length;
        int R[][] = new int [n][n];

        /* Base condition*/
        if(n==1) {
            R[0][0]=A[0][0]*B[0][0];
        }

        else {
            int[][] A11 = new int[n/2][n/2];
            int[][] A12 = new int[n/2][n/2];
            int[][] A21 = new int[n/2][n/2];
            int[][] A22 = new int[n/2][n/2];
            int[][] B11 = new int[n/2][n/2];
            int[][] B12 = new int[n/2][n/2];
            int[][] B21 = new int[n/2][n/2];
            int[][] B22 = new int[n/2][n/2];

            // Partitioning Matrix A
            split(A, A11, 0 , 0);
            split(A, A12, 0 , n/2);
            split(A, A21, n/2, 0);
            split(A, A22, n/2, n/2);

            //Partitioning Matrix B
            split(B, B11, 0, 0);
            split(B, B12, 0, n/2);
            split(B, B21, n/2, 0);
            split(B, B22, n/2, n/2);

            Future<int[][]> future1;
            Future<int[][]> future2;
            Future<int[][]> future3;
            Future<int[][]> future4;
            Future<int[][]> future5;
            Future<int[][]> future6;
            Future<int[][]> future7;

//            future1 = executor.submit(new Strassen(multiply(add(A11,A22), add(B11,B22))));
//            future2 = executor.submit(new Strassen(multiply(add(A21,A22), B11)));
//            future3 = executor.submit(new Strassen(multiply(A11, sub(B12, B22))));
//            future4 = executor.submit(new Strassen(multiply(A22, sub(B21, B11))));
//            future5 = executor.submit(new Strassen(multiply(add(A11, A12), B22)));
//            future6 = executor.submit(new Strassen(multiply(sub(A21, A11), add(B11, B12))));
//            future7 = executor.submit(new Strassen(multiply(sub(A12, A22), add(B21, B22))));

            future1 = executor.submit(new Strassen(add(A11,A22), add(B11,B22)));
            future2 = executor.submit(new Strassen(add(A21,A22), B11));
            future3 = executor.submit(new Strassen(A11, sub(B12, B22)));
            future4 = executor.submit(new Strassen(A22, sub(B21, B11)));
            future5 = executor.submit(new Strassen(add(A11, A12), B22));
            future6 = executor.submit(new Strassen(sub(A21, A11), add(B11, B12)));
            future7 = executor.submit(new Strassen(sub(A12, A22), add(B21, B22)));

            /**
             M1 = (A11 + A22)(B11 + B22)
             M2 = (A21 + A22) B11
             M3 = A11 (B12 - B22)
             M4 = A22 (B21 - B11)
             M5 = (A11 + A12) B22
             M6 = (A21 - A11) (B11 + B12)
             M7 = (A12 - A22) (B21 + B22)
             **/
            /**
            int[][] M1 = multiply(add(A11,A22), add(B11,B22));
            int[][] M2 = multiply(add(A21,A22), B11);
            int [][] M3 = multiply(A11, sub(B12, B22));
            int [][] M4 = multiply(A22, sub(B21, B11));
            int [][] M5 = multiply(add(A11, A12), B22);
            int [][] M6 = multiply(sub(A21, A11), add(B11, B12));
            int [][] M7 = multiply(sub(A12, A22), add(B21, B22));
             **/
            int[][] M1 = future1.get();
            int[][] M2 = future2.get();
            int [][] M3 = future3.get();
            int [][] M4 = future4.get();
            int [][] M5 = future5.get();
            int [][] M6 = future6.get();
            int [][] M7 = future7.get();

            /**
             C11 = M1 + M4 - M5 + M7
             C12 = M3 + M5
             C21 = M2 + M4
             C22 = M1 - M2 + M3 + M6
             **/

            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);

            /** join 4 halves into one result matrix **/
            join(C11, R, 0 , 0);
            join(C12, R, 0 , n/2);
            join(C21, R, n/2, 0);
            join(C22, R, n/2, n/2);

        }

        return R;
    }

    public int[][] add(int[][] A, int[][] B){
        int n = A.length;
        int[][] C = new int [n][n];
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    public int[][] sub(int[][] A, int[][] B){
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    public void join(int[][] C, int[][] P, int ib, int jb){
        for (int i = 0,x = ib; i < C.length; i++,x++) {
            for (int j = 0, y = jb; j < C.length; j++,y++) {
                P[x][y] = C[i][j];
            }
        }
    }

    public void split(int[][] P, int[][] C, int ib,int jb){

        for (int i = 0, x=ib; i < C.length; i++,x++) {
            for (int j = 0, y=jb; j < C.length; j++,y++) {
                C[i][j] = P[x][y];
            }
        }
    }




    //TODO change the class name pls....
}
