package utilities;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    /**
     * Utility class providing helper methods for reading user input, validating data, and formatting strings.
     * This class simplifies repetitive tasks such as reading integers, dates, and strings, as well as validating email formats.
     */


    /**
     * Reads an integer from the user after displaying a prompt.
     * Ensures that the input is valid and repeats the prompt if the input is not a valid integer.
     *
     * @param question The prompt to display to the user.
     * @return A valid integer input by the user.
     */
    public static int readInteger(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        int num;
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                num = Integer.parseInt(line);
                return num;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");

            } catch (NumberFormatException nfe) {
                System.out.println(" ERROR: Must be a whole number.");
            }
        }
    }

    /**
     * Reads a string from the user after displaying a prompt.
     * Ensures that the input is valid and repeats the prompt if an error occurs during reading.
     *
     * @param question The prompt to display to the user.
     * @return A valid string input by the user.
     */
    public static String readString(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                return line;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");
            }
        }
    }

    /**
     * Validates an email string to ensure it follows a proper email format.
     *
     * @param email The email string to validate.
     * @return {@code true} if the email format is valid; {@code false} otherwise.
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher mather = pattern.matcher(email);
        if (mather.find()) {
            return true;
        } else {
            System.out.println("-> Please follow the email format: example@example.com");
            System.out.println("----------------------------------------------");
            return false;
        }
    }
}
