
/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * ZeusMsn.java
 *
 * Created on 7-lug-2009, 21.35.26  Francesco Lopez
 */

package gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.sf.jml.DisplayPictureListener;
import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnObject;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.event.MsnMessengerAdapter;
import net.sf.jml.event.MsnSwitchboardAdapter;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnUnknownMessage;
import net.sf.jml.message.p2p.DisplayPictureRetrieveWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zeusmsn.Generic;
import zeusmsn.MenuContatti;


/**
 *
 * @author Francesco Lopez
 */
public class ZeusMsn extends javax.swing.JFrame{

    private String email;
    private String password;
    private int stato;
    MsnMessenger messenger;
    private  Vector vtUtenti = new Vector();
    private MenuContatti oUtenti;

    private int statoGui = Generic.statoINDEFINITO;

    public DefaultTreeModel treeModel;
    
    
    private static final Log log = LogFactory.getLog(ZeusMsn.class);

    /**
     * List of contacts.
     */
    private MsnContact[] contacts;

    /**
     * Index of the processend contact.
     */
    private int index = 0;

    /**
     * Number of retrieved avatars.
     */
    private int processed = 0;

    private int MAX_AVATAR_RETRIEVED = 500;

    /**
     * MsnContacts with null avatars.
     */
    private Set<String> nullAvatars = new HashSet<String>();

    /**
     * MsnContacts with unavailable avatars.
     */
    private Set<String> unavailableAvatars = new HashSet<String>();

    /**
     * MsnContacts whose avataras has been retrieved.
     */
    private Set<String> goodAvatars = new HashSet<String>();

    /** Creates new form ZeusMsn */
    public ZeusMsn() {
        
        initComponents();
        
        setTitle("Zeus MSN");
        
        //impostaPannelli();
        settaStatoGui(Generic.statoINDEFINITO);
        // setta le immagini
        settaImmagini();

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu contattiMenu = new JMenu("Contatti");
        menuBar.add(contattiMenu);

        JMenu azioniMenu = new JMenu("Azioni");
        menuBar.add(azioniMenu);

        JMenu strumentiMenu = new JMenu("Strumenti");
        menuBar.add(strumentiMenu);

        JMenu helpMenu = new JMenu("?");
        menuBar.add(helpMenu);

        JMenuItem openItem = new JMenuItem("Disconnetti");
        openItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                       stop();
            }

        });
        fileMenu.add(openItem);

        JMenu statoMenu = new JMenu("Stato");
        fileMenu.add(statoMenu);

        JMenuItem openDisp = new JMenuItem("Disponibile");
        openDisp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                  messenger.getOwner().setStatus(MsnUserStatus.ONLINE);
            }

        });
        statoMenu.add(openDisp);
        
         JMenuItem openOccupato = new JMenuItem("Occupato");
        openOccupato.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                  messenger.getOwner().setStatus(MsnUserStatus.BUSY);
            }

        });
        statoMenu.add(openOccupato);

         JMenuItem openNoPc = new JMenuItem("Non al computer");
        openNoPc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                  messenger.getOwner().setStatus(MsnUserStatus.AWAY);
            }

        });
        statoMenu.add(openNoPc);

         JMenuItem openInv = new JMenuItem("Invisibile");
        openInv.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                     messenger.getOwner().setStatus(MsnUserStatus.HIDE);
            }

        });
        statoMenu.add(openInv);


        JMenuItem openItem3 = new JMenuItem("Vai a");
        openItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {

            }

        });
        fileMenu.add(openItem3);
        JMenuItem openItem4 = new JMenuItem("Invia un singolo file...");
        openItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {

            }

        });
        fileMenu.add(openItem4);
        JMenuItem openItem5 = new JMenuItem("Visualizza cronologia messaggi...");
        openItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {

            }

        });
        fileMenu.add(openItem5);
        JMenuItem openItem6 = new JMenuItem("Chiudi");
        openItem6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                
                System.exit(0);
            }

        });
        fileMenu.add(openItem6);

        JMenuItem contattiItem1 = new JMenuItem("Aggiungi contatto...");
        contattiItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem1);

        JMenuItem contattiItem2 = new JMenuItem("Modifica contatto...");
        contattiItem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem2);

        JMenuItem contattiItem3 = new JMenuItem("Elimina contatto...");
        contattiItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem3);

        JMenuItem contattiItem4 = new JMenuItem("Crea gruppo...");
        contattiItem4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem4);

        JMenuItem contattiItem5 = new JMenuItem("Crea categoria...");
        contattiItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem5);

        JMenuItem contattiItem6 = new JMenuItem("Modifica categoria...");
        contattiItem6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem6);

       JMenuItem contattiItem7 = new JMenuItem("Elimina categoria...");
        contattiItem7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem7);

        JMenuItem contattiItem8 = new JMenuItem("Salva elenco contatti...");
        contattiItem8.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem8);

        JMenuItem contattiItem9 = new JMenuItem("Importa elenco contatti...");
        contattiItem9.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        contattiMenu.add(contattiItem9);

        JMenuItem azioniItem1 = new JMenuItem("Invia un messaggio istantaneo...");
        azioniItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        azioniMenu.add(azioniItem1);
         JMenuItem azioniItem2 = new JMenuItem("Invia altro");
        azioniItem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        azioniMenu.add(azioniItem2);


        JMenuItem strumentiItem1 = new JMenuItem("Sempre in primo piano");
        strumentiItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        strumentiMenu.add(strumentiItem1);


        JMenuItem strumentiItem2 = new JMenuItem("Emoticon...");
        strumentiItem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        strumentiMenu.add(strumentiItem2);

         JMenuItem strumentiItem3 = new JMenuItem("Cambia immagine personale...");
        strumentiItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        strumentiMenu.add(strumentiItem3);

        JMenuItem strumentiItem4 = new JMenuItem("Animoticon...");
        strumentiItem4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        strumentiMenu.add(strumentiItem4);

        JMenuItem strumentiItem5 = new JMenuItem("Opzioni di configurazione...");
        strumentiItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        strumentiMenu.add(strumentiItem5);

        JMenuItem helpItem1 = new JMenuItem("Guida...");
        helpItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        helpMenu.add(helpItem1);

        JMenuItem helpItem2 = new JMenuItem("Informazioni su Zeus MSN...");
        helpItem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {


            }

        });
        helpMenu.add(helpItem2);

        setJMenuBar(menuBar);

    }

    private void settaImmagini() {
        // percorso
	    String path = "Images/";

	    ImageIcon sfd        = new ImageIcon(path + "zeus.png");
	    //Generic.getURL(
        lblsfondo.setIcon(sfd);
     
    }

    private void settaStatoGui(int nuovoStato){
        
        this.statoGui = nuovoStato;

         Color c=new Color(0,255,255,15);
        
        p1.setVisible((statoGui == Generic.statoINDEFINITO));
        p2.setVisible((statoGui == Generic.statoINSERIMENTO));
        p1.setOpaque(true);
        p2.setOpaque(true);
        p1.setBackground(c);
        p2.setBackground(c);

        // disabilita i campi che non servono
        settaAvvioMenu();
        ripristina();

    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        sfondo = new javax.swing.JPanel();
        lblsfondo = new javax.swing.JLabel();
        p2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaFrase = new javax.swing.JTextArea();
        p1 = new javax.swing.JPanel();
        jtfUsername = new javax.swing.JTextField();
        jtpPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jbtConnetti = new javax.swing.JButton();
        jcbStato = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jpImmagine = new javax.swing.JPanel();
        lblImmagine = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        tree.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                treeKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tree);

        jScrollPane2.setHorizontalScrollBar(null);

        jtaFrase.setColumns(20);
        jtaFrase.setRows(5);
        jtaFrase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtaFraseFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(jtaFrase);

        javax.swing.GroupLayout p2Layout = new javax.swing.GroupLayout(p2);
        p2.setLayout(p2Layout);
        p2Layout.setHorizontalGroup(
            p2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
            .addGroup(p2Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        p2Layout.setVerticalGroup(
            p2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jtfUsername.setSelectionColor(new java.awt.Color(204, 0, 0));
        jtfUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtfUsernameMousePressed(evt);
            }
        });
        jtfUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfUsernameFocusGained(evt);
            }
        });
        jtfUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtfUsernameKeyTyped(evt);
            }
        });

        jtpPassword.setEchoChar('#');
        jtpPassword.setSelectionColor(new java.awt.Color(204, 0, 0));
        jtpPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtpPasswordMousePressed(evt);
            }
        });
        jtpPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtpPasswordFocusGained(evt);
            }
        });
        jtpPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtpPasswordKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel1.setText("Password:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel2.setText("Username:");

        jbtConnetti.setText("Connetti");
        jbtConnetti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtConnettiActionPerformed(evt);
            }
        });

        jcbStato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Disponibile", "Occupato", "Non al computer", "Invisibile" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabel3.setText("Accedi come:");

        jpImmagine.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));

        javax.swing.GroupLayout jpImmagineLayout = new javax.swing.GroupLayout(jpImmagine);
        jpImmagine.setLayout(jpImmagineLayout);
        jpImmagineLayout.setHorizontalGroup(
            jpImmagineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImmagine, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
        );
        jpImmagineLayout.setVerticalGroup(
            jpImmagineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImmagine, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout p1Layout = new javax.swing.GroupLayout(p1);
        p1.setLayout(p1Layout);
        p1Layout.setHorizontalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p1Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jpImmagine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbtConnetti, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(p1Layout.createSequentialGroup()
                                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtpPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(jtfUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)))
                            .addGroup(p1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcbStato, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        p1Layout.setVerticalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jpImmagine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbStato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtConnetti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(131, 131, 131))
        );

        javax.swing.GroupLayout sfondoLayout = new javax.swing.GroupLayout(sfondo);
        sfondo.setLayout(sfondoLayout);
        sfondoLayout.setHorizontalGroup(
            sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sfondoLayout.createSequentialGroup()
                    .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sfondoLayout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(lblsfondo, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        sfondoLayout.setVerticalGroup(
            sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sfondoLayout.createSequentialGroup()
                .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sfondoLayout.createSequentialGroup()
                    .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(sfondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sfondoLayout.createSequentialGroup()
                    .addComponent(lblsfondo, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(79, Short.MAX_VALUE)))
        );

        sfondo.setBounds(0, 0, 310, 490);
        desktop.add(sfondo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settaAvvioMenu(){

        
        //statoMenu.setEnable(false);
    }

    private void jtfUsernameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfUsernameMousePressed
       
}//GEN-LAST:event_jtfUsernameMousePressed

    private void jtfUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfUsernameFocusGained
        
}//GEN-LAST:event_jtfUsernameFocusGained

    private void jtfUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfUsernameKeyTyped
        
}//GEN-LAST:event_jtfUsernameKeyTyped

    private void jtpPasswordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtpPasswordMousePressed
        
}//GEN-LAST:event_jtpPasswordMousePressed

    private void jtpPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtpPasswordFocusGained
        
}//GEN-LAST:event_jtpPasswordFocusGained

    private void jtpPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtpPasswordKeyTyped
        
}//GEN-LAST:event_jtpPasswordKeyTyped

    private void jbtConnettiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtConnettiActionPerformed
        // AVVIA LA CONNESSIONE
        if(verificaField()){
           inizializza();
           avvia();
           
        }
    }//GEN-LAST:event_jbtConnettiActionPerformed

    /**
     *  Visualizza i messaggi sul pulsante e sul loader
     */
    private void inizializza(){
         jbtConnetti.setIcon(new ImageIcon("Images/Ico/loader.gif"));
         jbtConnetti.setLabel("Caricamento in corso...");
    }

    /**
     *  Reimposta i messaggi come erano in precedenza
     */
    private void ripristina(){
         jbtConnetti.setIcon(new ImageIcon(""));
         jbtConnetti.setLabel("Connetti");
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMouseClicked
          if (evt.getClickCount() == 2) {
            if (!tree.isEnabled()) {
                return;
            }

            tree.setEnabled(false);
            int selRow = tree.getRowForLocation(evt.getX(), evt.getY());
            if (selRow != -1) {
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                TreePath selPath = tree.getPathForLocation(evt.getX(), evt.getY());
                eseguiComando((DefaultMutableTreeNode) selPath.getLastPathComponent());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            tree.setEnabled(true);

          }
    }//GEN-LAST:event_treeMouseClicked

    private void treeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_treeKeyReleased
        String press = evt.getKeyText(evt.getKeyCode());
        if (press.equals("Enter")) {
            if (!tree.isEnabled()) {
                return;
            }
            tree.setEnabled(false);
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            eseguiComando((DefaultMutableTreeNode) (tree.getSelectionPath().getLastPathComponent()));
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            tree.setEnabled(true);
        }
    }//GEN-LAST:event_treeKeyReleased

    private void jtaFraseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtaFraseFocusLost
        setTopic();
    }//GEN-LAST:event_jtaFraseFocusLost

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // La classe Toolkit iteragisce con il sistema operativo
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize(); //restituisce la dimensione dello schermo come oggetto Dimension
        int screenHeight = screenSize.height / 2; //Ottieni la metà della lunghezza dello schermo
        int screenWidth = screenSize.width / 2; //Ottieni la metà della larghezza dello schermo
        this.tree.setSize(screenHeight, screenWidth);
        //disegna la label al centro
        //int centroAscissaImage= screenWidth - lblImmagine.getWidth(null)/2;
        //int centroOrdinataImage=screenHeight - lblImmagine.getHeight(null)/2;
        //g.drawImage(image,centroAscissaImage,centroOrdinataImage,null);


    }//GEN-LAST:event_formWindowOpened

    public void setVisible(boolean b)
    {
        if(b)
            Generic.centraGui(this);
        super.setVisible(b);
    }

    /**
     **/
    private void avvia() {



        this.email = this.jtfUsername.getText();
        this.password = this.jtpPassword.getText();

        // Di default a zero 0 - Online 1 - Occupato 2 - Non al Computer 3 - Invisibile
        this.stato = 0;
        if (jcbStato.getSelectedIndex() != -1){
            stato = jcbStato.getSelectedIndex();
        }


        //Crea l'istanza Msn
        messenger = MsnMessengerFactory.createMsnMessenger(email, password);

        //Di default supporta tutti i protocolli
        messenger.setSupportedProtocol(new MsnProtocol[] { MsnProtocol.MSNP11 });
        
        // default dello stato ad OnLIne
        if(stato == 1){
             messenger.getOwner().setInitStatus(MsnUserStatus.BUSY );
        }else if(stato == 2 ){
             messenger.getOwner().setInitStatus(MsnUserStatus.AWAY);
        }else if(stato == 3 ){
             messenger.getOwner().setInitStatus(MsnUserStatus.HIDE);
        }else{
             messenger.getOwner().setInitStatus(MsnUserStatus.ONLINE);
        }

        //log incoming message
        messenger.setLogIncoming(true);

        //log outgoing message
        messenger.setLogOutgoing(true);
        
        messenger.addListener(new MsnListener());
        messenger.login();

        messenger.addContactListListener(new MsnContactListAdapter()
        {
			public void contactStatusChanged(MsnMessenger msn, MsnContact con)
			{
				//System.out.println(con.getDisplayName());
				//System.out.println(con.getPersonalMessage());
				//System.out.println(((MsnContactImpl) con).getCurrentMedia());
			}

			public void contactListInitCompleted(MsnMessenger messenger)
			{
				if(listaContatti()){
                   settaStatoGui(Generic.statoINSERIMENTO);
                }


			}


	});

    initMessenger(messenger);
    
    // imposta il topic
    this.jtaFrase.setText(this.getTopic());


	//messenger.getOwner().setDisplayName("Blah");
	//messenger.getOwner().getDisplayName();




    }

    /**
     * Termina la connessione
     */
    private void stop(){
        
        
        /*
        try
        {
                Thread.sleep(10000);
        } catch (InterruptedException e)
        {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }*/
        messenger.logout();
        settaStatoGui(Generic.statoINDEFINITO);
        //if(tree != null)
        tree.removeAll();
        tree.repaint();

    }

     /**
     *  ricava il topic 
     */
    private String getTopic(){

       return "" +  messenger.getOwner().getPersonalMessage();
    }

      /**
     *  settaggio del topic
     */
    private void setTopic(){

        messenger.getOwner().setPersonalMessage(this.jtaFrase.getText());
    }


    private boolean listaContatti()
	{

        boolean caricato = false;
        
       
        oUtenti = new MenuContatti();
        oUtenti.Id      =  0;
        oUtenti.IdPadre = -1;
        oUtenti.Descr   = "ROOT";

        DefaultMutableTreeNode nodoRadice = new DefaultMutableTreeNode(oUtenti);
        treeModel = new DefaultTreeModel(nodoRadice);
        tree.setModel(treeModel);

        
       

        // GRUPPO
        oUtenti = new MenuContatti();
        oUtenti.Id      =  0;
        oUtenti.IdPadre = -1;
        oUtenti.Descr   = "Contatti";
        oUtenti.Enable =true;
        oUtenti.Visible=true;
        oUtenti.Opzioni=true;
        oUtenti.IconaAbil="";
        oUtenti.IconaDis="";
        DefaultMutableTreeNode contattiGruppo = new DefaultMutableTreeNode(oUtenti);
        treeModel.insertNodeInto(contattiGruppo, nodoRadice, nodoRadice.getChildCount());
        nodoRadice.add(contattiGruppo);


        // Aggiunge ricorsivamente le opzioni al menu
        //aggiungiNodo(nodoRadice,oUtenti,vtUtenti,true);
        tree.setCellRenderer(new RendContatti());
        String  PercorsoStringa= "Images/Ico/";

        vtUtenti = new Vector();
        MsnContact[] cons = messenger.getContactList().getContacts();
        int i=0;
        for (MsnContact con : cons) {



            oUtenti = new MenuContatti();

            oUtenti.nickname = con.getDisplayName();
            oUtenti.email    = "" + con.getEmail();
            oUtenti.stato    = "" + con.getStatus();
            oUtenti.Descr     =  con.getPersonalMessage();

            oUtenti.Id = 0;
            oUtenti.IdPadre=0;
            oUtenti.IdApp = i;
            oUtenti.Posizione = i;
            oUtenti.Command="messaggio";
            oUtenti.Enable =true;
            oUtenti.Visible=true;
            oUtenti.Opzioni=true;
            oUtenti.IconaAbil="";
            oUtenti.IconaDis="";

            vtUtenti.add(oUtenti);

            i= i+1;
            // Aggiunge ricorsivamente le opzioni al menu
            aggiungiNodo(contattiGruppo,oUtenti,vtUtenti,true);

            // default dello stato ad OnLIne
            /*
            if(gestione.stato == 1){
                

                messenger.getOwner().setInitStatus(MsnUserStatus.BUSY );
            }else if(gestione.stato == 2 ){
                 messenger.getOwner().setInitStatus(MsnUserStatus.AWAY);
            }else if(gestione.stato == 3 ){
                 messenger.getOwner().setInitStatus(MsnUserStatus.HIDE);
            }else{
                 messenger.getOwner().setInitStatus(MsnUserStatus.ONLINE);
            }

*/

            //DefaultMutableTreeNode sottonodo1 = new DefaultMutableTreeNode(oUtenti.nickname);
            //treeModel.insertNodeInto(sottonodo1, contattiGruppo, contattiGruppo.getChildCount());
            //contattiGruppo.add(sottonodo1);


        }
        
        
        // Se nessuna opzione disponibile
        if (vtUtenti==null || vtUtenti.size()==0){
            oUtenti = new MenuContatti();
            oUtenti.nickname = "Non disponibile";
            oUtenti.email    = "Non disponibile";
            oUtenti.stato    = "Non disponibile";
            oUtenti.Id = -1;
            oUtenti.IdPadre=0;
            oUtenti.IdApp = 0;
            oUtenti.Posizione = 0;
            oUtenti.Descr="Nessun contatto disponibile";
            oUtenti.Command="";
            oUtenti.Enable =false;
            oUtenti.Visible=false;
            oUtenti.Opzioni=false;
            oUtenti.IconaAbil="";
            oUtenti.IconaDis="";

            
            vtUtenti.addElement(oUtenti);

            // Aggiunge ricorsivamente le opzioni al menu
            aggiungiNodo(nodoRadice,oUtenti,vtUtenti,true);
            //DefaultMutableTreeNode sottonodo1 = new DefaultMutableTreeNode(oUtenti.nickname);
            //treeModel.insertNodeInto(sottonodo1, contattiGruppo, contattiGruppo.getChildCount());
            //contattiGruppo.add(sottonodo1);

            

        }


       
       
        tree.setRootVisible(false);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        treeModel.reload();
        tree.scrollPathToVisible(new TreePath(nodoRadice.getPath()));
        tree.setSelectionRow(0);

            
        int row = 0;
        if (tree.getRowCount() >= 0) {
            tree.expandRow(row);
        }



        return caricato = true;

    }

 
    public void aggiungiNodo(DefaultMutableTreeNode padre,
                        MenuContatti gestione,
                        Vector vtUtenti,
                        boolean isvisibile) {

            DefaultMutableTreeNode gestioneNodo=null;
            // Aggiungo l'opzione
            gestioneNodo = new DefaultMutableTreeNode(gestione);
            treeModel.insertNodeInto(gestioneNodo, padre, padre.getChildCount());

          if (isvisibile) {
              tree.scrollPathToVisible(new TreePath(gestioneNodo.getPath()));
           }



            /*DefaultMutableTreeNode gestioneNodo=null;

        if (gestione.IdPadre==-1){
            // RootNode già aggiunto
            gestioneNodo= (DefaultMutableTreeNode)treeModel.getRoot();
        }else{
            // Aggiungo l'opzione
            gestioneNodo = new DefaultMutableTreeNode(gestione);
            treeModel.insertNodeInto(gestioneNodo, padre, padre.getChildCount());


        }


      

       
        Vector vtFigli = ordinaFigli(vtUtenti,gestione.Id);
        if ((vtFigli!=null) && (vtFigli.size() > 0)){
            for (int i=0;i<vtFigli.size();i++){
            MenuContatti figli = (MenuContatti)vtFigli.elementAt(i);
            // Per ogni figlio dell'opzione corrente
            if (gestione.IdPadre==0)
               aggiungiNodo(gestioneNodo,figli,vtUtenti,true);
            else
               aggiungiNodo(gestioneNodo,figli,vtUtenti,false);
            }
        }


*/


    }

   /**
    *
    * Verifica che i campi siano valorizzati
    */
    private boolean verificaField(){

         // Descrizione principale
         if (Generic.strVuota(jtfUsername.getText()))
         {
            Generic.notifica("Inserire l'indirizzo e-mail prima di continuare.", 3);
            jtfUsername.requestFocus();
            return false;
         }

          // Descrizione secondaria
         if (Generic.strVuota(this.jtpPassword.getText()))
         {
            Generic.notifica("Inserire la password prima di continuare.", 3);
            jtpPassword.requestFocus();
            return false;
         }



        return true;
    }


  public Vector ordinaFigli(Vector vtUtenti,int id){
        Vector vtFigli = new Vector();

        
        // Esce nel caso di ID = 0 (Vettore Menù VUOTO)
        if (id < 0)
            return vtFigli;


        // Riempio il vettore con i figli dell'opzione ID
        for (int i=0;i<vtUtenti.size();i++){
            MenuContatti contatti = (MenuContatti)vtUtenti.elementAt(i);
            if (contatti.IdPadre == id){
                vtFigli.addElement(contatti);
            }
        }


        if (vtFigli==null)
           return null;


        // Ordino il vettore per posizione
	/*
        int lunghezza = vtFigli.size();
	    int npassaggi = lunghezza-1;
	    int ultimodisordinato= lunghezza-1;

	    for (int i=0;i< npassaggi;i++){
	        for (int j=0;j< ultimodisordinato;j++){
	            MenuContatti cont1 = (MenuContatti)vtFigli.elementAt(j);
	            MenuContatti cont2 = (MenuContatti)vtFigli.elementAt(j+1);
	            if (cont1.Posizione > cont2.Posizione){
	                vtFigli.setElementAt(cont2,j);
                    vtFigli.setElementAt(cont1,j+1);
	            }
	        }
	        ultimodisordinato--;
	    }*/

	    return vtFigli;
	}


    public String getEmail() {
        return email;
    }


    private void retrieveContactsAvatars(MsnMessenger messenger) {

		contacts = messenger.getContactList().getContacts();
		index = 0;
        // look for someone
        //  for (int i = 0 ; i < contacts.length ; i++) {
        //      if (contacts[i].getEmail().equals(
        //          Email.parseStr("XXX@hotmail.com"))) {
        //          index = i;
        //          break;
        //      }
        //  }
        retrieveNextContactAvatar(messenger);
	}


    private void retrieveNextContactAvatar(MsnMessenger messenger) {

		while (index < contacts.length && processed < MAX_AVATAR_RETRIEVED) {

			// Get the contact
			MsnContact contact = contacts[index++];

			// Get the MSnObject
			MsnObject avatar = contact.getAvatar();

			// Check if it exists
			if (avatar != null) {

				// Retrieve the avatar
				messenger.retrieveDisplayPicture(
						avatar,
						new DisplayPictureListener() {

							public void notifyMsnObjectRetrieval(
									MsnMessenger messenger,
									DisplayPictureRetrieveWorker worker,
									MsnObject msnObject,
									ResultStatus result,
                                                                    byte[] resultBytes,
                                                                    Object context) {

                                                                                                // Log the result
                                                                                                log.info("Finished Avatar retrieval " + result);

                                                                                                // Check for the value
                                                                                                if (result == ResultStatus.GOOD) {
                                                                    // Create a new file to store the avatar
                                                                    File storeFile = new File (
                                                                            "avatar" + System.currentTimeMillis() + ".png");
                                                                    try {
                                                                        FileOutputStream storeStream = new FileOutputStream(storeFile);
                                                                        storeStream.write(resultBytes);
                                                                        goodAvatars.add(msnObject.getCreator());
                                                                        storeStream.close();
                                                                    }
                                                                    catch (FileNotFoundException e) {
                                                                        System.err.println("Critical error: Unable to find file we just created.");
                                                                    }
                                                                    catch (IOException e) {
                                                                        System.err.println("Critical error: Unable to write data to file system.");
                                                                    }
								}
								else {
									unavailableAvatars.add(
											msnObject.getCreator());
								}

								// Process next contact
								retrieveNextContactAvatar(messenger);
							}
						});

				// Stop launching workers
				processed++;
				break;
			}
			else {
				nullAvatars.add(contact.getEmail().getEmailAddress());
			}
		}

		// Check for finalization
		if (index >= contacts.length || processed >= MAX_AVATAR_RETRIEVED) {
			System.out.println("********* FINISHED CONTACTS AVATAR RETRIEVAL *********");

			// Set the list of all contacts with null avatars
			System.out.println("**** NULL Avatars ****");
			for (String contact: nullAvatars) {
				String status = messenger.getContactList().getContactByEmail(
						Email.parseStr(contact)).getStatus().toString();
				System.out.println(contact + " " + status);
			}

			// Set the list of contacts that couldn't retrieve the avatar
			System.out.println("**** Unavailable Avatars ****");
			for (String contact: unavailableAvatars) {
				String status = messenger.getContactList().getContactByEmail(
						Email.parseStr(contact)).getStatus().toString();
				System.out.println(contact + " " + status);
			}

			// Set the list of contact that we have retrieved the avatar
			System.out.println("**** Retrieved Avatars ****");
			for (String contact: goodAvatars) {
				String status = messenger.getContactList().getContactByEmail(
						Email.parseStr(contact)).getStatus().toString();
				System.out.println(contact + " " + status);
			}

			// Set the total of contacts and the total of processed ones
			System.out.println("Total contacts: " +
					 messenger.getContactList().getContacts().length);
			System.out.println("Processed contacts: " +
					           (nullAvatars.size() +
					            unavailableAvatars.size() +
					            goodAvatars.size() ));

			// Close the messenger
			messenger.logout();
		}

	}

    protected void initMessenger(MsnMessenger messenger) {

        try {
            //"./resource/UserTile/headset.png"
        MsnObject displayPicture = MsnObject.getInstance(getEmail(), "./Images/Ico/online.jpg");
             System.out.println(messenger.getOwner().getDisplayPicture());
             messenger.getOwner().setInitDisplayPicture(displayPicture);
                this.lblImmagine.setIcon(new ImageIcon("" + messenger.getOwner().getDisplayPicture()) );
        } catch (Exception ex) {
                log.warn("non è possibile caricare lo user tile.",ex);
        }


        messenger.addMessengerListener(new MsnMessengerAdapter() {

             public void contactListInitCompleted(MsnMessenger messenger) {
	            log.info(messenger + " contact list init completeted");
	            retrieveContactsAvatars(messenger);
	     }


            public void loginCompleted(MsnMessenger messenger) {
                System.out.println(messenger.getOwner().getEmail() + " login");
                abilitaDisabilitaPannelli();
            }

            public void logout(MsnMessenger messenger) {
                System.out.println(messenger.getOwner().getEmail() + " logout");
            }

            @Override
            public void exceptionCaught(MsnMessenger messenger,
                    Throwable throwable) {
                System.out.println("caught exception: " + throwable);
            }
        });

    }


    
    private void abilitaDisabilitaPannelli(){
        
        p2.setVisible(true);
        p1.setVisible(false);
        
    }

    private static class MsnListener extends MsnAdapter
	{

		public void exceptionCaught(MsnMessenger messenger, Throwable throwable)
		{
			log.error(messenger + throwable.toString(), throwable);
		}

		public void loginCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " login complete ");
		}

		public void logout(MsnMessenger messenger)
		{
			log.info(messenger + " logout");
		}

		public void instantMessageReceived(MsnSwitchboard switchboard,
				MsnInstantMessage message, MsnContact friend)
		{
			log.info(switchboard + " recv instant message " + message);
			//switchboard.sendMessage(message, false);
		}

		public void systemMessageReceived(MsnMessenger messenger,
				MsnSystemMessage message)
		{
			//log.info(messenger + " recv system message " + message);
		}

		public void controlMessageReceived(MsnSwitchboard switchboard,
				MsnControlMessage message, MsnContact contact)
		{
			//log.info(switchboard + " recv control message from "
			//		+ contact.getEmail());
			//switchboard.sendMessage(message, false);
		}

		public void datacastMessageReceived(MsnSwitchboard switchboard,
				MsnDatacastMessage message, MsnContact friend)
		{
			//log.info(switchboard + " recv datacast message " + message);

			//switchboard.sendMessage(message, false);
		}

		public void unknownMessageReceived(MsnSwitchboard switchboard,
				MsnUnknownMessage message, MsnContact friend)
		{
			log.info(switchboard + " recv unknown message " + message);
		}

		public void contactListInitCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " contact list init completeted");
		}

		public void contactListSyncCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " contact list sync completed");
		}

		public void contactStatusChanged(MsnMessenger messenger,
				MsnContact friend)
		{
			log.info(messenger + " friend " + friend.getEmail()
					+ " status changed from " + friend.getOldStatus() + " to "
					+ friend.getStatus());
		}

		public void ownerStatusChanged(MsnMessenger messenger)
		{
			log.info(messenger + " status changed from "
					+ messenger.getOwner().getOldStatus() + " to "
					+ messenger.getOwner().getStatus());
		}

		public void contactAddedMe(MsnMessenger messenger, MsnContact friend)
		{
			log.info(friend.getEmail() + " add " + messenger);
		}

		public void contactRemovedMe(MsnMessenger messenger, MsnContact friend)
		{
			log.info(friend.getEmail() + " remove " + messenger);
		}

		public void switchboardClosed(MsnSwitchboard switchboard)
		{
			log.info(switchboard + " closed");
		}

		public void switchboardStarted(MsnSwitchboard switchboard)
		{
			log.info(switchboard + " started");
		}

		public void contactJoinSwitchboard(MsnSwitchboard switchboard,
				MsnContact friend)
		{
			log.info(friend.getEmail() + " join " + switchboard);
		}

		public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
				MsnContact friend)
		{
			log.info(friend.getEmail() + " leave " + switchboard);
		}

	}


    private class RendContatti extends DefaultTreeCellRenderer {

        ImageIcon online;
        ImageIcon offline;
        ImageIcon occupato;
        ImageIcon nonpc;
        ImageIcon icodisabilitato;
        ImageIcon openedFolder;
        ImageIcon closedFolder;
        ImageIcon openedFolderdis;
        ImageIcon closedFolderdis;

        public RendContatti() {
            // percorso
            String PercorsoStringa = "Images/Ico/";

            
            online = new ImageIcon(PercorsoStringa + "online.jpg");
            offline = new ImageIcon(PercorsoStringa + "offline.jpg");
            nonpc = new ImageIcon(PercorsoStringa + "nonpc.jpg");
            occupato = new ImageIcon(PercorsoStringa + "occupato.jpg");

            icodisabilitato = new ImageIcon(PercorsoStringa + "PermessoGrigio.gif");
            openedFolder = new ImageIcon(PercorsoStringa + "LibAperto.gif");
            closedFolder = new ImageIcon(PercorsoStringa + "libro.gif");
            openedFolderdis = new ImageIcon(PercorsoStringa + "LibApertoGrigio.gif");
            closedFolderdis = new ImageIcon(PercorsoStringa + "libroGrigio.gif");
           
        }

        public Component getTreeCellRendererComponent(JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {


           MenuContatti gestione = (MenuContatti) ((DefaultMutableTreeNode) value).getUserObject();
           
           if (gestione == null) {
                return this;
            }

           String PercorsoStringa = "Images/Ico/";

           if (!leaf) {
                if (gestione.Opzioni) {
                    if (gestione.IconaAbil.equals("")) {
                        setOpenIcon(openedFolder);
                    } else {
                        setOpenIcon(new ImageIcon(PercorsoStringa + gestione.IconaAbil));
                    }

                    if (gestione.IconaDis.equals("")) {
                        setClosedIcon(closedFolder);
                    } else {
                        setClosedIcon(new ImageIcon(PercorsoStringa + gestione.IconaDis));
                    }

                } else {
                    setOpenIcon(openedFolderdis);
                    setClosedIcon(closedFolderdis);
                }


            }


            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);

            setText(gestione.nickname);

             if (leaf) {
                if (gestione.Opzioni) {
                    if (Generic.strVuota(gestione.IconaAbil)) {

                       setIcon(online);
                       if(gestione.stato.equals("OFFLINE")){
                          setIcon(offline);
                       }
                       if(gestione.stato.equals("BUSY")){
                          setIcon(occupato);
                       }
                       if(gestione.stato.equals("AWAY")){
                          setIcon(nonpc);
                       }


                    } else {
                        setIcon(new ImageIcon(PercorsoStringa + gestione.IconaAbil));
                    }

                } else {
                    if (Generic.strVuota(gestione.IconaAbil)) {
                        setIcon(icodisabilitato);
                    } else {
                       setIcon(new ImageIcon(PercorsoStringa + gestione.IconaDis));
                    }
                }
             }
            return this;
        }
    }




    public void eseguiComando(DefaultMutableTreeNode nodo) {
        if (nodo.isLeaf()) {

            MenuContatti gestione = (MenuContatti) nodo.getUserObject();


            if (gestione.Opzioni) {

               for (MsnContact contact : contacts) {
                    //don't send message to offline contact
                    if (contact.getEmail().toString().equals("" + gestione.email)) {
                        guiMessaggi(gestione, contact);
                    }
               }



            }
        }
    }

    private void guiMessaggi(MenuContatti gestione, MsnContact contatto){


        if (gestione.Command.trim().indexOf("disabilitato") > -1) {
            // Tabelle di Base

            // Clienti 0
        } else if (gestione.Command.trim().equals("messaggio")) {
            try {
                String Titolo = "Conversazione con: " + gestione.nickname;
                
                Chat oGui = new Chat(Titolo,contatto, messenger);
                //false per il richiamo delle stampe
                //oGui.setModal(true);
                oGui.setVisible(true);

            } catch (Exception e) {
            }

            // Autorizzazioni Gruppi Utente
        }


    }




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtConnetti;
    private javax.swing.JComboBox jcbStato;
    private javax.swing.JPanel jpImmagine;
    private javax.swing.JTextArea jtaFrase;
    private javax.swing.JTextField jtfUsername;
    private javax.swing.JPasswordField jtpPassword;
    private javax.swing.JLabel lblImmagine;
    private javax.swing.JLabel lblsfondo;
    private javax.swing.JPanel p1;
    private javax.swing.JPanel p2;
    private javax.swing.JPanel sfondo;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

}
