# Simple CLI based Todo App

The goal of the project is to create a simple Todo app used by CLI commands.
Database is based in csv.file database.txt

## Features

- Adding todos
- Checking todos
- Delete todos
- List todos


## How to run it

- download app into your pc
- path for csv file is relative, so it should be saving and updating data as it is 
- git bash into project file
- compile Main.java (run javac Main.java)
- to run set commands, always start command with java Main.java {your command}


### Start screen 

Command Line Todo application
=============================


Command line arguments:
-    -l   Lists all uncheck tasks
-    -la  Lists all the tasks
-    -a   Adds a new task
-    -r   Removes an task
-    -c   Completes an task

#### Commands
- "{java Main.java} -l" will show all the unchecked tasks
- "{java Main.java} -la" will show all tasks
- "{java Main.java} -a 'Your task'" will save a task into csv
- "{java Main.java} -r {index of task}" will delete task on setted index
- "{java Main.java} -c {index of task}" will check the task

