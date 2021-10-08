import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static String[] host = new String[8];
    static int[] puertos = new int[8];
    static int num_nodos;
    static int nodo;

    static long reloj_logico;
    static Object lock = new Object();

    static int coordinador_actual;

    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            try {
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());

                System.out.println("Inicio del thread Worker");
                //lectura del 'hola'
                entrada.readUTF();
                String mensaje = entrada.readUTF();

                if ( mensaje == "ELECCION" ) {
                    salida.writeUTF("OK");
                    //invocar la funcion eleccion con el nodo
                    eleccion(nodo);
                }else if ( mensaje == "COORDINADOR" ) {
                    coordinador_actual = entrada.readInt();
                }


                // entrada.close();
                // salida.close();
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

    public static void main(String[] args) throws Exception {
        if (args.length<2) {
            System.err.println("Error, debe pasar bien los argumentos - nodo");
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

        //implementar barrera :(
        for (int i=0; i<num_nodos; i++) {
            if ( i!=nodo ) {
                barrera(host[i], puertos[i]);
            }
        }
        //esperar 3 segundos
        Thread.sleep(3000);
        if ( nodo == 7 ) {
            System.exit(0);
        } else if ( nodo == 4 ) {
            eleccion(nodo);
        }
        servidor.join();
    }

    public static String envia_mensaje_eleccion (String host, int puerto) {
        try {
            Socket conexion = new Socket(host, puerto);
            String cad = "";
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                salida.writeUTF("ELECCION");
                cad = entrada.readUTF();
                conexion.close();
                return cad;
            } finally {
                conexion.close();
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    public static void envia_mensaje_coordinador (String host, int puerto) {
        try {
            Socket conexion = new Socket(host, puerto);
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                salida.writeUTF("COORDINADOR");
                salida.writeInt(nodo);
    
                conexion.close();
                return;
            } finally {
                conexion.close();
            }
        } catch (Exception e) {
            return;
        }
    }

    public static void eleccion (int nodo) {
        //implementar algoritmo
        for (int i=nodo+1; i<num_nodos; i++) {
            if ( envia_mensaje_eleccion(host[i], puertos[i]) == "OK" ) {
                return;
            }
        }
        for (int i=0; i<nodo; i++) {
            envia_mensaje_coordinador(host[i], puertos[i]);
        }
    }
    public static void barrera(String host, int puerto) throws Exception {
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
        salida.writeUTF("HOLA");
        salida.close();
    }
}

/* 
java Main 0 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 1 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 2 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 3 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 4 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 5 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 6 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
java Main 7 localhost:50000 localhost:50001 localhost:50002 localhost:50003 localhost:50004 localhost:50005 localhost:50006 localhost:50007
*/