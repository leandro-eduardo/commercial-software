/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import dao.DaoUsuario;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import pojo.Usuario;
import util.TelaCarregamentoLogin;

/**
 *
 * @author leand
 */
public class TelaLogin extends javax.swing.JFrame {

    Preferences prefs = Preferences.userNodeForPackage(TelaLogin.class);
    final String USER_PREF_USUARIO = "usuario";
    final String USER_PREF_LEMBRAR_LOGIN = "lembrarLogin";

    TelaLogin telaLogin = this;
    private final Point point = new Point();
    private Usuario usuario = new Usuario();
    private DaoUsuario daoUsuario = new DaoUsuario(usuario);

    /**
     * Creates new form TelaLogin
     */
    public TelaLogin() {
        ImageIcon img = new ImageIcon(getClass().getResource("/icones/logo64px.png"));
        this.setIconImage(img.getImage());
        initComponents();
        jButtonEntrar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    jButtonEntrar.doClick();
                }
            }
        });
        jPasswordFieldSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    jButtonEntrar.doClick();
                }
            }
        });
        jButtonSair.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    jButtonSair.doClick();
                }
            }
        });
        //se a preferencia do usuário ainda não existir, define o valor "" para o texto de login
        System.out.println("PREF USER = " + prefs.get(USER_PREF_USUARIO, "value"));
        System.out.println("PREF CHECKBOX = " + prefs.get(USER_PREF_LEMBRAR_LOGIN, "value"));
        if (prefs.get(USER_PREF_USUARIO, "value").equals("value")) {
            jTextFieldUsuario.setText("");
        } else { //se existir, pega o valor da preferencia do usuário. Exemplo: usuário salvo como preferência - ADMIN
            jTextFieldUsuario.setText(prefs.get(USER_PREF_USUARIO, "value"));
        }
        //se a preferencia do usuário for igual a 0 ou ainda não existir, deixa o checkBox do Lembrar-me desmarcado
        if (prefs.get(USER_PREF_LEMBRAR_LOGIN, "value").equals("0") || (prefs.get(USER_PREF_LEMBRAR_LOGIN, "value").equals("value"))) {
            jTextFieldUsuario.setText("");
            jCheckBoxLembrar.setSelected(false);
        } else { //se for igual a 1, seleciona o checkBox do Lembrar-me
            jCheckBoxLembrar.setSelected(true);
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

        jPanel1 = new javax.swing.JPanel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jLabelMensagens = new javax.swing.JLabel();
        jButtonEntrar = new javax.swing.JButton();
        jButtonSair = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jCheckBoxLembrar = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPasswordFieldSenha = new javax.swing.JPasswordField();
        jButtonConfigurar = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(420, 412));
        setMinimumSize(new java.awt.Dimension(420, 412));
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(400, 410));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(87, 170, 241));

        jTextFieldUsuario.setBackground(new java.awt.Color(87, 170, 241));
        jTextFieldUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldUsuario.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextFieldUsuario.setToolTipText("");
        jTextFieldUsuario.setBorder(null);
        jTextFieldUsuario.setSelectionColor(new java.awt.Color(255, 255, 255));
        jTextFieldUsuario.setVerifyInputWhenFocusTarget(false);
        jTextFieldUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUsuarioActionPerformed(evt);
            }
        });

        jLabelMensagens.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelMensagens.setForeground(new java.awt.Color(245, 2, 46));
        jLabelMensagens.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButtonEntrar.setBackground(new java.awt.Color(0, 112, 192));
        jButtonEntrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonEntrar.setText("Entrar");
        jButtonEntrar.setBorder(null);
        jButtonEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEntrar.setFocusPainted(false);
        jButtonEntrar.setFocusable(false);
        jButtonEntrar.setRequestFocusEnabled(false);
        jButtonEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonEntrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonEntrarMouseExited(evt);
            }
        });
        jButtonEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntrarActionPerformed(evt);
            }
        });

        jButtonSair.setBackground(new java.awt.Color(217, 81, 51));
        jButtonSair.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonSair.setText("Sair");
        jButtonSair.setBorder(null);
        jButtonSair.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSair.setFocusPainted(false);
        jButtonSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonSairMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonSairMouseExited(evt);
            }
        });
        jButtonSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user.png"))); // NOI18N

        jCheckBoxLembrar.setBackground(new java.awt.Color(87, 170, 241));
        jCheckBoxLembrar.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jCheckBoxLembrar.setText("Lembrar-me");
        jCheckBoxLembrar.setFocusPainted(false);
        jCheckBoxLembrar.setRequestFocusEnabled(false);
        jCheckBoxLembrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLembrarActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/padlock.png"))); // NOI18N
        jLabel1.setToolTipText("");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/userLogin.png"))); // NOI18N
        jLabel6.setToolTipText("");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setEnabled(false);
        jSeparator1.setRequestFocusEnabled(false);

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setEnabled(false);
        jSeparator2.setRequestFocusEnabled(false);

        jPasswordFieldSenha.setBackground(new java.awt.Color(87, 170, 241));
        jPasswordFieldSenha.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPasswordFieldSenha.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jPasswordFieldSenha.setToolTipText("");
        jPasswordFieldSenha.setBorder(null);
        jPasswordFieldSenha.setSelectionColor(new java.awt.Color(255, 255, 255));
        jPasswordFieldSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldSenhaActionPerformed(evt);
            }
        });

        jButtonConfigurar.setBackground(new java.awt.Color(87, 170, 241));
        jButtonConfigurar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/settings24.png"))); // NOI18N
        jButtonConfigurar.setBorderPainted(false);
        jButtonConfigurar.setFocusable(false);
        jButtonConfigurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigurarActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabelMensagens, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(jPasswordFieldSenha))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(jTextFieldUsuario))
                        .addComponent(jButtonSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonEntrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBoxLembrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonConfigurar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConfigurar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxLembrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMensagens, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSair, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonEntrar, jButtonSair});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseEntered

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        point.x = evt.getX();
        point.y = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        Point p = this.getLocation();
        this.setLocation(p.x + evt.getX() - point.x, p.y + evt.getY() - point.y);
    }//GEN-LAST:event_formMouseDragged

    private void jButtonEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEntrarActionPerformed
        TelaConfiguracao telaConfiguracao = new TelaConfiguracao();
        if (prefs.get(telaConfiguracao.DATABASE_PATH, "value").equals("value") || prefs.get(telaConfiguracao.DATABASE_PATH, "value").equals("")) {
            JOptionPane.showMessageDialog(this, "Local do Banco de Dados não definido. Clique no ícone de Configuração e indique o caminho correto do arquivo.");
            return;
        }

        String login = jTextFieldUsuario.getText();
        String senha = jPasswordFieldSenha.getText();
        if (daoUsuario.verificaBloqueio(login, senha)) {
            jLabelMensagens.setText("<html><center>Usuário bloqueado.</center>"
                    + "Entre em contato com um administrador.</html>");
        } else if (daoUsuario.checarLogin(login, senha)) {
            jLabelMensagens.setForeground(new Color(18, 102, 28));
            jLabelMensagens.setText("Login efetuado com sucesso!");
            System.setProperty("login", jTextFieldUsuario.getText());

            //Se o checkBox Lembrar-me for selecionado, salva as preferências de nome de usuário e também do checkBox selecionado
            if (jCheckBoxLembrar.isSelected()) {
                prefs.put(USER_PREF_USUARIO, jTextFieldUsuario.getText());
                prefs.putInt(USER_PREF_LEMBRAR_LOGIN, 1);
                //Se não, salva apenas o checkBox como 0 para indicar que não deve abir a tela de login com ele marcado
            } else {
                prefs.putInt(USER_PREF_LEMBRAR_LOGIN, 0);
            }

            TelaCarregamentoLogin telaCarregamentoLogin = new TelaCarregamentoLogin();
            Thread threadCarregamento = new Thread() {
                public void run() {
                    try {
                        //A threadCarregamento fica 1,5 segundo "parada" para que o usuário observe que o login foi
                        //trealizad com sucesso

                        sleep(1500);

                        //Depois desse 1,5 segundo, a tela de login desaparece e a tela de carregamento é iniciada, indicando o início carregamento do sistema
                        telaLogin.dispose();
                        //Abertura da tela de carregamento
                        telaCarregamentoLogin.setVisible(true);
                        //Aguarda 9 segundos para o progresso de carregamento terminar por completo, nisso as duas thread estão rodando juntas desde o início
                        sleep(5000);

                        //Assim que acabar os 9 segundos de carregamento e tela é retirada
                        telaCarregamentoLogin.dispose();
                        //E a tela principal do sistema é aberta para o usuário
                        TelaSistema telaSistema = new TelaSistema();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TelaLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            //Inicio da thread de carregamento com barra de progresso
            telaCarregamentoLogin.threadCarregamento.start();
            //Inicio da thread de carregamento
            threadCarregamento.start();

        } else if (login.isEmpty() || senha.isEmpty()) {
            jLabelMensagens.setText("Informe todos os dados.");
        } else {
            Point p = this.getLocation();
            new Thread() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 6; i++) {
                            telaLogin.setLocation(p.x - 10, p.y);
                            sleep(20);
                            telaLogin.setLocation(p.x + 10, p.y);
                            sleep(20);
                        }
                        telaLogin.setLocation(p.x, p.y);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TelaLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
            jLabelMensagens.setText("Dados inválidos. Tente novamente.");
        }
    }//GEN-LAST:event_jButtonEntrarActionPerformed

    private void jButtonEntrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEntrarMouseExited
        jButtonEntrar.setBackground(new Color(0, 112, 192));
        jButtonEntrar.setForeground(Color.BLACK);
    }//GEN-LAST:event_jButtonEntrarMouseExited

    private void jButtonEntrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEntrarMouseEntered
        jButtonEntrar.setBackground(new Color(235, 235, 235));
        jButtonEntrar.setForeground(new Color(0, 112, 192));
    }//GEN-LAST:event_jButtonEntrarMouseEntered

    private void jButtonSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonSairActionPerformed

    private void jButtonSairMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSairMouseExited
        jButtonSair.setBackground(new Color(217, 81, 51));
        jButtonSair.setForeground(Color.BLACK);
    }//GEN-LAST:event_jButtonSairMouseExited

    private void jButtonSairMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSairMouseEntered
        jButtonSair.setBackground(new Color(235, 235, 235));
        jButtonSair.setForeground(new Color(217, 81, 51));
    }//GEN-LAST:event_jButtonSairMouseEntered

    private void jCheckBoxLembrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLembrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxLembrarActionPerformed

    private void jTextFieldUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldUsuarioActionPerformed

    private void jPasswordFieldSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordFieldSenhaActionPerformed

    private void jButtonConfigurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigurarActionPerformed
        TelaConfiguracao telaConfiguracao = new TelaConfiguracao();
        telaConfiguracao.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButtonConfigurarActionPerformed

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonFecharActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonConfigurar;
    private javax.swing.JButton jButtonEntrar;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JButton jButtonSair;
    private javax.swing.JCheckBox jCheckBoxLembrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelMensagens;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordFieldSenha;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
