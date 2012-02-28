
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MancalaGUI.java
 *
 * Created on Jan 30, 2012, 3:13:28 PM
 */
/**
 *
 * @author afurusaw
 */
public class MancalaGUI extends javax.swing.JFrame {

    public MancalaPlayer player[];
    public MancalaGameState gs;
    //for animation
    LinkedList<Object> link = new LinkedList<Object>();
    
    //initial player selection type
    static String player1 = "";
    static String player2 = "";
    
    int startingPoint;
    int stones;
    int currentPlayer;
    

    public MancalaGUI() {
        initComponents();
        InitialFrame.setVisible(true);
        InitialFrame.pack();
    }

    /** Creates new form MancalaGUI */
    public MancalaGUI(String gameType, int startingStones, String player0Name, String player1Name) {
        initComponents();

        try {
            Class<?> cls = Class.forName(gameType + "GameState");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(startingStones);
            gs = (MancalaGameState) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
        player = new MancalaPlayer[2];
        try {
            Class<?> cls = Class.forName(player0Name + "_Player");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(0);
            player[0] = (MancalaPlayer) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
        try {
            Class<?> cls = Class.forName(player1Name + "_Player");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(1);
            player[1] = (MancalaPlayer) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }

        /*Initializations */
        opit0.setText("4");
        opit1.setText("4");
        opit2.setText("4");
        opit3.setText("4");
        opit4.setText("4");
        opit5.setText("4");
        oppStore.setText("0");

        pit0.setText("4");
        pit1.setText("4");
        pit2.setText("4");
        pit3.setText("4");
        pit4.setText("4");
        pit5.setText("4");
        myStore.setText("0");
        unmarkPits();
    }
    
    //This runnable class is used in a thread to animate the stone moves.
    private class Animator implements Runnable {
        private int startPoint; //index of selected pit.
        private int stones;  //number of stones in selected pit.
        private int currentPlayer; //whos turn.
        
        void setStartPoint(int n) { startPoint = n; }
        void setStones(int n) { stones = n; }
        void setPlayer(int n ) { currentPlayer = n; }
        
        public void run() {
            int PITS = 5; //pit 6 is the score pit, then wraps to other side.
        
            //for each stone, mark the next pit.
            //make sure to mark the score pit too.
            //make sure to wrap and mark other side.
            for(int currStone = 0; currStone < stones; currStone++) {

                int currentPit = startPoint + currStone; //location of the current pit.
                int currentOPit = startPoint - currStone;
                //for player 1, check if p1 pit number is greater than 5.
                if((currentPit > PITS) && (currentPlayer == 1)) {
                    if(currentPit == 6) { myStore.setBackground(Color.red); }
                    markOPit(currentPit%6 - 1); //
                }
                //for player 2. if the current pit pos is greater than 5,
                //mark the pit on the opposite side
                else if((currentOPit < 0) && currentPlayer == 2) {
                    if(currentOPit == 0) { oppStore.setBackground(Color.red); }
                    markPit(Math.abs(currentOPit+1)); //TODO fix this logic.

                }

                //mark the pit that was used.
                else if(currentPlayer == 1) {
                    markPit(currentPit);
                }
                else if(currentPlayer == 2) {
                    markOPit(currentPit);
                }

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

            } //end for
            
            //add delay after your move.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Set the status at bottom of gui.
    void status(String s) {
        status.setText(s);
    }

    void p(String s) {
        System.out.println(s);
    }

    //returns the amount of stones moved.
    public int play(int n) throws Exception {

        stones = 0;
        if (!gs.checkEndGame()) {
            //gs.printState();
            status("Player " + (gs.CurrentPlayer() + 1) + "'s move: ");
            p("Player " + (gs.CurrentPlayer() + 1) + "'s move: ");

            if (gs.CurrentPlayer() == 0) {
                if (player1.equals("interactive")) {
                    stones = gs.stonesAt(gs.CurrentPlayer(), n);
                    interactiveMove(n);
                    updateBoard();
                } else {
                    stones = gs.stonesAt(gs.CurrentPlayer(), n);
                    computerMove();
                    updateBoard();
                }
            } //Player 2's move        
            else {
                if (player2.equals("interactive")) {
                    stones = gs.stonesAt(gs.CurrentPlayer(), n);
                    interactiveMove(n);
                    updateBoard();
                } else {
                    stones = gs.stonesAt(gs.CurrentPlayer(), n);
                    computerMove();
                    updateBoard();
                }
            }

        } //Endgame actions        
        else {
            gs.printState();
            System.out.println("\nPerforming player 1 post game actions...");
            player[0].postGameActions(gs);
            System.out.println("Performing player 2 post game actions...");
            player[1].postGameActions(gs);

            gs.computeFinalScore();
            System.out.println("After moving in remaining pieces: ");
            gs.printState();

            System.out.println("Player 1 score: " + gs.getScore(0));
            System.out.println("Player 2 score: " + gs.getScore(1));
            System.out.println();
            
            //disable gui
            if(gs.getScore(0) > gs.getScore(1)) {
                status("YOU WIN!");
            }
            else {
                status("YOU LOSE!");
            }
            
            gs.reset();
        }

        return stones;
    }

    
    
    void mark(int player, int i) {
        if(player == 1) {
            markPit(i);
        }
        else {
            markOPit(i);
        }
    }
    void markPit(int i) {
        if(i==0) { pit0.setBackground(Color.lightGray); }
        if(i==1) { pit1.setBackground(Color.lightGray); }
        if(i==2) { pit2.setBackground(Color.lightGray); }
        if(i==3) { pit3.setBackground(Color.lightGray); }
        if(i==4) { pit4.setBackground(Color.lightGray); }
        if(i==5) { pit5.setBackground(Color.lightGray); }
    }
    void markOPit(int i) {
        if(i==0) { opit0.setBackground(Color.lightGray); }
        if(i==1) { opit1.setBackground(Color.lightGray); }
        if(i==2) { opit2.setBackground(Color.lightGray); }
        if(i==3) { opit3.setBackground(Color.lightGray); }
        if(i==4) { opit4.setBackground(Color.lightGray); }
        if(i==5) { opit5.setBackground(Color.lightGray); }
    }       
    void unmarkPits() {
        pit0.setBackground(Color.white);
        pit1.setBackground(Color.white);
        pit2.setBackground(Color.white); 
        pit3.setBackground(Color.white); 
        pit4.setBackground(Color.white); 
        pit5.setBackground(Color.white);
        opit0.setBackground(Color.white);
        opit1.setBackground(Color.white);
        opit2.setBackground(Color.white); 
        opit3.setBackground(Color.white); 
        opit4.setBackground(Color.white); 
        opit5.setBackground(Color.white);
        
    } 
    void updateBoard() {
        opit0.setText(Integer.toString(gs.stonesAt(1, 0)));
        opit1.setText(Integer.toString(gs.stonesAt(1, 1)));
        opit2.setText(Integer.toString(gs.stonesAt(1, 2)));
        opit3.setText(Integer.toString(gs.stonesAt(1, 3)));
        opit4.setText(Integer.toString(gs.stonesAt(1, 4)));
        opit5.setText(Integer.toString(gs.stonesAt(1, 5)));
        oppStore.setText(Integer.toString(gs.getScore(1)));

        pit0.setText(Integer.toString(gs.stonesAt(0, 0)));
        pit1.setText(Integer.toString(gs.stonesAt(0, 1)));
        pit2.setText(Integer.toString(gs.stonesAt(0, 2)));
        pit3.setText(Integer.toString(gs.stonesAt(0, 3)));
        pit4.setText(Integer.toString(gs.stonesAt(0, 4)));
        pit5.setText(Integer.toString(gs.stonesAt(0, 5)));
        myStore.setText(Integer.toString(gs.getScore(0)));
    }
    void enableSide(int player) {
        if(player == 1) {
            opit0.setEnabled(false);
            opit1.setEnabled(false);
            opit2.setEnabled(false);
            opit3.setEnabled(false);
            opit4.setEnabled(false);
            opit5.setEnabled(false);
            pit0.setEnabled(true);
            pit1.setEnabled(true);
            pit2.setEnabled(true);
            pit3.setEnabled(true);
            pit4.setEnabled(true);
            pit5.setEnabled(true);  
        }
        else {
            pit0.setEnabled(false);
            pit1.setEnabled(false);
            pit2.setEnabled(false);
            pit3.setEnabled(false);
            pit4.setEnabled(false);
            pit5.setEnabled(false);
            opit0.setEnabled(true);
            opit1.setEnabled(true);
            opit2.setEnabled(true);
            opit3.setEnabled(true);
            opit4.setEnabled(true);
            opit5.setEnabled(true); 
        }
    }
  
    
    //used in the play() method
    void computerMove() {
        try {
            //for computer player
            int move = player[gs.CurrentPlayer()].getMove(gs);
            
            while (!gs.validMove(move)) {
                status("Invalid move!");
                move = player[gs.CurrentPlayer()].getMove(gs);
            }
            
            //status("Player " + (gs.CurrentPlayer() + 1) + " moved: " + move);
            
            stones = gs.stonesAt(gs.CurrentPlayer(), move); //global
            
            gs.play(move);
            unmarkPits();
            
            mark(gs.CurrentPlayer(), move);
            
        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //used in the play() method
    void interactiveMove(int n) {
        try {
            //for interactive player
            int move = n;
            if (!gs.validMove(move)) {
                //System.out.println("Invalid move!");                
            }
            status("Player " + (gs.CurrentPlayer() + 1) + "'s move: " + move);
            gs.play(move);
        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        InitialFrame = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        computerP2 = new javax.swing.JRadioButton();
        interactiveP2 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        computerP1 = new javax.swing.JRadioButton();
        interactiveP1 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        initialSettingButton = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        opit1 = new javax.swing.JButton();
        opit0 = new javax.swing.JButton();
        opit5 = new javax.swing.JButton();
        opit4 = new javax.swing.JButton();
        opit3 = new javax.swing.JButton();
        opit2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pit3 = new javax.swing.JButton();
        pit2 = new javax.swing.JButton();
        pit5 = new javax.swing.JButton();
        pit4 = new javax.swing.JButton();
        pit0 = new javax.swing.JButton();
        pit1 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        store = new javax.swing.JPanel();
        myStore = new javax.swing.JLabel();
        ostore = new javax.swing.JPanel();
        oppStore = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        InitialFrame.setTitle("Game Option Selector");
        InitialFrame.setAlwaysOnTop(true);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        buttonGroup2.add(computerP2);
        computerP2.setSelected(true);
        computerP2.setText("Computer");

        buttonGroup2.add(interactiveP2);
        interactiveP2.setText("Interactive");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(computerP2)
                    .addComponent(interactiveP2))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(interactiveP2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(computerP2)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        buttonGroup1.add(computerP1);
        computerP1.setText("Computer");

        buttonGroup1.add(interactiveP1);
        interactiveP1.setSelected(true);
        interactiveP1.setText("Interactive");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(computerP1)
                    .addComponent(interactiveP1))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(interactiveP1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(computerP1)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel6.setText("Please select a game option:");

        initialSettingButton.setFont(new java.awt.Font("Tahoma", 1, 14));
        initialSettingButton.setText("Start Game");
        initialSettingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialSettingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InitialFrameLayout = new javax.swing.GroupLayout(InitialFrame.getContentPane());
        InitialFrame.getContentPane().setLayout(InitialFrameLayout);
        InitialFrameLayout.setHorizontalGroup(
            InitialFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialFrameLayout.createSequentialGroup()
                .addGroup(InitialFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InitialFrameLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6))
                    .addGroup(InitialFrameLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(InitialFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(initialSettingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InitialFrameLayout.setVerticalGroup(
            InitialFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(InitialFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(initialSettingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        setTitle("Group 10 Mancala");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Player 2"));

        opit1.setBackground(new java.awt.Color(255, 255, 255));
        opit1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit1.setText("11");
        opit1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit1ActionPerformed(evt);
            }
        });

        opit0.setBackground(new java.awt.Color(255, 255, 255));
        opit0.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit0.setText("12");
        opit0.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit0ActionPerformed(evt);
            }
        });

        opit5.setBackground(new java.awt.Color(255, 255, 255));
        opit5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit5.setText("7");
        opit5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit5ActionPerformed(evt);
            }
        });

        opit4.setBackground(new java.awt.Color(255, 255, 255));
        opit4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit4.setText("8");
        opit4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit4ActionPerformed(evt);
            }
        });

        opit3.setBackground(new java.awt.Color(255, 255, 255));
        opit3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit3.setText("9");
        opit3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit3ActionPerformed(evt);
            }
        });

        opit2.setBackground(new java.awt.Color(255, 255, 255));
        opit2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        opit2.setText("10");
        opit2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        opit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(opit0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opit1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opit2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opit3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opit4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(opit5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {opit0, opit1, opit2, opit3, opit4, opit5});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(opit0, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opit1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opit2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opit3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(opit4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(opit5)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {opit0, opit1, opit2, opit3, opit4, opit5});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Player 1"));

        pit3.setBackground(new java.awt.Color(255, 255, 255));
        pit3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit3.setText("3");
        pit3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit3ActionPerformed(evt);
            }
        });

        pit2.setBackground(new java.awt.Color(255, 255, 255));
        pit2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit2.setText("2");
        pit2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit2ActionPerformed(evt);
            }
        });

        pit5.setBackground(new java.awt.Color(255, 255, 255));
        pit5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit5.setText("5");
        pit5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit5ActionPerformed(evt);
            }
        });

        pit4.setBackground(new java.awt.Color(255, 255, 255));
        pit4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit4.setText("4");
        pit4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit4ActionPerformed(evt);
            }
        });

        pit0.setBackground(new java.awt.Color(255, 255, 255));
        pit0.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit0.setText("0");
        pit0.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit0.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                pit0MouseMoved(evt);
            }
        });
        pit0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit0ActionPerformed(evt);
            }
        });

        pit1.setBackground(new java.awt.Color(255, 255, 255));
        pit1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pit1.setText("1");
        pit1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 3, true));
        pit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pit1ActionPerformed(evt);
            }
        });

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pit0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pit1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pit2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pit3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pit4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pit5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(414, 414, 414)
                    .addComponent(jTextField7, 0, 0, Short.MAX_VALUE)
                    .addGap(415, 415, 415)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pit1, pit2, pit3, pit4, pit5});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(pit2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pit3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pit4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pit5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pit0, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pit1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pit0, pit1, pit2, pit3, pit4, pit5});

        store.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 51), 3, true));

        myStore.setBackground(new java.awt.Color(255, 255, 255));
        myStore.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        myStore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        myStore.setText("0");

        javax.swing.GroupLayout storeLayout = new javax.swing.GroupLayout(store);
        store.setLayout(storeLayout);
        storeLayout.setHorizontalGroup(
            storeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myStore, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addContainerGap())
        );
        storeLayout.setVerticalGroup(
            storeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(storeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myStore, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );

        ostore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 51), 3, true));

        oppStore.setBackground(new java.awt.Color(255, 255, 255));
        oppStore.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        oppStore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        oppStore.setText("0");

        javax.swing.GroupLayout ostoreLayout = new javax.swing.GroupLayout(ostore);
        ostore.setLayout(ostoreLayout);
        ostoreLayout.setHorizontalGroup(
            ostoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ostoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oppStore, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addContainerGap())
        );
        ostoreLayout.setVerticalGroup(
            ostoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ostoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oppStore, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );

        status.setText("Status Bar.");

        jLabel1.setText("Player 1 Score:");

        jLabel2.setText("Player 2 Score:");

        jMenu1.setText("File");

        jMenuItem2.setText("Close");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mode");

        jMenuItem1.setText("Player vs. AI");
        jMenu2.add(jMenuItem1);

        jMenuItem3.setText("AI vs. AI");
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(status, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ostore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, 0, 422, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(store, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, store});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(store, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ostore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(status)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void opit0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit0ActionPerformed
        // TODO add your handling code here:
        try {
            //playAnimation(this.opit0,play(0));
            Animator a = new Animator();
            a.setPlayer(2);
            a.setStartPoint(0);
            a.setStones(play(0));
            Thread t = new Thread(a);         
            t.start();
            t.join();
            //animate(0, play(0), 2);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 0);
                computerMove();
                
                updateBoard();
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

}//GEN-LAST:event_opit0ActionPerformed

    //sets what type of player is who, then launches the game.
    private void initialSettingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialSettingButtonActionPerformed
        if (interactiveP1.isSelected()) {
            player1 = "interactive";
        } else {
            player1 = "AILearning";
        }

        if (interactiveP2.isSelected()) {
            player2 = "interactive";
        } else {
            player2 = "AILearning";
        }

        MancalaGUI gui = new MancalaGUI("KalahPie", 4, player1, player2);
        try {
            InitialFrame.setVisible(false);

            gui.setVisible(true);
            gui.pack();

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

//        for (int i =1; i<= 100000; i++){
//            try {
//                game.play();
//            } catch (Exception ex) {
//                Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_initialSettingButtonActionPerformed

    private void pit0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit0ActionPerformed
        try {

            if(gs.CurrentPlayer() == 1) {
                if (!player2.equals("interactive")) {
                    stones = gs.stonesAt(gs.CurrentPlayer(), 0);
                    computerMove();
                    updateBoard();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit0ActionPerformed

    private void pit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit1ActionPerformed
        try {
            
            if(gs.CurrentPlayer() == 1) {
            if (!player2.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 1);
                computerMove();
                updateBoard();
            }
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit1ActionPerformed

    private void pit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit2ActionPerformed
        // TODO add your handling code here:
        try {
            
            if(gs.CurrentPlayer() == 1) {
                if (!player2.equals("interactive")) {
                    stones = gs.stonesAt(gs.CurrentPlayer(), 2);
                    computerMove();
                    updateBoard();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit2ActionPerformed

    private void pit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit3ActionPerformed
        // TODO add your handling code here:
        try {
            play(3);

            if(gs.CurrentPlayer() == 1) {
                if (!player2.equals("interactive")) {
                    stones = gs.stonesAt(gs.CurrentPlayer(), 3);
                    computerMove();
                    updateBoard();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit3ActionPerformed

    private void pit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit4ActionPerformed
        // TODO add your handling code here:
        try {
            play(4);

                        if(gs.CurrentPlayer() == 1) {
            if (!player2.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 4);
                computerMove();
                updateBoard();
            }
                        }
        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit4ActionPerformed

    private void pit5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pit5ActionPerformed
        // TODO add your handling code here:
        try {
            play(5);

                        if(gs.CurrentPlayer() == 1) {
            if (!player2.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 5);
                computerMove();
                updateBoard();
            }
                        }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pit5ActionPerformed

    private void opit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit1ActionPerformed
        // TODO add your handling code here:
        try {
            play(1);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 1);
                computerMove();
                updateBoard();
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_opit1ActionPerformed

    private void opit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit2ActionPerformed
        // TODO add your handling code here:
        try {
            play(2);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 2);
                computerMove();
                updateBoard();
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_opit2ActionPerformed

    private void opit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit3ActionPerformed
        // TODO add your handling code here:
        try {
            play(3);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 3);
                computerMove();
                updateBoard();
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_opit3ActionPerformed

    private void opit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit4ActionPerformed
        // TODO add your handling code here:
        try {
            play(4);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 4);
                computerMove();
                updateBoard();
            }

        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_opit4ActionPerformed

    private void opit5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opit5ActionPerformed
        // TODO add your handling code here:
        try {
            play(5);

            if (!player1.equals("interactive")) {
                stones = gs.stonesAt(gs.CurrentPlayer(), 5);
                computerMove();
                updateBoard();
            }
        } catch (Exception ex) {
            Logger.getLogger(MancalaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_opit5ActionPerformed

    
    /**************************************************************************/
    /**************************************************************************/

    private void pit0MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pit0MouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_pit0MouseMoved

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MancalaGUI gui = new MancalaGUI();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame InitialFrame;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton computerP1;
    private javax.swing.JRadioButton computerP2;
    private javax.swing.JButton initialSettingButton;
    private javax.swing.JRadioButton interactiveP1;
    private javax.swing.JRadioButton interactiveP2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel myStore;
    private javax.swing.JButton opit0;
    private javax.swing.JButton opit1;
    private javax.swing.JButton opit2;
    private javax.swing.JButton opit3;
    private javax.swing.JButton opit4;
    private javax.swing.JButton opit5;
    private javax.swing.JLabel oppStore;
    private javax.swing.JPanel ostore;
    private javax.swing.JButton pit0;
    private javax.swing.JButton pit1;
    private javax.swing.JButton pit2;
    private javax.swing.JButton pit3;
    private javax.swing.JButton pit4;
    private javax.swing.JButton pit5;
    private javax.swing.JLabel status;
    private javax.swing.JPanel store;
    // End of variables declaration//GEN-END:variables
}
