/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import Dao.ConexaoBd;
import jdk.Exported;
import jdk.nashorn.internal.objects.NativeString;
import static jdk.nashorn.internal.objects.NativeString.substring;
import net.proteanit.sql.DbUtils;
import java.lang.RuntimeException;

/**
 *
 * @author Matheus
 */
public class Painel extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Painel() {
        initComponents();
        conexao = ConexaoBd.conector();
        atualizaHoras();
//setExtendedState(MAXIMIZED_BOTH);
        setIcon();
    }

    Timer timer;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");

    public void PesquisarCaixa() {

        String sql = "select *from caixa";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblCaixa.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {

        }
    }

    public void PesquisarCaixaPorData() {
        String sql = "select *from caixa where Data_ like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisarDataCaixa.getText() + "%");
            rs = pst.executeQuery();
            tblCaixa.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    public String justificar(String data) {
        return data.substring(0, 1) + "/" + data.substring(3, 4) + "/" + data.substring(7, 11);
    }

    public void atualizaHoras() {
        timer = new Timer(1000, (ActionEvent e) -> {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000);
//            System.out.println(hora.format(calendar.getTime()));
            HORRA.setText(hora.format(calendar.getTime()));
        });
        timer.start();
    }

    public void ADDCAIXA() {

        String sql = "INSERT INTO caixa(AtendidoPor,ToTalCompra, valorPago, TrocoDevolvido, Data_, Hora) VALUES (?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, chega.getText());
            pst.setString(2, totalCompra.getText());
            pst.setString(3, receber.getText());
            pst.setString(4, txtTroco.getText());
            pst.setString(5, DATTA.getText());
            pst.setString(6, HORRA.getText());
            int adiconado = pst.executeUpdate();
            if (adiconado > 0) {
                JOptionPane.showMessageDialog(null, "Compra na caixa");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }

    }

    public void FazerConta() {
        //txtTroco.setText(String.valueOf(Double.parseDouble(totalCompra.getText())- Double.parseDouble(receber.getText())));
        //if (receber.getText().equals("0")) {
        //    txtTroco.setText("000");
        // }
        int resultado;
        int pago = Integer.parseInt(receber.getText());
        int TotalCompraa = Integer.parseInt(totalCompra.getText());

        if (TotalCompraa > pago) {
            resultado = TotalCompraa - pago;
            JOptionPane.showMessageDialog(null, "Falta " + resultado + " Kzs para Realizar a compra");

        } else {
            btnRealizar.setEnabled(true);
            resultado = TotalCompraa - pago;
            if (resultado < 1) {
                resultado = resultado * -1;
            }
            txtTroco.setText(String.valueOf(resultado));
        }
    }

    public void SaberQuantidadeDeProduto() {

    }

    public void SetaID() {

        int setar = tblCompras.getSelectedRow();
        setarID.setText(tblCompras.getModel().getValueAt(setar, 0).toString());

    }

    public void CalcularRendimentos() {
        int ResultadoFinal, soma = 0, valor, menos, MenosSoma = 0, ValorMenos;
        int contador = tblCaixa.getRowCount();
        int contador2 = tblCaixa.getRowCount();
        for (int i = 0; i < contador; i++) {
            valor = (int) tblCaixa.getValueAt(i, 3);
            soma += valor;
        }
        for (int t = 0; t < contador2; t++) {
            ValorMenos = (int) tblCaixa.getValueAt(t, 4);
            MenosSoma += ValorMenos;
        }
        ResultadoFinal = soma - MenosSoma;

        valorNoCaixa.setText(String.valueOf(ResultadoFinal));
    }

    private void ContaProduto() {
        int soma = 0;
        int valor;
        int contador = tblCompras.getRowCount();
        for (int i = 0; i < contador; i++) {
            valor = (int) tblCompras.getValueAt(i, 4);
            soma = soma + valor;
        }
        totalCompra.setText(String.valueOf(soma));
    }

    public void ApagaProduto() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Retirar este produto da tabela? ", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "Delete from compras where Codico=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, setarID.getText());
                int apagar = pst.executeUpdate();
                if (apagar > 0) {
                    JOptionPane.showMessageDialog(null, "Produto Retirado");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
                PesquisarCompras();
            }
        }
    }

    public void CancelaString() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Cancelar Esta Compra? ", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "Truncate compras";
            try {
                pst = conexao.prepareStatement(sql);
                pst.executeUpdate();
                receber.setText(null);
                txtCodico.setText(null);
                txtPreco.setText(null);
                txtnome.setText(null);
                txtTroco.setText(null);
                totalCompra.setText("000");
                txtPesquisarProduto.setText(null);
                PesquisarCompras();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());

            }
        }
    }

    public void RealizarCompra() {
        String sql = "Truncate compras";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Compra Realizada");
            receber.setText(null);
            txtTroco.setText("00");
            totalCompra.setText(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    public void AddProdutoInTable() {

        String sql = "INSERT INTO compras(nome, Preço, Quantidade) VALUES (?,?,?)";
        try {
            String qtd = JOptionPane.showInputDialog("Digite a quatidade do Produto a ser Comprada");
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtnome.getText());
            pst.setString(2, txtPreco.getText());
            pst.setString(3, qtd);
            if (txtnome.getText().isEmpty() || (txtPreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Clique em um Produto");
            } else if (qtd.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantidade do  " + txtnome.getText() + " não pode Estar Vazia");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, txtnome.getText() + " Foi Adicionado na sacola");
                    txtnome.setText(null);
                    txtnome.setText(null);
                    txtCodico.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }

    }

    public void PesquisarProduto() {
        String sql = "select *from produtos where nome like?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisarProduto.getText() + "%");
            rs = pst.executeQuery();
            tblProdutos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    public void PesquisarCompras() {
        String sql = "SELECT * FROM compras order by Codico desc";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblCompras.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e.getMessage());
        }
    }

    public void setar() {
        int setar = tblProdutos.getSelectedRow();
        txtCodico.setText(tblProdutos.getModel().getValueAt(setar, 0).toString());
        txtnome.setText(tblProdutos.getModel().getValueAt(setar, 1).toString());
        txtPreco.setText(tblProdutos.getModel().getValueAt(setar, 2).toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        componentResizerUtil1 = new com.k33ptoo.utils.ComponentResizerUtil();
        kGradientPanel14 = new com.k33ptoo.components.KGradientPanel();
        tabbed = new javax.swing.JTabbedPane();
        Usuario = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        Add = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Editar = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        Pesquisar = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        Deletar = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        Visu = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        nome = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        painelUsuario = new javax.swing.JDesktopPane();
        Produto = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        Add1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        Editar1 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        Pesquisar1 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        Deletar1 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        Visu1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        Produtos = new javax.swing.JLabel();
        PainelProdutos = new javax.swing.JDesktopPane();
        Caixa = new javax.swing.JPanel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCaixa = new br.com.cyber.componente.Ktable();
        valorNoCaixa = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jjj = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        txtPesquisarDataCaixa = new br.com.cyber.componente.KTextField();
        Vendas = new javax.swing.JPanel();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        btnRealizar = new br.com.cyber.componente.KButton();
        kButton3 = new br.com.cyber.componente.KButton();
        kButton5 = new br.com.cyber.componente.KButton();
        kButton6 = new br.com.cyber.componente.KButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCompras = new br.com.cyber.componente.Ktable();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProdutos = new br.com.cyber.componente.Ktable();
        txtPesquisarProduto = new br.com.cyber.componente.KTextField();
        kButton2 = new br.com.cyber.componente.KButton();
        totalCompra = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPreco = new javax.swing.JTextField();
        txtnome = new javax.swing.JTextField();
        txtCodico = new javax.swing.JTextField();
        setarID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        receber = new numeric.textField.NumericTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTroco = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Info = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        chega = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        HORRA = new javax.swing.JLabel();
        DATTA = new javax.swing.JLabel();
        status = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DayX - ponto de vendas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        kGradientPanel14.setkBorderRadius(0);
        kGradientPanel14.setkEndColor(new java.awt.Color(251, 184, 2));
        kGradientPanel14.setkStartColor(new java.awt.Color(255, 102, 0));
        kGradientPanel14.setLayout(null);

        tabbed.setBackground(new java.awt.Color(10, 113, 218));
        tabbed.setForeground(new java.awt.Color(10, 113, 218));
        tabbed.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tabbed.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tabbedMouseDragged(evt);
            }
        });

        jPanel15.setBackground(new java.awt.Color(10, 113, 218));

        Add.setBackground(new java.awt.Color(10, 113, 218));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/add_32.png"))); // NOI18N
        jLabel1.setText("Adicionar");
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel1MouseMoved(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout AddLayout = new javax.swing.GroupLayout(Add);
        Add.setLayout(AddLayout);
        AddLayout.setHorizontalGroup(
            AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        AddLayout.setVerticalGroup(
            AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        Editar.setBackground(new java.awt.Color(10, 113, 218));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/edit_32.png"))); // NOI18N
        jLabel16.setText("Editar");
        jLabel16.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel16MouseMoved(evt);
            }
        });
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });

        javax.swing.GroupLayout EditarLayout = new javax.swing.GroupLayout(Editar);
        Editar.setLayout(EditarLayout);
        EditarLayout.setHorizontalGroup(
            EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        EditarLayout.setVerticalGroup(
            EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(EditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        );

        Pesquisar.setBackground(new java.awt.Color(10, 113, 218));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/pesquisar_32.png"))); // NOI18N
        jLabel18.setText("Pesquisar");
        jLabel18.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel18MouseMoved(evt);
            }
        });
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel18MouseExited(evt);
            }
        });

        javax.swing.GroupLayout PesquisarLayout = new javax.swing.GroupLayout(Pesquisar);
        Pesquisar.setLayout(PesquisarLayout);
        PesquisarLayout.setHorizontalGroup(
            PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        PesquisarLayout.setVerticalGroup(
            PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(PesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        );

        Deletar.setBackground(new java.awt.Color(10, 113, 218));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/delete_32.png"))); // NOI18N
        jLabel19.setText("Apagar");
        jLabel19.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel19MouseMoved(evt);
            }
        });
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel19MouseExited(evt);
            }
        });

        javax.swing.GroupLayout DeletarLayout = new javax.swing.GroupLayout(Deletar);
        Deletar.setLayout(DeletarLayout);
        DeletarLayout.setHorizontalGroup(
            DeletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
            .addGroup(DeletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        DeletarLayout.setVerticalGroup(
            DeletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(DeletarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
        );

        Visu.setBackground(new java.awt.Color(10, 113, 218));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/gerencia_32.png"))); // NOI18N
        jLabel20.setText("Visualizar");
        jLabel20.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel20MouseMoved(evt);
            }
        });
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel20MouseExited(evt);
            }
        });

        javax.swing.GroupLayout VisuLayout = new javax.swing.GroupLayout(Visu);
        Visu.setLayout(VisuLayout);
        VisuLayout.setHorizontalGroup(
            VisuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
            .addGroup(VisuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        VisuLayout.setVerticalGroup(
            VisuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(VisuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
        );

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/User_64_64.png"))); // NOI18N

        nome.setFont(new java.awt.Font("Cambria Math", 0, 15)); // NOI18N
        nome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nome.setText("jLabel18");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Pesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Add, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Editar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(Deletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Visu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nome)
                .addGap(57, 57, 57)
                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Editar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Deletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Visu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painelUsuario.setBackground(new java.awt.Color(10, 113, 218));

        javax.swing.GroupLayout painelUsuarioLayout = new javax.swing.GroupLayout(painelUsuario);
        painelUsuario.setLayout(painelUsuarioLayout);
        painelUsuarioLayout.setHorizontalGroup(
            painelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1019, Short.MAX_VALUE)
        );
        painelUsuarioLayout.setVerticalGroup(
            painelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(painelUsuario);

        javax.swing.GroupLayout UsuarioLayout = new javax.swing.GroupLayout(Usuario);
        Usuario.setLayout(UsuarioLayout);
        UsuarioLayout.setHorizontalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsuarioLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2))
        );
        UsuarioLayout.setVerticalGroup(
            UsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tabbed.addTab("Usuario", Usuario);

        jPanel16.setBackground(new java.awt.Color(10, 113, 218));

        Add1.setBackground(new java.awt.Color(10, 113, 218));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/add_produtos_32.png"))); // NOI18N
        jLabel21.setText("Adicionar");
        jLabel21.setToolTipText("Adicionar produto");
        jLabel21.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel21MouseMoved(evt);
            }
        });
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel21MouseExited(evt);
            }
        });

        javax.swing.GroupLayout Add1Layout = new javax.swing.GroupLayout(Add1);
        Add1.setLayout(Add1Layout);
        Add1Layout.setHorizontalGroup(
            Add1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        Add1Layout.setVerticalGroup(
            Add1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        Editar1.setBackground(new java.awt.Color(10, 113, 218));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/editar_32.png"))); // NOI18N
        jLabel22.setText("Editar");
        jLabel22.setToolTipText("Editar Produto");
        jLabel22.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel22MouseMoved(evt);
            }
        });
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel22MouseExited(evt);
            }
        });

        javax.swing.GroupLayout Editar1Layout = new javax.swing.GroupLayout(Editar1);
        Editar1.setLayout(Editar1Layout);
        Editar1Layout.setHorizontalGroup(
            Editar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(Editar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        Editar1Layout.setVerticalGroup(
            Editar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(Editar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        );

        Pesquisar1.setBackground(new java.awt.Color(10, 113, 218));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/lupa.png"))); // NOI18N
        jLabel23.setText("Pesquisar");
        jLabel23.setToolTipText("Pesquisar Produto");
        jLabel23.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel23MouseMoved(evt);
            }
        });
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel23MouseExited(evt);
            }
        });

        javax.swing.GroupLayout Pesquisar1Layout = new javax.swing.GroupLayout(Pesquisar1);
        Pesquisar1.setLayout(Pesquisar1Layout);
        Pesquisar1Layout.setHorizontalGroup(
            Pesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(Pesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        Pesquisar1Layout.setVerticalGroup(
            Pesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(Pesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pesquisar1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        Deletar1.setBackground(new java.awt.Color(10, 113, 218));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/deletar.png"))); // NOI18N
        jLabel24.setText("Apagar");
        jLabel24.setToolTipText("Apagar Produto");
        jLabel24.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel24MouseMoved(evt);
            }
        });
        jLabel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel24MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel24MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel24MouseExited(evt);
            }
        });

        javax.swing.GroupLayout Deletar1Layout = new javax.swing.GroupLayout(Deletar1);
        Deletar1.setLayout(Deletar1Layout);
        Deletar1Layout.setHorizontalGroup(
            Deletar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
            .addGroup(Deletar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        Deletar1Layout.setVerticalGroup(
            Deletar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
            .addGroup(Deletar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE))
        );

        Visu1.setBackground(new java.awt.Color(10, 113, 218));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/produto_32.png"))); // NOI18N
        jLabel25.setText("Visualizar");
        jLabel25.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel25MouseMoved(evt);
            }
        });
        jLabel25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel25MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel25MouseExited(evt);
            }
        });

        javax.swing.GroupLayout Visu1Layout = new javax.swing.GroupLayout(Visu1);
        Visu1.setLayout(Visu1Layout);
        Visu1Layout.setHorizontalGroup(
            Visu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 191, Short.MAX_VALUE)
            .addGroup(Visu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
        Visu1Layout.setVerticalGroup(
            Visu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(Visu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
        );

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/produto_64.png"))); // NOI18N

        Produtos.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        Produtos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Produtos.setText("Produtos");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Produtos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(Pesquisar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Add1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Editar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(Deletar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Visu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produtos)
                .addGap(57, 57, 57)
                .addComponent(Add1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Editar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Pesquisar1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Deletar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Visu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        PainelProdutos.setBackground(new java.awt.Color(10, 113, 218));

        javax.swing.GroupLayout PainelProdutosLayout = new javax.swing.GroupLayout(PainelProdutos);
        PainelProdutos.setLayout(PainelProdutosLayout);
        PainelProdutosLayout.setHorizontalGroup(
            PainelProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1024, Short.MAX_VALUE)
        );
        PainelProdutosLayout.setVerticalGroup(
            PainelProdutosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ProdutoLayout = new javax.swing.GroupLayout(Produto);
        Produto.setLayout(ProdutoLayout);
        ProdutoLayout.setHorizontalGroup(
            ProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProdutoLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(PainelProdutos))
        );
        ProdutoLayout.setVerticalGroup(
            ProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PainelProdutos)
        );

        tabbed.addTab("Produtos", Produto);

        kGradientPanel2.setkEndColor(new java.awt.Color(0, 142, 214));
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 0, 255));

        tblCaixa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblCaixa);

        valorNoCaixa.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        valorNoCaixa.setForeground(new java.awt.Color(255, 255, 255));
        valorNoCaixa.setText("jLabel2");

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TOTAL CAIXA(KZ)");

        jjj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jjjKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Pesquisar por Data");

        txtPesquisarDataCaixa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarDataCaixaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jjj, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(txtPesquisarDataCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 624, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(valorNoCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisarDataCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(valorNoCaixa)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jjj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout CaixaLayout = new javax.swing.GroupLayout(Caixa);
        Caixa.setLayout(CaixaLayout);
        CaixaLayout.setHorizontalGroup(
            CaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CaixaLayout.setVerticalGroup(
            CaixaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbed.addTab("Caixa", Caixa);

        kGradientPanel1.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkStartColor(new java.awt.Color(0, 179, 225));
        kGradientPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                kGradientPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kGradientPanel1MouseMoved(evt);
            }
        });
        kGradientPanel1.setLayout(null);

        btnRealizar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRealizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/get_cash_32.png"))); // NOI18N
        btnRealizar.setText("Realizar Venda");
        btnRealizar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnRealizarMouseMoved(evt);
            }
        });
        btnRealizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRealizarMouseExited(evt);
            }
        });
        btnRealizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarActionPerformed(evt);
            }
        });
        kGradientPanel1.add(btnRealizar);
        btnRealizar.setBounds(980, 70, 227, 34);

        kButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/cancel_2_32.png"))); // NOI18N
        kButton3.setText("Cancelar Venda");
        kButton3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kButton3MouseMoved(evt);
            }
        });
        kButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kButton3MouseExited(evt);
            }
        });
        kButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton3ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton3);
        kButton3.setBounds(980, 120, 227, 34);

        kButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/icons8_delete_32.png"))); // NOI18N
        kButton5.setText("Eliminar ");
        kButton5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kButton5MouseMoved(evt);
            }
        });
        kButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kButton5MouseExited(evt);
            }
        });
        kButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton5ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton5);
        kButton5.setBounds(980, 220, 227, 34);

        kButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/calculator_32.png"))); // NOI18N
        kButton6.setText("Realizar Calculo");
        kButton6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kButton6MouseMoved(evt);
            }
        });
        kButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kButton6MouseExited(evt);
            }
        });
        kButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton6ActionPerformed(evt);
            }
        });
        kGradientPanel1.add(kButton6);
        kButton6.setBounds(980, 170, 227, 34);

        tblCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblComprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCompras);

        kGradientPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 330, 1200, 220);

        jInternalFrame1.setTitle("Lista De produtos");
        jInternalFrame1.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Form/iconBlack.png"))); // NOI18N
        jInternalFrame1.setVisible(true);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdutosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProdutos);

        txtPesquisarProduto.setToolTipText("Pesquisar Produtos em stock para Vender");
        txtPesquisarProduto.setK_placeholder_text("Pesquisar nome de um produto");
        txtPesquisarProduto.setK_placeholder_text_color(new java.awt.Color(51, 51, 51));
        txtPesquisarProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarProdutoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisarProdutoKeyTyped(evt);
            }
        });

        kButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/ADD_Produto_32.png"))); // NOI18N
        kButton2.setText("Adicionar Produto");
        kButton2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                kButton2MouseMoved(evt);
            }
        });
        kButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                kButton2MouseExited(evt);
            }
        });
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtPesquisarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesquisarProduto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        kGradientPanel1.add(jInternalFrame1);
        jInternalFrame1.setBounds(20, 10, 478, 310);

        totalCompra.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        totalCompra.setForeground(new java.awt.Color(255, 255, 255));
        totalCompra.setText("000");
        kGradientPanel1.add(totalCompra);
        totalCompra.setBounds(780, 220, 90, 20);

        jLabel6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Valor a pagar:");
        kGradientPanel1.add(jLabel6);
        jLabel6.setBounds(600, 210, 170, 30);

        txtPreco.setOpaque(false);
        txtPreco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecoActionPerformed(evt);
            }
        });
        kGradientPanel1.add(txtPreco);
        txtPreco.setBounds(910, 20, 110, 20);

        txtnome.setOpaque(false);
        txtnome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnomeActionPerformed(evt);
            }
        });
        kGradientPanel1.add(txtnome);
        txtnome.setBounds(790, 20, 110, 20);

        txtCodico.setOpaque(false);
        txtCodico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodicoActionPerformed(evt);
            }
        });
        kGradientPanel1.add(txtCodico);
        txtCodico.setBounds(660, 20, 110, 20);
        kGradientPanel1.add(setarID);
        setarID.setBounds(940, 290, 120, 20);

        jLabel5.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kz(s)");
        kGradientPanel1.add(jLabel5);
        jLabel5.setBounds(870, 210, 60, 40);

        receber.setText("numericTextField1");
        receber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                receberFocusLost(evt);
            }
        });
        receber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receberActionPerformed(evt);
            }
        });
        receber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                receberKeyReleased(evt);
            }
        });
        kGradientPanel1.add(receber);
        receber.setBounds(600, 120, 189, 32);

        jLabel8.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Valor a devolver");
        kGradientPanel1.add(jLabel8);
        jLabel8.setBounds(600, 170, 173, 29);

        txtTroco.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        txtTroco.setForeground(new java.awt.Color(255, 255, 255));
        txtTroco.setText("000");
        kGradientPanel1.add(txtTroco);
        txtTroco.setBounds(790, 170, 80, 30);

        jLabel7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Recebido:");
        kGradientPanel1.add(jLabel7);
        jLabel7.setBounds(610, 80, 189, 30);

        jLabel9.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Kz(s)");
        kGradientPanel1.add(jLabel9);
        jLabel9.setBounds(870, 170, 60, 30);

        javax.swing.GroupLayout VendasLayout = new javax.swing.GroupLayout(Vendas);
        Vendas.setLayout(VendasLayout);
        VendasLayout.setHorizontalGroup(
            VendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        VendasLayout.setVerticalGroup(
            VendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );

        tabbed.addTab("Vendas", Vendas);

        javax.swing.GroupLayout InfoLayout = new javax.swing.GroupLayout(Info);
        Info.setLayout(InfoLayout);
        InfoLayout.setHorizontalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1235, Short.MAX_VALUE)
        );
        InfoLayout.setVerticalGroup(
            InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 552, Short.MAX_VALUE)
        );

        tabbed.addTab("Informações", Info);

        kGradientPanel14.add(tabbed);
        tabbed.setBounds(0, 108, 1240, 590);

        jLabel3.setFont(new java.awt.Font("Tahoma", 2, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/amdico_48.png"))); // NOI18N
        jLabel3.setText("DayX - Ponto de Venda");
        kGradientPanel14.add(jLabel3);
        jLabel3.setBounds(383, 11, 456, 48);

        chega.setFont(new java.awt.Font("Calibri Light", 3, 18)); // NOI18N
        chega.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/icons8_user_filled_32.png"))); // NOI18N
        chega.setText("ABCDEFGHIJKLMNOPGRSQ");
        chega.setToolTipText("Nome");
        kGradientPanel14.add(chega);
        chega.setBounds(10, 30, 250, 32);

        jLabel4.setFont(new java.awt.Font("Calibri Light", 1, 20)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/icons8_shutdownBlack_32.png"))); // NOI18N
        jLabel4.setText("Sair");
        jLabel4.setToolTipText("Voltar para tela de login");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        kGradientPanel14.add(jLabel4);
        jLabel4.setBounds(10, 70, 69, 33);

        HORRA.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        HORRA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/icons8_time_filled_32.png"))); // NOI18N
        HORRA.setText("HH:mm:ss");
        kGradientPanel14.add(HORRA);
        HORRA.setBounds(1030, 70, 142, 32);

        DATTA.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        DATTA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icones/icons8_calendar_filled_32.png"))); // NOI18N
        DATTA.setText("dd:mm:yyyy");
        kGradientPanel14.add(DATTA);
        DATTA.setBounds(1030, 30, 142, 32);

        status.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        status.setText("ABCDEFGHIJKLMNOPGRSQ");
        kGradientPanel14.add(status);
        status.setBounds(420, 70, 390, 22);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 1241, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1257, 736));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // ao ativar a janela, mude e atualize a data:
        Date data = new Date();
// atenção ao .toString
        //lblData.setText(data.toString());
        // formatando a data
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        DATTA.setText(formatador.format(data));
        btnRealizar.setEnabled(false);
        PesquisarCaixa();
        CalcularRendimentos();
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        int Confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja fazer logout", "Atenção", JOptionPane.YES_NO_OPTION);
        if (Confirma == JOptionPane.YES_OPTION) {
            Login tela = new Login();
            tela.setVisible(true);
            dispose();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel21MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseMoved
        Add1.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel21MouseMoved

    private void jLabel21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseExited
        Add1.setBackground(new Color(10, 113, 218));
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel21MouseExited

    private void jLabel22MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseMoved
        Editar1.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel22MouseMoved

    private void jLabel22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseExited
        Editar1.setBackground(new Color(10, 113, 218));
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel22MouseExited

    private void jLabel23MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseMoved
        Pesquisar1.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel23MouseMoved

    private void jLabel23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseExited
        Pesquisar1.setBackground(new Color(10, 113, 218));
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel23MouseExited

    private void jLabel24MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseMoved
        Deletar1.setBackground(new Color(240, 240, 240));
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel24MouseMoved

    private void jLabel24MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseExited
        Deletar1.setBackground(new Color(10, 113, 218));
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel24MouseExited

    private void jLabel25MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseMoved
        Visu1.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel25MouseMoved

    private void jLabel25MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseExited
        // TODO add your handling code here:
        Visu1.setBackground(new Color(10, 113, 218));
    }//GEN-LAST:event_jLabel25MouseExited

    private void jLabel20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseExited
        // TODO add your handling code here:
        Visu.setBackground(new Color(10, 113, 218));
    }//GEN-LAST:event_jLabel20MouseExited

    private void jLabel20MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseMoved

        Visu.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel20MouseMoved

    private void jLabel19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseExited
        Deletar.setBackground(new Color(10, 113, 218));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel19MouseExited

    private void jLabel19MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseMoved
        Deletar.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel19MouseMoved

    private void jLabel18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseExited
        Pesquisar.setBackground(new Color(10, 113, 218));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel18MouseExited

    private void jLabel18MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseMoved
        Pesquisar.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel18MouseMoved

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        Editar.setBackground(new Color(10, 113, 218));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseExited

    private void jLabel16MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseMoved
        Editar.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseMoved

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        Add.setBackground(new Color(10, 113, 218));           // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseMoved
        Add.setBackground(new Color(240, 240, 240));        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseMoved

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        status.setText("Usuario >> Adicionar");
        AddUsuario tela = new AddUsuario();
        painelUsuario.removeAll();
        painelUsuario.add(tela).setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        status.setText("Usuario >> Visualizar");
        VisuUsuario tela = new VisuUsuario();
        painelUsuario.removeAll();
        painelUsuario.add(tela).setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        status.setText("Usuario >> Pesquisar");
        PesquiUsuario tela = new PesquiUsuario();
        painelUsuario.removeAll();
        painelUsuario.add(tela).setVisible(true);
// TODO add your handling code here::
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        status.setText("Usuario >> Editar");
        EditarUsuario tela = new EditarUsuario();
        painelUsuario.removeAll();
        painelUsuario.add(tela).setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        status.setText("Produto >> Adicionar");
        AddProduto tela = new AddProduto();
        PainelProdutos.removeAll();
        PainelProdutos.add(tela).setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        status.setText("Produto >> Editar");
        EditarProdutos tela = new EditarProdutos();
        PainelProdutos.removeAll();
        PainelProdutos.add(tela).setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel22MouseClicked

    private void jLabel24MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel24MouseEntered

    private void jLabel24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel24MouseClicked
        status.setText("Produto >> Apagar");
        ApagarProdutos tela = new ApagarProdutos();
        PainelProdutos.removeAll();
        PainelProdutos.add(tela).setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel24MouseClicked

    private void jLabel25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseClicked
        status.setText("Produto >> Visuaizar");
        VisuProduto tela = new VisuProduto();
        PainelProdutos.removeAll();
        PainelProdutos.add(tela).setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel25MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        status.setText("Usuario >> Apagar");
        ApagarUsuario tela = new ApagarUsuario();
        painelUsuario.removeAll();
        painelUsuario.add(tela).setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        status.setText("Produto >> Pesquisar");
        PesquiProdutos pr = new PesquiProdutos();
        PainelProdutos.removeAll();
        PainelProdutos.add(pr).setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jLabel23MouseClicked

    private void kGradientPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MouseDragged
        status.setText("Vendas");
        // TODO add your handling code here:
    }//GEN-LAST:event_kGradientPanel1MouseDragged

    private void txtPesquisarProdutoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarProdutoKeyReleased
        PesquisarProduto();        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisarProdutoKeyReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        PesquisarProduto();        // TODO add your handling code here:
        PesquisarCompras();
        ContaProduto();
        txtCodico.setVisible(false);
        txtnome.setVisible(false);
        txtPreco.setVisible(false);
        txtCodico.setEnabled(false);
        txtnome.setEnabled(false);
        txtPreco.setEnabled(false);
        setarID.setEnabled(false);
        setarID.setVisible(false);
    }//GEN-LAST:event_formWindowOpened

    private void kGradientPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MouseMoved
        status.setText("Vendas");        // TODO add your handling code here:
    }//GEN-LAST:event_kGradientPanel1MouseMoved

    private void txtPrecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecoActionPerformed

    private void txtnomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomeActionPerformed

    private void txtCodicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodicoActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        AddProdutoInTable();

        PesquisarCompras();
        ContaProduto();
    }//GEN-LAST:event_kButton2ActionPerformed

    private void tblProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdutosMouseClicked
        setar();        // TODO add your handling code here:
    }//GEN-LAST:event_tblProdutosMouseClicked

    private void btnRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarActionPerformed
        ADDCAIXA();

        FazerConta();
        RealizarCompra();        // TODO add your handling code here:
        PesquisarCompras();
    }//GEN-LAST:event_btnRealizarActionPerformed

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        CancelaString();
        FazerConta();
        PesquisarCompras();
        PesquisarProduto();
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton3ActionPerformed

    private void tblComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblComprasMouseClicked
        SetaID();
    }//GEN-LAST:event_tblComprasMouseClicked

    private void kButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton5ActionPerformed
        ApagaProduto();
        PesquisarCompras();
        ContaProduto();
// TODO add your handling code here:
    }//GEN-LAST:event_kButton5ActionPerformed

    private void receberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_receberKeyReleased
    }//GEN-LAST:event_receberKeyReleased

    private void kButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton6ActionPerformed
        if (totalCompra.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Adicione um produto na sacola");
        } else if (receber.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Digite o valor que o cliente vai pagar");
            receber.requestFocusInWindow();
        } else {
            FazerConta();
        }   // TODO add your handling code here:
    }//GEN-LAST:event_kButton6ActionPerformed

    private void receberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_receberActionPerformed

    private void tabbedMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_tabbedMouseDragged

    private void receberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_receberFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_receberFocusLost

    private void txtPesquisarProdutoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarProdutoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisarProdutoKeyTyped

    private void jjjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jjjKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jjjKeyReleased

    private void btnRealizarMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRealizarMouseMoved
        btnRealizar.setBackground(new Color(0, 179, 225));
        btnRealizar.setForeground(Color.white);
// TODO add your handling code here:
    }//GEN-LAST:event_btnRealizarMouseMoved

    private void btnRealizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRealizarMouseExited
        btnRealizar.setBackground(new Color(240, 240, 240));
        btnRealizar.setForeground(Color.black);
// TODO add your handling code here:
    }//GEN-LAST:event_btnRealizarMouseExited

    private void kButton2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton2MouseMoved
        kButton2.setBackground(new Color(0, 0, 255));
        kButton2.setForeground(Color.white);
// TODO add your handling code here:
    }//GEN-LAST:event_kButton2MouseMoved

    private void kButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton2MouseExited
        kButton2.setForeground(Color.black);
        kButton2.setBackground(new Color(240, 240, 240));
// TODO add your handling code here:
    }//GEN-LAST:event_kButton2MouseExited

    private void kButton3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton3MouseMoved
        kButton3.setBackground(new Color(0, 179, 225));
        kButton3.setForeground(Color.white);
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton3MouseMoved

    private void kButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton3MouseExited
        kButton3.setForeground(Color.black);
        kButton3.setBackground(new Color(240, 240, 240));
// TOD        // TODO add your handling code here:
    }//GEN-LAST:event_kButton3MouseExited

    private void kButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton6MouseExited
        kButton6.setForeground(Color.black);
        kButton6.setBackground(new Color(240, 240, 240));
// TOD        // TODO add your handling code here:
    }//GEN-LAST:event_kButton6MouseExited

    private void kButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton5MouseExited
        kButton5.setForeground(Color.black);
        kButton5.setBackground(new Color(240, 240, 240));
// TOD        // TODO add your handling code here:
    }//GEN-LAST:event_kButton5MouseExited

    private void kButton6MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton6MouseMoved
        kButton6.setBackground(new Color(0, 179, 225));
        kButton6.setForeground(Color.white);
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton6MouseMoved

    private void kButton5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kButton5MouseMoved
        kButton5.setBackground(new Color(0, 179, 225));
        kButton5.setForeground(Color.white);
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton5MouseMoved

    @SuppressWarnings("null")
    private void txtPesquisarDataCaixaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarDataCaixaKeyReleased
        String data = txtPesquisarDataCaixa.getText();
// data.substring(0,1)+"/"+data.substring(3,5)+"/"+data.substring(7,11);   
//justificar(txtPesquisarDataCaixa.getText().toString());
        // txtPesquisarDataCaixa.getText().substring(0,2)+ "/" +txtPesquisarDataCaixa.getText().substring(3,5) + "/" + txtPesquisarDataCaixa.getText().substring(6,10);
        PesquisarCaixaPorData();        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisarDataCaixaKeyReleased

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Painel().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Add;
    private javax.swing.JPanel Add1;
    private javax.swing.JPanel Caixa;
    private javax.swing.JLabel DATTA;
    private javax.swing.JPanel Deletar;
    private javax.swing.JPanel Deletar1;
    private javax.swing.JPanel Editar;
    private javax.swing.JPanel Editar1;
    private javax.swing.JLabel HORRA;
    private javax.swing.JPanel Info;
    private javax.swing.JDesktopPane PainelProdutos;
    private javax.swing.JPanel Pesquisar;
    private javax.swing.JPanel Pesquisar1;
    private javax.swing.JPanel Produto;
    public static javax.swing.JLabel Produtos;
    private javax.swing.JPanel Usuario;
    private javax.swing.JPanel Vendas;
    private javax.swing.JPanel Visu;
    private javax.swing.JPanel Visu1;
    private br.com.cyber.componente.KButton btnRealizar;
    public static javax.swing.JLabel chega;
    private com.k33ptoo.utils.ComponentResizerUtil componentResizerUtil1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.toedter.calendar.JDateChooser jjj;
    private br.com.cyber.componente.KButton kButton2;
    private br.com.cyber.componente.KButton kButton3;
    private br.com.cyber.componente.KButton kButton5;
    private br.com.cyber.componente.KButton kButton6;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel14;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    public static javax.swing.JLabel nome;
    public static javax.swing.JDesktopPane painelUsuario;
    private numeric.textField.NumericTextField receber;
    private javax.swing.JTextField setarID;
    public static javax.swing.JLabel status;
    public static javax.swing.JTabbedPane tabbed;
    private br.com.cyber.componente.Ktable tblCaixa;
    private br.com.cyber.componente.Ktable tblCompras;
    private br.com.cyber.componente.Ktable tblProdutos;
    private javax.swing.JLabel totalCompra;
    private javax.swing.JTextField txtCodico;
    private br.com.cyber.componente.KTextField txtPesquisarDataCaixa;
    private br.com.cyber.componente.KTextField txtPesquisarProduto;
    private javax.swing.JTextField txtPreco;
    private javax.swing.JLabel txtTroco;
    private javax.swing.JTextField txtnome;
    private javax.swing.JLabel valorNoCaixa;
    // End of variables declaration//GEN-END:variables
   
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("iconBlack.png")));
    }
}
