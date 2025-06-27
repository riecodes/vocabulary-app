# Vocabulary Learning Application

A comprehensive Java-based vocabulary learning application designed to help users learn English vocabulary through interactive lessons and quizzes. The application currently focuses on school-related vocabulary with plans for expansion to other categories.

## 🌟 Features

### 📚 Vocabulary Learning
- **Interactive Vocabulary Browser**: Navigate through vocabulary items with visual and audio support
- **Multiple Vocabulary Categories**: Currently includes School vocabulary with placeholders for Academic, House, Hygiene, Animals, Food & Drinks, Common Places, and Nature
- **Visual Learning**: Each vocabulary item includes high-quality images for better memorization
- **Audio Pronunciation**: Native audio playback for proper pronunciation learning
- **Rich Descriptions**: Detailed explanations for each vocabulary term

### 🎯 Quiz System
- **Interactive Quizzes**: Multiple-choice questions to test vocabulary knowledge
- **Score Tracking**: Real-time scoring system with results out of 10
- **Name Registration**: Player name system to track individual progress
- **Duplicate Name Prevention**: Ensures unique player identities

### 🏆 Leaderboard System
- **Score Persistence**: Saves quiz scores to local files
- **Top 5 Rankings**: Displays the top 5 performers
- **Medal System**: Visual medals for 1st, 2nd, and 3rd place
- **Beautiful UI**: Styled leaderboard with custom fonts and colors

### 🎨 User Interface
- **Modern Design**: Clean, colorful interface with rounded corners and smooth animations
- **Custom Typography**: Uses specialized fonts (Balsamiq Sans, Bricolage Grotesque, Krabuler, Manjari)
- **Responsive Layout**: Adaptive UI components that work across different screen sizes
- **Themed Color Scheme**: Consistent coral and pink color palette throughout the application
- **Hover Effects**: Interactive button states for better user experience

## 📁 Project Structure

```
vocab-app/
├── src/vocab/                      # Source code
│   ├── AudioPlayer.java           # Audio playback utility
│   ├── Checkpoint.java            # Main entry screen
│   ├── CheckpointQuiz.java        # Quiz introduction screen
│   ├── ChooseVocab.java           # Vocabulary category selection
│   ├── Score.java                 # Score display and results
│   ├── StartQuiz.java             # Quiz implementation
│   ├── StartVocab.java            # Vocabulary learning interface
│   └── ViewLeaderboard.java       # Leaderboard display
├── assets/                        # Application resources
│   ├── fonts/                     # Custom typography files
│   ├── house audio/               # Audio files for house vocabulary
│   ├── house pictures/            # Images for house vocabulary  
│   ├── school audio/              # Audio files for school vocabulary
│   ├── school pictures/           # Images for school vocabulary
│   ├── school text/               # Text definitions for school vocabulary
│   ├── score/                     # UI elements for scoring system
│   └── select vocabulary/         # Category selection images
├── bin/                           # Compiled Java classes
└── pdfs/                          # Documentation files
```

## 🛠️ Technologies Used

- **Java Swing**: For the graphical user interface
- **Java Sound API**: For audio playback functionality
- **Custom Graphics**: Java 2D API for rounded panels and custom rendering
- **File I/O**: For data persistence and resource management
- **Module System**: Java 9+ module system for better encapsulation

## 📋 System Requirements

- **Java Version**: Java 11 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 256MB RAM
- **Storage**: At least 50MB free space for application and assets

## 🚀 Installation & Setup

### Prerequisites
Ensure you have Java Development Kit (JDK) 11 or higher installed:
```bash
java --version
javac --version
```

### Running the Application

1. **Clone or download the project**
2. **Navigate to the project directory**
   ```bash
   cd vocab-app
   ```

3. **Compile the source code** (if not already compiled)
   ```bash
   javac -d bin src/module-info.java src/vocab/*.java
   ```

4. **Run the application**
   ```bash
   java --module-path bin -m vocab/vocab.ChooseVocab
   ```

### Alternative Entry Points

You can start the application from different screens:

- **Vocabulary Selection**: `java --module-path bin -m vocab/vocab.ChooseVocab`
- **School Checkpoint**: `java --module-path bin -m vocab/vocab.Checkpoint`
- **Direct Vocabulary Learning**: `java --module-path bin -m vocab/vocab.StartVocab`
- **Quiz Mode**: `java --module-path bin -m vocab/vocab.StartQuiz`

## 🎮 How to Use

### 1. Starting the Application
- Launch the application to see the vocabulary category selection screen
- Currently, only "SCHOOL" category is fully implemented
- Click on "SCHOOL" to begin learning

### 2. Learning Vocabulary
- Navigate through vocabulary items using arrow buttons
- Click the audio icon to hear pronunciation
- Read the descriptions to understand word meanings
- Use left/right arrows or click to move between items

### 3. Taking Quizzes
- From the checkpoint screen, you can access the quiz
- Enter your name (must be unique)
- Answer 10 multiple-choice questions
- View your score and ranking on the leaderboard

### 4. Viewing Progress
- Access the leaderboard to see top performers
- Scores are automatically saved after each quiz
- Medal system recognizes top 3 performers

## 📊 Current Vocabulary Content

### School Vocabulary (15 items)
- Teacher, Student, Classroom, Homework, Book
- Pencil, Desk, Blackboard, Notebook, Uniform  
- Exam, Subject, Recess, Library, Backpack

Each item includes:
- High-quality image representation
- Audio pronunciation file (.mp3 or .wav)
- Detailed description and usage context

## 🔧 Data Files

### Score Tracking
- `assets/score.txt`: Stores quiz scores in format "Name,Score"
- `assets/name.txt`: Tracks registered player names
- `assets/leaderboard.txt`: Maintains leaderboard data

### Vocabulary Data
- `assets/school text/school.txt`: Text definitions for school vocabulary
- Audio files in `assets/school audio/`
- Images in `assets/school pictures/`

## 🎨 Customization

### Adding New Vocabulary Categories
1. Create image assets in `assets/[category] pictures/`
2. Add audio files in `assets/[category] audio/`
3. Create text definitions in `assets/[category] text/`
4. Update `ChooseVocab.java` to include the new category

### Modifying UI Themes
- Colors are defined as RGB values in each class
- Fonts can be replaced in the `assets/fonts/` directory
- UI dimensions and spacing can be adjusted in the respective classes

## 🔍 Architecture Overview

### Design Patterns
- **Singleton Pattern**: AudioPlayer manages single audio instance
- **Observer Pattern**: Mouse listeners for UI interactions
- **MVC-like Structure**: Separation of UI, data, and logic

### Key Components
- **AudioPlayer**: Centralized audio management
- **RoundedPanel**: Custom UI component for modern aesthetics
- **Font Management**: Dynamic font loading and registration
- **File I/O Operations**: Persistent data storage

## 🐛 Known Issues & Limitations

1. **Audio Format Support**: Currently supports WAV and MP3 files
2. **Single Vocabulary Set**: Only school vocabulary is fully implemented
3. **File Path Dependencies**: Requires specific asset folder structure
4. **No User Profiles**: Basic name-based tracking without advanced user management

## 🚧 Future Enhancements

### Planned Features
- [ ] Complete implementation of all 8 vocabulary categories
- [ ] User profile system with progress tracking
- [ ] Difficulty levels and adaptive learning
- [ ] Spaced repetition algorithm
- [ ] Export/import vocabulary sets
- [ ] Mobile-friendly responsive design
- [ ] Multiplayer quiz competitions

### Technical Improvements
- [ ] Database integration for better data management
- [ ] Configuration file for easy customization
- [ ] Plugin system for vocabulary expansion
- [ ] Better error handling and logging
- [ ] Unit tests and automated testing
- [ ] Internationalization support

## 📄 License

This project is developed as an educational tool. Please ensure you have appropriate rights for any audio or image assets used.

## 🤝 Contributing

To contribute to this project:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For issues, questions, or suggestions, please refer to the project documentation or create an issue in the project repository.

---

**Note**: This application is designed for educational purposes and provides an engaging way to learn English vocabulary through visual, auditory, and interactive methods. 