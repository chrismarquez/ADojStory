# ADojStory
A compiler from Dog Lang to Javascript


## Dependencies: 
The project is written for the JVM, so to be able to run, you must have Java installed on the computer; the project is compatible from Java 8 onwards. As an additional dependency, in order to compile you must have Gradle installed, as well as JDK 8 onwards

## How to use:
Once the source is downloaded, to compile the application, the command must be executed:

```gradle jar```

being inside the folder of the application. This will generate the JAR file build / libs / ADojStory-1.0.jar. To execute the application, execute the command:

```java -jar ADojStory-1.0.jar```

When executing the command, the application will start normally. The program will open a window with the option to select a file with the `.dog` extension, either by dragging it or by clicking on the "Target file" button. Once selected, the code conversion will be displayed in the same window.
