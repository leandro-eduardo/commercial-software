/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas_relatorios;

import bd.Conexao;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import telas.TelaSistema;
import static telas.TelaSistema.jdp;
import util.TelaCarregamentoRelatorios;

/**
 *
 * @author leand
 */
public class TelaRelatorioProdutosMaisVendidos extends javax.swing.JInternalFrame {

    public static TelaRelatorioProdutosMaisVendidos tela;

    /**
     * Creates new form TelaRelatorioProduto1
     */
    public TelaRelatorioProdutosMaisVendidos() {
        initComponents();
        setFrameIcon(new ImageIcon(this.getClass().getResource("/icones/relatorio.png")));
    }

    public static TelaRelatorioProdutosMaisVendidos getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaRelatorioProdutosMaisVendidos();
            tela.addInternalFrameListener(new InternalFrameAdapter() { //Adiciona um listener para verificar quando a tela for fechada, fazendo assim a remoção da mesma junto ao JDesktopPane da TelaSistema e setando a variável tela = null para permitir que a tela possa ser aberta novamente em outro momento. Basicamente o mesmo controle efetuado pela tela de pesquisa, porém de uma forma um pouco diferente.
                @Override
                public void internalFrameClosed(InternalFrameEvent e) {
                    TelaSistema.jdp.remove(tela);
                    tela = null;
                }
            });
            TelaSistema.jdp.add(tela);
        }
        //Depois do teste acima, independentemente dela já existir ou não, ela é selecionada e movida para frente
        TelaSistema.jdp.setSelectedFrame(tela);
        TelaSistema.jdp.moveToFront(tela);
        return tela;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabelCodigo = new javax.swing.JLabel();
        jButtonImprimir = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabelSitacao1 = new javax.swing.JLabel();
        jComboBoxOrdenar = new javax.swing.JComboBox<>();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setBorder(null);
        setClosable(true);
        setForeground(java.awt.Color.white);
        setTitle("Relatório de Produtos Mais Vendidos");
        setMaximumSize(null);
        setMinimumSize(null);

        jLabelCodigo.setText("Data:");

        jButtonImprimir.setText("Imprimir");
        jButtonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirActionPerformed(evt);
            }
        });

        jLabelSitacao1.setText("Ordenar:");

        jComboBoxOrdenar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nome", "Qtde Vendida" }));
        jComboBoxOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrdenarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelSitacao1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(15, 15, 15)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxOrdenar)
                    .addComponent(jLabelSitacao1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        setBounds(0, 0, 475, 185);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed
//        try {
//            //Lista com os parametros para o relátorio
//            HashMap params = new HashMap<>();
//            
//            //Passândo parâmetros e convertendo o dados pra ser compativel
//            params.put("IDPRODUTOPAR", jTextField1.getText());
//            
//            //Invocando a geração do relatório
//            String file = new RelatorioService().gerarRelatorio(params,
//                    "relatorioProdutos", "pdf");
//            
//            //Exibindo o relatório na tela para o usuário
//            Desktop.getDesktop().open(new File(file));
//        } catch (Exception ex) {
//            Logger.getLogger(TelaRelatorioProduto.class.getName()).log(Level.SEVERE, null, ex);
//        }

        //Abertura do diálogo de confirmação para o usuário
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            //Abertura da tela de carregamento
            TelaCarregamentoRelatorios telaCarregamento = new TelaCarregamentoRelatorios();
            jdp.add(telaCarregamento);
            telaCarregamento.setVisible(true);

            //Criação da thread que gera o relatório, que apesar de iniciada aguarda a execução da threadCarregamento
            Thread threadGerarRelatorio = new Thread() {
                public void run() {
                    try {
                        //Nesse momento a threadCarregamento é solicitada e a threadGerarRelatório é interrompida
                        //até que a threadCarregamento seja totalmente executada.
                        telaCarregamento.threadCarregamento.join();
                        //A threadGerarRelatorio fica 1 segundo "parada" para que o usuário observe que o relatório foi
                        //totalmente carregado
                        sleep(1000);
                        //Depois desse 1 segundo, a tela de carregamento é fechada, indicando o término total do carregamento
                        //do relatório
                        telaCarregamento.dispose();
                        //Imprimindo o relatório
                        //Caminho absoluto comentado para utilizar o caminho relativo que pega os arquivo da pasta src do projeto         
                        //String caminhoRelatorio = System.getProperty("user.dir") + "/JasperReports/RelatorioProdutosMaisVendidos.jasper";/*"C:\\Users\\leand\\JaspersoftWorkspace\\MyReports\\RelatorioProdutos.jasper";*/

                        //Caminho relativo
                        InputStream caminhoRelatorio = getClass().getResourceAsStream("/relatorios/RelatorioProdutosMaisVendidos.jasper");
                        JasperPrint jasperPrint = null;

                        //Lista com os parametros para o relátorio
                        HashMap parametros = new HashMap<>();

                        //Passando parâmetros e convertendo os dados pra ser compativel 
                        String parametro = "SELECT PRODUTO.ID, PRODUTO.NOME, SUM(ITEMVENDA.QUANTIDADE) AS QTDE FROM PRODUTO\n"
                                + "INNER JOIN ITEMVENDA ON PRODUTO.ID = ITEMVENDA.IDPRODUTO\n"
                                + "INNER JOIN VENDA ON ITEMVENDA.IDVENDA = VENDA.ID WHERE VENDA.STATUS = 'VF' AND ";

                        if (jDateChooser1.getDate() == null) {
                            parametro = parametro + "VENDA.DATA BETWEEN '01.01.1900' AND ";
                        } else {
                            parametro = parametro + "VENDA.DATA BETWEEN '" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooser1.getDate()) + "' AND ";
                        }

                        if (jDateChooser2.getDate() == null) {
                            parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "' AND ";
                        } else {
                            parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooser2.getDate()) + "' AND ";
                        }

                        parametro = parametro + "0=0 ";
                        parametro = parametro + "GROUP BY PRODUTO.ID, PRODUTO.NOME";

                        switch (jComboBoxOrdenar.getSelectedIndex()) {
                            case 0:
                                parametro = parametro + " ORDER BY ID";
                                break;
                            case 1:
                                parametro = parametro + " ORDER BY PRODUTO.NOME";
                                break;
                            case 2:
                                parametro = parametro + " ORDER BY QTDE";
                                break;
                        }

                        System.out.println("PRINTA AI SQL = " + parametro);

                        parametros.put("SQL", parametro);
                        parametros.put("DATA_INICIAL", jDateChooser1.getDate());
                        parametros.put("DATA_FINAL", jDateChooser2.getDate());
                        parametros.put("ORDENACAO", jComboBoxOrdenar.getSelectedItem());

                        try {
                            jasperPrint = JasperFillManager.fillReport(caminhoRelatorio, parametros, Conexao.getConexao());
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(rootPane, "Erro ao gerar o relatório - " + e);
                        }

                        //Relatório finalmente é exibido na tela para o usuário
                        JasperViewer jv = new JasperViewer(jasperPrint, false);
                        URL iconReportURL = getClass().getResource("/icones/relatorio.png");
                        ImageIcon icon = new ImageIcon(iconReportURL);
                        jv.setIconImage(icon.getImage());
                        jv.setTitle("Relatório");
                        jv.setVisible(true);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TelaRelatorioProdutosMaisVendidos.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            telaCarregamento.threadCarregamento.start();

            threadGerarRelatorio.start();
        }
    }//GEN-LAST:event_jButtonImprimirActionPerformed

    private void jComboBoxOrdenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrdenarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxOrdenarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JComboBox<String> jComboBoxOrdenar;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabelCodigo;
    private javax.swing.JLabel jLabelSitacao1;
    // End of variables declaration//GEN-END:variables

}
