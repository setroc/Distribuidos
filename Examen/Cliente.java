import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Cliente {

    public static void main(String[] args) throws Exception {
        Socket conexion = new Socket("sisdis.sytes.net", 50000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
        long t1 = 1632744934;

        long t2 = entrada.readLong();
        long t3 = entrada.readLong();

        long t4 = t3 + 3;

        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);

        salida.close();
        entrada.close();
        conexion.close();
    }
    // public static void main(String[] args) throws Exception {
    //     Socket conexion = new Socket("sisdis.sytes.net", 20000);
    //     DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    //     DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
    //     salida.writeInt(83);
    //     salida.writeInt(39);
    //     salida.writeDouble(35.0);
    //     salida.writeDouble(53.0);

    //     double num = entrada.readDouble();
    //     System.out.println(num);


    //     salida.close();
    //     entrada.close();
    //     conexion.close();
    // }

    // public static void main(String[] args) throws Exception {
    //     Socket conexion = new Socket("sisdis.sytes.net", 20020);
    //     DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    //     DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        
    //     salida.writeDouble(19.0);
    //     salida.writeDouble(19.0);
    //     salida.writeDouble(79.0);
    //     salida.writeInt(70);

    //     double numero=entrada.readDouble();
    //     System.out.println(numero);


    //     salida.close();
    //     entrada.close();
    //     conexion.close();
    // }

    // static void read(DataInputStream f, byte[] b, int posicion, int longitud) throws Exception {
    //     while(longitud > 0){
    //         int n = f.read(b, posicion, longitud);
    //         posicion += n;
    //         longitud -= n;
    //     }
    // }
}
