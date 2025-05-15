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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class StartQuiz extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Custom fonts
    private Font balsamiqBold;
    private Font balsamiqRegular;
    private Font bricolageGrotesque;
    
    // Quiz data
    private List<QuizItem> quizItems;
    private int currentQuestion = 0;
    private int score = 0;
    private String playerName;
    private Set<String> existingNames = new HashSet<>();
    
    // UI Components
    private JPanel mainPanel;
    private JLabel questionLabel;
    private RoundedPanel[] answerButtons = new RoundedPanel[3];
    private JLabel[] answerLabels = new JLabel[3];
    private JTextField nameField;
    
    // Constructor
    public StartQuiz() {
        // Load name data
        loadExistingNames();
        
        // Load custom fonts
        try {
            balsamiqBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans bold.ttf")).deriveFont(32f);
            balsamiqRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/balsamiq sans regular.ttf")).deriveFont(24f);
            bricolageGrotesque = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/bricolage grotesque.otf")).deriveFont(36f);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(balsamiqBold);
            ge.registerFont(balsamiqRegular);
            ge.registerFont(bricolageGrotesque);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            balsamiqBold = new Font("Arial", Font.BOLD, 32);
            balsamiqRegular = new Font("Arial", Font.PLAIN, 24);
            bricolageGrotesque = new Font("Arial", Font.BOLD, 36);
        }
        
        // Set up the frame
        setTitle("School Vocabulary Quiz");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with cream/light yellow background
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 248, 220)); // Light cream background
        
        // Show the name entry screen first
        showNameEntryScreen();
        
        // Add the main panel to the frame
        setContentPane(mainPanel);
    }
    
    // Load existing names from file
    private void loadExistingNames() {
        File nameFile = new File("assets/name.txt");
        if (nameFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(nameFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        existingNames.add(line.trim().toLowerCase());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Save new name to file
    private void saveName(String name) {
        File nameFile = new File("assets/name.txt");
        try {
            // Create directory if it doesn't exist
            File directory = new File("assets");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nameFile, true))) {
                writer.write(name);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Show name entry screen
    private void showNameEntryScreen() {
        // Clear the main panel
        mainPanel.removeAll();
        
        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 248, 220));
        contentPanel.setBorder(new EmptyBorder(100, 0, 0, 0));
        
        // Create label
        JLabel promptLabel = new JLabel("Please enter your name");
        promptLabel.setFont(balsamiqBold);
        promptLabel.setForeground(new Color(233, 126, 126)); // Coral color
        promptLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Create text field with red border
        nameField = new JTextField(20);
        nameField.setFont(balsamiqRegular);
        nameField.setPreferredSize(new Dimension(300, 50));
        nameField.setMaximumSize(new Dimension(500, 50));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(233, 91, 91), 2));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        
        // Create start quiz button
        RoundedPanel startButton = new RoundedPanel(20, new Color(233, 91, 91));
        startButton.setLayout(new GridBagLayout());
        startButton.setPreferredSize(new Dimension(220, 60));
        startButton.setMaximumSize(new Dimension(220, 60));
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        
        // Create the label for the button
        JLabel startLabel = new JLabel("START QUIZ");
        startLabel.setFont(balsamiqRegular);
        startLabel.setForeground(Color.WHITE);
        startButton.add(startLabel);
        
        // Add hover effect and click behavior
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
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(StartQuiz.this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (existingNames.contains(name.toLowerCase())) {
                    JOptionPane.showMessageDialog(StartQuiz.this, "This name already exists. Please enter a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                playerName = name;
                saveName(name);
                
                // Load quiz data
                loadQuizData();
                
                // Show quiz screen
                showQuizScreen();
            }
        });
        
        // Add components to content panel with spacing
        contentPanel.add(promptLabel);
        contentPanel.add(Box.createVerticalStrut(50)); // Add space
        contentPanel.add(nameField);
        contentPanel.add(Box.createVerticalStrut(50)); // Add space
        contentPanel.add(startButton);
        
        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Refresh the panel
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    // Load quiz data from files
    private void loadQuizData() {
        quizItems = new ArrayList<>();
        Map<String, String> textDescriptions = new HashMap<>();
        List<String> correctAnswers = new ArrayList<>();
        
        // Load text descriptions from file
        File textFile = new File("assets/school text/school.txt");
        if (!textFile.exists()) {
            JOptionPane.showMessageDialog(this, "Quiz data file not found", "Error", JOptionPane.ERROR_MESSAGE);
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
                    correctAnswers.add(word.toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading quiz data", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create quiz items with question and answer choices
        List<String> allWords = new ArrayList<>(correctAnswers);
        
        for (String correctAnswer : correctAnswers) {
            String description = textDescriptions.get(correctAnswer);
            
            // Create list of possible wrong answers (excluding the correct one)
            List<String> wrongAnswers = new ArrayList<>(allWords);
            wrongAnswers.remove(correctAnswer);
            Collections.shuffle(wrongAnswers);
            
            // Select two wrong answers
            List<String> choices = new ArrayList<>();
            choices.add(correctAnswer);
            choices.add(wrongAnswers.get(0));
            choices.add(wrongAnswers.get(1));
            
            // Shuffle choices
            Collections.shuffle(choices);
            
            // Create quiz item
            quizItems.add(new QuizItem(description, choices, correctAnswer));
        }
        
        // Shuffle quiz items
        Collections.shuffle(quizItems);
        
        // Reset counters
        currentQuestion = 0;
        score = 0;
    }
    
    // Show quiz screen
    private void showQuizScreen() {
        // Clear the main panel
        mainPanel.removeAll();
        
        // Create quiz panel
        JPanel quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBackground(new Color(255, 248, 220));
        
        // Create header panel with layered components
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
                int targetWidth = quizPanel.getWidth() > 0 ? quizPanel.getWidth() - 40 : 960;
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
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);                        g2d.setFont(balsamiqBold.deriveFont(85f));
                        g2d.setColor(new Color(233, 126, 126)); // Coral color
                        
                        // Calculate text width to center it
                        String text = "School";
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
        
        // Create content panel for question and answers with WHITE background
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE); // Changed to white background (#fff)
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        // Create question panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE); // Match white background
        questionPanel.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        // Create question label
        questionLabel = new JLabel();
        questionLabel.setFont(balsamiqBold);
        questionLabel.setForeground(new Color(233, 126, 126)); // Coral color
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        
        // Create answers panel - Changed to FlowLayout for HORIZONTAL alignment (side by side)
        JPanel answersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px horizontal gap
        answersPanel.setBackground(Color.WHITE); // Match white background
        answersPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Create answer buttons horizontally (side by side)
        for (int i = 0; i < 3; i++) {
            // Create rounded panel button with increased border radius
            answerButtons[i] = new RoundedPanel(30, Color.WHITE); // Increased radius to 30, white background
            answerButtons[i].setLayout(new GridBagLayout()); // Changed to GridBagLayout for better centering
            
            // Make buttons smaller to fit side by side
            answerButtons[i].setPreferredSize(new Dimension(220, 60));
            
            // Remove the border that might be hiding the rounded corners
            answerButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            // Create answer label
            final int index = i;
            String optionLetter = (char)('A' + i) + ". ";
            answerLabels[i] = new JLabel(optionLetter);
            answerLabels[i].setFont(balsamiqRegular);
            answerLabels[i].setForeground(new Color(227, 132, 83)); // #e38453 text color
            
            answerButtons[i].add(answerLabels[i]);
            
            // Add hover effect and click behavior
            answerButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            answerButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    answerButtons[index].setBackground(new Color(255, 245, 240)); // Light hover color
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    answerButtons[index].setBackground(Color.WHITE);
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkAnswer(index);
                }
            });
            
            // Add to answers panel side by side
            answersPanel.add(answerButtons[i]);
        }
        
        // Add components to content panel
        contentPanel.add(questionPanel, BorderLayout.NORTH);
        contentPanel.add(answersPanel, BorderLayout.CENTER);
        
        // Add components to quiz panel
        quizPanel.add(headerPanel, BorderLayout.NORTH);
        quizPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add quiz panel to main panel
        mainPanel.add(quizPanel, BorderLayout.CENTER);
        
        // Display the first question
        displayQuestion();
        
        // Refresh the panel
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    // Display current question
    private void displayQuestion() {
        if (currentQuestion < quizItems.size() && currentQuestion < 10) {
            QuizItem item = quizItems.get(currentQuestion);
            
            // Set question text
            questionLabel.setText("<html><div style='text-align: center; width: 500px; padding: 20px;'>" + item.getQuestion() + "</div></html>");
            
            // Set answer choices
            List<String> choices = item.getChoices();
            for (int i = 0; i < 3; i++) {
                String optionLetter = (char)('A' + i) + ". ";
                answerLabels[i].setText(optionLetter + capitalizeFirstLetter(choices.get(i)));
            }
        } else {
            // Quiz is complete, show score screen
            saveScore();
            showScoreScreen();
        }
    }
    
    // Check if the answer is correct
    private void checkAnswer(int selectedIndex) {
        QuizItem item = quizItems.get(currentQuestion);
        String selectedAnswer = item.getChoices().get(selectedIndex);
        
        if (selectedAnswer.equalsIgnoreCase(item.getCorrectAnswer())) {
            score++;
            saveScore(); // Save score after each correct answer
        }
        
        // Move to next question
        currentQuestion++;
        displayQuestion();
    }
    
    // Save score to file
    private void saveScore() {
        File scoreFile = new File("assets/score.txt");
        try {
            // Create directory if it doesn't exist
            File directory = new File("assets");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreFile))) {
                writer.write(String.valueOf(score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Show score screen
    private void showScoreScreen() {
        dispose(); // Close current window
        SwingUtilities.invokeLater(() -> {
            Score scoreScreen = new Score(score, playerName);
            scoreScreen.setVisible(true);
        });
    }
    
    // Helper method to capitalize first letter
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
    
    // Quiz item class
    private class QuizItem {
        private String question;
        private List<String> choices;
        private String correctAnswer;
        
        public QuizItem(String question, List<String> choices, String correctAnswer) {
            this.question = question;
            this.choices = choices;
            this.correctAnswer = correctAnswer;
        }
        
        public String getQuestion() {
            return question;
        }
        
        public List<String> getChoices() {
            return choices;
        }
        
        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
    
    // Custom panel with rounded corners
    private class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int cornerRadius;
        private final Color bgColor;
        
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setBackground(bgColor);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            
            // Draw border for answer buttons
            if (this == answerButtons[0] || this == answerButtons[1] || this == answerButtons[2]) {
                g2.setColor(new Color(247, 150, 109));  // Salmon border color
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, cornerRadius, cornerRadius);
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, cornerRadius, cornerRadius);
            }
            
            g2.dispose();
        }
    }
    
    // For testing purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartQuiz startQuiz = new StartQuiz();
            startQuiz.setVisible(true);
        });
    }
} 
