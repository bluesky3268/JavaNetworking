package ch19.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class ParseJsonEx {

    public static void main(String[] args) throws IOException {
        // 파일로부터 Json얻기
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/hbcho/Desktop/json.txt", Charset.forName("UTF-8")));

        String json = br.readLine();
        br.close();

        // json 파싱
        JSONObject parseJson = new JSONObject(json);

        // json 속성 읽기
        System.out.println("id : " + parseJson.getString("id"));
        System.out.println("name : " + parseJson.getString("name"));
        System.out.println("age : " + parseJson.getInt("age"));
        System.out.println("student : " + parseJson.getBoolean("student"));
        System.out.println("tel : " + parseJson.getJSONObject("tel").get("mobile"));


         // 배열 속성 정보 얻기
        JSONArray arr = parseJson.getJSONArray("skills");
        System.out.println("skills : ");

        for (int i = 0; i < arr.length(); i++) {
            System.out.print(arr.get(i) + ";");
        }

    }
}
