import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

public class ClientemultithreadSSL {

    static byte[] lee_archivo(String archivo) throws Exception {
        FileInputStream f = new FileInputStream(archivo);
        byte[] buffer;
        try {
            buffer = new byte[f.available()];
            f.read(buffer);
        } finally {
            f.close();
        }
        return buffer;
    }
    public static void main(String[] args) throws Exception {
        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket conexion = null;        
        for(;;) {
            try {
                conexion = cliente.createSocket("localhost", 50000);
                break;
            } catch (Exception e) {
                System.out.println(e);
                Thread.sleep(100);
            }
        }
        
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        String nombreArchivo = args[0];
        byte[] buffer = lee_archivo(nombreArchivo);
        salida.writeUTF(nombreArchivo);
        salida.writeInt(buffer.length);
        salida.write(buffer);


        salida.close();
        entrada.close();
        conexion.close();
    }
    static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
        while(longitud > 0){
            int n = f.read(b, posicion, longitud);
            posicion += n;
            longitud -= n;
        }
    }
}
