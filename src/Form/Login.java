/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.awt.Color;
import static javafx.scene.paint.Color.color;
import java.sql.*;
import Dao.ConexaoBd;
import static Form.Painel.jPanel15;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

/**
 *
 * @author Matheus
 */
public class Login extends javax.swing.JFrame {

    // criando variáveis especiais para conexão com o banco
    //Prepared Statement e ResultSet são frameworks do pacote java.sql
    // e servem para preparar e executar as instruções SQL
    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection conexao = null;

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        SetIcon();
        conexao = ConexaoBd.conector();
    }

    public void logar() {
//logica principal para pesquisar no banco de dados
        String sql = "select * from usuarios where usuario=? and senha=?";
        try {
            //as linhas abaixo preparam a consulta em função do que foi 
            //digitado nas caixas de texto. O ? é substituído pelo conteúdo
            //das variáveis que são armazenadas em pst.setString

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtusuario.getText());
            pst.setString(2, txtsenha.getText());
            //a linha abaixo executa a query(consulta)
            rs = pst.executeQuery();
            //se existir um usuário e senha correspondente
            if (rs.next()) {
                //A linha abaixo obtem o conteudo do  campo perfil da tabela tbusuario

                String perfil = rs.getString(7);
                System.out.println(perfil);
                String nome = rs.getString(2);

                if (perfil.equals("Atendente")) {
                    Painel tela = new Painel();
                    tela.setVisible(true);
                    Painel.nome.setText(nome);
                    Painel.chega.setText(nome);
                    jPanel15.setVisible(false);
                    Painel.painelUsuario.setToolTipText("Voçê não tem Permissão para aceder a estas opções");
                    dispose();
                } else {
                    Painel tela = new Painel();
                    tela.setVisible(true);
                    Painel.nome.setText(nome);
                    Painel.chega.setText(nome);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "usuário e/ou senha inválido(s)");
                txtusuario.setText(null);
                txtsenha.setText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        txtusuario = new br.com.cyber.componente.KTextField();
        kButton1 = new br.com.cyber.componente.KButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtsenha = new br.com.cyber.componente.KPasswordField();
        jLabel4 = new javax.swing.JLabel();
        versenha = new javax.swing.JCheckBox();
        mm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DayX-autentição de usuario");
        setResizable(false);

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(7, 101, 197));
        kGradientPanel1.setkStartColor(new java.awt.Color(7, 101, 197));
        kGradientPanel1.setkTransparentControls(false);
        kGradientPanel1.setLayout(null);

        txtusuario.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        txtusuario.setK_placeholder_text("Usuário");
        txtusuario.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        kGradientPanel1.add(txtusuario);
        txtusuario.setBounds(150, 210, 282, 35);

        kButton1.setBackground(new java.awt.Color(7, 101, 197));
        kButton1.setBorder(null);
        kButton1.setForeground(new java.awt.Color(255, 255, 255));
        kButton1.setText("Entrar");
        kButton1.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        kButton1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kButton1MouseMoved(evt);
            }
        });
        kButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kButton1MouseExited(evt);
            }
        });
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton1);
        kButton1.setBounds(150, 310, 255, 34);

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Login");
        kGradientPanel1.add(jLabel2);
        jLabel2.setBounds(220, 120, 115, 57);

        jLabel3.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Esqueci a senha? ");
        jLabel3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel3MouseMoved(evt);
            }
        });
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        kGradientPanel1.add(jLabel3);
        jLabel3.setBounds(230, 350, 105, 18);

        txtsenha.setK_obrigatory(true);
        txtsenha.setK_placeholder_text("senha");
        txtsenha.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        kGradientPanel1.add(txtsenha);
        txtsenha.setBounds(150, 250, 282, 35);

        jLabel4.setFont(new java.awt.Font("Cambria Math", 2, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/amdWhite_64.png"))); // NOI18N
        jLabel4.setText("DayX - Ponto De Venda");
        kGradientPanel1.add(jLabel4);
        jLabel4.setBounds(20, 23, 370, 64);

        versenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versenhaActionPerformed(evt);
            }
        });
        kGradientPanel1.add(versenha);
        versenha.setBounds(440, 260, 21, 21);

        mm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Form/olho_aberto_32.png"))); // NOI18N
        kGradientPanel1.add(mm);
        mm.setBounds(470, 260, 30, 20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(578, 512));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void kButton1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton1MouseMoved
        kButton1.setBackground(Color.white);
        kButton1.setForeground(new Color(7, 101, 197));
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1MouseMoved

    private void kButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton1MouseExited
        kButton1.setBackground(new Color(7, 101, 197));
        kButton1.setForeground(Color.white);
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1MouseExited

    private void jLabel3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseMoved
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));

    }//GEN-LAST:event_jLabel3MouseMoved

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setBorder(null);        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseExited

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        if (txtusuario.getText().isEmpty() || txtsenha.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Prença todos os campos para Entrar!!");
        } else {
            logar();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        Esqueci tela = new Esqueci();
        tela.setVisible(true);
        dispose();
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void versenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versenhaActionPerformed
        if (versenha.isSelected()) {
            txtsenha.setEchoChar((char) 0);
            mm.setIcon(new javax.swing.ImageIcon(getClass().getResource("olho_fechado_32.png")));
        } else {
            txtsenha.setEchoChar('*');
            mm.setIcon(new javax.swing.ImageIcon(getClass().getResource("olho_aberto_32.png")));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_versenhaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private br.com.cyber.componente.KButton kButton1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel mm;
    private br.com.cyber.componente.KPasswordField txtsenha;
    private br.com.cyber.componente.KTextField txtusuario;
    private javax.swing.JCheckBox versenha;
    // End of variables declaration//GEN-END:variables

    private void SetIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    }
}
