/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Matheus
 */

public class ConexaoBd {
    public static Connection conector(){
        //criando uma variável especial para estabelecer uma conexão com o banco
        java.sql.Connection conexao = null;
        //carregando o driver correspondente ao banco (não esqueça de importar ele em libraries
        String driver = "com.mysql.jdbc.Driver";
        //armazenando informações referente ao banco de dados
        String url = "jdbc:mysql://localhost:3306/ponto";
        String user = "root";
        String password = "";
//Estabeleçendo a conexao com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("Conectou");
            return conexao;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO:" + e.getMessage());
            return null;
        }
    }
}
