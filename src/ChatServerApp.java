import java.io.IOException;

public class ChatServerApp {

    public static void main(String[] args) throws IOException {
        if (args.length == 0){
            return;
        }
        int port = Integer.parseInt(args[0]);
//        System.out.println("Tu jest");
        ChatServer chat = new ChatServer(port);
        chat.start();
    }


}
