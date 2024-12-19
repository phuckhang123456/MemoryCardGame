package memorycardgame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class Homepage {
    private JLabel titleLabel = new JLabel("MCG HomePage", SwingConstants.CENTER);

    private JButton botButton = new JButton("Play with bot");
    private JButton aloneButton = new JButton("Play alone");
    private JButton friendButton = new JButton("Play with a friend");

    private JLabel gameModeLabel = new JLabel("Game Mode");
    private JLabel settingLabel = new JLabel("Setting");

    private JLabel gameDifficultyLabel = new JLabel("Game difficulty");
    private JLabel numberOfCardsLabel = new JLabel("Number of cards");

    private String[] difficulties = { "Easy", "Medium", "Hard" };
    private JSpinner difficultySpinner = new JSpinner(new SpinnerListModel(difficulties));
    private JSpinner cardsSpinner = new JSpinner(new SpinnerNumberModel(6, 6, 10, 2));

    private JButton playButton = new JButton("Play");
    boolean bot = false;
    boolean alone = false;
    boolean friend = false;
    

    JFrame frame = new JFrame("Home Page");
    JPanel Homepage = new JPanel();

    int temp = 0;
    int challenging = 0;

    Homepage() {
        // add a timer for alone with different difficulty, end time => Time out , win=>
        // Finished
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);

        Homepage.setLayout(new BorderLayout(10, 10));

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        Homepage.add(titleLabel, BorderLayout.NORTH);

        // Left Panel for Game Mode Buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        gameModeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        botButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        aloneButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        friendButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(gameModeLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(botButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(aloneButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(friendButton);

        Homepage.add(leftPanel, BorderLayout.WEST);

        // Right Panel for Settings
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // GridLayout for alignment

        settingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        difficultySpinner.setEnabled(false);
        cardsSpinner.setEnabled(false);
        gameDifficultyLabel.setEnabled(false);
        numberOfCardsLabel.setEnabled(false);
        playButton.setEnabled(false);

        settingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        gameDifficultyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        numberOfCardsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultySpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(settingLabel);
        rightPanel.add(Box.createVerticalStrut(6));

        rightPanel.add(gameDifficultyLabel);

        rightPanel.add(Box.createVerticalStrut(6));

        rightPanel.add(difficultySpinner);

        rightPanel.add(Box.createVerticalStrut(6));

        rightPanel.add(numberOfCardsLabel);

        rightPanel.add(Box.createVerticalStrut(6));

        rightPanel.add(cardsSpinner);

        Homepage.add(rightPanel, BorderLayout.EAST);

        // Bottom Section for Play Button
        JPanel bottomPanel = new JPanel();
        
        playButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bottomPanel.add(playButton);

        Homepage.add(bottomPanel, BorderLayout.SOUTH);


        //Add action listener for the buttons
        botButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bot = true;
                alone = false;
                friend = false;
                difficultySpinner.setEnabled(true);
                cardsSpinner.setEnabled(true);
                gameDifficultyLabel.setEnabled(true);
                numberOfCardsLabel.setEnabled(true);
                playButton.setEnabled(true);

            }
        });

        aloneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bot = false;
                alone = true;
                friend = false;
                difficultySpinner.setEnabled(true);
                cardsSpinner.setEnabled(true);
                gameDifficultyLabel.setEnabled(true);
                numberOfCardsLabel.setEnabled(true);
                playButton.setEnabled(true);
            }
        });

        friendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bot = false;
                alone = false;
                friend = true;
                difficultySpinner.setEnabled(false);
                cardsSpinner.setEnabled(true);
                gameDifficultyLabel.setEnabled(false);
                numberOfCardsLabel.setEnabled(true);
                playButton.setEnabled(true);
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (difficultySpinner.getValue().toString() == "Easy")
                    challenging = 1;
                else if (difficultySpinner.getValue().toString() == "Medium")
                    challenging = 2;
                else if (difficultySpinner.getValue().toString() == "Hard")
                    challenging = 3;

                if (bot)
                    temp = 1;
                else if (alone)
                    temp = 2;
                else if (friend)
                    temp = 3;

                frame.dispose();
                new MemoryCardGame(temp, challenging, (int) cardsSpinner.getValue());

            }
        });

        frame.add(Homepage);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }
}
