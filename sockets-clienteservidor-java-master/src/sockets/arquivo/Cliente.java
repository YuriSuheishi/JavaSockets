/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.arquivo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author yuris
 */
public class Cliente {
        private static Arquivo arquivo;
        
	public static void main(String[] args) throws IOException, InterruptedException {
          WatchService watchService = FileSystems.getDefault().newWatchService();

          //local da pasta cliente
         Path path = Paths.get("../Cliente");

        //Habilitando Watcher
        path.register(
          watchService, 
            StandardWatchEventKinds.ENTRY_CREATE, 
              StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        //verifica o que aconteceu
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if( event.kind().toString() == "ENTRY_DELETE"){
                        System.out.println(event.context() + " deletado");    
                        //cria um objeto do tipo arquivo
                        CriaArquivo(event.context().toString(),0);
                        //transforma em byte e envia para o servidor
                        EnviarArquivoServidor();
                    
                }
                else if(event.kind().toString() == "ENTRY_CREATE"){
                        System.out.println(event.context() + " criado");  
                        //cria um objeto do tipo arquivo     
                        CriaArquivo(event.context().toString(),1);
                        //transforma em byte e envia para o servidor
                        EnviarArquivoServidor();
                }
                else{
                    System.out.println(event.context() + " modificado");  
                        //cria um objeto do tipo arquivo   
                    CriaArquivo(event.context().toString(),2);
                        //transforma em byte e envia para o servidor
                    EnviarArquivoServidor();                      
                }
            }
            key.reset();
        }
        }
        
        private static void CriaArquivo(String fileName, int tipo) throws FileNotFoundException, IOException {
            FileInputStream fis;
                   System.out.println("Arquivo: " +fileName);
                   File file = new File("../Cliente/" + fileName);
                   
                   arquivo = new Arquivo();
                   if(tipo!= 0){
                   byte[] bFile = new byte[(int) file.length()];
                   fis = new FileInputStream(file);
                   fis.read(bFile);
                   fis.close();

                   long kbSize = file.length() / 1024;

                   arquivo.setConteudo(bFile);
                   arquivo.setDataHoraUpload(new Date());
                   arquivo.setTamanhoKB(kbSize);
                   }
                   arquivo.setTipo(tipo);
                   arquivo.setNome(fileName);
                       
                   
         }
        
        private static void EnviarArquivoServidor(){
            try {
                //socket do servidor principal
                Socket socket = new Socket("127.0.0.1", 1111);

                BufferedOutputStream bf = new BufferedOutputStream
                (socket.getOutputStream());

                byte[] bytea = serializarArquivo();
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

        private static byte[] serializarArquivo(){
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
