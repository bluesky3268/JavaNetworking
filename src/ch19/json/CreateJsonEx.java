package ch19.json;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class CreateJsonEx {
    /**
     * JSONObject를 사용하기 위해서는 jar파일을 라이브러리에 추가해줘야 한다.
     * https://github.com/stleary/JSON-java
     */
    public static void main(String[] args) throws IOException {
        // JSON 객체 생성
        JSONObject root = new JSONObject();

        // 속성 추가
        root.put("id", "bluesky3268");
        root.put("name", "hyunbin");
        root.put("age", 33);
        root.put("language", "Korean");
        root.put("student", false);

        // JSON 객체에 객체속성 추가하기
        JSONObject tel = new JSONObject();
        tel.put("mobile", "010-1234-5678");
        tel.put("home", "");

        root.put("tel", tel);


        // 배열 추가
        JSONArray skills = new JSONArray();
        skills.put("java");
        skills.put("spring");
        skills.put("jpa");
        skills.put("mybatis");
        skills.put("oracle");
        skills.put("mysql");

        root.put("skills", skills);

        // JSON 얻기
        String json = root.toString();

        // 콘솔 출력
        System.out.println(json);


        // 파일로 저장하기
        Writer writer = new FileWriter("C:/Users/hbcho/Desktop/json.txt", Charset.forName("UTF-8"));
        writer.write(json);
        writer.flush();
        writer.close();

    }
}
