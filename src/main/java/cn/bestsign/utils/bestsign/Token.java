package cn.bestsign.utils.bestsign;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

/**
 * Created by dai on 2020/1/20.
 */
public class Token {

    private  String token;

    public  String getToken(String host, String clientId, String clientSecret) {

        if (token == null) {
            System.err.println("create token!!!");
            String path = "/api/oa2/client-credentials/token";

            HashMap<String, Object> body = new HashMap<>();
            body.put("clientId", clientId);
            body.put("clientSecret", clientSecret);

//            RestAssured.proxy("127.0.0.1",8888);
            useRelaxedHTTPSValidation();
            Response response = given()
                    .header("Content-type", "application/json; charset=UTF-8")
                    .body(body)
                    .when()
//                    .log().all()
                    .post(host + path)
                    .then()
//                    .log().all()
                    .extract().response();

//            String tmp = response.asString();// body结果所有的数据
            //{"code":"0","message":"请求成功","data":{"tokenType":"bearer","accessToken":"2S6Z6dpHxJbwRwj5MUXMV5UG0Y8jx15S","expiresIn":"86400"}}
            String tokenType = response.path("data.tokenType");
            String accessToken = response.path("data.accessToken");

            token = tokenType + " " + accessToken;
        }

        return token;
    }


    private Token() {}

    private static class GetTokenHolder {

        private static final Token instance = new Token();
    }

    public static Token getTokenClass() {

        return Token.GetTokenHolder.instance;
    }


//    public static void main(String[] args) {
//
//        String host = "https://api.bestsign.info";
//        String clientId="QNIrZvP5MCVtMF4PjacrXbUShJ3qyVhv";
//        String clientSecret = "OCcfKKb4AibUpodRY5Qr66vdFy2uxxum";
//
//        System.out.println(Token.getTokenClass().getToken(host, clientId, clientSecret));
//
//
//    }


}


