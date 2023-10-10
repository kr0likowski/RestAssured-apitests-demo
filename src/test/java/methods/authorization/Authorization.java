package methods.authorization;/*
@Author: jkrolikowski
@Date: 10/10/2023
*/
import io.restassured.RestAssured;
public class Authorization {
    public static void authorize (AuthorizationTO authTO, String baseURL){
        RestAssured.get(baseURL+"api/v1/auth/login");
    }
}
