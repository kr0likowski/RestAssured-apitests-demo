package methods.products;
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

public class Products {

    public static void getAllProducts(){
        Response response = RestAssured
                .given()
                    .get("api/v1/products");

                response.then().statusCode(200);


    }
    public static void getSingleProduct(String id){
        Response response = RestAssured
                .given()
                    .get("api/v1/products/"+id);

        response.then().statusCode(200);

    }

    public static String createProduct(ProductTO productTO){

        JsonObject productJSON = new JsonObject();

        productJSON.addProperty("title",productTO.title);
        productJSON.addProperty("price",productTO.price);
        productJSON.addProperty("description",productTO.description);
        productJSON.addProperty("categoryId",productTO.categoryId);
        productJSON.addProperty("images",productTO.imgUrl);

        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(productJSON.toString())
                    .log().all()
                .when()
                    .post("api/v1/products/");

        response
                .then()
                .statusCode(201)
                .body("id", not(emptyOrNullString()));

        String responseBody = response.getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        return jsonObject.get("id").getAsString();
    }

    public static void updateProductTitle(String id, String title){

        JsonObject updateProductJSON = new JsonObject();

        updateProductJSON.addProperty("title", title);

        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(updateProductJSON.toString())
                    .log().all()
                .when()
                .put("api/v1/products/"+id);

        response
                .then()
                .statusCode(200);

    }

    public static void deleteProduct(String id){

        Response response = RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                .delete("api/v1/products/"+id);

        response
                .then()
                .statusCode(200);

    }

}
