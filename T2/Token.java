/* 
Dessarollo de Sistemas Distribuidos
Tarea 2
Realizado por Isaac Godínez Cortés
*/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;


class Token {
    static DataInputStream entrada;
    static DataOutputStream salida;
    static boolean inicio = true;
    static String ip;
    static int nodo;
    static long token;
    
    static class Worker extends Thread {
        
        public void run () {
            //Algoritmo 1
            try {
                //SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                ServerSocket servidor = socket_factory.createServerSocket(20000 + nodo );
                // ServerSocket servidor = new ServerSocket( 20000 + nodo );
                Socket conexion = servidor.accept();
                entrada = new DataInputStream(conexion.getInputStream());

                // servidor.close();
                // conexion.close();
                // entrada.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if ( args.length != 2 ) {
            System.err.println("Se debe pasar como parametros el numero del nodo y la IP del siguiente nodo en el anillo");
            System.exit(1);
        }
        nodo  = Integer.valueOf(args[0]);
        ip = args[1];

        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        //Algoritmo 2
        Worker w = new Worker();
        w.start();
        Socket conexion = null;
        while (true) {
            try {
                // conexion = new Socket(ip, 20000+(nodo+1)%4);
                conexion = cliente.createSocket(ip, 20000+(nodo+1)%4);
                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }
        salida = new DataOutputStream(conexion.getOutputStream());
        w.join();
        while (true) {
            if ( nodo  == 0 ) {
                if ( inicio ) {
                    inicio = false;
                    token = 1;
                } else {
                    token = entrada.readLong();
                    token++;
                    System.out.println("Nodo: "+nodo);
                    System.out.println("Token: "+token);
                }
            } else {
                token = entrada.readLong();
                token++;   
                System.out.println("Nodo: "+nodo);
                System.out.println("Token: "+token);
            }
            if ( nodo == 0 && token>=1000 ) {
                break;
            }
            salida.writeLong(token);
        }

        conexion.close();
        salida.close();
    }
}