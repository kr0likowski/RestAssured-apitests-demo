package methods.authorization;
/*
@Author: jkrolikowski
@Date: 10/10/2023
*/

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class Authorization {
    public static String authorize (AuthorizationTO authTO){

        // Request parameters
        JsonObject authorizeJSON = new JsonObject();
        authorizeJSON.addProperty("email",authTO.email);
        authorizeJSON.addProperty("password",authTO.password);

        // POST generate token
        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(authorizeJSON.toString())
                    .log().all()
                .when()
                    .post("api/v1/auth/login");

        // Assertions
        response.then()
                .statusCode(201)
                .body("access_token", not(emptyOrNullString()))
                .body("refresh_token", not(emptyOrNullString()));

        // Parse response body
        String responseBody = response.getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        // Return access token
        return jsonObject.get("access_token").getAsString();

    }
}
