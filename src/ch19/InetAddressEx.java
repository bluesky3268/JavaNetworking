package ch19;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressEx {

    public static void main(String[] args) {
        try {
            InetAddress local = InetAddress.getLocalHost();
            System.out.println("host name : " + local.getHostName());
            System.out.println("host ip: " + local.getHostAddress());

            InetAddress[] iaArr = InetAddress.getAllByName("www.naver.com");
            for (InetAddress inetAddress : iaArr) {
                System.out.println("www.naver.com IP : " + inetAddress.getHostAddress());
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
