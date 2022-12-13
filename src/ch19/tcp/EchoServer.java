package ch19.tcp;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoServer {

    private static ServerSocket serverSocket = null;
    private static int port = 7777;

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("서버를 종료하려면 q 또는 Q를 입력 후 Enter를 누르세요.");
        System.out.println("====================================================");

        // tcp 서버 시작
        startServer(port);

        // 키보드 입력
        Scanner sc = new Scanner(System.in);
        while (true) {
            String key = sc.nextLine();
            if (key.toLowerCase().equals("q")) {
                break;
            }
        }

        sc.close();
        
        // tcp 서버 종료
        stopServer();
    }

    public static void startServer(int port) {
        // 작업 스레드 정의
        Thread thread = new Thread(() -> {
            try {
                // ServerSocket생성 및 Port바인딩
                serverSocket = new ServerSocket(port);

                System.out.println("[서버 시작]");

                while (true) {
                    System.out.println("연결 요청 대기 중...");

                    // 연결 수락
                    Socket socket = serverSocket.accept();

                    // 연결된 클라이언트 정보
                    InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                    System.out.println("[연결 요청 수락 :" + isa.getHostName() + " ] ");

                    // 데이터 수신
                    /*
                    InputStream is = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int readCount = is.read(bytes);
                    String message = new String(bytes, 0, readCount, "UTF-8");
                     */
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String message = dis.readUTF();
                    System.out.println("[수신 데이터 : " + message + "]");

                    // 데이터 송신
                    /*
                    OutputStream os = socket.getOutputStream();
                    bytes = message.getBytes("UTF-8");
                    os.write(bytes);
                    os.flush();
                     */
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(message);
                    dos.flush();
                    System.out.println("[수신 데이터 재송신 : " + message + "]");

                    // 연결 종료
                    socket.close();
                    System.out.println("[연결 요청 종료 :" + isa.getHostName() + " ] ");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        thread.start();
    }

    public static void stopServer() {
        try{
            serverSocket.close();
            System.out.println("[서버 종료]");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
