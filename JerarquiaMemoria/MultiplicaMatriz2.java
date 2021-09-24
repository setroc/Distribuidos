class MultiplicaMatriz2 {

    public static void impMatriz (int[][] m) {
        for (int[] i : m) {
            for (int j : i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int[][] A = new int[N][N];
        int[][] B = new int[N][N];
        int[][] C = new int[N][N];
        long t1 = System.currentTimeMillis();

        // inicializa las matrices A y B

        for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
        {
            A[i][j] = i+3*j;
            B[i][j] = i-3*j;
            C[i][j] = 0;
        }

        // transpone la matriz B, la matriz traspuesta queda en B

        for (int i = 0; i < N; i++)
        for (int j = 0; j < i; j++)
        {
            int x = B[i][j];
            B[i][j] = B[j][i];
            B[j][i] = x;
        }

        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C
        // notar que los indices de la matriz B se han intercambiado

        for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            for (int k = 0; k < N; k++)
            C[i][j] += A[i][k] * B[j][k];

        impMatriz(A);
        System.out.println();
        impMatriz(B);
        System.out.println();
        impMatriz(C);
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2 - t1) + "ms");
    }
}