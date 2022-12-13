package ch19.multiThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    /**
     * tcp와 udp 패키지의 작업은
     * 먼저 연결한 클라이언트의 요청을 처리 한 후 다음 클라이언트의 요청을 처리한다.
     * 이 경우, 먼저 연결한 클라이언트의 요청이 길어지면 다음 클라이언트의 작업도 지연된다.
     * 따라서 accept() 와 receive()를 제외한 요청 처리코드는 별도 쓰레드에서 작업하는 것이 좋다.
     * 또한 요청 폭증에 따른 과도한 쓰레드 생성을 막기 위해 쓰레드 풀을 사용해야 한다.
     */

    private static int port = 7777;
    private static ServerSocket serverSocket = null;
    private static ExecutorService executorService = Executors.newFixedThreadPool(10); // 쓰레드 풀의 쓰레드 개수 10개로 설정
    
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

                    // 작업 큐에 처리 작업 넣기
                    executorService.execute(() -> {
                        try {
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
                        } catch (IOException e) {

                        }

                    });

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
            executorService.shutdown(); // 쓰레드풀 종료
            System.out.println("[서버 종료]");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
