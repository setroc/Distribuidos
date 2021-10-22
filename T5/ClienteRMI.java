/* 
    Dessarollo de Sistemas Distribuidos
    Tarea 5
    Realizado por Isaac Godínez Cortés
*/
import java.rmi.Naming;

public class ClienteRMI {
    static int N = 3000;
    static double A[][] = new double[N][N];
    static double B[][] = new double[N][N];
    static double C[][] = new double[N][N];

    public static double[][] separa_matriz(double[][] A, int inicio) {
        double[][] M = new double[N/3][N];
        for (int i=0; i<N/3; i++) {
            for (int j=0; j<N; j++) {
                M[i][j] = A[i+inicio][j];
            }
        }
        return M;
    }
    public static double[][] multiplica_matrices(double[][] A, double[][] B) {
        double[][] C = new double[N/3][N/3];
        for (int i=0; i<N/3; i++) {
            for (int j=0; j<N/3; j++) {
                for (int k=0; k<N; k++) {
                    C[i][j] += A[i][k] * B[j][k];
                }
            }
        }
        return C;
    }
    public static void acomoda_matriz(double[][] C, double[][] c, int renglon, int columna) {
        for (int i=0; i<N/3; i++) {
            for (int j=0; j<N/3; j++) {
                C[i+renglon][j+columna] = c[i][j];
            }
        }
    }
    public static void checksum(double[][] m) {
        double checksum = 0;
        for (double[] i : m) {
            for (double j : i) {
                checksum += j;
            }
        }
        System.out.println("Checksum: " + checksum);
    }
    public static void impMatriz (double[][] m) {
        for (double[] i : m) {
            for (double j : i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) throws Exception {
        //cambiar localhost por la ip del servidor
        String url = "rmi://10.0.0.5/prueba";
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);

        String url1 = "rmi://10.0.0.6/prueba";
        InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url);

        String url2 = "rmi://10.0.0.7/prueba";
        InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url);

        //inicializar matrices
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                A[i][j] = 3*i+j;
                B[i][j] = i-4*j;
                C[i][j] = 0;
            }
        }
        //trasponer la matriz B.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                double x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }

        double[][] A1 = separa_matriz(A, 0);
        double[][] A2 = separa_matriz(A, N/3);
        double[][] A3 = separa_matriz(A, 2*N/3);
        double[][] B1 = separa_matriz(B, 0);
        double[][] B2 = separa_matriz(B, N/3);
        double[][] B3 = separa_matriz(B, 2*N/3);

        double[][] C1 = r.multiplica_matrices(A1, B1, N);
        double[][] C2 = r.multiplica_matrices(A1, B2, N);
        double[][] C3 = r.multiplica_matrices(A1, B3, N);
        double[][] C4 = r1.multiplica_matrices(A2, B1, N);
        double[][] C5 = r1.multiplica_matrices(A2, B2, N);
        double[][] C6 = r1.multiplica_matrices(A2, B3, N);
        double[][] C7 = r2.multiplica_matrices(A3, B1, N);
        double[][] C8 = r2.multiplica_matrices(A3, B2, N);
        double[][] C9 = r2.multiplica_matrices(A3, B3, N);

        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/3);
        acomoda_matriz(C, C3, 0, 2*N/3);
        acomoda_matriz(C, C4, N/3, 0);
        acomoda_matriz(C, C5, N/3, N/3);
        acomoda_matriz(C, C6, N/3, 2*N/3);
        acomoda_matriz(C, C7, 2*N/3, 0);
        acomoda_matriz(C, C8, 2*N/3, N/3);
        acomoda_matriz(C, C9, 2*N/3, 2*N/3);
        //imprimir matriz
        if (N == 9) {
            System.out.println("Matriz A");
            impMatriz(A);
            System.out.println("Matriz B transpuesta");
            impMatriz(B);
            System.out.println("Matriz C");
            impMatriz(C);
        }
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