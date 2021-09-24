import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

class ClienteSSL {
    public static void main(String[] args) throws Exception {
        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket conexion = cliente.createSocket("localhost", 50000);

        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());

        salida.writeDouble(123456789.123465789);

        salida.close();
        entrada.close();
        conexion.close();
    }
}