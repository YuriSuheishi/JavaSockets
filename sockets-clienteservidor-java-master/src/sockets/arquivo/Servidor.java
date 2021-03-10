/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.arquivo;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import sockets.thread.ThreadSockets;


/**
 *
 * @author yuris
 */
public class Servidor {

   public static void main(String args[]) {
     try {
        //1 - Habilita server socket
        ServerSocket srvSocket = new ServerSocket(1111);
        System.out.println("Aguardando envio de arquivo ...");
        
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = srvSocket.accept();
            byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
            BufferedInputStream bf = new BufferedInputStream(
               socket.getInputStream());
            bf.read(objectAsByte);

            //3 - 
            Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
            String dir = "../Servidor/" + arquivo.getNome();
            
            //mandando arquivo para servidores secundarios
            EnviarArquivoServidor(arquivo, 2221);     
            EnviarArquivoServidor(arquivo, 2222);  
            
            //criando arquivo
            if(arquivo.getTipo() == 1){
                    System.out.println("Criando arquivo " + dir);

                    FileOutputStream fos = new FileOutputStream(dir);
                    fos.write(arquivo.getConteudo());
                    fos.close();         
            }
            else if(arquivo.getTipo() == 0){
                    boolean success = (new File(dir)).delete();
                    System.out.println("Deletando arquivo " + dir);
                    if(success == true){
                        System.out.println(dir + " deletado com sucesso!");
                    }
                    else{
                        System.out.println(dir + " não encontrado!");                
                    } 
            }
            else{
                    System.out.println("Modificando arquivo " + dir);     
                    boolean success = (new File(dir)).delete();
                    if(success == true){
                        System.out.println(dir + " modificado com sucesso!");
                    }
                    else{
                        System.out.println("Não encontrado! criando arquivo " + dir);                
                    }
                    FileOutputStream fos = new FileOutputStream(dir);
                    fos.write(arquivo.getConteudo());
                    fos.close();    
            
            }
        }
     } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
     }
   }

   private static Object getObjectFromByte(byte[] objectAsByte) {
     Object obj = null;
     ByteArrayInputStream bis = null;
     ObjectInputStream ois = null;
     try {
        bis = new ByteArrayInputStream(objectAsByte);
        ois = new ObjectInputStream(bis);
        obj = ois.readObject();

        bis.close();
        ois.close();

     } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
     } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
     }

     return obj;

   }
        
        private static void EnviarArquivoServidor(Arquivo arquivo, int porta){
            try {
                Socket socket = new Socket("127.0.0.1", porta);

                BufferedOutputStream bf = new BufferedOutputStream
                (socket.getOutputStream());

                byte[] bytea = serializarArquivo(arquivo);
                bf.write(bytea);
                bf.flush();
                bf.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }

        private static byte[] serializarArquivo(Arquivo arquivo){
           try {
              ByteArrayOutputStream bao = new ByteArrayOutputStream();
              ObjectOutputStream ous;
              ous = new ObjectOutputStream(bao);
              ous.writeObject(arquivo);
              return bao.toByteArray();
           } catch (IOException e) {
              e.printStackTrace();
           }

           return null;
        }

}
