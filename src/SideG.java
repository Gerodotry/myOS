import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServerG {
    private static Map<Integer, Integer> memoizationCache = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(54321);
            System.out.println("Server G is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to Server G.");

                // Використовуємо CompletableFuture для асинхронної обробки запиту
                CompletableFuture.runAsync(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        int x = (int) in.readObject();

                        int resultG;

                        if (memoizationCache.containsKey(x)) {
                            resultG = memoizationCache.get(x);
                        } else {
                            // Обчислення g(x) і зберігання результату в кеші
                            resultG = g(x);
                            memoizationCache.put(x, resultG);
                        }

                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        out.writeObject(resultG);

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

    // Приклад функції g(x) (ваша реалізація може відрізнятися)
    private static int g(int x) {
        return x * 3;
    }
}
