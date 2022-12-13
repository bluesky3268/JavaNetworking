package ch19.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerEx {
    /**
     * TCP 서버 프로그램을 개발하기 위해서는 ServerSocket과 Socket을 생성해야 한다.
     * ServerSocket : 클라이언트의 연결 요청을 수락(accept())할 때 사용됨
     * Socket : 클라이언트에서 연결 요청할 때와 클라이언트와 데이터를 주고 받을 때 사용됨
     */

    private static ServerSocket serverSocket = null;

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("서버를 종료하려면 q 또는 Q를 입력 후 Enter를 누르세요.");
        System.out.println("====================================================");

        // 서버 시작
        startServer();

        // 키보드 입력
        Scanner sc = new Scanner(System.in);
        while (true) {
            String key = sc.nextLine();
            if (key.toLowerCase().equals("q")) {
                break;
            }
        }

        sc.close();

        stopServer();
    }
    
    public static void startServer(){
        // 작업 스레드 정의
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // ServerSocket생성 및 Port바인딩
                    serverSocket = new ServerSocket(7777);

                    System.out.println("[서버 시작]");

                    while (true) {
                        System.out.println("연결 요청 대기 중...");

                        // 연결 수락
                        Socket socket = serverSocket.accept();

                        // 연결된 클라이언트 정보
                        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                        System.out.println("[연결 요청 수락 :" + isa.getHostName() + " ] ");

                        // 연결 종료
                        socket.close();
                        System.out.println("[연결 요청 종료 :" + isa.getHostName() + " ] ");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void stopServer() {
        try{
            // ServerSocket닫기
            serverSocket.close();
            System.out.println("[서버 종료]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
