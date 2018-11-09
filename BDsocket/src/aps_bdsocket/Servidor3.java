package aps_bdsocket; 

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor3 {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        new Servidor3().Executa();
    }
    
    public void Executa() {               
        try {
            ServerSocket servidor = new ServerSocket(9997);
            System.out.println("Porta 9997 aberta! - Servidor 3");
            
            System.out.println("===== Esperando o cliente conectar =====");        
            Socket cliente = servidor.accept();            
            
            System.out.println("Conectado com o cliente: " + cliente.getInetAddress().getHostAddress());

            ObjectOutputStream resultado = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream dados = new ObjectInputStream(cliente.getInputStream());

            Requisicao req = (Requisicao) dados.readObject();

            Resposta res = RecRequisicao(req);    

            resultado.writeObject(res);            
            resultado.flush();

            resultado.close();
            dados.close();
            servidor.close();            

        } catch (Exception e) {
            System.out.println("Não consegui Acessar...");
        }      
    }
    
    private Resposta RecRequisicao(Requisicao req) throws FileNotFoundException, IOException {
        Resposta res = new Resposta();                       
                
        String linha = "";
        int cont = 1;
        String DadoBusca, DadoDelete,DadoAntigo,DadoNovo, Mensa;
        
        switch (req.getOp()) {
            case "select":
                FileReader arq = new FileReader("C:\\Servidor_3.txt");
                BufferedReader lerArq = new BufferedReader(arq);
    
                DadoBusca = req.getText1().toLowerCase();
                
                while (linha != null) {
                    linha = lerArq.readLine();
                    
                    if(linha != null){
                        if (!linha.toLowerCase().equals(DadoBusca)){
                            linha = "";
                            cont++;
                        }else{
                            break;
                        }
                    }
                }        
                arq.close();
                
                if(linha != null){
                    cont = cont + 1;
                    res.setTotal(linha);

                    res.setMsg("Registro: " + linha + " - Encontrado na Linha " + cont + " Do servidor 3");
                }else{
                    res.setMsg("Registro não encontrado no servidor 3");
                }
                break;                     
            case "insert":
                FileWriter arqInsert = new FileWriter("C:\\Servidor_3.txt", true);
                PrintWriter gravarArq = new PrintWriter(arqInsert);                
                gravarArq.printf(req.getText1().toUpperCase() + "\r\n");
                
                arqInsert.close();
                
                res.setMsg("Registro incluido com sucesso no servidor 3");
                break;
            case "delete":
                
                FileReader arqDelete = new FileReader("C:\\Servidor_3.txt");
                BufferedReader lerArqDelete = new BufferedReader(arqDelete);
                
                DadoDelete = req.getText1().toUpperCase();
                linha = lerArqDelete.readLine(); 
                ArrayList<String> salvar = new ArrayList();
                
                Mensa = "Registro não encontrado!";
                
                while (linha != null) {
                    if (!linha.equals(DadoDelete)){
                        salvar.add(linha);                        
                    }else{
                        Mensa = "Registro Deletado com sucesso no servidor 3";
                    }    
                    linha = lerArqDelete.readLine(); 
                }
                
                FileWriter arqDel = new FileWriter("C:\\Servidor_3.txt");
                PrintWriter DeleteArq = new PrintWriter(arqDel); 
                
                for (int i = 0; i < salvar.size(); i++) {
                    DeleteArq.printf(salvar.get(i).toUpperCase() + "\r\n");                    
                }
                
                arqDel.close();
                res.setMsg(Mensa);
                break;
            case "update":                              
                FileReader arqUpdate = new FileReader("C:\\Servidor_3.txt");
                BufferedReader lerArqUpdate = new BufferedReader(arqUpdate);
                
                DadoAntigo = req.getText1().toUpperCase();
                DadoNovo = req.getText2().toUpperCase();
                linha = lerArqUpdate.readLine(); 
                ArrayList<String> salvarUpdate = new ArrayList();
                
                Mensa = "Registro não encontrado!";
               
                while (linha != null) {
                    if (!linha.equals(DadoAntigo)){
                        salvarUpdate.add(linha);
                    }else{
                        salvarUpdate.add(DadoNovo);
                        Mensa = "Registro Atualizado com sucesso no servidor 3";
                    }     
                    linha = lerArqUpdate.readLine(); 
                }
                
                FileWriter arqUpd = new FileWriter("C:\\Servidor_3.txt");
                PrintWriter UpdateArq = new PrintWriter(arqUpd); 
                
                for (int i = 0; i < salvarUpdate.size(); i++) {
                    UpdateArq.printf(salvarUpdate.get(i).toUpperCase() + "\r\n");                    
                }
                
                arqUpd.close();
                res.setMsg(Mensa);                
                break;
            default: 
                linha = "Passei no DEFAULT";
        }        
        return res;
    }
}