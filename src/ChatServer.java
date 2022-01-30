import org.w3c.dom.UserDataHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer{

    private ConcurrentHashMap<Integer, PrintWriter> clients = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Serwer został uruchomiony");

        while (true){
            Socket socket = serverSocket.accept();
            final UserHandler userHandler = new UserHandler(socket);
            userHandler.start();
        }
    }
}
