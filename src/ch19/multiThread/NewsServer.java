package ch19.multiThread;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsServer {
    private static int port = 7777;
    private static DatagramSocket datagramSocket = null;
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("서버를 종료하려면 q 또는 Q를 입력 후 Enter를 누르세요.");
        System.out.println("====================================================");

        // UDP 서버 시작
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

        // UDP 서버 종료
        stopServer();
    }

    public static void startServer(int port) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    datagramSocket = new DatagramSocket(port);
                    System.out.println("[서버 시작]");

                    while (true) {
                        // 클라이언트가 구독하고 싶은 뉴스 주제 얻기
                        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                        datagramSocket.receive(receivePacket);

                        // 작업큐에 처리 작업 넣기
                        executorService.execute(() -> {
                            String newsKind = null;
                            try {
                                newsKind = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                                System.out.println("newsKind : " + newsKind);

                                // 클라이언트의 IP, PORT얻기
                                SocketAddress socketAddress = receivePacket.getSocketAddress();

                                // 뉴스 10개 클라이언트로 전송
                                for (int i = 0; i < 20; i++) {
                                    String data = newsKind + " : 뉴스" + (i+1);
                                    byte[] bytes = data.getBytes("UTF-8");
                                    DatagramPacket sendPacket = new DatagramPacket(bytes, 0, bytes.length, socketAddress);
                                    datagramSocket.send(sendPacket);
                                    System.out.println("[전송 완료 : " + socketAddress + "]");
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });


                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        thread.start();
    }

    private static void stopServer() {
        datagramSocket.close();
        executorService.shutdown();
        System.out.println("[서버 종료]");
    }
}
