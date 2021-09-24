import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;

public class ServidormultithreadSSL {
    static void escribe_archivo(String archivo, byte[] buffer) throws Exception {
        FileOutputStream f = new FileOutputStream(archivo);
        try {
            f.write(buffer);
        } finally {
            f.close();
        }
    }
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while(longitud > 0){
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
    static class Worker extends Thread {
        Socket conexion;
        Worker(Socket conexion) {
            this.conexion = conexion;
        }
        public void run() {
            try {
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                
                String nombreArchivo = entrada.readUTF();
                int longitudArchivo = entrada.readInt();
                byte[] buffer = new byte[longitudArchivo];
                read(entrada, buffer, 0, longitudArchivo);
                // System.out.println(new String(buffer, "UTF-8"));    
                escribe_archivo(nombreArchivo, buffer);
                salida.close();
                entrada.close();
                conexion.close(); 
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000);

        for (;;) {
            Socket conexion = socket_servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }
}
