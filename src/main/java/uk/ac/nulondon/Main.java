package uk.ac.nulondon;

import java.io.IOException;
import java.util.Scanner;

/*APPLICATION CONTROLLER LAYER*/
public final class Main {
    private final ImageEditor editor = new ImageEditor();

    /**
     * Print the UI menu options to the user
     */
    private static void printMenu() {
        System.out.println("Please enter a command");
        System.out.println("g - Remove the greenest column");
        System.out.println("r[number] - Remove a specified column");
        System.out.println("u - Undo previous edit");
        System.out.println("q - Quit");
    }


    private void undo(Scanner scan) throws IOException {
        if (editor.canUndo()) {
            boolean success = editor.undo();
            if (success) {
                System.out.println("Undo operation successful");
                System.out.printf("The image now contains %d columns%n", editor.getWidth());
            } else {
                System.out.println("Undo operation failed");
            }
        } else {
            System.out.println("Nothing to undo");
        }
    }

    private void removeSpecific(Scanner scan, String choice) throws IOException {
        try {
            // Extract the column index from the command (e.g., "r5" -> 5)
            String indexStr = choice.substring(1);
            int columnIndex = Integer.parseInt(indexStr);
            
            boolean success = editor.removeColumn(columnIndex);
            if (success) {
                System.out.printf("Successfully removed column %d%n", columnIndex);
                System.out.printf("The image now contains %d columns%n", editor.getWidth());
            } else {
                System.out.printf("Failed to remove column %d (index out of bounds)%n", columnIndex);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid column number format. Please use r[number] (e.g., r5)");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid column index: " + e.getMessage());
        }
    }

    private void removeGreenest(Scanner scan) throws IOException {
        int greenestIndex = editor.removeGreenestColumn();
        if (greenestIndex >= 0) {
            System.out.printf("Successfully removed the greenest column (index: %d)%n", greenestIndex);
            System.out.printf("The image now contains %d columns%n", editor.getWidth());
        } else {
            System.out.println("Failed to remove the greenest column");
        }
    }

    private void run() throws IOException {
        //Scanner is closeable, so we put it into try-with-resources
        try (Scanner scan = new Scanner(System.in)) {
            // src/main/resources/beach.png
            System.out.println("Welcome! Enter file path");
            String filePath = scan.next();
            // import the file
            editor.load(filePath);
            System.out.printf("The image contains %d columns%n", editor.getWidth());
            String choice = "";
            while (!"q".equalsIgnoreCase(choice)) {
                // display the menu after every edit
                printMenu();
                // get and handle user input
                choice = scan.next();
                String command = choice.isEmpty() ? "" : choice.substring(0, 1);
                switch (command.toLowerCase()) {
                    //Extract all the actions into methods besides the trivial ones
                    case "g" -> removeGreenest(scan);
                    case "r" -> removeSpecific(scan, choice);
                    case "u" -> undo(scan);
                    case "q" -> System.out.println("Thanks for playing.");
                    default -> System.out.println("That is not a valid option.");
                }
            }
            // After the user exits, export the final image
            editor.save("target/newImg.png");
        }
    }


    public static void main(String[] args) {
        /*Keep main method short. Only create a main class and execute
        the entry point. Also, you may handle specific exceptions here*/
        try {
            new Main().run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
