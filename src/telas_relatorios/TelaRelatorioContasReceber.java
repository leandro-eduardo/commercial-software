/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas_relatorios;

import bd.Conexao;
import componentes.FiltroPesquisa;
import dao.DaoContaReceber;
import dao.DaoPessoa;
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
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import pojo.ParametrosConsulta;
import renderizador.CellRendererData;
import renderizador.InteiroRender;
import renderizador.MonetarioRender;
import renderizador.RenderizadorStatus;
import renderizador.RenderizadorTexto;
import telas.TelaConsultaFiltro;
import telas.TelaSistema;
import static telas.TelaSistema.jdp;
import util.RestricaoCaracteresRelatorios;
import util.TelaCarregamentoRelatorios;

/**
 *
 * @author leand
 */
public class TelaRelatorioContasReceber extends javax.swing.JInternalFrame {

    public static TelaRelatorioContasReceber tela;

    public ParametrosConsulta parametrosConsulta;

    /**
     * Creates new form TelaRelatorioProduto1
     */
    public TelaRelatorioContasReceber() {
        initComponents();
        buttonGroupTipoRelatorio.add(jRadioButtonAnalitico);
        buttonGroupTipoRelatorio.add(jRadioButtonSintetico);
        jTextFieldCodigo.setDocument(new RestricaoCaracteresRelatorios(100));
        jTextFieldPessoa.setDocument(new RestricaoCaracteresRelatorios(100));
        setFrameIcon(new ImageIcon(this.getClass().getResource("/icones/relatorio.png")));
    }

    public static TelaRelatorioContasReceber getTela() {  //Estático para poder ser chamado de outras classes sem a necessidade de ter criado um objeto anteriormente.
        if (tela == null) {   //Tela não está aberta, pode criar uma nova tela
            tela = new TelaRelatorioContasReceber();
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
        buttonGroupTipoRelatorio = new javax.swing.ButtonGroup();
        jLabelData = new javax.swing.JLabel();
        jButtonImprimir = new javax.swing.JButton();
        jDateChooserDataInicial = new com.toedter.calendar.JDateChooser();
        jLabelPessoa = new javax.swing.JLabel();
        jTextFieldPessoa = new javax.swing.JTextField();
        jLabelSituacao = new javax.swing.JLabel();
        jComboBoxSituacao = new javax.swing.JComboBox<>();
        jLabelOrdenar = new javax.swing.JLabel();
        jComboBoxOrdenar = new javax.swing.JComboBox<>();
        jDateChooserDataFinal = new com.toedter.calendar.JDateChooser();
        jButtonConsultaPessoa = new javax.swing.JButton();
        jTextFieldCodigo = new javax.swing.JTextField();
        jButtonConsultaCodigo = new javax.swing.JButton();
        jLabelCodigo = new javax.swing.JLabel();
        jRadioButtonSintetico = new javax.swing.JRadioButton();
        jRadioButtonAnalitico = new javax.swing.JRadioButton();

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
        setTitle("Relatório de Contas Recebidas e a Receber");
        setMaximumSize(null);
        setMinimumSize(null);

        jLabelData.setText("Data vencto:");

        jButtonImprimir.setText("Imprimir");
        jButtonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirActionPerformed(evt);
            }
        });

        jLabelPessoa.setText("Cliente:");

        jTextFieldPessoa.setColumns(10);
        jTextFieldPessoa.setToolTipText("Para filtar por mais de um código, utilize vírgula como separador. Ex: (1, 2, 3)");

        jLabelSituacao.setText("Situação:");

        jComboBoxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Quitada", "Em Aberto" }));
        jComboBoxSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSituacaoActionPerformed(evt);
            }
        });

        jLabelOrdenar.setText("Ordenar:");

        jComboBoxOrdenar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Fornecedor", "Data", "Situação", "Nº Parcelas", "Desconto", "Valor Total", "Valor em Aberto" }));
        jComboBoxOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrdenarActionPerformed(evt);
            }
        });

        jButtonConsultaPessoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/search.png"))); // NOI18N
        jButtonConsultaPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaPessoaActionPerformed(evt);
            }
        });

        jTextFieldCodigo.setColumns(10);
        jTextFieldCodigo.setToolTipText("Para filtar por mais de um código, utilize vírgula como separador. Ex: (1, 2, 3)");

        jButtonConsultaCodigo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/search.png"))); // NOI18N
        jButtonConsultaCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaCodigoActionPerformed(evt);
            }
        });

        jLabelCodigo.setText("Código:");

        jRadioButtonSintetico.setText("Sintético");
        jRadioButtonSintetico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSinteticoActionPerformed(evt);
            }
        });

        jRadioButtonAnalitico.setText("Analítico");
        jRadioButtonAnalitico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAnaliticoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jRadioButtonSintetico)
                        .addGap(15, 15, 15)
                        .addComponent(jRadioButtonAnalitico))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelOrdenar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelSituacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelPessoa, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonConsultaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBoxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jDateChooserDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                                .addComponent(jDateChooserDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTextFieldPessoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonConsultaPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBoxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jDateChooserDataFinal, jDateChooserDataInicial});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonSintetico)
                    .addComponent(jRadioButtonAnalitico))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonConsultaCodigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldPessoa))
                    .addComponent(jButtonConsultaPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooserDataInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooserDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxSituacao))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxOrdenar))
                .addGap(15, 15, 15)
                .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonConsultaPessoa, jTextFieldPessoa});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonConsultaCodigo, jTextFieldCodigo});

        setBounds(0, 0, 496, 335);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed

        if (!jRadioButtonSintetico.isSelected() && !jRadioButtonAnalitico.isSelected()) {
            JOptionPane.showMessageDialog(null, "Selecione o tipo do relatório.");
            jRadioButtonSintetico.requestFocus();
            return;
        }

        //verifica se o último dígito dos campos é uma virgula. Se for, ela é removida para não dar conflito com a String do SQL
        if (jTextFieldCodigo.getText().endsWith(",")) {
            jTextFieldCodigo.setText(jTextFieldCodigo.getText().substring(0, jTextFieldCodigo.getText().length() - 1));
        }
        if (jTextFieldPessoa.getText().endsWith(",")) {
            jTextFieldPessoa.setText(jTextFieldPessoa.getText().substring(0, jTextFieldPessoa.getText().length() - 1));
        }

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

                        //Verifica o tipo do relatório
                        if (jRadioButtonSintetico.isSelected()) {
                            //Imprimindo o relatório
                            //Caminho absoluto comentado para utilizar o caminho relativo que pega os arquivo da pasta src do projeto
                            //String caminhoRelatorio = System.getProperty("user.dir") + "/JasperReports/RelatorioContasReceberSintetico.jasper";/*"C:\\Users\\leand\\JaspersoftWorkspace\\MyReports\\RelatorioProdutos.jasper";*/

                            //Caminho relativo
                            InputStream caminhoRelatorio = getClass().getResourceAsStream("/relatorios/RelatorioContasReceberSintetico.jasper");
                            JasperPrint jasperPrint = null;

                            //Lista com os parametros para o relátorio
                            HashMap parametros = new HashMap<>();

                            //Passando parâmetros e convertendo os dados pra ser compativel
                            String parametro = "SELECT PARCELACONTARECEBER.ID, PESSOA.NOMERAZAOSOCIAL AS NOMECLIENTE, CONTARECEBER.DESCRICAO, CONTARECEBER.DATA,\n"
                                    + "PARCELACONTARECEBER.VENCIMENTO, PARCELACONTARECEBER.valor,  parcelacontareceber.valorpendente, PARCELACONTARECEBER.quitada\n"
                                    + "from PARCELACONTARECEBER\n"
                                    + "INNER JOIN CONTARECEBER ON CONTARECEBER.ID = PARCELACONTARECEBER.IDCONTARECEBER\n"
                                    + "INNER JOIN PESSOA ON PESSOA.ID = CONTARECEBER.IDPESSOA WHERE ";

                            if (!jTextFieldCodigo.getText().trim().isEmpty()) {
                                parametro = parametro + "CONTARECEBER.ID IN (" + jTextFieldCodigo.getText() + ") AND ";
                            }

                            if (!jTextFieldPessoa.getText().trim().isEmpty()) {
                                parametro = parametro + "CONTARECEBER.IDPESSOA IN (" + jTextFieldPessoa.getText() + ") AND ";
                            }

                            if (jDateChooserDataInicial.getDate() == null) {
                                parametro = parametro + "PARCELACONTARECEBER.VENCIMENTO BETWEEN '01.01.1900' AND ";
                            } else {
                                parametro = parametro + "PARCELACONTARECEBER.VENCIMENTO BETWEEN '" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooserDataInicial.getDate()) + "' AND ";
                            }

                            if (jDateChooserDataFinal.getDate() == null) {
                                parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "' AND ";
                            } else {
                                parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooserDataFinal.getDate()) + "' AND ";
                            }

                            switch (jComboBoxSituacao.getSelectedIndex()) {
                                case 0:
                                    parametro = parametro + "PARCELACONTARECEBER.QUITADA IN ('S', 'N') AND ";
                                    break;
                                case 1:
                                    parametro = parametro + "PARCELACONTARECEBER.QUITADA IN ('S') AND ";
                                    break;
                                case 2:
                                    parametro = parametro + "PARCELACONTARECEBER.QUITADA IN ('N') AND ";
                                    break;
                            }

                            parametro = parametro + "0=0 ";

                            switch (jComboBoxOrdenar.getSelectedIndex()) {
                                case 0:
                                    parametro = parametro + " ORDER BY PARCELACONTARECEBER.ID";
                                    break;
                                case 1:
                                    parametro = parametro + " ORDER BY NOMECLIENTE";
                                    break;
                                case 2:
                                    parametro = parametro + " ORDER BY CONTARECEBER.DATA";
                                    break;
                                case 3:
                                    parametro = parametro + " ORDER BY PARCELACONTARECEBER.VENCIMENTO";
                                    break;
                                case 4:
                                    parametro = parametro + " ORDER BY PARCELACONTARECEBER.QUITADA";
                                    break;
                                case 5:
                                    parametro = parametro + " ORDER BY PARCELACONTARECEBER.VALOR";
                                    break;
                                case 6:
                                    parametro = parametro + " ORDER BY PARCELACONTARECEBER.VALORPENDENTE";
                                    break;
                            }

                            parametros.put("SQL", parametro);
                            parametros.put("ID", jTextFieldCodigo.getText());
                            parametros.put("IDCLIENTE", jTextFieldPessoa.getText());
                            parametros.put("ORDENACAO", jComboBoxOrdenar.getSelectedItem());
                            parametros.put("SITUACAO", jComboBoxSituacao.getSelectedItem());
                            parametros.put("DATA_INICIAL", jDateChooserDataInicial.getDate());
                            parametros.put("DATA_FINAL", jDateChooserDataFinal.getDate());

                            System.out.println("PRINTA AI SQL = " + parametro);
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
                        } else {
                            //Imprimindo o relatório
                            //Caminho absoluto comentado para utilizar o caminho relativo que pega os arquivo da pasta src do projeto                            
                            //String caminhoRelatorio = System.getProperty("user.dir") + "/JasperReports/RelatorioContasReceberAnalitico.jasper";/*"C:\\Users\\leand\\JaspersoftWorkspace\\MyReports\\RelatorioProdutos.jasper";*/

                            //Caminho relativo
                            InputStream caminhoRelatorio = getClass().getResourceAsStream("/relatorios/RelatorioContasReceberAnalitico.jasper");
                            JasperPrint jasperPrint = null;

                            //Lista com os parametros para o relátorio
                            HashMap parametros = new HashMap<>();

                            //Passando parâmetros e convertendo os dados pra ser compativel
                            String parametro = "SELECT CONTARECEBER.ID, CONTARECEBER.DESCRICAO, CONTARECEBER.DATA, CONTARECEBER.VALORTOTAL, CONTARECEBER.DESCONTO, CONTARECEBER.VALORLIQUIDO, CONTARECEBER.VALORPENDENTE, \n"
                                    + "CONTARECEBER.QUITADA, condicaopagamento.descricao AS CONDPAGTODDESC, condicaopagamento.parcelas, CONTARECEBER.IDPESSOA AS IDFORNECEDOR,\n"
                                    + "PESSOA.NOMERAZAOSOCIAL AS NOMECLIENTE, CONTARECEBER.IDVENDA\n"
                                    + "from CONTARECEBER\n"
                                    + "INNER JOIN condicaopagamento ON condicaopagamento.ID = CONTARECEBER.idcondicaopagamento\n"
                                    + "INNER JOIN PESSOA ON PESSOA.ID = CONTARECEBER.IDPESSOA WHERE ";

                            if (!jTextFieldCodigo.getText().trim().isEmpty()) {
                                parametro = parametro + "CONTARECEBER.ID IN (" + jTextFieldCodigo.getText() + ") AND ";
                            }

                            if (!jTextFieldPessoa.getText().trim().isEmpty()) {
                                parametro = parametro + "CONTARECEBER.IDPESSOA IN (" + jTextFieldPessoa.getText() + ") AND ";
                            }

                            if (jDateChooserDataInicial.getDate() == null) {
                                parametro = parametro + "CONTARECEBER.DATA BETWEEN '01.01.1900' AND ";
                            } else {
                                parametro = parametro + "CONTARECEBER.DATA BETWEEN '" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooserDataInicial.getDate()) + "' AND ";
                            }

                            if (jDateChooserDataFinal.getDate() == null) {
                                parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + "' AND ";
                            } else {
                                parametro = parametro + "'" + new SimpleDateFormat("dd.MM.yyyy").format(jDateChooserDataFinal.getDate()) + "' AND ";
                            }

                            switch (jComboBoxSituacao.getSelectedIndex()) {
                                case 0:
                                    parametro = parametro + "CONTARECEBER.QUITADA IN ('S', 'N') AND ";
                                    break;
                                case 1:
                                    parametro = parametro + "CONTARECEBER.QUITADA IN ('S') AND ";
                                    break;
                                case 2:
                                    parametro = parametro + "CONTARECEBER.QUITADA IN ('N') AND ";
                                    break;
                            }

                            parametro = parametro + "0=0 ";

                            switch (jComboBoxOrdenar.getSelectedIndex()) {
                                case 0:
                                    parametro = parametro + " ORDER BY ID";
                                    break;
                                case 1:
                                    parametro = parametro + " ORDER BY NOMECLIENTE";
                                    break;
                                case 2:
                                    parametro = parametro + " ORDER BY CONTARECEBER.DATA";
                                    break;
                                case 3:
                                    parametro = parametro + " ORDER BY CONTARECEBER.QUITADA";
                                    break;
                                case 4:
                                    parametro = parametro + " ORDER BY CONDICAOPAGAMENTO.PARCELAS DESC";
                                    break;
                                case 5:
                                    parametro = parametro + " ORDER BY CONTARECEBER.DESCONTO DESC";
                                    break;
                                case 6:
                                    parametro = parametro + " ORDER BY CONTARECEBER.VALORLIQUIDO DESC";
                                    break;
                                case 7:
                                    parametro = parametro + " ORDER BY CONTARECEBER.VALORPENDENTE DESC";
                                    break;
                            }

                            parametros.put("SQL", parametro);
                            
                            //Caminho relativo do sub relatório
                            URL caminhoSubRelatorio = getClass().getResource("/relatorios/subrelatorios/SubRelatorioParcelasContaReceber.jasper");
                            parametros.put("SUBREPORT_DIR", caminhoSubRelatorio);
                            
                            parametros.put("ID", jTextFieldCodigo.getText());
                            parametros.put("IDCLIENTE", jTextFieldPessoa.getText());
                            parametros.put("ORDENACAO", jComboBoxOrdenar.getSelectedItem());
                            parametros.put("SITUACAO", jComboBoxSituacao.getSelectedItem());
                            parametros.put("DATA_INICIAL", jDateChooserDataInicial.getDate());
                            parametros.put("DATA_FINAL", jDateChooserDataFinal.getDate());

                            System.out.println("PRINTA AI SQL = " + parametro);
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
                        }

                    } catch (InterruptedException ex) {
                        Logger.getLogger(TelaRelatorioContasReceber.class.getName()).log(Level.SEVERE, null, ex);
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

    private void jComboBoxSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSituacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxSituacaoActionPerformed

    private void jButtonConsultaPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaPessoaActionPerformed
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Cliente para Relatório",
                DaoPessoa.SQLPESQUISARCLIENTES,
                new String[]{"Código", "Nome", "CPF/CNPJ", "Cidade", "Situação"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Nome", "NOMERAZAOSOCIAL", String.class),
                    new FiltroPesquisa("CPF/CNPJ", "CPFCNPJ", String.class),
                    new FiltroPesquisa("Cidade", "CIDADE", String.class),
                    new FiltroPesquisa("Situação", "ATIVO", String.class)
                },
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new RenderizadorTexto(),
                    new RenderizadorTexto(), new RenderizadorTexto()
                },
                this, this, false, false, false)
        );
    }//GEN-LAST:event_jButtonConsultaPessoaActionPerformed

    private void jButtonConsultaCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaCodigoActionPerformed
        new TelaConsultaFiltro(parametrosConsulta = new ParametrosConsulta("Consulta de Conta a Receber para Relatório",
                DaoContaReceber.SQLPESQUISAR,
                new String[]{"Código", "Nome", "Venda/OS", "Data", "Valor", "Pagamento", "Quitada"},
                new FiltroPesquisa[]{new FiltroPesquisa("Código", "ID", String.class),
                    new FiltroPesquisa("Nome", "PESSOA_NOME", String.class),
                    new FiltroPesquisa("Venda", "COMPRA_ID", String.class),
                    new FiltroPesquisa("Data", "DATA", Date.class),
                    new FiltroPesquisa("Situação", "QUITADA", String.class),},
                new DefaultTableCellRenderer[]{new InteiroRender(), new RenderizadorTexto(), new InteiroRender(),
                    new CellRendererData(), new MonetarioRender(), new RenderizadorStatus(), new RenderizadorStatus()},
                this, TelaConsultaFiltro.class, true, false, false)
        );
    }//GEN-LAST:event_jButtonConsultaCodigoActionPerformed

    private void jRadioButtonSinteticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSinteticoActionPerformed
        jLabelData.setText("Data vencto:");
        jComboBoxOrdenar.removeAllItems();
        String[] items = {"Código", "Fornecedor", "Data", "Vencimento", "Situação", "Valor", "Valor em Aberto"};
        for (int i = 0; i < items.length; i++) {
            jComboBoxOrdenar.addItem(items[i]);
        }
    }//GEN-LAST:event_jRadioButtonSinteticoActionPerformed

    private void jRadioButtonAnaliticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonAnaliticoActionPerformed
        jLabelData.setText("Data conta:  ");
        jComboBoxOrdenar.removeAllItems();
        String[] items = {"Código", "Fornecedor", "Data", "Situação", "Nº Parcelas", "Desconto", "Valor Total", "Valor em Aberto"};
        for (int i = 0; i < items.length; i++) {
            jComboBoxOrdenar.addItem(items[i]);
        }
    }//GEN-LAST:event_jRadioButtonAnaliticoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupTipoRelatorio;
    private javax.swing.JButton jButtonConsultaCodigo;
    private javax.swing.JButton jButtonConsultaPessoa;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JComboBox<String> jComboBoxOrdenar;
    private javax.swing.JComboBox<String> jComboBoxSituacao;
    private com.toedter.calendar.JDateChooser jDateChooserDataFinal;
    private com.toedter.calendar.JDateChooser jDateChooserDataInicial;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabelCodigo;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelOrdenar;
    private javax.swing.JLabel jLabelPessoa;
    private javax.swing.JLabel jLabelSituacao;
    private javax.swing.JRadioButton jRadioButtonAnalitico;
    private javax.swing.JRadioButton jRadioButtonSintetico;
    public javax.swing.JTextField jTextFieldCodigo;
    public javax.swing.JTextField jTextFieldPessoa;
    // End of variables declaration//GEN-END:variables

}
