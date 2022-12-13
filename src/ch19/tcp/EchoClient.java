package ch19.tcp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 7777;
        try {
            Socket socket = new Socket(host, port);

            System.out.println("[연결 성공]");

            // 데이터 송신
            String sendMessage = "반갑습니다";
            /*
            OutputStream os = socket.getOutputStream();
            os.write(sendMessage.getBytes(StandardCharsets.UTF_8));
            os.flush();            
            */

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(sendMessage);
            dos.flush();

            System.out.println("[데이터 전송 : " + sendMessage + "]");
            
            // 데이터 수신
            /*
            InputStream is = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int readCount = is.read(bytes);            
            String receivedMessage = new String(bytes, 0, readCount, "UTF-8");
             */

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String receivedMessage = dis.readUTF();

            System.out.println("[수신 메시지 : " + receivedMessage + "]");
            
            
            // 연결 끊기 
            socket.close();
            System.out.println("[연결 종료]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
