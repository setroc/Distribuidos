package Thread;

class P {
    static class Worker extends Thread {
        //metodo que debe ir siempre para crear un thread
        public void run () {
            for (var i=1; i<=10; i++) System.out.println(i);
        }
    }
    public static void main(String[] args) throws Exception {
        Worker w1 = new Worker();
        Worker w2 = new Worker();
        //inicializa los hilos dormidos
        w1.start();
        w2.start();
        
        //ejecuta los hilos y pone una barrera
        w1.join();
        w2.join();
    }
}