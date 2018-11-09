package aps_bdsocket;

import java.io.IOException;
import javax.swing.JOptionPane;

public class Cliente {

    public static void main(String[] args) throws IOException {
        String operacao,DadosJOP,DadosUpdate;
        DadosUpdate = "";
        operacao = JOptionPane.showInputDialog("Qual operação desejada? \n - Select \n - Update \n - Delete \n - Insert").toLowerCase();  
        
        DadosJOP = JOptionPane.showInputDialog("Digite o Dado que deseja fazer o " + operacao + " - (Não diferencio Maiusculas e Minusculas)").toLowerCase();  
        
        if (operacao.equals("update")){
            DadosUpdate = JOptionPane.showInputDialog("Digite o Dado que deseja fazer o update pelo " + DadosJOP + " - (Não diferencio Maiusculas e Minusculas)").toLowerCase();        
        }
        ControladorBD ContBD = new ControladorBD();                   
                    
        System.out.println(ContBD.Controlador(operacao,DadosJOP, DadosUpdate));                                  
   
    }
    
}