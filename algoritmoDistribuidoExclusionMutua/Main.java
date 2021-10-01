import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    static String[] host = new String[3];
    static int[] puertos = new int[3];
    static int num_nodos;
    static int nodo;

    static long reloj_logico;
    static Object lock = new Object();

    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            try {
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                System.out.println("Inicio del thread Worker");
                long tiempo_recibido = entrada.readLong();
                //recibir el nodo y el mensaje ok o peticion
                synchronized (lock) {
                    if ( tiempo_recibido >= reloj_logico ) reloj_logico = tiempo_recibido + 1;
                }


            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
    static class Servidor extends Thread {
        
        public void run() {
            try {
                ServerSocket servidor = new ServerSocket(puertos[nodo]);
                while (true) {
                    Socket conexion = servidor.accept();
                    Worker w = new Worker(conexion);
                    w.start();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
    static class Reloj extends Thread {
        public void run() {
            try {
                while (true) {
                    synchronized (lock) {
                        System.out.println("Reloj logico " + reloj_logico);
                        if (nodo == 0) reloj_logico +=4;
                        else
                        if (nodo == 1) reloj_logico +=5;
                        else
                        if (nodo == 2) reloj_logico +=6;
                    }
                    Thread.sleep(1000);
                }
                
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.err.println("Error, debe pasar bien los argumentos nodo - puertos");
            System.exit(1);
        }
        nodo = Integer.parseInt(args[0]);
        num_nodos = args.length - 1; // numeros de puertos que abre menos el nodo
        for (int i=0; i<args.length - 1; i++) {
            String[] partes = args[i+1].split(":");
            host[i] = partes[0];
            puertos[i] = Integer.parseInt(partes[1]);
        }
        Servidor servidor = new Servidor();
        servidor.start();

        //barrera
        for (int i=0; i<num_nodos; i++) {
            if ( i != nodo) envia_mensaje(-1, host[i], puertos[i]);
        }
        Reloj reloj = new Reloj();
        reloj.start();
        servidor.join();
    }

    public static void envia_mensaje (long tiempo_logico, String host, int puerto) throws Exception {
        Socket conexion = null;
        while (true) {
            try {
                conexion = new Socket(host, puerto);
                break;
            } catch (Exception e) {
                // System.err.println(e);
                Thread.sleep(100);
            }
        }
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        //enviar el tiempo logico
        salida.writeLong(tiempo_logico);
        //enviar el numero de nodo
        salida.writeInt(nodo);
        //enviar el mensaje de ok o peticio
        
        salida.close();
        conexion.close();
    }
}

// java Main 0 localhost:50000 localhost:50001 localhost:50002
// java Main 1 localhost:50000 localhost:50001 localhost:50002
// java Main 2 localhost:50000 localhost:50001 localhost:50002