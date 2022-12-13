package ch19.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class NewsClient {

    public static void main(String[] args) {

        try {
            String host = "localhost";
            int port = 7777;

            DatagramSocket datagramSocket = new DatagramSocket();

            // 구독하고 싶은 주제 보내기
            String data = "스포츠";

            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(host, port));

            datagramSocket.send(sendPacket);

            while (true) {
                // 뉴스 받기
                DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
                datagramSocket.receive(receivedPacket);

                // 받은 패킷 문자열로 변환
                String receivedNews = new String(receivedPacket.getData(), 0, receivedPacket.getLength(), "UTF-8");
                System.out.println(receivedNews);

                // 마지막 뉴스를 받으면 while문 종료
                if (receivedNews.contains("뉴스10")) {
                    break;
                }
            }

            // DatagramSocket 닫기
            datagramSocket.close();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
