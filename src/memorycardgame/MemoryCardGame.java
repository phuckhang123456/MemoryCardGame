/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package memorycardgame;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.*;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author ZBook
 */
public class MemoryCardGame {

    class Card {

        String CardName;
        ImageIcon cardImageIcon;

        Card(String CardName, ImageIcon cardImageIcon) {
            this.CardName = CardName;
            this.cardImageIcon = cardImageIcon;

        }

        public String toString() {
            return CardName;
        }
    }

    String[] cardList = {
            "darkness", "double", "fairy", "fighting", "fire", "grass", "lightning", "metal", "psychic", "water"
    };

    // wanna change the no of card, change cardList and rows vs collums
    int cardWidth = 90;
    int cardHeight = 128;
    ArrayList<Card> CardSet;
    ImageIcon cardBackImageIcon;

    JFrame frame = new JFrame("Memory Matching Card Game");
    JLayeredPane layerPane = new JLayeredPane();// hold the cards grid and the gameover label
    JLabel PointLabel = new JLabel();
    JLabel OtherPointLabel = new JLabel();
    JLabel gameOverLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel gameOverPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    JButton goHome = new JButton();
    JPanel goHomPanel = new JPanel();
    JPanel countdowPanel = new JPanel();
    ArrayList<JButton> board;

    // Timer for alone
    JLabel countdownLabel = new JLabel();
    Font font1 = new Font("Arial", Font.PLAIN, 28);
    Timer countdownTimer;
    int second = 10;
    String ddSecond;
    DecimalFormat df = new DecimalFormat("00");

    //////////////////////////////////////////////////////////////////////

    int playerPoint = 0;
    int playerPoint2 = 0;
    int botPoint = 0;

    int cards = 0;
    Timer hideCardTimer;
    Timer waitBot;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    // BOT'S SETTING
    boolean botTurn = false;
    boolean friendTurn = false;
    boolean alonestart = false;
    HashMap<Integer, ImageIcon> botMemory = new HashMap<Integer, ImageIcon>();
    Set<Integer> matchCard = new HashSet<Integer>();

    MemoryCardGame(int mode, int challenging, int cards) {// constructor
        this.cards = cards;
        //////////////// set up the grid for card
        int rows = 4;
        int columns = 5;

        if (cards == 6) {
            rows = 3;
            columns = 4;

        }

        else if (cards == 8) {
            rows = 4;
            columns = 4;

        }
        ///////////////////////////////////////////

        ///// set up timer for alone////////////////////////////////////

        ////////////////////////////////////////////////////////
        int boardWidth = columns * cardWidth;
        int boardHeight = rows * cardHeight;
        setupCard(cards);
        shuffleCard();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        // Component on top of the frame//////////////////////////////////////
        goHome.setEnabled(false);
        goHome.setText("Home");
        goHomPanel.setLayout(new FlowLayout());
        goHomPanel.add(goHome);
        goHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Homepage();
            }
        });

        second = (cards * 10) / 2 - challenging * 5;
        countdownLabel.setHorizontalAlignment(JLabel.CENTER);
        countdownLabel.setFont(font1);
        countdownLabel.setText(Integer.toString(second));

        countdowPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 106, 0));
        countdowPanel.add(countdownLabel);

        PointLabel.setFont(new Font("Arial", Font.BOLD, 20));
        PointLabel.setHorizontalAlignment(JLabel.CENTER);
        if (mode == 1)
            PointLabel.setText("Player:" + Integer.toString(playerPoint));// gone
        else if (mode == 3)
            PointLabel.setText("Player 1:" + Integer.toString(playerPoint));

        OtherPointLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        OtherPointLabel.setHorizontalAlignment(JLabel.CENTER);
        if (mode == 1)
            OtherPointLabel.setText("Bot:" + Integer.toString(botPoint));
        else if (mode == 3)
            OtherPointLabel.setText("Player 2:" + Integer.toString(playerPoint2));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.setLayout(new BorderLayout());
        if (mode == 1 || mode == 3) {
            textPanel.add(PointLabel, BorderLayout.WEST);
            textPanel.add(OtherPointLabel, BorderLayout.EAST);
            textPanel.add(goHomPanel, BorderLayout.CENTER);
        } else {
            textPanel.add(countdowPanel, BorderLayout.CENTER);
            textPanel.add(goHomPanel, BorderLayout.WEST);

        }

        frame.add(textPanel, BorderLayout.NORTH);// add textpanel have the texLabel error to the top of the frame
        ///////////////////////////////////////////////////////////////////////////////////////////

        /// Component in the center of the
        /// frame//////////////////////////////////////////////////////
        gameOverLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        if (mode == 1 || mode == 3)
            gameOverLabel.setText("GAME OVER");
        else if (mode == 2)
            gameOverLabel.setText("FINISHED");

        gameOverPanel.setPreferredSize(new Dimension(boardWidth, 30));
        gameOverPanel.setLayout(new BorderLayout());
        gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);
        gameOverPanel.setBounds(0, (boardHeight / 2) - 50, boardWidth + 20, 40);
        gameOverPanel.setVisible(false);

        board = new ArrayList<JButton>();
        boardPanel.setBounds(0, 0, boardWidth + 10, boardHeight);
        boardPanel.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < CardSet.size(); i++) {// for every card in the CardSet create a new button tile
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));// make the button the size of the card
            tile.setOpaque(true);
            tile.setIcon(CardSet.get(i).cardImageIcon);// every tile(button) get the card icon from the CardSet
            tile.setFocusable(false);// all events resulting from typing on the keyboard won't be pass to Jbutton
            tile.addActionListener(new ActionListener() {// every tile on the grid is assign a actionlister
                @Override
                public void actionPerformed(ActionEvent e) {// Whenever you clik the tile(button), this method is called
                    if (!gameReady) {
                        return;
                    }
                    if (botTurn) {
                        return;
                    }

                    JButton tile = (JButton) e.getSource();// assign the tile(button) is being clicked to tile(buttom)
                    // the getSource() return the object that fired the event.And because it is an
                    // Object, cast it to Jbutton
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {// if card1 has't been select
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);// get the index of card1 on the grid to set the
                                                                     // icon to it
                            card1Selected.setIcon(CardSet.get(index).cardImageIcon);
                        } else if (card2Selected == null) {// The same for card2
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(CardSet.get(index).cardImageIcon);

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {// if the two card don't match
                                if (mode == 1) {
                                    botTurn = true;
                                    PointLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                                    OtherPointLabel.setFont(new Font("Arial", Font.BOLD, 20));

                                    botDifficulty(challenging);
                                } else if (mode == 3 && !friendTurn) {
                                    friendTurn = true;

                                    PointLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                                    OtherPointLabel.setFont(new Font("Arial", Font.BOLD, 20));

                                } else if (friendTurn) {
                                    friendTurn = false;
                                    PointLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                    OtherPointLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                                }

                                hideCardTimer.start();// call the hideCardTimer which will then call the hideCards
                                                      // method

                            } else {

                                int index1 = board.indexOf(card1Selected);
                                if (botMemory.containsKey(index1))
                                    botMemory.remove(index1);
                                if (botMemory.containsKey(index))
                                    botMemory.remove(index);

                                card1Selected = null;// the two card remain face-up
                                card2Selected = null;
                                matchCard.add(index);
                                matchCard.add(index1);
                                if (friendTurn)
                                    playerPoint2++;
                                else if (!friendTurn)
                                    playerPoint++;

                                if (mode == 1) {

                                    PointLabel.setText("Player:" + Integer.toString(playerPoint));
                                }

                                else if (mode == 3) {
                                    PointLabel.setText("Player 1:" + Integer.toString(playerPoint));
                                    OtherPointLabel.setText("Player 2:" + Integer.toString(playerPoint2));
                                }

                                GameOver();

                                return;

                            }

                        }
                    }

                }
            });
            board.add(tile);// add this tile to the board arrayList<button>
            boardPanel.add(tile);// then add this to the boardPanel, which is divide into grids

        }
        layerPane.setPreferredSize(new Dimension(boardWidth, boardHeight));
        layerPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        layerPane.add(gameOverPanel, JLayeredPane.PALETTE_LAYER);
        frame.add(layerPane, BorderLayout.CENTER);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////// Component in the bottom of the
        /////////// frame///////////////////////////////////////////////////////////
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        // RESET BUTTON
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                gameOverPanel.setVisible(false);
                countdownTimer.stop();
                gameReady = false;
                restartButton.setEnabled(false);
                goHome.setEnabled(false);
                card1Selected = null;
                card2Selected = null;

                matchCard.clear();
                // alone setting////////////////////////////////////
                second = (cards * 10) / 2 - challenging * 5;
                ;
                alonestart = false;

                ///////////////////////////////////////////////////
                shuffleCard();
                for (int i = 0; i < board.size(); i++) {// Show the player the cards for 1.5 sec then hide the card
                    board.get(i).setEnabled(true);
                    board.get(i).setIcon(CardSet.get(i).cardImageIcon);

                }
                playerPoint = 0;
                botPoint = 0;
                playerPoint2 = 0;
                PointLabel.setFont(new Font("Arial", Font.BOLD, 20));
                OtherPointLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                if (mode == 1) {
                    PointLabel.setText("Player:" + Integer.toString(playerPoint));
                    OtherPointLabel.setText("Bot:" + Integer.toString(botPoint));
                } else if (mode == 2)
                    countdownLabel.setText(Integer.toString(second));
                else if (mode == 3) {
                    PointLabel.setText("Player 1:" + Integer.toString(playerPoint));
                    OtherPointLabel.setText("Player 2:" + Integer.toString(playerPoint2));
                }

                botTurn = false;
                friendTurn = false;
                botMemory.clear();
                hideCardTimer.restart();

            }
        });

        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ///// Timer/////////////////////////////////////////////////////////////////////////
        hideCardTimer = new Timer(1500, new ActionListener() {// after 1500 milisecond countdown, an action will happen
            @Override
            public void actionPerformed(ActionEvent e) {

                hideCards();// that action is hideCards

                if (mode == 2 && !alonestart) {
                    countdownTimer.start();
                    alonestart = true;
                } else if (botTurn)
                    waitBot.start();

            }
        });

        waitBot = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!GameOver())
                    botPlay();

            }
        });

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                second--;

                if (second < 0) {
                    countdownTimer.stop();
                    hideCardTimer.stop();
                    countdownLabel.setText("00");
                    gameOverLabel.setText("Time's up");
                    gameOverPanel.setVisible(true);
                    for (JButton button : board) {
                        button.setEnabled(false);
                    }
                    return;
                }
                ddSecond = df.format(second);
                countdownLabel.setText("" + ddSecond);

            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        waitBot.setRepeats(false);

        hideCardTimer.setRepeats(false);// the countdown won't repeate util reset
        hideCardTimer.start();// start the countdownTimer

    }

    void botPlay() {
        // 1 pairs in the bot memory
        HashMap<ImageIcon, ArrayList<Integer>> reverseBotMemory = new HashMap<ImageIcon, ArrayList<Integer>>();
        for (int i : botMemory.keySet()) {

            reverseBotMemory.computeIfAbsent(botMemory.get(i), k -> new ArrayList<>()).add(i);

        }

        for (ImageIcon i : reverseBotMemory.keySet()) {
            ArrayList<Integer> reverseValue = reverseBotMemory.get(i);
            if (reverseValue.size() > 1) {

                flippedCardforBot(reverseValue.get(0), reverseValue.get(1));

                matchCard.addAll(reverseValue);
                botMemory.remove(reverseValue.get(0));
                botMemory.remove(reverseValue.get(1));

                botPoint++;
                OtherPointLabel.setText("Bot:" + Integer.toString(botPoint));

                System.out.println("We have two cards in memory");
                // reverseBotMemory.clear();
                waitBot.restart();
                return;

            }

        }

        // No pairs in memory; pick a random card
        Random rand = new Random();
        ArrayList<Integer> validRanNo = new ArrayList<Integer>();

        for (int i = 0; i < board.size(); i++) {
            if (!botMemory.containsKey(i) && !matchCard.contains(i)) {
                validRanNo.add(i);
            }

        }

        int nIndex = rand.nextInt(validRanNo.size());
        int n = validRanNo.get(nIndex);

        // Check if the selected card matches any in memory
        for (int i : botMemory.keySet()) {
            if (CardSet.get(n).cardImageIcon == botMemory.get(i)) {
                System.out.println("We have 1 card in memory.");

                flippedCardforBot(n, i);

                botMemory.remove(i);
                matchCard.add(n);
                matchCard.add(i);

                botPoint++;
                OtherPointLabel.setText("Bot:" + Integer.toString(botPoint));

                waitBot.restart(); // Restart the countdownTimer for the next action
                return; // Exit method as the bot gets another turn
            }
        }

        validRanNo.remove(nIndex);
        int m = validRanNo.get(rand.nextInt(validRanNo.size()));

        flippedCardforBot(n, m);

        if (CardSet.get(n).cardImageIcon == CardSet.get(m).cardImageIcon) {
            System.out.println("We have 0 cards in memory but found a match.");
            matchCard.add(n);
            matchCard.add(m);

            botPoint++;
            OtherPointLabel.setText("Bot:" + Integer.toString(botPoint));

            waitBot.restart(); // Restart the countdownTimer for the next action
            return; // Exit method as the bot gets another turn
        }

        // If no match, add the cards to memory
        System.out.println("No card matching.");
        botMemory.put(n, CardSet.get(n).cardImageIcon);
        botMemory.put(m, CardSet.get(m).cardImageIcon);

        // End the bot's turn
        botTurn = false;
        PointLabel.setFont(new Font("Arial", Font.BOLD, 20));
        OtherPointLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        hideCardTimer.restart();
        // hide the unmatch card
    }

    void botDifficulty(int difficulty) {
        // Set<Integer> noDuplicate = new HashSet<>(botMemory);
        int index1 = board.indexOf(card1Selected);
        int index2 = board.indexOf(card2Selected);
        if (difficulty == 2) {

            if (!botMemory.containsKey(index1))
                botMemory.put(index1, CardSet.get(index1).cardImageIcon);
            else if (!botMemory.containsKey(index2))
                botMemory.put(index2, CardSet.get(index2).cardImageIcon);

        } else if (difficulty == 3) {

            botMemory.putIfAbsent(index1, CardSet.get(index1).cardImageIcon);
            botMemory.putIfAbsent(index2, CardSet.get(index2).cardImageIcon);
        }
    }

    void flippedCardforBot(int index1, int index2) {
       

        card1Selected = board.get(index1);
        card1Selected.setIcon(CardSet.get(index1).cardImageIcon);

        card2Selected = board.get(index2);
        card2Selected.setIcon(CardSet.get(index2).cardImageIcon);

    }

    void setupCard(int cards) {
        CardSet = new ArrayList<Card>();
        for (int i = 0; i < cards; i++) {
            URL resource = getClass().getResource("ImgCard/" + cardList[i] + ".jpg");

            if (resource != null) {
                Image cardImage = new ImageIcon(resource).getImage();
                ImageIcon cardImageIcon = new ImageIcon(
                        cardImage.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
                Card card = new Card(cardList[i], cardImageIcon);
                CardSet.add(card);
            } else {
                System.err.println("Image not found for: " + cardList[i]);
            }
        }
        // double the card we have
        CardSet.addAll(CardSet);

        // load back of the card image
        Image cardBackImg = new ImageIcon(getClass().getResource("ImgCard/back.jpg")).getImage();
        // if it in the same package memorycardgame just write getResource("back.jpg")
        // ImgCard is the subfolder of memorycardgame
        cardBackImageIcon = new ImageIcon(
                cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

    }

    void shuffleCard() {
        System.out.println(CardSet);

        for (int i = 0; i < CardSet.size(); i++) {
            int j = (int) (Math.random() * CardSet.size());
            Card temp = CardSet.get(i);
            CardSet.set(i, CardSet.get(j));
            CardSet.set(j, temp);
        }
        System.out.println(CardSet);

    }

    void hideCards() {

        if (gameReady && card1Selected != null && card2Selected != null) {// if the game is ready and two card is
                                                                          // flipped
            card1Selected.setIcon(cardBackImageIcon);// hide the two card
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        } else {// this is for the inizalation of the game
            for (int i = 0; i < CardSet.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);// loop through all the button, changing the imgage to the back
                                                        // of the card

            }
            gameReady = true;
            restartButton.setEnabled(true);
            goHome.setEnabled(true);
        }

    }

    boolean GameOver() {
        if (matchCard.size() == cards * 2) {
            System.out.println("The Game has ended");
            botTurn = false;
            gameOverPanel.setVisible(true);
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setEnabled(false);
            }
            countdownTimer.stop();
            frame.revalidate();
            frame.repaint();
            return true;
        }
        return false;
    }

    
}
