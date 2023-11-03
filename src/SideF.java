import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServerF {
    private static Map<Integer, Integer> memoizationCache = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server F is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to Server F.");

                // Використовуємо CompletableFuture для асинхронної обробки запиту
                CompletableFuture.runAsync(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        int x = (int) in.readObject();

                        int resultF;

                        if (memoizationCache.containsKey(x)) {
                            resultF = memoizationCache.get(x);
                        } else {
                            // Обчислення f(x) і зберігання результату в кеші
                            resultF = f(x);
                            memoizationCache.put(x, resultF);
                        }

                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        out.writeObject(resultF);

                        clientSocket.close();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Приклад функції f(x) (ваша реалізація може відрізнятися)
    private static int f(int x) {
        return x + 2;
    }
}
