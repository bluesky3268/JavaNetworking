package ch19.chat;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    /**
     * 클라이언트와 1:1로 통신하는 역할을 한다.
     */

    protected ChatServer chatServer;
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected String clientIp;
    protected String chatName;

    public SocketClient(ChatServer chatServer, Socket socket) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());

            InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();

            this.clientIp = isa.getHostName();
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JSON 받기
    public void receive() {
        chatServer.threadPool.execute(() -> {
            try{
                while (true) {
                    String receivedJson = dis.readUTF();

                    JSONObject jsonObject = new JSONObject(receivedJson);
                    String command = jsonObject.getString("command");

                    switch (command) {
                        case "incoming" :
                            this.chatName = jsonObject.getString("data");
                            chatServer.sendToAll(this, "입장했습니다.");
                            chatServer.addSocketClient(this);
                            break;
                        case "message" :
                            String message = jsonObject.getString("data");
                            chatServer.sendToAll(this, message);
                            break;
                    }
                }
            } catch (IOException e) {
                chatServer.sendToAll(this, "퇴장했습니다.");
                chatServer.removeSocketClient(this);
            }
        });
    }

    public void send(String json) {
        try{
            dos.writeUTF(json);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
