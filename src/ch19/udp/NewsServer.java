package ch19.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NewsServer {
    /**
     * UDP는 발신자가 일방적으로 수신자에게 데이터를 보내는 방식
     * TCP처럼 연결 수락 과정이 없기 때문에 데이터 전송 속도가 상대적으로 빠르다.
     */

    private static DatagramSocket datagramSocket = null;
    private static int port = 7777;

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

                        String newsKind = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
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
        System.out.println("[서버 종료]");
    }

}
