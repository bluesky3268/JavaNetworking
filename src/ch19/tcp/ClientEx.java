package ch19.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientEx {

    public static void main(String[] args) {
        try{
            // 소켓 생성 및 연결 요청
            Socket socket = new Socket("localhost", 7777);

            System.out.println("[연결 성공]");
            
            // socket 닫기
            socket.close();
            System.out.println("[연결 종료]");

        } catch (UnknownHostException e) {
            // IP 표기 방법이 잘못된 경우
            e.printStackTrace();
        } catch (IOException e) {
            // 해당 서버에 연결할 수 없는 경우
            e.printStackTrace();
        }
    }
}
