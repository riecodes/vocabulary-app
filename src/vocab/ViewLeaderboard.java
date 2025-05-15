package vocab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ViewLeaderboard extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font balsamiqRegular;
    private Font bricolageGrotesque;
    
    // Leaderboard data
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
    
    public ViewLeaderboard() {
        // Load leaderboard data
        loadLeaderboardData();
        
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(32f);
            balsamiqRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans regular.ttf")).deriveFont(24f);
            bricolageGrotesque = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/bricolage grotesque.otf")).deriveFont(30f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(balsamiqRegular);
            ge.registerFont(bricolageGrotesque);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            balsamiqBold = new Font("Arial", Font.BOLD, 32);
            balsamiqRegular = new Font("Arial", Font.PLAIN, 24);
            bricolageGrotesque = new Font("Arial", Font.BOLD, 30);
        }
        
        // Set up the frame
        setTitle("School Vocabulary Quiz - Leaderboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with white background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
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
                    private static final long serialVersionUID = 1L;

					@Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // Draw the banner image
                        headerIcon.paintIcon(this, g, 0, 0);
                        
                        // Setup for text rendering
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2d.setFont(balsamiqBold.deriveFont(50f));
                        g2d.setColor(new Color(236, 122, 104)); // #ec7a68 color
                        
                        // Calculate text width to center it
                        String text = "School Vocabulary";
                        int textWidth = g2d.getFontMetrics().stringWidth(text);
                        int x = (getWidth() - textWidth) / 2;
                        int y = (int)(getHeight() / 1.5); // Position at bottom half
                        
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
        
        // Create leaderboard panel
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBackground(Color.WHITE);
        leaderboardPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        // Create leaderboard title button (non-clickable)
        RoundedPanel leaderboardTitle = new RoundedPanel(20, new Color(235, 76, 76)); // #eb4c4c color
        leaderboardTitle.setLayout(new BorderLayout());
        leaderboardTitle.setPreferredSize(new Dimension(300, 60));
        leaderboardTitle.setMaximumSize(new Dimension(300, 60));
        leaderboardTitle.setAlignmentX(CENTER_ALIGNMENT);
        
        // Add leaderboard title text
        JLabel leaderboardTitleLabel = new JLabel("LEADERBOARD");
        leaderboardTitleLabel.setFont(balsamiqBold);
        leaderboardTitleLabel.setForeground(Color.WHITE);
        leaderboardTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leaderboardTitle.add(leaderboardTitleLabel, BorderLayout.CENTER);
        
        // Create headers panel
        JPanel headersPanel = new JPanel(new GridLayout(1, 2));
        headersPanel.setBackground(Color.WHITE);
        headersPanel.setMaximumSize(new Dimension(700, 40));
        headersPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Name header
        JLabel nameHeaderLabel = new JLabel("NAME");
        nameHeaderLabel.setFont(balsamiqRegular);
        nameHeaderLabel.setForeground(new Color(255, 153, 102)); // Orange color
        nameHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Add left padding
        
        // Score header
        JLabel scoreHeaderLabel = new JLabel("SCORE");
        scoreHeaderLabel.setFont(balsamiqRegular);
        scoreHeaderLabel.setForeground(new Color(255, 153, 102)); // Orange color
        scoreHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headersPanel.add(nameHeaderLabel);
        headersPanel.add(scoreHeaderLabel);
        
        // Add components to leaderboard panel
        leaderboardPanel.add(leaderboardTitle);
        leaderboardPanel.add(Box.createVerticalStrut(30));
        leaderboardPanel.add(headersPanel);
        leaderboardPanel.add(Box.createVerticalStrut(10));
        
        // Add entries to leaderboard
        for (int i = 0; i < Math.min(leaderboardEntries.size(), 5); i++) {
            LeaderboardEntry entry = leaderboardEntries.get(i);
            
            // Create entry panel with fixed height to ensure consistent spacing
            JPanel entryPanel = new JPanel(new GridLayout(1, 2));
            entryPanel.setBackground(Color.WHITE);
            entryPanel.setMaximumSize(new Dimension(700, 50)); // Fixed height for all rows
            entryPanel.setPreferredSize(new Dimension(700, 50)); // Fixed height for all rows
            entryPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0)); // Add 100px left padding
            
            // Create name panel with consistent width for position indicator
            JPanel namePanel = new JPanel(new BorderLayout(10, 0)); // Add gap between position and name
            namePanel.setBackground(Color.WHITE);
            
            // Position indicator panel - fixed width and height for consistent alignment
            JPanel positionPanel = new JPanel(new BorderLayout());
            positionPanel.setBackground(Color.WHITE);
            positionPanel.setPreferredSize(new Dimension(50, 40)); // Fixed width and height
            
            // Create position label or image
            JLabel positionLabel = new JLabel();
            positionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Use medal images for top 3
            if (i < 3) {
                try {
                    // Load medal image for top 3
                    String medalFile = "";
                    switch (i) {
                        case 0:
                            medalFile = "assets/score/first.png";
                            break;
                        case 1:
                            medalFile = "assets/score/second.png";
                            break;
                        case 2:
                            medalFile = "assets/score/third.png";
                            break;
                    }
                    
                    File medalImageFile = new File(medalFile);
                    if (medalImageFile.exists()) {
                        Image medalImage = ImageIO.read(medalImageFile);
                        // Increase medal size while maintaining aspect ratio
                        int medalHeight = 60; // Increased from 30 to 40px
                        double aspectRatio = (double) medalImage.getWidth(null) / medalImage.getHeight(null);
                        int medalWidth = (int) (medalHeight * aspectRatio);
                        if (medalWidth > 100) medalWidth = 100; // Increased max width to 45px
                        
                        Image scaledMedal = medalImage.getScaledInstance(medalWidth, medalHeight, Image.SCALE_SMOOTH);
                        ImageIcon medalIcon = new ImageIcon(scaledMedal);
                        positionLabel.setIcon(medalIcon);
                        
                        // Adjust vertical position to center within row
                        positionLabel.setVerticalAlignment(SwingConstants.CENTER);
                    } else {
                        System.out.println("Medal file not found: " + medalFile);
                        positionLabel.setText((i + 1) + ".");
                        positionLabel.setFont(balsamiqBold.deriveFont(20f));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    positionLabel.setText((i + 1) + ".");
                    positionLabel.setFont(balsamiqBold.deriveFont(20f));
                }
            } else {
                positionLabel.setText((i + 1) + ".");
                positionLabel.setFont(balsamiqBold.deriveFont(20f));
                positionLabel.setForeground(new Color(255, 153, 102)); // Orange color
            }
            
            // Add position label to position panel for consistent width
            positionPanel.add(positionLabel, BorderLayout.CENTER);
            
            // Add name label with consistent alignment
            JLabel nameLabel = new JLabel(entry.getName());
            nameLabel.setFont(bricolageGrotesque);
            nameLabel.setForeground(new Color(255, 153, 102)); // Orange color
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add left padding
            
            // Add components to name panel
            namePanel.add(positionPanel, BorderLayout.WEST);
            namePanel.add(nameLabel, BorderLayout.CENTER);
            
            // Create score label
            JLabel scoreLabel = new JLabel(entry.getScore() + " / 10");
            scoreLabel.setFont(bricolageGrotesque);
            scoreLabel.setForeground(new Color(255, 153, 102)); // Orange color
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Add to entry panel
            entryPanel.add(namePanel);
            entryPanel.add(scoreLabel);
            
            // Add to leaderboard panel
            leaderboardPanel.add(entryPanel);
            
            // Add consistent vertical spacing between entries
            if (i < Math.min(leaderboardEntries.size(), 5) - 1) { // Don't add spacing after the last item
                leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leaderboardPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
    
    // Load leaderboard data from file
    private void loadLeaderboardData() {
        File leaderboardFile = new File("assets/leaderboard.txt");
        if (leaderboardFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(leaderboardFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        try {
                            int score = Integer.parseInt(parts[1].trim());
                            leaderboardEntries.add(new LeaderboardEntry(name, score));
                        } catch (NumberFormatException e) {
                            // Ignore invalid score entries
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Sort entries by score (highest first)
        leaderboardEntries.sort(Comparator.comparing(LeaderboardEntry::getScore).reversed());
    }
    
    // Leaderboard entry class
    private static class LeaderboardEntry {
        private String name;
        private int score;
        
        public LeaderboardEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
        
        public String getName() {
            return name;
        }
        
        public int getScore() {
            return score;
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
        javax.swing.SwingUtilities.invokeLater(() -> {
            ViewLeaderboard leaderboard = new ViewLeaderboard();
            leaderboard.setVisible(true);
        });
    }
} 