import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            while (true) {
                Socket serverF = new Socket("localhost", 12345); // Connect to Server F
                Socket serverG = new Socket("localhost", 54321); // Connect to Server G

                Scanner scanner = new Scanner(System.in);

                // Get user input for x
                System.out.print("Enter x (or 'exit' to quit): ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    break; // Exit the loop if the user inputs "exit"
                }

                try {
                    int x = Integer.parseInt(input);

                    // Send x to Server F
                    ObjectOutputStream outF = new ObjectOutputStream(serverF.getOutputStream());
                    outF.writeObject(x);

                    // Send x to Server G
                    ObjectOutputStream outG = new ObjectOutputStream(serverG.getOutputStream());
                    outG.writeObject(x);

                    // Receive results from servers
                    ObjectInputStream inF = new ObjectInputStream(serverF.getInputStream());
                    ObjectInputStream inG = new ObjectInputStream(serverG.getInputStream());

                    int resultF = (int) inF.readObject(); // Result of f(x)
                    int resultG = (int) inG.readObject(); // Result of g(x)

                    // Calculate gcd(f(x), g(x))
                    int gcd = gcd(resultF, resultG);

                    System.out.println("gcd(f(x), g(x)) = " + gcd);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid x format. Please enter an integer.");
                } catch (SocketException se) {
                    // Handle broken pipe (client disconnected)
                    System.out.println("Connection to the server was lost. Reconnecting...");
                    continue; // Reconnect to the server and continue with the next input
                }

                serverF.close();
                serverG.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to calculate GCD
    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
