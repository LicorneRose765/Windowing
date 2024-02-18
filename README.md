# Windowing
Project for our Data Structure course at the University of Mons.

## About
Application to apply the windowing technique to a set of vertical or horizontal segments  using the Priority Search Tree data structure.
The main goal of this project is to implement the Priority Search Tree data structure described in the [[Book]](./pdf/reference.pdf) from M. De Berg et al.

The app is written in Java and uses the JavaFX library for the GUI. Moreover, all the code and comments are written in English.

For more details, you can find the report (written in French) of this project [[here]](./pdf/SDD___Windowing.pdf). 


## How to run
To run this project, you will need : 
- Java 11 or higher
- Gradle

To run the project, you can use the following command in the root directory of the project:
```bash
gradle build
gradle run
```

## Usage
This application uses .seg or .txt files to load segments. The format of the file is as follows:
- The first line contains 4 real numbers X X' Y Y', the coordinates of the window [X : X'] X [Y : Y'] containing the segments.
- Each line following the first one contains also 4 real numbers X1 X2 Y1 Y2, the coordinates of the segment (X1, Y1), (X2, Y2).

Some examples of files are available in the `app/src/main/resources/segments` directory. These examples can be loaded directly in the application.
You can also create your own files and load them in the app.

**Remark**: The segments must be horizontal or vertical, other segments are not supported.