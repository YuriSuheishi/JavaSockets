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

         Path path = Paths.get("../Cliente");

        path.register(
          watchService, 
            StandardWatchEventKinds.ENTRY_CREATE, 
              StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if( event.kind().toString() == "ENTRY_DELETE"){
                        System.out.println(event.context() + " deletado");    
                        CriaArquivo(event.context().toString(),0);
                        EnviarArquivoServidor();
                    //1 - Abrir conexão
//                    Socket socket = new Socket("127.0.0.1", 54323);
                    
                    //2 - Definir stream de saída de dados do cliente
//                    ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
//                    Pessoa p = new Pessoa("Rafael Vargas", 38);
//                    saida.writeObject(p); 

                    //3 - Definir stream de entrada de dados no cliente
//                    DataInputStream entrada = new DataInputStream(socket.getInputStream());
//                    String novaMensagem = entrada.readUTF();//Receber mensagem em maiúsculo do servidor
//                    System.out.println(novaMensagem); //Mostrar mensagem em maiúsculo no cliente

                    //4 - Fechar streams de entrada e saída de dados
//                    entrada.close();
//                    saida.close();

                    //5 - Fechar o socket
//                    socket.close();
                    
                }
                else if(event.kind().toString() == "ENTRY_CREATE"){
                        System.out.println(event.context() + " criado");       
                        CriaArquivo(event.context().toString(),1);
                        EnviarArquivoServidor();
//                    try (Socket socket = new Socket("127.0.0.1", 54323)) {
//                        //2 - Definir stream de saída de dados do cliente
//                        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
//                        saida.writeUTF(event.context() + " criado"); //Enviar  mensagem em minúsculo para o servidor
//                        
//                        //3 - Definir stream de entrada de dados no cliente
//                        DataInputStream entrada = new DataInputStream(socket.getInputStream());
//                        String novaMensagem = entrada.readUTF();//Receber mensagem em maiúsculo do servidor
//                        System.out.println(novaMensagem); //Mostrar mensagem em maiúsculo no cliente
//                    }
                    
                    
                    
                }
                else{
                    System.out.println(event.context() + " modificado");     
                    CriaArquivo(event.context().toString(),2);
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
                Socket socket = new Socket("127.0.0.1", 5566);

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
