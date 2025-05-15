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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Score extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font balsamiqRegular;
    private Font bricolageGrotesque;
    private Font manjari;
    
    // Score data
    private int finalScore;
    private String playerName;
    
    public Score(int score, String name) {
        this.finalScore = score;
        this.playerName = name;
        
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(32f);
            balsamiqRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans regular.ttf")).deriveFont(24f);
            bricolageGrotesque = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/bricolage grotesque.otf")).deriveFont(48f);
            manjari = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/manjari.ttf")).deriveFont(24f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(balsamiqRegular);
            ge.registerFont(bricolageGrotesque);
            ge.registerFont(manjari);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            balsamiqBold = new Font("Arial", Font.BOLD, 32);
            balsamiqRegular = new Font("Arial", Font.PLAIN, 24);
            bricolageGrotesque = new Font("Arial", Font.BOLD, 48);
            manjari = new Font("Arial", Font.PLAIN, 24);
        }
        
        // Set up the frame
        setTitle("School Vocabulary Quiz - Score");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255)); // White background
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 248, 220));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        try {
            // Load school design image for header
            File headerImageFile = new File("assets/school quiz design/school design.png");
            if (headerImageFile.exists()) {
                Image headerImage = ImageIO.read(headerImageFile);
                
                // Properly scale the image to fit the width while maintaining aspect ratio
                double aspectRatio = (double) headerImage.getWidth(null) / headerImage.getHeight(null);
                int targetWidth = 960;  // Set fixed width since getWidth() is 0 at this point
                int targetHeight = (int) (targetWidth / aspectRatio);
                
                // Reduce height by 5%
                targetHeight = (int) (targetHeight * 0.95);
                
                // Create a scaled version of the image
                Image scaledImage = headerImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                ImageIcon headerIcon = new ImageIcon(scaledImage);
                
                // Create a layered panel for absolute positioning of text over image
                JPanel layeredHeaderPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // Draw the banner image
                        headerIcon.paintIcon(this, g, 0, 0);
                        
                        // Setup for text rendering
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2d.setFont(balsamiqBold.deriveFont(50f)); // Reduced font size
                        g2d.setColor(new Color(236, 122, 104)); // #ec7a68 color for School Vocabulary
                        
                        // Calculate text width to center it
                        String text = "School Vocabulary";
                        int textWidth = g2d.getFontMetrics().stringWidth(text);
                        int x = (getWidth() - textWidth) / 2;
                        int y = (int)(getHeight() / 1.5); // Cast double to int
                        
                        // Draw text over image
                        g2d.drawString(text, x, y);
                        g2d.dispose();
                    }
                };
                
                layeredHeaderPanel.setPreferredSize(new Dimension(targetWidth, targetHeight));
                headerPanel.add(layeredHeaderPanel, BorderLayout.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Create content panel with absolute positioning
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null); // Use absolute positioning
        contentPanel.setBackground(new Color(255, 255, 255)); // White background
        
        // Create medal, score components but position them later
        final JLabel medalLabel = new JLabel();
        final JLabel scoreTitleLabel = new JLabel("SCORE:");
        scoreTitleLabel.setFont(balsamiqRegular);
        scoreTitleLabel.setForeground(new Color(235, 76, 76)); // #eb4c4c color
        scoreTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        final JLabel scoreValueLabel = new JLabel(finalScore + " / 10");
        scoreValueLabel.setFont(bricolageGrotesque);
        scoreValueLabel.setForeground(new Color(235, 76, 76)); // #eb4c4c color
        scoreValueLabel.setHorizontalAlignment(JLabel.CENTER);
        
        try {
            File medalFile = new File("assets/score/good job.png");
            if (medalFile.exists()) {
                Image medalImage = ImageIO.read(medalFile);
                // Scale medal to appropriate size - increase size to 0.15 (from 0.1)
                int newWidth = (int)(medalImage.getWidth(null) * 0.15);
                int newHeight = (int)(medalImage.getHeight(null) * 0.15);
                Image scaledMedalImage = medalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon medalIcon = new ImageIcon(scaledMedalImage);
                medalLabel.setIcon(medalIcon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Create view leaderboard button
        final RoundedPanel leaderboardButton = new RoundedPanel(20, new Color(235, 76, 76));
        leaderboardButton.setLayout(new GridBagLayout());
        
        // Create the label for the button
        JLabel leaderboardLabel = new JLabel("View Leaderboard");
        leaderboardLabel.setFont(manjari);
        leaderboardLabel.setForeground(Color.WHITE);
        leaderboardButton.add(leaderboardLabel);
        
        // Add hover effect and click behavior
        leaderboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leaderboardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leaderboardButton.setBackground(new Color(235, 96, 96)); // Lighter version of #eb4c4c
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                leaderboardButton.setBackground(new Color(235, 76, 76)); // #eb4c4c color
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Save score to leaderboard
                saveScoreToLeaderboard();
                
                dispose(); // Close current window
                SwingUtilities.invokeLater(() -> {
                    ViewLeaderboard leaderboardScreen = new ViewLeaderboard();
                    leaderboardScreen.setVisible(true);
                });
            }
        });
        
        // Add components to the content panel
        contentPanel.add(medalLabel);
        contentPanel.add(scoreTitleLabel);
        contentPanel.add(scoreValueLabel);
        contentPanel.add(leaderboardButton);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        setContentPane(mainPanel);
        
        // Save the score to the leaderboard when the score screen is shown
        saveScoreToLeaderboard();
        
        // Add a component listener to adjust positions after frame is visible and sized
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int panelWidth = contentPanel.getWidth();
                int panelHeight = contentPanel.getHeight();
                
                // Get medal size
                int medalWidth = medalLabel.getIcon() != null ? medalLabel.getIcon().getIconWidth() : 0;
                int medalHeight = medalLabel.getIcon() != null ? medalLabel.getIcon().getIconHeight() : 0;
                
                // Calculate position for centered leaderboard button
                int buttonWidth = 300;
                int centerX = panelWidth / 2;
                int buttonX = centerX - (buttonWidth / 2);
                
                // Center the score title with the same center as the leaderboard button - moved up
                int scoreWidth = 200;
                scoreTitleLabel.setBounds(centerX - (scoreWidth / 2), 120, scoreWidth, 40);
                
                // Center the score value with the same center as the leaderboard button - moved up
                int scoreValueWidth = 200;
                scoreValueLabel.setBounds(centerX - (scoreValueWidth / 2), 170, scoreValueWidth, 50);
                
                // Position medal to the left of the centered score elements - moved up
                medalLabel.setBounds(centerX - medalWidth - 50, 50, medalWidth, medalHeight);
                
                // Center the leaderboard button at the bottom - moved up
                leaderboardButton.setBounds(buttonX, 300, buttonWidth, 60);
            }
        });
    }
    
    // Save score to leaderboard file
    private void saveScoreToLeaderboard() {
        File leaderboardFile = new File("assets/leaderboard.txt");
        try {
            // Create directory if it doesn't exist
            File directory = new File("assets");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Append score to leaderboard file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(leaderboardFile, true))) {
                writer.write(playerName + "," + finalScore);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Score scoreScreen = new Score(0, "Player");
            scoreScreen.setVisible(true);
        });
    }
} 