package aps_bdsocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ControladorBD {
    public String Controlador(String operacao,String DadosJOP, String DadosUpdate) throws IOException{
        String res = "";
        String diretorio;
        try{
            //########## TXT para saber a ordem de gravação dos servidores ##########
            diretorio = "C:\\OrdemServidor.txt";              
            java.io.File Ordem = new File(diretorio);
            if (!Ordem.exists()){
                FileWriter arq1 = new FileWriter("C:\\OrdemServidor.txt", true);               
                PrintWriter gravarArq = new PrintWriter(arq1);                
                gravarArq.printf("1");
                arq1.close();             
            }
            
            //########## Verifico se os arquivos TXT já existem, se existirem não crio de novo oara não perder os dados ##########
            diretorio = "C:\\Servidor_1.txt";              
            java.io.File file1 = new File(diretorio);
            if (!file1.exists()){
                FileWriter arq1 = new FileWriter("C:\\Servidor_1.txt");
            }
            
            diretorio = "C:\\Servidor_2.txt";              
            java.io.File file2 = new File(diretorio);
            if (!file2.exists()){
                FileWriter arq1 = new FileWriter("C:\\Servidor_2.txt");
            }
            
            diretorio = "C:\\Servidor_3.txt";              
            java.io.File file3 = new File(diretorio);
            if (!file3.exists()){
                FileWriter arq1 = new FileWriter("C:\\Servidor_3.txt");
            }              
            
            if (operacao.equals("insert")){
                FileReader Grav = new FileReader("C:\\OrdemServidor.txt");
                BufferedReader ordemGrav = new BufferedReader(Grav);

                String UltimaGravacao = ordemGrav.readLine(); 

                if (UltimaGravacao.equals("1")){
                    try {
                        //################## Gravo o mesmo Dado no Servidor 1 e 2 ##################
                        res = BancoDeDados1(operacao,DadosJOP,DadosUpdate);
                        res = res + "\n" + BancoDeDados2(operacao,DadosJOP,DadosUpdate);
                        Socket cliente = new Socket("192.168.7.93", 9997); // linha apenas para conectar ao socket e executar a classe
                        //##########################################################################

                        //##########################################################################
                        //Atualizo a ordem da gravação para que o 
                        //sistema saiba qual será o próximo servidor a ser gravado
                        FileWriter arq1 = new FileWriter("C:\\OrdemServidor.txt");               
                        PrintWriter gravarArq = new PrintWriter(arq1);                
                        gravarArq.printf("2");
                        arq1.close();  
                        //##########################################################################
                    } catch (Exception e) {
                        res = "Erro ao Acessar o Servidor 1";
                    }                          
                }else if(UltimaGravacao.equals("2")){
                    //################## Gravo o mesmo Dado no Servidor 2 e 3 ##################
                    res = BancoDeDados2(operacao,DadosJOP,DadosUpdate);
                    res = res + "\n" + BancoDeDados3(operacao,DadosJOP,DadosUpdate);
                    Socket cliente = new Socket("192.168.7.93", 9999); // linha apenas para conectar ao socket e executar a classe
                    //##########################################################################

                    //##########################################################################
                    //Atualizo a ordem da gravação para que o 
                    //sistema saiba qual será o próximo servidor a ser gravado
                    FileWriter arq1 = new FileWriter("C:\\OrdemServidor.txt");               
                    PrintWriter gravarArq = new PrintWriter(arq1);                
                    gravarArq.printf("3");
                    arq1.close();                 
                    //##########################################################################
                }else if(UltimaGravacao.equals("3")){
                    //################## Gravo o mesmo Dado no Servidor 3 e 1 ##################
                    res = BancoDeDados3(operacao,DadosJOP,DadosUpdate);   
                    res = res + "\n" + BancoDeDados1(operacao,DadosJOP,DadosUpdate);
                    Socket cliente = new Socket("192.168.7.93", 9998); // linha apenas para conectar ao socket e executar a classe
                    //##########################################################################

                    //##########################################################################
                    //Atualizo a ordem da gravação para que o 
                    //sistema saiba qual será o próximo servidor a ser gravado
                    FileWriter arq1 = new FileWriter("C:\\OrdemServidor.txt");               
                    PrintWriter gravarArq = new PrintWriter(arq1);                
                    gravarArq.printf("1");
                    arq1.close();   
                    //##########################################################################
                }
            }else{
                res = BancoDeDados1(operacao,DadosJOP,DadosUpdate);
                res = res + "\n" + BancoDeDados2(operacao,DadosJOP,DadosUpdate);
                res = res + "\n" + BancoDeDados3(operacao,DadosJOP,DadosUpdate);
            }
        }catch (Exception e){
            System.out.println("Erro no Controlador do Banco - Verifique!");
        }  
        return res;
    }
//####################################### SERVIDOR 1 ########################################     
    public String BancoDeDados1(String operacao,String DadosJOP, String DadosUpdate){
        String res = "";      
     
        try{
            Socket cliente = new Socket("192.168.7.93", 9999);      

            ObjectInputStream resultado = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream Dados = new ObjectOutputStream(cliente.getOutputStream());         

            switch (operacao) {
                case "select":
                    //Acesso ao servidor 1  
                    Requisicao req = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(req);         
                    Resposta resp = (Resposta) resultado.readObject();
                    res = resp.Msg;
                    break;
                case "delete":
                    //Acesso ao servidor 1  
                    Requisicao reqDelete = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqDelete);         
                    Resposta respDelete = (Resposta) resultado.readObject();
                    res = respDelete.Msg;
                    break;
                case "update":
                    //Acesso ao servidor 1                   
                    Requisicao reqUpdate = new Requisicao(DadosJOP, DadosUpdate, operacao);
                    Dados.writeObject(reqUpdate);         
                    Resposta respUpdate = (Resposta) resultado.readObject();
                    res = respUpdate.Msg;
                    break;
                case "insert":
                    //Acesso ao servidor 1  
                    Requisicao reqInsert = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqInsert);         
                    Resposta respInsert = (Resposta) resultado.readObject();
                    res = respInsert.Msg;
                    break; 
                default:
                    System.out.println("Você digitou uma operação inválida, Verifique!");
                    System.exit(0);
            }
            resultado.close();
            Dados.close();
            cliente.close();
        }catch(Exception e){
            res = "Servidor 1 está fora do ar";
        }
        return res;
    }
//####################################### SERVIDOR 2 ########################################      
     public String BancoDeDados2(String operacao,String DadosJOP, String DadosUpdate){
        String res = "";
                    
        try{
            Socket cliente = new Socket("192.168.7.93", 9998);      

            ObjectInputStream resultado = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream Dados = new ObjectOutputStream(cliente.getOutputStream());         

            switch (operacao) {
                case "select":
                    //Acesso ao servidor 2
                    Requisicao req = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(req);         
                    Resposta resp = (Resposta) resultado.readObject();
                    res = resp.Msg;
                    break;
                case "delete":
                    //Acesso ao servidor 2
                    Requisicao reqDelete = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqDelete);         
                    Resposta respDelete = (Resposta) resultado.readObject();
                    res = respDelete.Msg;
                    break;
                case "update":
                    //Acesso ao servidor 2                   
                    Requisicao reqUpdate = new Requisicao(DadosJOP, DadosUpdate, operacao);
                    Dados.writeObject(reqUpdate);         
                    Resposta respUpdate = (Resposta) resultado.readObject();
                    res = respUpdate.Msg;
                    break;
                case "insert":
                    //Acesso ao servidor 2
                    Requisicao reqInsert = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqInsert);         
                    Resposta respInsert = (Resposta) resultado.readObject();
                    res = respInsert.Msg;
                    break; 
                default:
                    System.out.println("Você digitou uma operação inválida, Verifique!");
                    System.exit(0);
            }
            resultado.close();
            Dados.close();
            cliente.close();
        }catch(Exception e){
            res = "Servidor 2 está fora do ar";
        }
        return res;
    }
//####################################### SERVIDOR 3 ########################################          
      public String BancoDeDados3(String operacao,String DadosJOP, String DadosUpdate){
        String res = "";
                    
        try{
            Socket cliente = new Socket("192.168.7.93", 9997);      

            ObjectInputStream resultado = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream Dados = new ObjectOutputStream(cliente.getOutputStream());         

            switch (operacao) {
                case "select":
                    //Acesso ao servidor 3
                    Requisicao req = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(req);         
                    Resposta resp = (Resposta) resultado.readObject();
                    res = resp.Msg;
                    break;
                case "delete":
                    //Acesso ao servidor 3
                    Requisicao reqDelete = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqDelete);         
                    Resposta respDelete = (Resposta) resultado.readObject();
                    res = respDelete.Msg;
                    break;
                case "update":
                    //Acesso ao servidor 3               
                    Requisicao reqUpdate = new Requisicao(DadosJOP, DadosUpdate, operacao);
                    Dados.writeObject(reqUpdate);         
                    Resposta respUpdate = (Resposta) resultado.readObject();
                    res = respUpdate.Msg;
                    break;
                case "insert":
                    //Acesso ao servidor 3
                    Requisicao reqInsert = new Requisicao(DadosJOP, "", operacao);
                    Dados.writeObject(reqInsert);         
                    Resposta respInsert = (Resposta) resultado.readObject();
                    res = respInsert.Msg;
                    break; 
                default:
                    System.out.println("Você digitou uma operação inválida, Verifique!");
                    System.exit(0);
            }
            resultado.close();
            Dados.close();
            cliente.close();
        }catch(Exception e){
            res = "Servidor 3 está fora do ar";
        }
        return res;
    } 
}      
