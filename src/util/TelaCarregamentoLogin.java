/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Dimension;
import java.awt.Toolkit;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author leand
 */
public class TelaCarregamentoLogin extends javax.swing.JFrame {

    public static TelaCarregamentoLogin tela;

    //Instanciamento da Thread que executa a barra de carregamento, que é chamada pela tela de login
    public Thread threadCarregamento = new Thread() {
        public void run() {
            try {
                //Interrupção de 1,5 segundo para iniciar o carregamento apenas quando a tela de login desaparecer
                sleep(1500);

            } catch (InterruptedException ex) {
                Logger.getLogger(TelaCarregamentoLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Estrutura de repetição FOR que indica a porcentagem de carregamento
            for (int i = 0; i < 101; i++) {
                try {
                    //Cada vez que a Thread passa entra no FOR de 0 a 101 ela fica "parada" por 0,08 segundo, indicando o tempo
                    //de carregamento do login
                    //jp_progress.UpdateProgress(i);
                    //jp_progress.repaint();
                    sleep(45);
                    rSProgressBar1.setValue(i);
                    if (rSProgressBar1.getValue() <= 1) {
                        jLabelCarregamento.setText("Carregando Sistema...");
                    } else if (rSProgressBar1.getValue() == 25) {
                        jLabelCarregamento.setText("Carregando Banco de Dados...");
                    } else if (rSProgressBar1.getValue() == 50) {
                        jLabelCarregamento.setText("Abrindo Tabelas...");
                    } else if (rSProgressBar1.getValue() == 75) {
                        jLabelCarregamento.setText("O Sistema está sendo iniciado...");
                    }
                } catch (InterruptedException ie) {

                }
            }
        }
    };

    /**
     * Creates new form NewJInternalFrame
     */
    public TelaCarregamentoLogin() {
        ImageIcon img = new ImageIcon(getClass().getResource("/icones/logo64px.png"));
        this.setIconImage(img.getImage());
        initComponents();
        centralizaJFrame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void centralizaJFrame() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelCarregamento = new javax.swing.JLabel();
        rSProgressBar1 = new rojerusan.componentes.RSProgressBar();
        jButtonFechar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rSProgressCircleAnimated1 = new rojerusan.componentes.RSProgressCircleAnimated();

        setTitle("Inicializando sistema...");
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(466, 240));
        setMinimumSize(new java.awt.Dimension(466, 240));
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(466, 240));

        jPanel1.setBackground(new java.awt.Color(87, 170, 241));
        jPanel1.setMaximumSize(new java.awt.Dimension(466, 240));
        jPanel1.setMinimumSize(new java.awt.Dimension(466, 240));
        jPanel1.setPreferredSize(new java.awt.Dimension(466, 240));

        jLabelCarregamento.setBackground(new java.awt.Color(0, 0, 0));
        jLabelCarregamento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCarregamento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCarregamento.setText("jLabel1");

        rSProgressBar1.setBorder(null);
        rSProgressBar1.setFont(new java.awt.Font("Roboto Bold", 1, 14)); // NOI18N

        jButtonFechar.setBackground(new java.awt.Color(87, 170, 241));
        jButtonFechar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/iconFechar.png"))); // NOI18N
        jButtonFechar.setBorderPainted(false);
        jButtonFechar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFechar.setFocusable(false);
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/logoTI.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LSE Software");

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tecnologia da Informação");

        rSProgressCircleAnimated1.setString("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rSProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rSProgressCircleAnimated1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabelCarregamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rSProgressCircleAnimated1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCarregamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rSProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelCarregamento;
    private javax.swing.JPanel jPanel1;
    private rojerusan.componentes.RSProgressBar rSProgressBar1;
    private rojerusan.componentes.RSProgressCircleAnimated rSProgressCircleAnimated1;
    // End of variables declaration//GEN-END:variables
}