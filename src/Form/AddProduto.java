/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.sql.*;
import Dao.ConexaoBd;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Matheus
 */
public class AddProduto extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public AddProduto() {
        initComponents();
        conexao = ConexaoBd.conector();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
    }
    
    public void cadastrar() {
        
        String sql = "insert into produtos (nome,preço,descrição) Values(?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText());
            pst.setString(2, txtpreco.getText());
            pst.setString(3, txtdescricao.getText());
            if (txtnome.getText().isEmpty() || (txtpreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Prencha todos os campos para continuar");
            } else {
                int adicionar = pst.executeUpdate();
                if (adicionar > 0) {
                    JOptionPane.showMessageDialog(null, txtnome.getText() + " Foi adicionado com sucesso");
                    txtnome.setText(null);
                    txtpreco.setText(null);
                    txtdescricao.setText(null);
                }
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
        txtpreco = new br.com.cyber.componente.KTextField();
        txtnome = new br.com.cyber.componente.KTextField();
        txtdescricao = new br.com.cyber.componente.KTextField();
        kButton1 = new br.com.cyber.componente.KButton();

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(0, 179, 225));
        kGradientPanel1.setkGradientFocus(900);
        kGradientPanel1.setkStartColor(new java.awt.Color(10, 113, 218));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtpreco.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtpreco.setK_obrigatory(true);
        txtpreco.setK_placeholder_text("Preço");
        txtpreco.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        kGradientPanel1.add(txtpreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, 541, -1));

        txtnome.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtnome.setK_obrigatory(true);
        txtnome.setK_placeholder_text("Nome ");
        txtnome.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        kGradientPanel1.add(txtnome, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 541, -1));

        txtdescricao.setFont(new java.awt.Font("Malgun Gothic", 0, 18)); // NOI18N
        txtdescricao.setK_placeholder_text("Descrição");
        txtdescricao.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        kGradientPanel1.add(txtdescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 541, 110));

        kButton1.setText("Salvar");
        kButton1.setToolTipText("Adicionar novo usuário");
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 420, 242, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1015, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1015, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        cadastrar();        // TODO add your handling code here:
    }//GEN-LAST:event_kButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private br.com.cyber.componente.KButton kButton1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private br.com.cyber.componente.KTextField txtdescricao;
    private br.com.cyber.componente.KTextField txtnome;
    private br.com.cyber.componente.KTextField txtpreco;
    // End of variables declaration//GEN-END:variables
}
