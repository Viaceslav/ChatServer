import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

//import static java.awt.DefaultKeyboardFocusManager.sendMessage;

public class UserHandler extends Thread {

    private Socket socket;
    private int userId;
    private String userName;
    private BufferedReader inputBufferedReader;
    private PrintWriter outputPrintWriter;
    private static Map<Integer, PrintWriter> clients = new ConcurrentHashMap<>();


    public UserHandler(Socket socket) throws IOException {
        this.socket = socket;
        registerUser();
    }


    private void registerUser() throws IOException {
        inputBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);

        userId = new Random().nextInt(Integer.MAX_VALUE);
        userName = inputBufferedReader.readLine();
        clients.putIfAbsent(userId, outputPrintWriter);
        System.out.println("Name: " + userName);
        System.out.println("UID: " + userId);
    }


    @Override
    public void run() {

        while (true) {
            try {
                inputBufferedReader.lines()
                        .forEach(this::read);
            } finally {
                clients.remove(userId);
                try {
                    socket.close();
                    inputBufferedReader.close();
                    System.out.println("User removed. ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void read(String userMessage) {
        if (userMessage == null) {
            return;
        }

        if (!userMessage.isEmpty()) {
            clients.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != userId)
                    .forEach(entry -> sendMessage(entry.getValue(), userMessage));
        }
    }

    private void sendMessage(PrintWriter output, String userMessage) {
        final char SEP = (char) 31;
        String serverMsg = "MSG" + userName + SEP + userMessage;
        System.out.println(serverMsg);
        output.println(serverMsg);

    }
}
