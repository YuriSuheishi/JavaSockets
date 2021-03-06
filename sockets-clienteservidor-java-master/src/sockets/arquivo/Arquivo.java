/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.arquivo;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author yuris
 */
public class Arquivo implements Serializable {

   /**
    *
    */

   private String nome;
   private byte[] conteudo;
   private transient long tamanhoKB;
   private transient Date dataHoraUpload;
   private int tipo;
   
   public int getTipo(){
       return tipo;
   }
   public void setTipo(int tipo){
       this.tipo = tipo;
   }
   public String getNome() {
             return nome;
   }
   public void setNome(String nome) {
             this.nome = nome;
   }
   public byte[] getConteudo() {
             return conteudo;
   }
   public void setConteudo(byte[] conteudo) {
             this.conteudo = conteudo;
   }
   public long getTamanhoKB() {
             return tamanhoKB;
   }
   public void setTamanhoKB(long tamanhoKB) {
             this.tamanhoKB = tamanhoKB;
   }
   public Date getDataHoraUpload() {
             return dataHoraUpload;
   }
   public void setDataHoraUpload(Date dataHoraUpload) {
             this.dataHoraUpload = dataHoraUpload;
   }
}
