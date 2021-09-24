class MultiplicaMatriz {

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

        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C

        for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            for (int k = 0; k < N; k++)
            C[i][j] += A[i][k] * B[k][j];

        impMatriz(A);
        System.out.println();
        impMatriz(B);
        System.out.println();
        impMatriz(C);
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo: " + (t2 - t1) + "ms");
  }
}
