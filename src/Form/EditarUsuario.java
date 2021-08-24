/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import Dao.ConexaoBd;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Matheus
 */
public class EditarUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public EditarUsuario() {
        initComponents();
        conexao = ConexaoBd.conector();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) getUI();
        ui.setNorthPane(null);
    }

    private void Alterar() {

        String sql = "update usuarios set nome=?,numero=?,email=?,usuario=?,senha=?,perfil=? where ID=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText());
            pst.setString(2, txtnumero.getText());
            pst.setString(3, txtemail.getText());
            pst.setString(4, txtusuario.getText());
            pst.setString(5, txtsenha.getText());
            pst.setString(6, PerfilJ.getSelectedItem().toString());
            pst.setString(7, txtID.getText());

            if (txtnome.getText().isEmpty() || (txtnumero.getText().isEmpty()) || (txtemail.getText().isEmpty()) || (txtusuario.getText().isEmpty()) || (txtsenha.getText().isEmpty()) || (txtID.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Prencha todos os campos para continuar");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    private void PesquisarNome() {

        String sql = "Select *from usuarios where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nomePesq.getText() + "%");
            rs = pst.executeQuery();
            tblEditar.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    private void setar_campos() {
        int setar = tblEditar.getSelectedRow();
        txtID.setText(tblEditar.getModel().getValueAt(setar, 0).toString());
        txtnome.setText(tblEditar.getModel().getValueAt(setar, 1).toString());
        txtnumero.setText(tblEditar.getModel().getValueAt(setar, 2).toString());
        txtemail.setText(tblEditar.getModel().getValueAt(setar, 3).toString());
        txtusuario.setText(tblEditar.getModel().getValueAt(setar, 4).toString());
        txtsenha.setText(tblEditar.getModel().getValueAt(setar, 5).toString());
        PerfilJ.setSelectedItem(tblEditar.getModel().getValueAt(setar, 6).toString());
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
        tblEditar = new br.com.cyber.componente.Ktable();
        nomePesq = new br.com.cyber.componente.KTextField();
        txtnome = new javax.swing.JTextField();
        txtusuario = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        txtnumero = new javax.swing.JTextField();
        txtsenha = new javax.swing.JPasswordField();
        kButton1 = new br.com.cyber.componente.KButton();
        jLabel1 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        PerfilJ = new javax.swing.JComboBox<>();
        mm = new javax.swing.JLabel();
        versenha = new javax.swing.JCheckBox();

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

        tblEditar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEditarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEditar);

        nomePesq.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        nomePesq.setK_placeholder_text("Pesquisar Nome ");
        nomePesq.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        nomePesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomePesqKeyReleased(evt);
            }
        });

        txtnome.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        txtnome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnomeActionPerformed(evt);
            }
        });

        txtusuario.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N

        txtemail.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N

        txtnumero.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        txtnumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnumeroActionPerformed(evt);
            }
        });

        txtsenha.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N

        kButton1.setText("Salvar");
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

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Perfil");

        PerfilJ.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Atendente", "Administrador" }));

        mm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Form/olho_aberto_32.png"))); // NOI18N

        versenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versenhaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(nomePesq, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(versenha)
                                .addGap(9, 9, 9)
                                .addComponent(mm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtsenha, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtnome, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtnumero, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(jLabel1)
                                .addGap(8, 8, 8)
                                .addComponent(PerfilJ, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(PerfilJ, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addComponent(txtnumero, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kGradientPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(versenha)
                    .addComponent(mm, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nomePesq, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nomePesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomePesqKeyReleased
        PesquisarNome();
        //TODO add your handling code here:
    }//GEN-LAST:event_nomePesqKeyReleased

    private void txtnomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomeActionPerformed

    private void txtnumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumeroActionPerformed

    private void tblEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEditarMouseClicked
        setar_campos();        // TODO add your handling code here:
    }//GEN-LAST:event_tblEditarMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        PesquisarNome();
        txtID.setEnabled(false);
        txtID.setVisible(false);
        nomePesq.requestFocusInWindow();
        nomePesq.requestFocus();
// TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameOpened

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        Alterar();
        txtID.setText(null);
        txtnumero.setText(null);
        txtemail.setText(null);
        txtusuario.setText(null);
        txtsenha.setText(null);
        txtnome.setText(null);

        PesquisarNome();

// TODO add your handling code here:
    }//GEN-LAST:event_kButton1ActionPerformed

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

    private void kButton1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton1MouseMoved
        kButton1.setBackground(new Color(10, 113, 218));
        kButton1.setForeground(Color.white);        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1MouseMoved

    private void kButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton1MouseExited
        kButton1.setBackground(new Color(240, 240, 240));
        kButton1.setForeground(Color.black);        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1MouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> PerfilJ;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private br.com.cyber.componente.KButton kButton1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel mm;
    private br.com.cyber.componente.KTextField nomePesq;
    private br.com.cyber.componente.Ktable tblEditar;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtnome;
    private javax.swing.JTextField txtnumero;
    private javax.swing.JPasswordField txtsenha;
    private javax.swing.JTextField txtusuario;
    private javax.swing.JCheckBox versenha;
    // End of variables declaration//GEN-END:variables

}
