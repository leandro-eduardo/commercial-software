/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 * @author leand
 */
public class TelaConfiguracao extends javax.swing.JFrame {

    Preferences prefs = Preferences.userNodeForPackage(TelaConfiguracao.class);
    public final String DATABASE_PATH = "database_path";
    TelaLogin telaLogin = new TelaLogin();

    /**
     * Creates new form TelaConfiguracao
     */
    public TelaConfiguracao() {

        ImageIcon img = new ImageIcon(getClass().getResource("/icones/settings24.png"));
        this.setIconImage(img.getImage());
        initComponents();
        //se a preferencia do usuário ainda não existir, define o valor "" para o caminho do arquivo do banco
        System.out.println("DATABASE_PATH = " + prefs.get(DATABASE_PATH, "value"));
        if (prefs.get(DATABASE_PATH, "value").equals("value")) {
            jTextFieldDBPath.setText("");
        } else { //se existir, pega o valor da preferencia do usuário. EX: C:/users/xxx/banco/teste.fdb
            jTextFieldDBPath.setText(prefs.get(DATABASE_PATH, "value"));
        }
        //SEMPRE CENTRALIZAR APÓS O INITCOMPONENTS
        this.setLocationRelativeTo(null); //centraliza a tela
        //centralizaTela(); método que faz a mesma coisa que a linha de cima

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
        jTextFieldDBPath = new javax.swing.JTextField();
        jLabelImg = new javax.swing.JLabel();
        jLabelCfgDB = new javax.swing.JLabel();
        jButtonFechar = new javax.swing.JButton();
        jButtonSalvar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButtonSelecionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuração do Banco de Dados");
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(87, 170, 241));

        jTextFieldDBPath.setEditable(false);
        jTextFieldDBPath.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldDBPath.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jTextFieldDBPath.setBorder(null);
        jTextFieldDBPath.setSelectionColor(new java.awt.Color(0, 153, 255));
        jTextFieldDBPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDBPathActionPerformed(evt);
            }
        });

        jLabelImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/settings24.png"))); // NOI18N

        jLabelCfgDB.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCfgDB.setText("Configuração do Banco de Dados");

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

        jButtonSalvar.setBackground(new java.awt.Color(0, 112, 192));
        jButtonSalvar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSalvar.setText("Salvar");
        jButtonSalvar.setBorder(null);
        jButtonSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSalvar.setFocusPainted(false);
        jButtonSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonSalvarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonSalvarMouseExited(evt);
            }
        });
        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarActionPerformed(evt);
            }
        });

        jButtonCancelar.setBackground(new java.awt.Color(217, 81, 51));
        jButtonCancelar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setBorder(null);
        jButtonCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonCancelarMouseExited(evt);
            }
        });
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonSelecionar.setBackground(new java.awt.Color(0, 112, 192));
        jButtonSelecionar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSelecionar.setText("Selecionar");
        jButtonSelecionar.setBorder(null);
        jButtonSelecionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSelecionar.setFocusPainted(false);
        jButtonSelecionar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonSelecionarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonSelecionarMouseExited(evt);
            }
        });
        jButtonSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelecionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelImg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelCfgDB, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(379, 379, 379)
                        .addComponent(jButtonSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jTextFieldDBPath, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSelecionar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonCancelar, jButtonSalvar, jButtonSelecionar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelImg)
                            .addComponent(jLabelCfgDB))))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldDBPath, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelecionar))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonCancelar, jButtonSalvar, jButtonSelecionar, jTextFieldDBPath});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldDBPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDBPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDBPathActionPerformed

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        this.dispose();
        telaLogin.setVisible(true);
    }//GEN-LAST:event_jButtonFecharActionPerformed

    private void jButtonSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelecionarActionPerformed
        //Aplicando o Look and Feel
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel()); //top
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos .FDB", "FDB");

        fileChooser.setFileFilter(filter);
        int retorno = fileChooser.showDialog(null, "Selecionar");
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            jTextFieldDBPath.setText(file.getPath());
        }
        //Aplicando o Look and Feel padrão
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel()); //top
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSelecionarActionPerformed

    private void jButtonSelecionarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSelecionarMouseExited
        jButtonSelecionar.setBackground(new Color(0, 112, 192));
        jButtonSelecionar.setForeground(Color.BLACK);
    }//GEN-LAST:event_jButtonSelecionarMouseExited

    private void jButtonSelecionarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSelecionarMouseEntered
        jButtonSelecionar.setBackground(new Color(235, 235, 235));
        jButtonSelecionar.setForeground(new Color(0, 112, 192));
    }//GEN-LAST:event_jButtonSelecionarMouseEntered

    private void jButtonSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarActionPerformed
        prefs.put(DATABASE_PATH, jTextFieldDBPath.getText());
        this.dispose();
        telaLogin.setVisible(true);
    }//GEN-LAST:event_jButtonSalvarActionPerformed

    private void jButtonSalvarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSalvarMouseExited
        jButtonSalvar.setBackground(new Color(0, 112, 192));
        jButtonSalvar.setForeground(Color.BLACK);
    }//GEN-LAST:event_jButtonSalvarMouseExited

    private void jButtonSalvarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSalvarMouseEntered
        jButtonSalvar.setBackground(new Color(235, 235, 235));
        jButtonSalvar.setForeground(new Color(0, 112, 192));
    }//GEN-LAST:event_jButtonSalvarMouseEntered

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        this.dispose();
        telaLogin.setVisible(true);
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCancelarMouseExited
        jButtonCancelar.setBackground(new Color(217, 81, 51));
        jButtonCancelar.setForeground(Color.BLACK);
    }//GEN-LAST:event_jButtonCancelarMouseExited

    private void jButtonCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCancelarMouseEntered
        jButtonCancelar.setBackground(new Color(235, 235, 235));
        jButtonCancelar.setForeground(new Color(217, 81, 51));
    }//GEN-LAST:event_jButtonCancelarMouseEntered

    public void centralizaTela() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JButton jButtonSelecionar;
    private javax.swing.JLabel jLabelCfgDB;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldDBPath;
    // End of variables declaration//GEN-END:variables
}
