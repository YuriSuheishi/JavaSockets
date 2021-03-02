/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.watcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;



/**
 *
 * @author yuris
 */
public class Cliente {
    
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
                    try (Socket socket = new Socket("127.0.0.1", 54323)) {
                        //2 - Definir stream de saída de dados do cliente
                        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                        saida.writeUTF(event.context() + " criado"); //Enviar  mensagem em minúsculo para o servidor
                        
                        //3 - Definir stream de entrada de dados no cliente
                        DataInputStream entrada = new DataInputStream(socket.getInputStream());
                        String novaMensagem = entrada.readUTF();//Receber mensagem em maiúsculo do servidor
                        System.out.println(novaMensagem); //Mostrar mensagem em maiúsculo no cliente
                    }
                    
                    
                    
                }
                else{
                    System.out.println(event.context() + " modificado");                        
                }
            }
            key.reset();
        }
        }
}
