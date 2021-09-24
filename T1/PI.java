/* 
    Dessarollo de Sistemas Distribuidos
    Tarea 1
    Realizado por Isaac Godínez Cortés
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class PI {
    static Object obj = new Object();
    static float pi = 0;

    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            //Algoritmo 1
            try {
                //Streams de entrada y salida
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                //Declaración de la variable suma
                float suma;
                //Recepcion de la suma calculada por el cliente
                suma = entrada.readFloat();
                
                synchronized( obj ) {
                    pi += suma;
                }

                salida.close();
                entrada.close();
                conexion.close();

            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if ( args.length != 1 ) {
            System.err.println("Uso: ");
            System.err.println("java PI <nodo>");
            System.exit(0);
        }
        int nodo = Integer.valueOf(args[0]);
        if ( nodo == 0 ) {
            //Algoritmo 2
            ServerSocket servidor = new ServerSocket(40000);
            Worker v[] = new Worker[4];
            int i=0;
            while ( i!=4 ) {
                Socket conexion = servidor.accept();
                v[i] = new Worker(conexion);
                v[i].start();
                i++;
            }
            i = 0;
            while ( i!=4 ) {
                v[i].join();
                i++;
            }
            System.out.println("Valor de pi: "+pi);
            servidor.close();
        }else {
            //Algoritmo 3
            Socket conexion = null;

            //conexion al server con reitento
            for ( ;; ) {
                try {
                    conexion = new Socket("localhost", 40000);
                    break;                    
                } catch (Exception e) {
                    System.err.println(e);
                    Thread.sleep(100);
                }
            }
            //Streams de entrada y salida
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());

            float suma = 0;
            int i = 0;

            for ( i=0; i<1000000; i++ ) {
                suma += 4.0/(8*i+2*(nodo-2)+3);
            }
            suma = (nodo%2 == 0) ? (-suma) : (suma);
            salida.writeFloat(suma);
            salida.close();
            entrada.close();
            conexion.close();
        }
    }
}