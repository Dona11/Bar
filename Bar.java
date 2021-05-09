
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utente
 */
public class Bar {
    
    private static Semaphore semVuoto = new Semaphore(10);
    private static Semaphore semPieno = new Semaphore(0);
    
    public static class BarCliente{
    
        private int posti;

        public BarCliente(int posti) {
            this.posti = posti;
            
            System.out.println("Il bar apre\n");
        }
        
        public void clienteEntra(){
        
            posti--;
        }
        
//        public void clienteEsce(){
//        
//            posti++;
//        }
    }
    
    public static class Cliente extends Thread{
    
        private BarCliente bar;
        private int caffeOrdinati = (int) (Math.random() * 4) + 1;
        private static int incasso = 0;

        public Cliente(String nome, BarCliente bar) {
            
            super(nome);
            this.bar = bar;
            start();
        }
        
        public void run(){
            
            while(true){

                try{
                    sleep((int) (Math.random() * 20000));
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                try{

                    semVuoto.acquire();
                }catch(InterruptedException ex){

                }

                bar.clienteEntra();
                incasso += caffeOrdinati;
                System.out.println(getName() + " entra, il cliente ordina " + caffeOrdinati + " caffè");
                semPieno.release();
                
                try{

                    sleep((int) (Math.random() * 30000));
                }catch(InterruptedException e){

                    e.printStackTrace();
                }

                try{

                    semPieno.acquire();
                }catch(InterruptedException ex){

                }

                
                System.out.println(getName() + " esce");
                semVuoto.release();
                try {
                    this.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Bar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void main(String[] args){
    
        BarCliente bar = new BarCliente(10);
        
        for(int i = 1; i <= 10; i++){
        
            Cliente c = new Cliente("Cliente " + i, bar);
            
        }
        
        
    }
}
