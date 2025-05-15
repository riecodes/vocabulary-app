package vocab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class StartVocab extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font krabuler;
    
    // Current vocabulary data
    private List<VocabItem> vocabItems;
    private int currentIndex = 0;
    
    // UI Components
    private JLabel imageLabel;
    private JLabel descriptionLabel;
    private final JLabel wordLabel;
    private RoundedPanel leftArrow;
    private RoundedPanel rightArrow;
    private JLabel volumeIcon;
    
    // Constructor
    public StartVocab() {
        // Load vocabulary data
        loadVocabularyData();
        
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(32f);
            krabuler = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/krabuler.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(krabuler);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            balsamiqBold = new Font("Arial", Font.BOLD, 32);
            krabuler = new Font("Arial", Font.BOLD, 60);
        }
        
        // Set up the frame
        setTitle("Vocabulary Learning");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with pink background
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(252, 227, 252)); // Light pink background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Content panel (white rounded rectangle) - Remove border
        RoundedPanel contentPanel = new RoundedPanel(20, Color.WHITE);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel for image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Right panel for description - Move text down by adding top padding
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setOpaque(false);
        
        // Add empty panel at top to push description down
        JPanel topPadding = new JPanel();
        topPadding.setOpaque(false);
        topPadding.setPreferredSize(new Dimension(1, 60)); // 60px padding at top
        descriptionPanel.add(topPadding, BorderLayout.NORTH);
        
        // Main content panel for description and audio
        JPanel contentArea = new JPanel(new BorderLayout(10, 0));
        contentArea.setOpaque(false);
        
        // Center the description in its own panel
        JPanel descriptionTextPanel = new JPanel(new BorderLayout());
        descriptionTextPanel.setOpaque(false);
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(balsamiqBold);
        descriptionLabel.setForeground(new Color(102, 72, 39)); // Brown color
        descriptionLabel.setHorizontalAlignment(JLabel.CENTER); // Center the text
        descriptionLabel.setVerticalAlignment(JLabel.CENTER);
        descriptionTextPanel.add(descriptionLabel, BorderLayout.CENTER);
        
        // Add description text panel to the content area
        contentArea.add(descriptionTextPanel, BorderLayout.CENTER);
        
        // Create audio panel with a proper layout manager
        JPanel audioPanel = new JPanel(new BorderLayout());
        audioPanel.setOpaque(false);
        
        // Create a panel to center the audio icon vertically
        JPanel audioCenterPanel = new JPanel();
        audioCenterPanel.setLayout(new BoxLayout(audioCenterPanel, BoxLayout.Y_AXIS));
        audioCenterPanel.setOpaque(false);
        
        // Add top padding to align with description text
        audioCenterPanel.add(Box.createVerticalStrut(0));
        
        // Create a panel for horizontal centering
        JPanel audioIconPanel = new JPanel(new GridBagLayout());
        audioIconPanel.setOpaque(false);
        
        // Check if file exists and load appropriate icon
        File audioIconFile = new File("assets/score/audio.png");
        if (audioIconFile.exists()) {
            try {
                Image audioImage = ImageIO.read(audioIconFile);
                // Keep exact size as specified (90x160)
                ImageIcon audioIcon = new ImageIcon(audioImage.getScaledInstance(90, 160, Image.SCALE_SMOOTH));
                volumeIcon = new JLabel(audioIcon);
                audioIconPanel.add(volumeIcon); // Add to icon panel
            } catch (IOException e) {
                e.printStackTrace();
                volumeIcon = new JLabel("🔊"); // Fallback to text if image can't be loaded
                audioIconPanel.add(volumeIcon);
            }
        } else {
            System.out.println("Audio icon not found at: " + audioIconFile.getAbsolutePath());
            volumeIcon = new JLabel("🔊"); // Fallback to text
            audioIconPanel.add(volumeIcon);
        }
        
        // Add icon panel to the center panel
        audioCenterPanel.add(audioIconPanel);
        
        // Add flexible space below the icon
        audioCenterPanel.add(Box.createVerticalGlue());
        
        // Add center panel to audio panel
        audioPanel.add(audioCenterPanel, BorderLayout.CENTER);
        
        // Add audio panel to the content area
        contentArea.add(audioPanel, BorderLayout.EAST);
        
        // Add content area to the description panel
        descriptionPanel.add(contentArea, BorderLayout.CENTER);
        
        // Add glue to push content to the top
        descriptionPanel.add(Box.createVerticalGlue(), BorderLayout.SOUTH);
        
        // Fix audio playback to use a more reliable approach
        volumeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volumeIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // If audio is already playing, stop it first - otherwise play the current audio
                if (AudioPlayer.isPlaying()) {
                    AudioPlayer.stopAudio();
                    System.out.println("Stopped existing audio playback");
                } else {
                    playCurrentAudio();
                }
            }
        });
        
        // Split pane for image and description - remove divider
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagePanel, descriptionPanel);
        splitPane.setOpaque(false);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null); // Remove border
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel with navigation and word - now outside the white content panel
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 20));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Create a panel to hold all navigation elements with proper spacing
        JPanel navigationContainer = new JPanel(new BorderLayout(30, 0));
        navigationContainer.setOpaque(false);
        
        // Left arrow button
        leftArrow = createArrowButton(false);
        JPanel leftArrowContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        leftArrowContainer.setOpaque(false);
        leftArrowContainer.add(leftArrow);
        navigationContainer.add(leftArrowContainer, BorderLayout.WEST);
        
        // Word panel (center) with its own background
        RoundedPanel wordPanel = new RoundedPanel(20, new Color(253, 236, 166)); // Light yellow background
        wordPanel.setLayout(new BorderLayout());
        wordPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        wordLabel = new JLabel("", JLabel.CENTER);
        wordLabel.setFont(krabuler);
        wordLabel.setForeground(new Color(102, 72, 39)); // Brown color
        wordPanel.add(wordLabel, BorderLayout.CENTER);
        navigationContainer.add(wordPanel, BorderLayout.CENTER);
        
        // Right arrow button
        rightArrow = createArrowButton(true);
        JPanel rightArrowContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightArrowContainer.setOpaque(false);
        rightArrowContainer.add(rightArrow);
        navigationContainer.add(rightArrowContainer, BorderLayout.EAST);
        
        // Add navigation container to bottom panel
        bottomPanel.add(navigationContainer, BorderLayout.CENTER);
        
        // Add bottom panel to main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add the main panel to the frame
        setContentPane(mainPanel);
        
        // Display the first vocabulary item
        if (!vocabItems.isEmpty()) {
            displayVocabItem(currentIndex);
        } else {
            // Show a message if no vocabulary items are found
            wordLabel.setText("No Items");
            descriptionLabel.setText("<html><div style='width:300px; text-align:center;'>No vocabulary items found. Please check your data files.</div></html>");
        }
    }
    
    // Create arrow button with the panel trick
    private RoundedPanel createArrowButton(boolean isRightArrow) {
        // Create arrow panel with 4:3 ratio but reduced size
        RoundedPanel arrowPanel = new RoundedPanel(20, new Color(255, 191, 159)); // Light orange background
        arrowPanel.setLayout(new GridBagLayout());
        arrowPanel.setPreferredSize(new Dimension(100, 90)); // Reduced size, 4:3 ratio (65:50)
        
        // Use image icons or fallback to text if images aren't available
        String arrowText = isRightArrow ? "→" : "←";
        JLabel arrowLabel;
        
        try {
            String imagePath = isRightArrow ? "assets/score/arrow right.png" : "assets/score/arrow left.png";
            File arrowFile = new File(imagePath);
            
            if (arrowFile.exists()) {
                System.out.println("Loading arrow image: " + arrowFile.getAbsolutePath());
                Image arrowImage = ImageIO.read(arrowFile);
                // Scale to fit the arrow panel
                Image scaledArrow = arrowImage.getScaledInstance(100, 90, Image.SCALE_SMOOTH);
                arrowLabel = new JLabel(new ImageIcon(scaledArrow));
            } else {
                System.out.println("Arrow image not found: " + imagePath);
                arrowLabel = new JLabel(arrowText);
                arrowLabel.setFont(new Font("Arial", Font.BOLD, 40));
                arrowLabel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            arrowLabel = new JLabel(arrowText);
            arrowLabel.setFont(new Font("Arial", Font.BOLD, 40));
            arrowLabel.setForeground(Color.WHITE);
        }
        
        arrowPanel.add(arrowLabel);
        
        arrowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        arrowPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (arrowPanel.isEnabled()) {
                    arrowPanel.setBackground(new Color(255, 171, 139)); // Darker orange on hover
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (arrowPanel.isEnabled()) {
                    arrowPanel.setBackground(new Color(255, 191, 159)); // Return to original color
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!arrowPanel.isEnabled()) {
                    return; // Don't process click if disabled
                }
                
                if (isRightArrow) {
                    // Next vocabulary item
                    if (!vocabItems.isEmpty()) {
                        currentIndex = (currentIndex + 1) % vocabItems.size();
                        displayVocabItem(currentIndex);
                        // Enable left arrow when moving forward
                        leftArrow.setEnabled(true);
                        leftArrow.setBackground(new Color(255, 191, 159));
                        
                        // Check if we've completed the vocabulary
                        if (currentIndex == 0) {
                            showCompletionDialog();
                        }
                    }
                } else {
                    // Previous vocabulary item
                    if (!vocabItems.isEmpty()) {
                        currentIndex = (currentIndex - 1 + vocabItems.size()) % vocabItems.size();
                        displayVocabItem(currentIndex);
                        // Disable left arrow if we're at the first item
                        if (currentIndex == 0) {
                            leftArrow.setEnabled(false);
                            leftArrow.setBackground(new Color(200, 200, 200)); // Gray out when disabled
                        }
                    }
                }
            }
        });
        
        return arrowPanel;
    }
    
    // Load vocabulary data from files
    private void loadVocabularyData() {
        vocabItems = new ArrayList<>();
        Map<String, String> textDescriptions = new HashMap<>();
        
        // Debug info
        System.out.println("Loading vocabulary data...");
        
        // Load text descriptions from file
        File textFile = new File("assets/school text/school.txt");
        if (!textFile.exists()) {
            System.out.println("Text file not found: " + textFile.getAbsolutePath());
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Trim to handle possible whitespace
                line = line.trim();
                if (line.isEmpty()) continue;
                
                int separatorIndex = line.indexOf(" – "); // Note: this is an en dash, not a hyphen
                if (separatorIndex <= 0) {
                    separatorIndex = line.indexOf(" - "); // Try regular hyphen as fallback
                }
                
                if (separatorIndex > 0) {
                    String word = line.substring(0, separatorIndex).trim();
                    String description = line.substring(separatorIndex + 3).trim();
                    textDescriptions.put(word.toLowerCase(), description);
                    System.out.println("Found word: " + word + " with description: " + description);
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading text file: " + e.getMessage());
        }
        
        // Search for matching image files
        File imageDir = new File("assets/school pictures");
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            System.out.println("Image directory not found: " + imageDir.getAbsolutePath());
            return;
        }
        
        // Debug audio directory
        File audioDir = new File("assets/school audio");
        if (!audioDir.exists() || !audioDir.isDirectory()) {
            System.out.println("Audio directory not found: " + audioDir.getAbsolutePath());
        } else {
            System.out.println("Audio directory found: " + audioDir.getAbsolutePath());
            File[] audioFiles = audioDir.listFiles();
            if (audioFiles != null) {
                System.out.println("Audio files found: " + audioFiles.length);
                for (File audioFile : audioFiles) {
                    System.out.println("  - " + audioFile.getName());
                }
            } else {
                System.out.println("No audio files found in directory");
            }
        }
        
        File[] imageFiles = imageDir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No image files found in: " + imageDir.getAbsolutePath());
            return;
        }
        
        // Create a map to store image files for quick lookup
        Map<String, File> imageFileMap = new HashMap<>();
        for (File imageFile : imageFiles) {
            String filename = imageFile.getName();
            if (filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpg")) {
                String word = filename.substring(0, filename.lastIndexOf(".")).toLowerCase();
                imageFileMap.put(word, imageFile);
            }
        }
        
        // Add vocabulary items in the order they appear in the text file
        for (String word : textDescriptions.keySet()) {
            File imageFile = imageFileMap.get(word);
            if (imageFile != null) {
                System.out.println("Found matching image for word: " + word);
                
                // Check for audio file
                File audioFile = new File("assets/school audio/" + word + " audio.wav");
                String audioPath = audioFile.exists() ? audioFile.getAbsolutePath() : null;
                
                // Add to vocabulary items
                vocabItems.add(new VocabItem(
                    word,
                    imageFile.getAbsolutePath(),
                    audioPath,
                    textDescriptions.get(word)
                ));
            } else {
                System.out.println("No image found for word: " + word);
            }
        }
        
        System.out.println("Total vocabulary items loaded: " + vocabItems.size());
    }
    
    // Display vocabulary item at the given index
    private void displayVocabItem(int index) {
        if (index >= 0 && index < vocabItems.size()) {
            VocabItem item = vocabItems.get(index);
            
            // Update left arrow state
            if (index == 0) {
                leftArrow.setEnabled(false);
                leftArrow.setBackground(new Color(200, 200, 200)); // Gray out when disabled
            } else {
                leftArrow.setEnabled(true);
                leftArrow.setBackground(new Color(255, 191, 159)); // Normal color when enabled
            }
            
            // Load and display image
            try {
                File imageFile = new File(item.getImagePath());
                if (imageFile.exists()) {
                    // Load the image and scale it properly while maintaining aspect ratio
                    BufferedImage originalImage = ImageIO.read(imageFile);
                    
                    // Calculate dimensions for proper display
                    // Original is 1080x1920, we want to fit in a smaller panel
                    int panelWidth = 650;
                    int panelHeight = 650;
                    
                    double widthRatio = (double) panelWidth / originalImage.getWidth();
                    double heightRatio = (double) panelHeight / originalImage.getHeight();
                    double ratio = Math.min(widthRatio, heightRatio);
                    
                    int scaledWidth = (int) (originalImage.getWidth() * ratio);
                    int scaledHeight = (int) (originalImage.getHeight() * ratio);
                    
                    Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    System.out.println("Image file not found: " + item.getImagePath());
                    imageLabel.setIcon(null);
                    imageLabel.setText("Image not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setIcon(null);
                imageLabel.setText("Error loading image");
            }
            
            // Set description text - center aligned
            descriptionLabel.setText("<html><div style='width:300px; text-align:center;'>" + item.getDescription() + "</div></html>");
            
            // Set word text (capitalize first letter for display)
            String displayWord = item.getWord();
            if (displayWord != null && !displayWord.isEmpty()) {
                displayWord = displayWord.substring(0, 1).toUpperCase() + displayWord.substring(1);
            }
            wordLabel.setText(displayWord);
            
            // Debug output
            System.out.println("Displaying: " + item.getWord() + " - " + item.getDescription());
        }
    }
    
    // Vocabulary item class
    private class VocabItem {
        private String word;
        private String imagePath;
        private String audioPath;
        private String description;
        
        public VocabItem(String word, String imagePath, String audioPath, String description) {
            this.word = word;
            this.imagePath = imagePath;
            this.audioPath = audioPath;
            this.description = description;
        }
        
        public String getWord() {
            return word;
        }
        
        public String getImagePath() {
            return imagePath;
        }
        
        public String getAudioPath() {
            return audioPath;
        }
        
        public String getDescription() {
            return description;
        }
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
    
    // Updated method to play WAV audio files directly using our AudioPlayer class
    private void playCurrentAudio() {
        if (vocabItems.isEmpty()) {
            System.out.println("No vocabulary items loaded, can't play audio");
            return;
        }
        
        VocabItem currentItem = vocabItems.get(currentIndex);
        String audioPath = currentItem.getAudioPath();
        
        System.out.println("Audio debug information:");
        System.out.println("  Current word: " + currentItem.getWord());
        System.out.println("  Audio path: " + audioPath);
        
        if (audioPath == null) {
            System.out.println("  No audio path specified for this item");
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, 
                "No audio file available for this word.",
                "Audio Playback Notice", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        File audioFile = new File(audioPath);
        if (!audioFile.exists()) {
            System.out.println("  Audio file does not exist: " + audioPath);
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, 
                "Audio file not found: " + audioPath,
                "Audio Playback Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("  Audio file exists, attempting to play...");
        
        // Play the WAV file using our AudioPlayer class
        new Thread(() -> {
            boolean success = AudioPlayer.playAudio(audioFile);
            
            if (success) {
                System.out.println("  Audio playback started successfully");
            } else {
                System.out.println("  Failed to play audio file");
                showAudioError("Failed to play audio file. Please check console for details.");
            }
        }).start();
    }
    
    private void showAudioError(String message) {
        SwingUtilities.invokeLater(() -> {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,
                message,
                "Audio Playback Error",
                JOptionPane.ERROR_MESSAGE);
        });
    }
    
    private void showCompletionDialog() {
        String[] options = {"Restart", "Back to Menu"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Congratulations! You've completed all vocabulary items.",
            "Vocabulary Complete",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == 0) {
            // Restart
            currentIndex = 0;
            displayVocabItem(currentIndex);
        } else {
            // Back to menu
            dispose();
            SwingUtilities.invokeLater(() -> {
                ChooseVocab chooseScreen = new ChooseVocab();
                chooseScreen.setVisible(true);
            });
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartVocab app = new StartVocab();
            app.setVisible(true);
        });
    }
} 
