import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.dobiasjakub.ToDo;


public class Main {

    public static void main(String[] args)  {

        ToDo toDo = new ToDo();

        String path = "com/dobiasjakub/database.txt";

        List<String> tasks = toDo.readFile(path);


        String head ="\nCommand Line Todo application\n"
                     + "=============================\n";

        String body =
                "\n" +
                "Command line arguments:\n" +
                "    -l   Lists all uncheck tasks\n" +
                "    -la  Lists all the tasks\n" +
                "    -a   Adds a new task\n" +
                "    -r   Removes an task\n" +
                "    -c   Completes an task";

        try {
            switch (args[0]) {
                case "-l" -> toDo.getUncheck(tasks);
                case "-la" -> toDo.getList(tasks);
                case "-a" -> {
                    try {
                        toDo.addTask(tasks, args[1]);
                        toDo.appendFile(tasks, path);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Unable to add: no task provided");
                    }
                }
                case "-r" -> {
                    try {
                        toDo.removeTask(tasks, args[1]);
                        toDo.writeFile(tasks, path);
                    } catch (NumberFormatException e) {
                        System.err.println("Unable to remove: index is not a number");
                    } catch (ArrayIndexOutOfBoundsException d) {
                        System.err.println("Unable to remove: no index provided");
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Unable to remove: index is out of bound");
                    }
                }
                case "-c" -> {
                    try {
                        toDo.checkTask(tasks, args[1]);
                        Collections.sort(tasks);
                        toDo.writeFile(tasks, path);
                    } catch (ArrayIndexOutOfBoundsException d) {
                        System.err.println("Unable to check: no index provided");
                    } catch (IndexOutOfBoundsException p) {
                        System.err.println("Unable to check: index is out of bound");
                    } catch (NumberFormatException e) {
                        System.err.println("Unable to check: index is not a number");
                    }
                }
                default -> {System.out.println("Unsupported argument");}
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(head);
            System.out.println(body);
        } catch (UnsupportedCharsetException e) {
            System.err.println("Unsupported argument");
        }
    }
}


