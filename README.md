# Java Bug Squash
1. Clone this project
1. Execute the `Main` class in your preferred development environment
1. Find the root cause of and fix the IllegalStateException that is raised while reading the input file

## Importing into selected development environments
### Eclipse

You must have the [Eclipse IDE for Java developers](https://www.eclipse.org/downloads/packages/release/2020-12/r/eclipse-ide-java-developers) (or some variant with similar functionality) installed.

1. From the main menu select _File_ | _Import..._
1. Select _Existing Maven Projects_
1. Select _Browse_ 
1. Navigate to the folder in which you cloned this project and select it

### VSCode

You must have the VSCode Java development [extensions pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed.

1. From the main menu select _File_ | _Open Folder_
1. Navigate to the folder in which you cloned this project and select it
1. Eventually the Java Projects pane in the Explorer view should show a `midi` project

### Command Line

You must have [Maven 3.x](https://maven.apache.org/download.cgi) installed and the `mvn` executable on your path

1. Switch to the directory where you cloned the project
1. Execute the command `mvn exec:java -Dexec.mainClass=Main`

---

# Android MIDI Library

This project is mainly for use with Android applications that do not have access to Java's javax.sound.midi library. However, it is a stand-alone Java library with no Android-specific dependencies or considerations.

This code provides an interface to read, manipulate, and write MIDI files. "Playback" is supported as a real-time event dispatch system. This library does NOT include actual audio playback or device interfacing.
