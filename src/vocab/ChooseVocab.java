package vocab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChooseVocab extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font krabuler;
    
    public ChooseVocab() {
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(48f);
            krabuler = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/krabuler.ttf")).deriveFont(28f); // Increased text size
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(krabuler);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            balsamiqBold = new Font("Arial", Font.BOLD, 48);
            krabuler = new Font("Arial", Font.BOLD, 28); // Increased text size
        }
        
        // Set up the frame
        setTitle("Vocabulary App");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with pink background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(252, 227, 252)); // Light pink background
        
        // Title panel with increased top padding
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(252, 227, 252));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0)); // Added more vertical padding
        
        JLabel titleLabel = new JLabel("Choose your vocabulary");
        titleLabel.setFont(balsamiqBold);
        titleLabel.setForeground(new Color(233, 126, 126)); // Coral color
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Grid panel for vocabulary options with more spacing
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 25, 25)); // Increased grid spacing
        gridPanel.setBackground(new Color(252, 227, 252));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40)); // Adjusted border
        
        // Add vocabulary options
        gridPanel.add(createVocabOption("school.png", "SCHOOL"));
        gridPanel.add(createVocabOption("academic.png", "ACADEMIC"));
        gridPanel.add(createVocabOption("house.png", "HOUSE"));
        gridPanel.add(createVocabOption("hygiene.png", "HYGIENE"));
        gridPanel.add(createVocabOption("animals.png", "ANIMALS"));
        gridPanel.add(createVocabOption("food.png", "FOODS & DRINKS"));
        gridPanel.add(createVocabOption("places.png", "COMMON PLACES"));
        gridPanel.add(createVocabOption("nature.png", "NATURE"));
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        
        // Add the main panel to the frame
        setContentPane(mainPanel);
    }
    
    private JPanel createVocabOption(String imageName, String text) {
        // Create a square panel with white background and rounded corners
        JPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout()); // Changed to BorderLayout for better control
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create a panel for the image with center alignment
        JPanel imagePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        imagePanel.setOpaque(false);
        
        // Create a panel for the text with center alignment
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        
        try {
            // Load high quality image using ImageIO instead of ImageIcon
            BufferedImage originalImage = ImageIO.read(new File("assets/select vocabulary/" + imageName));
            
            // Scale the image to the desired size while maintaining aspect ratio
            int targetSize = 300; // Increased from 90 to 130
            
            // Use a higher quality scaling approach
            BufferedImage resizedImage = resizeImageWithHighQuality(originalImage, targetSize);
            
            // Create the image label
            JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
            imagePanel.add(imageLabel);
            
            // Create the text label
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(krabuler);
            textLabel.setForeground(new Color(233, 126, 126)); // Coral color
            textPanel.add(textLabel);
            
            // Add components to panel
            panel.add(imagePanel, BorderLayout.CENTER);
            panel.add(textPanel, BorderLayout.SOUTH);
            
            // Add hover effect and click listener
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    panel.setBackground(new Color(245, 245, 245));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBackground(Color.WHITE);
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (text.equals("SCHOOL")) {
                        // Navigate to selectedCheckpoint.java
                        dispose(); // Close current window
                        SwingUtilities.invokeLater(() -> {
                            Checkpoint checkpointScreen = new Checkpoint();
                            checkpointScreen.setVisible(true);
                        });
                    } else {
                        System.out.println("Selected: " + text);
                    }
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return panel;
    }
    
    // New helper method for high quality image resizing
    private BufferedImage resizeImageWithHighQuality(BufferedImage original, int targetSize) {
        // Calculate new dimensions while preserving aspect ratio
        double scale = Math.min(
            (double)targetSize / original.getWidth(),
            (double)targetSize / original.getHeight()
        );
        
        int newWidth = (int)(original.getWidth() * scale);
        int newHeight = (int)(original.getHeight() * scale);
        
        // For the best quality, use a progressive downscaling approach if image is much larger
        if (scale < 0.5 && original.getWidth() > 2*newWidth && original.getHeight() > 2*newHeight) {
            // Step 1: Create an intermediate image at 2x target size
            BufferedImage intermediate = new BufferedImage(
                newWidth * 2, newHeight * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = intermediate.createGraphics();
            
            setHighQualityHints(g2);
            g2.drawImage(original, 0, 0, newWidth * 2, newHeight * 2, null);
            g2.dispose();
            
            // Step 2: Scale down to final size
            BufferedImage output = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g3 = output.createGraphics();
            
            setHighQualityHints(g3);
            g3.drawImage(intermediate, 0, 0, newWidth, newHeight, null);
            g3.dispose();
            
            return output;
        } else {
            // Direct high-quality scaling for smaller adjustments
            BufferedImage output = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            
            setHighQualityHints(g2);
            g2.drawImage(original, 0, 0, newWidth, newHeight, null);
            g2.dispose();
            
            return output;
        }
    }
    
    // Set highest quality rendering hints
    private void setHighQualityHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    }
    
    // Custom panel with rounded corners
    private class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private int cornerRadius;
        
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
            ChooseVocab app = new ChooseVocab();
            app.setVisible(true);
        });
    }
} 
