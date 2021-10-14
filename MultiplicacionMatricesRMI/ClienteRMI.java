import java.rmi.Naming;

public class ClienteRMI {
    static int N = 6;
    static int A[][] = new int[N][N];
    static int B[][] = new int[N][N];
    static int C[][] = new int[N][N];

    public static int[][] separa_matriz(int[][] A, int inicio) {
        int[][] M = new int[N/2][N];
        for (int i=0; i<N/2; i++) {
            for (int j=0; j<N; j++) {
                M[i][j] = A[i+inicio][j];
            }
        }
        return M;
    }
    public static void acomoda_matriz(int[][] C, int[][] c, int renglon, int columna) {
        for (int i=0; i<N/2; i++) {
            for (int j=0; j<N/2; j++) {
                C[i+renglon][j+columna] = c[i][j];
            }
        }
    }
    public static void checksum(int[][] m) {
        int checksum = 0;
        for (int[] i : m) {
            for (int j : i) {
                checksum += j;
            }
        }
        System.out.println("Checksum: " + checksum);
    }

    public static void main(String[] args) throws Exception {
        //cambiar localhost por la ip del servidor
        String url = "rmi://localhost/prueba";
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);

        //inicializar matrices
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                A[i][j] = 2*i-j;
                B[i][j] = i+2*j;
                C[i][j] = 0;
            }
        }
        //trasponer la matriz B.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                int x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        int[][] A1 = separa_matriz(A, 0);
        int[][] A2 = separa_matriz(A, N/2);
        int[][] B1 = separa_matriz(B, 0);
        int[][] B2 = separa_matriz(B, N/2);

        int[][] C1 = r.multiplica_matrices(A1, B1, N);
        int[][] C2 = r.multiplica_matrices(A1, B2, N);
        int[][] C3 = r.multiplica_matrices(A2, B1, N);
        int[][] C4 = r.multiplica_matrices(A2, B2, N);

        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/2);
        acomoda_matriz(C, C3, N/2, 0);
        acomoda_matriz(C, C4, N/2, N/2);
        //calcular y desplegar el checksum
        checksum(C);
    }
}

//ejecucion
/* 
    rmiregistry
    java ServidorRMI
    jaca ClienteRMI
*/

/*
    N/3
    tres secciones
    b1 b2 b3 a1 a2 a4
    nueve secciones en c
    c1 c2 .... c9 
    
*/