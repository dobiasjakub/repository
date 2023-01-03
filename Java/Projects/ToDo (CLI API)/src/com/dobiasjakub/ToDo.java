package com.dobiasjakub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ToDo {

    public void getIntro(List<String> list) {
        if (list.size() == 0) {
            System.out.println("No todos for today! :)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                String line = String.valueOf(list.get(i));
                if (line.contains("[ ]")) {
                    System.out.println(i + 1 + " -" + line.substring(3));
                }
            }
        }
    }

    public void addTask(List<String> list, String task) {
        list.add("[ ] " + task);
        System.out.println("Added");
    }

    public void removeTask(List<String> list, String index) {
        int position = Integer.parseInt(index);
        list.remove(position - 1);
        System.out.println("Task "+ position +" removed");
    }

    public void getList(List<String> list) {
        if (list.size() == 0) {
            System.out.println("No todos for today! :)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                String line = String.valueOf(list.get(i));
                System.out.println(i + 1 + " - " + line);
            }
        }
    }

    public void getUncheck(List<String> list) {
        if (list.size() == 0) {
            System.out.println("No todos for today! :)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                String line = String.valueOf(list.get(i));
                if (line.contains("[ ]")) {
                    System.out.println(i + 1 + " - " + line);
                }
            }
        }
    }

    public List<String> readFile(String file)  {
         Path filePath = Paths.get(file);
            try {
                return Files.readAllLines(filePath, UTF_8);
            } catch (IOException e) {
                System.err.println("No todos for today! :)");
                return new ArrayList<>();
            }
        }

    public void appendFile(List<String> fileContent, String file) {
        try {
            Files.write(Paths.get(file), fileContent);
        } catch (IOException e) {
            System.err.println("Unable read file.");
        }
    }

   public void writeFile(List<String> fileContent, String file) {
        try {
            Files.write(Paths.get(file), fileContent);
        } catch (IOException e) {
            System.err.println("Unable read file.");
        }
    }

   public void checkTask (List<String> list, String check) {
        int checkIt = Integer.parseInt(check) - 1;
            String line = String.valueOf(list.get(checkIt));
            char[] word = line.toCharArray();
            if (word[1] == 'x') {
                word[1] = ' ';
                System.out.println("Unchecked");
            } else {
                word[1] = 'x';
            System.out.println("Checked");
            }
            line = String.valueOf(word);
            list.remove(checkIt);
            list.add(checkIt,line);
       }
   }






