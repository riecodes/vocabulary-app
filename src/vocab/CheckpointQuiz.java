package vocab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CheckpointQuiz extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font manjari;
    
    public CheckpointQuiz() {
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(48f);
            manjari = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/manjari.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(manjari);
        } catch (IOException | FontFormatException e) {
            balsamiqBold = new Font("Arial", Font.BOLD, 48);
            manjari = new Font("Arial", Font.BOLD, 24);
        }
        
        // Set up the frame
        setTitle("School Vocabulary Quiz");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with cream/light yellow background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 248, 220)); // Light cream background
        
        // Title panel
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(new Color(255, 248, 220));
        
        // Use HTML for line break in title
        JLabel titleLabel = new JLabel("<html><center>School Vocabulary<br>Quiz Time</center></html>");
        titleLabel.setFont(balsamiqBold);
        titleLabel.setForeground(new Color(233, 126, 126)); // Coral color
        titlePanel.add(titleLabel);
        
        // Add padding around the title panel
        titlePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(255, 248, 220));
        buttonPanel.setLayout(new BorderLayout(20, 0));
        
        // Create a centered panel for the buttons
        JPanel centeredButtonPanel = new JPanel(new GridBagLayout());
        centeredButtonPanel.setBackground(new Color(255, 248, 220));
        centeredButtonPanel.setPreferredSize(new Dimension(500, 100));
        
        // Create a rounded panel for the START button
        RoundedPanel startButton = new RoundedPanel(20, new Color(233, 91, 91));
        startButton.setLayout(new GridBagLayout());
        startButton.setPreferredSize(new Dimension(220, 60));
        
        // Create the label for the text
        JLabel startLabel = new JLabel("START");
        startLabel.setFont(manjari);
        startLabel.setForeground(Color.WHITE);
        startButton.add(startLabel);
        
        // Create a rounded panel for the EXIT button
        RoundedPanel exitButton = new RoundedPanel(20, new Color(233, 91, 91));
        exitButton.setLayout(new GridBagLayout());
        exitButton.setPreferredSize(new Dimension(220, 60));
        
        // Create the label for the text
        JLabel exitLabel = new JLabel("EXIT");
        exitLabel.setFont(manjari);
        exitLabel.setForeground(Color.WHITE);
        exitButton.add(exitLabel);
        
        // Add hover effect and click behavior for START button
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(233, 111, 111));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(233, 91, 91));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close current window
                SwingUtilities.invokeLater(() -> {
                    StartQuiz startQuiz = new StartQuiz();
                    startQuiz.setVisible(true);
                });
            }
        });
        
        // Add hover effect and click behavior for EXIT button
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(233, 111, 111));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(233, 91, 91));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // Exit the application
            }
        });
        
        // Add buttons to the centered panel with some space between them
        JPanel startButtonContainer = new JPanel();
        startButtonContainer.setBackground(new Color(255, 248, 220));
        startButtonContainer.add(startButton);
        
        JPanel exitButtonContainer = new JPanel();
        exitButtonContainer.setBackground(new Color(255, 248, 220));
        exitButtonContainer.add(exitButton);
        
        centeredButtonPanel.add(startButtonContainer);
        centeredButtonPanel.add(exitButtonContainer);
        
        buttonPanel.add(centeredButtonPanel, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add the main panel to the frame
        setContentPane(mainPanel);
    }
    
    // Custom panel with rounded corners
    private class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int cornerRadius;
        
        public RoundedPanel(int radius, Color bgColor) {
            super();
            cornerRadius = radius;
            setBackground(bgColor);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CheckpointQuiz checkpointQuiz = new CheckpointQuiz();
            checkpointQuiz.setVisible(true);
        });
    }
} 