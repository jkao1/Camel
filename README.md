# Squirrel

Squirrel is a software program designed by Jason Kao and William Soe. It is capable of creating and editing spreadsheets, saved with a .txt extension. General uses of Squirrel include cell-based calculation, random number generation, and various graphing tools.

##Working Features

- menu bar to access certain features
- with custom FileManager class, Squirrel is able to create new files, save current files, and open old files (with .txt extension)
- able to create histograms, bar charts, scatter plots, and line graphs
- histograms are also displayed in tabular format (includes bin, count, cumulative count, percent, and cumulative percent).
- random number generation through a uniform distribution and a normal distribution.
- basic math functions entered with "=<functionName>(<parameters>)". The functions are sum and mean, and are suggested to the user via a dropdown for each textfield.
- spreadsheet is in a JScrollPane, allowing infinite rows/columns.
- minor UI features: selecting a row or column labels highlights the entire row or column, UI is customized based on OS, 
##Unresolved Bugs

##How to Run
```
javac Squirrel.java
java Squirrel
```
##How to Use Squirrel

Squirrel's basic functionality works as any other spreadsheet editor: click to choose a cell, and then enter in a value. Cells can take in any values, even characters, but only numbers will run the graphs.

To enter the ranges for the graphs, you must use the notation [startCol][startRow]:[endCol][endRow]. This tells the program where to start reading the data from and where to end.
