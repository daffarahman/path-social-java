# Path Social

A Java Swing desktop application that replicates the core functionality of the original Path social networking app. This project implements an intimate social sharing platform where users can share moments with a limited circle of close friends.

## About

Path Social is built as an educational project demonstrating object-oriented programming principles in Java. It recreates the essence of the original Path app, which was known for its privacy-focused approach to social networking, limiting users to 50 friends to encourage more meaningful and personal connections.

The application features a clean, modern user interface powered by FlatLaf, with support for sharing various types of moments including photos, thoughts, music, locations, and sleep status updates.

## Features

### User Management
- User registration with unique username validation
- Secure login authentication
- Personalized user profiles with avatar initials

### Friend System
- Maximum of 50 friends per user, staying true to the original Path philosophy
- User search functionality to find and add new friends
- Mutual friendship connections (adding a friend automatically creates a two-way relationship)
- Automatic friendship moment generation when two users become friends

### Moments
Path Social supports seven types of moments:

| Type | Description |
|------|-------------|
| Photo | Share images with optional captions |
| Thought | Post text-based status updates |
| Music | Share what you are currently listening to |
| Location | Check in to places and share where you are |
| Awake | Let friends know when you wake up |
| Asleep | Share when you are going to sleep |
| Friendship | Automatically generated when you connect with a new friend |

### Timeline
- Chronological feed showing moments from you and your friends
- Real-time updates with automatic refresh every 15 seconds
- Relative timestamps (e.g., "5m ago", "2h ago", "3d ago")
- Visual timeline design with connected moment cards

### Data Persistence
- Local JSON-based data storage in the user home directory
- File watching for external changes with automatic sync every 2 seconds
- Image storage for photo moments

## Project Structure

```
/path-social # repository root
├── src/
│   └── madebydap/
│       └── pathsocial/
│           ├── App.java                 # Application entry point
│           ├── model/                   # Data models
│           │   ├── User.java            # User entity
│           │   ├── Moment.java          # Moment/post entity
│           │   └── MomentType.java      # Enum for moment types
│           ├── data/                    # Data layer
│           │   ├── DataStore.java       # Singleton data manager
│           │   └── PersistenceManager.java  # JSON file handling
│           └── ui/                      # User interface
│               ├── MainFrame.java       # Main application window
│               ├── LoginPanel.java      # Login and registration
│               ├── TimelinePanel.java   # Moment timeline view
│               ├── ProfilePanel.java    # User profile view
│               ├── FriendsPanel.java    # Friend management
│               ├── AddMomentDialog.java # Create new moment dialog
│               ├── components/          # Reusable UI components
│               │   ├── BottomNavBar.java
│               │   ├── FloatingActionButton.java
│               │   ├── MomentCard.java
│               │   └── RoundedPanel.java
│               └── style/               # Styling utilities
│                   ├── PathColors.java  # Color constants
│                   ├── PathFonts.java   # Font definitions
│                   └── PathIcons.java   # Icon rendering
├── lib/
│   └── flatlaf-3.4.jar                  # FlatLaf Look and Feel library
├── docs/                                # Generated Javadoc
├── runner.bat                           # Windows build and run script
├── build-jar.bat                        # JAR packaging script
└── generate-javadoc.bat                 # Javadoc generation script
```

## Requirements

- Java Development Kit (JDK) 11 or higher
- Windows operating system (for the provided batch scripts)

## Installation and Running

### Using the Batch Script (Windows)

The simplest way to build and run the application is using the provided batch script:

```batch
runner.bat
```

This script will:
1. Clean any previously compiled class files
2. Compile all Java source files with FlatLaf dependency
3. Launch the application if compilation succeeds

### Building a JAR

To create a self-contained JAR file:

```batch
build-jar.bat
```

This will generate `build/PathSocial.jar` which can be run with:

```batch
java -jar build/PathSocial.jar
```

### Manual Compilation

If you prefer to compile manually or are on a different operating system:

```bash
# Create output directory
mkdir bin

# Compile all Java files
javac -d bin -cp "lib/*" -sourcepath src src/madebydap/pathsocial/*.java src/madebydap/pathsocial/model/*.java src/madebydap/pathsocial/data/*.java src/madebydap/pathsocial/ui/*.java src/madebydap/pathsocial/ui/components/*.java src/madebydap/pathsocial/ui/style/*.java

# Run the application
java -cp "bin;lib/*" madebydap.pathsocial.App
```

For Linux or macOS, replace the semicolon with a colon in the classpath:

```bash
java -cp "bin:lib/*" madebydap.pathsocial.App
```

## Usage

### Getting Started

1. Launch the application using one of the methods described above
2. Create a new account by clicking "Create an account" on the login screen
3. Fill in your username, password, and display name
4. Click "Create Account" to register and automatically log in

### Creating Moments

1. Click the floating action button (red circular button) at the bottom right
2. Select the type of moment you want to share from the popup menu
3. Enter your content (and optionally select an image for photo moments)
4. Click "Share" to post your moment to your timeline

### Managing Friends

1. Navigate to the Friends tab using the bottom navigation
2. Use the search bar to find other users by username or display name
3. Click the add button next to a user to send a friend request
4. Your timeline will now include moments from your new friend

### Viewing Your Profile

1. Navigate to the Profile tab using the bottom navigation
2. View your display name, username, and friend count
3. Access the logout option to sign out of your account

## Data Storage

Application data is stored in the following location:

```
~/.pathsocial/
├── data.json    # User and moment data
└── images/      # Uploaded photo files
```

The data file uses a simple JSON format and can be manually edited if needed, though this is not recommended during normal usage.

## Architecture

The application follows a layered architecture pattern:

### Model Layer
Contains plain Java objects representing the core domain entities (User, Moment, MomentType). These classes are immutable where possible and contain no business logic beyond basic validation.

### Data Layer
Implements the repository pattern with a singleton DataStore that manages all data operations. The PersistenceManager handles JSON serialization without external libraries, using regex-based parsing for simplicity.

### UI Layer
Built entirely with Java Swing, featuring a CardLayout-based navigation system. The UI uses FlatLaf for a modern look and feel, with custom components for buttons, cards, and panels.

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| FlatLaf | 3.4 | Modern Look and Feel for Swing |

FlatLaf is included in the `lib/` directory and requires no additional installation.

## Generating Documentation

To generate Javadoc documentation:

```batch
generate-javadoc.bat
```

The generated documentation will be available in the `docs/` directory. Open `docs/index.html` in a web browser to view.

## Author

[Daffa Rahman](https://github.com/daffarahman)

## License

This project was created for educational purposes as part of a programming assignment.
