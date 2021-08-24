/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.sql.*;
import Dao.ConexaoBd;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Matheus
 */
public class PesquiUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public PesquiUsuario(){
        initComponents();
        conexao = ConexaoBd.conector();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();
        ui.setNorthPane(null);
    }

    public void PesquisarNome() {

        String sql = "select *from usuarios where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText() + "%");

            rs = pst.executeQuery();
            tblPes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }

    }

    public void PesquisarNumero() {

        String sql = "select *from usuarios where numero like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnumero.getText() + "%");
            rs = pst.executeQuery();
            tblPes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    public void PesquisarEmail() {

        String sql = "select *from usuarios where email like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtemail.getText() + "%");
            rs = pst.executeQuery();
            tblPes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }
public void PesquisarUsuario(){

    String sql= "select *from usuarios where usuario like?";
    try {
        pst = conexao.prepareStatement(sql);
        pst.setString(1,txtusuario.getText()+"%");
        rs = pst.executeQuery();
        tblPes.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"ERRO: "+e.getMessage());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPes = new br.com.cyber.componente.Ktable();
        txtnome = new br.com.cyber.componente.KTextField();
        txtnumero = new br.com.cyber.componente.KTextField();
        txtemail = new br.com.cyber.componente.KTextField();
        txtusuario = new br.com.cyber.componente.KTextField();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 179, 225));
        kGradientPanel1.setkStartColor(new java.awt.Color(10, 113, 208));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblPes);

        kGradientPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 264, 980, 320));

        txtnome.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtnome.setK_placeholder_text("Pesquisar Nome ");
        txtnome.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        txtnome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtnomeKeyReleased(evt);
            }
        });
        kGradientPanel1.add(txtnome, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 28, 737, -1));

        txtnumero.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtnumero.setK_placeholder_text("Pesquisar Numero");
        txtnumero.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        txtnumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtnumeroKeyReleased(evt);
            }
        });
        kGradientPanel1.add(txtnumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 89, 708, -1));

        txtemail.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtemail.setK_placeholder_text("Pesquisar Email");
        txtemail.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        txtemail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtemailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtemailKeyReleased(evt);
            }
        });
        kGradientPanel1.add(txtemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 150, 767, -1));

        txtusuario.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtusuario.setK_placeholder_text("Pesquisar Usuario");
        txtusuario.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        txtusuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtusuarioKeyReleased(evt);
            }
        });
        kGradientPanel1.add(txtusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 211, 800, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnomeKeyReleased
        PesquisarNome();
        //TODO add your handling code here:
    }//GEN-LAST:event_txtnomeKeyReleased

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        PesquisarNome();        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtnumeroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnumeroKeyReleased
        PesquisarNumero();        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumeroKeyReleased

    private void txtemailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailKeyPressed

    private void txtemailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtemailKeyReleased
    PesquisarEmail();        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailKeyReleased

    private void txtusuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtusuarioKeyReleased
   PesquisarUsuario();        // TODO add your handling code here:
    }//GEN-LAST:event_txtusuarioKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private br.com.cyber.componente.Ktable tblPes;
    private br.com.cyber.componente.KTextField txtemail;
    private br.com.cyber.componente.KTextField txtnome;
    private br.com.cyber.componente.KTextField txtnumero;
    private br.com.cyber.componente.KTextField txtusuario;
    // End of variables declaration//GEN-END:variables
}
