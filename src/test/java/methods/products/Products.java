package methods.products;
/*
@Author: jkrolikowski
@Date: 10/10/2023
*/

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

public class Products {

    public static void getAllProducts(String token){
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .get("api/v1/products");

                response.then()
                        .statusCode(200);


    }
    public static ProductTO getSingleProduct(int id, String token){
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .get("api/v1/products/"+id);

        response.then()
                .body("id", not(emptyOrNullString()))
                .statusCode(200);

        String responseBody = response.getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        JsonArray imagesArray = jsonObject.getAsJsonArray("images");

        // Convert the JsonArray to a string array
        String[] images = new String[imagesArray.size()];
        for (int i = 0; i < imagesArray.size(); i++) {
            images[i] = imagesArray.get(i).getAsString();
        }

        // Get categoryId
        int categoryId = jsonObject.getAsJsonObject("category").get("id").getAsInt();

       ProductTO productTO = ProductTO.builder()
               .title(jsonObject.get("title").getAsString())
               .description(jsonObject.get("description").getAsString())
               .categoryId(categoryId)
               .price(jsonObject.get("price").getAsString())
               .id(jsonObject.get("id").getAsInt())
               .imgUrl(images)
               .build();

        return productTO;
    }

    public static int createProduct(ProductTO productTO, String token){

        JsonObject productJSON = new JsonObject();

        // Create a JsonArray to represent the array of strings for images
        JsonArray imgUrlsArray = new JsonArray();
        for (String imgUrl : productTO.imgUrl) {
            imgUrlsArray.add(imgUrl);
        }

        productJSON.addProperty("title",productTO.title);
        productJSON.addProperty("price",productTO.price);
        productJSON.addProperty("description",productTO.description);
        productJSON.addProperty("categoryId",productTO.categoryId);
        productJSON.add("images", imgUrlsArray);

        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
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

        return jsonObject.get("id").getAsInt();
    }

    public static void updateProductTitle(int id, String title, String token){

        JsonObject updateProductJSON = new JsonObject();

        updateProductJSON.addProperty("title", title);

        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(updateProductJSON.toString())
                    .log().all()
                .when()
                .put("api/v1/products/"+id);

        response
                .then()
                .statusCode(200);

    }

    public static void deleteProduct(int id, String token){

        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                .delete("api/v1/products/"+id);

        response
                .then()
                .statusCode(200);

    }

}
