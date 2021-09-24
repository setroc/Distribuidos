import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/* 
Dessarollo de Sistemas Distribuidos
Tarea 3
Realizado por Isaac Godínez Cortés
*/



/* 
    A=  --------------
            A1
        --------------
            A2
        --------------
    
    B=  --------------
            B1
        --------------
            B2
        --------------

    C=  --------------
            C1 | C2
        --------------
            C3 | C4
        --------------
    C1 = A1xB1
    C2 = A1xB2
    C3 = A2xB1
    C4 = A2xB2
*/

class Matriz {
    static int N = 10;
    static long A[][] = new long[N][N];
    static long B[][] = new long[N][N];
    static long C[][] = new long[N][N];

    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            try {
                //Streams de entrada y salida
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                
                //recibe el nodo
                int nodo = entrada.readInt();

                switch(nodo) {
                    case 1:
                        //A1
                        for (int i=0; i<N/2; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(A[i][j]);
                            }
                        }
                        //B1
                        for (int i=0; i<N/2; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(B[i][j]);
                            }
                        }
                        //C1
                        for (int i=0; i<N/2; i++) {
                            for (int j=0; j<N/2; j++) {
                                C[i][j]=entrada.readLong();
                            }
                        }
                        break;
                    case 2:
                        //A1
                        for (int i=0; i<N/2; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(A[i][j]);
                            }
                        }
                        //B2
                        for (int i=N/2; i<N; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(B[i][j]);
                            }
                        }
                        //C2
                        for (int i=0; i<N/2; i++) {
                            for (int j=N/2; j<N; j++) {
                                C[i][j]=entrada.readLong();
                            }
                        }
                        break;
                    case 3:
                        //A2
                        for (int i=N/2; i<N; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(A[i][j]);
                            }
                        }
                        //B1
                        for (int i=0; i<N/2; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(B[i][j]);
                            }
                        }
                        //C3
                        for (int i=N/2; i<N; i++) {
                            for (int j=0; j<N/2; j++) {
                                C[i][j]=entrada.readLong();
                            }
                        }
                        break;
                    case 4:
                        //A2
                        for (int i=N/2; i<N; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(A[i][j]);
                            }
                        }
                        //B2
                        for (int i=N/2; i<N; i++) {
                            for (int j=0; j<N; j++) {
                                salida.writeLong(B[i][j]);
                            }
                        }
                        //C4
                        for (int i=N/2; i<N; i++) {
                            for (int j=N/2; j<N; j++) {
                                C[i][j]=entrada.readLong();
                            }
                        }
                        break;
                }

                salida.close();
                entrada.close();
                conexion.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void impMatriz (long[][] m) {
        for (long[] i : m) {
            for (long j : i) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }
    public static void checksum(long[][] m) {
        long checksum = 0;
        for (long[] i : m) {
            for (long j : i) {
                checksum += j;
            }
        }
        System.out.println("Checksum: " + checksum);
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Se debe pasar como parametro el nodo y la ip");
            System.exit(1);
        }
        int nodo = Integer.valueOf(args[0]);
        if (nodo == 0) { //nodo 0
            //inicializar matrices A y B
            for (int i=0; i<N; i++) {
                for (int j=0; j<N; j++) {
                    A[i][j] = i+3*j;
                    B[i][j] = i-3*j;
                    C[i][j] = 100;
                }
            }
            if (N == 10) {
                System.out.println("Matriz A");
                impMatriz(A);
                System.out.println("\nMatriz B");
                impMatriz(B);
            }
            //transponer la matriz B
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < i; j++) {
                    long x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }
            }
            
            ServerSocket servidor = new ServerSocket(40000);
            Worker v[] = new Worker[4];
            for (int i=0; i<v.length; i++) {
                Socket conexion = servidor.accept();
                v[i] = new Worker(conexion);
                v[i].start();
            }
            for (int i=0; i<v.length; i++) {
                v[i].join();
            }

            
            if (N == 10) {
                System.out.println("\nC");
                impMatriz(C);
            }

            checksum(C);
            servidor.close();
        } else if (nodo>0 && nodo<5) { // nodo 1 - 4
            Socket conexion = null;
            //conexion al server con reintento
            for ( ;; ) {
                try {
                    conexion = new Socket(args[1], 40000);
                    break;                    
                } catch (Exception e) {
                    Thread.sleep(100);
                }
            }
            //Streams de entrada y salida
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            //Enviar el nodo
            salida.writeInt(nodo);

            long a[][] = new long[N/2][N];
            long b[][] = new long[N/2][N];
            long c[][] = new long[N/2][N/2];

            for (int i=0; i<N/2; i++) {
                for (int j=0; j<N; j++) {
                    a[i][j] = entrada.readLong();
                }
            }
            for (int i=0; i<N/2; i++) {
                for (int j=0; j<N; j++) {
                    b[i][j] = entrada.readLong();
                }
            }
            for (int i = 0; i < N/2; i++) {
                for (int j = 0; j < N/2; j++) {
                    for (int k = 0; k < N; k++) {
                        c[i][j] += a[i][k] * b[j][k];
                    }
                }
            }
            for (int i=0; i<N/2; i++) {
                for (int j=0; j<N/2; j++) {
                    salida.writeLong(c[i][j]);
                }
            }

            salida.close();
            entrada.close();
            conexion.close();
        }
    }
}
