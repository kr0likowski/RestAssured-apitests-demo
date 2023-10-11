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
import utils.JsonUtils;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

public class Products {

    public static void getAllProducts(String token){

        // GET all products request
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .get("api/v1/products");

                response.then()
                        .statusCode(200);


    }
    public static ProductTO getSingleProduct(int id, String token){

        // GET product by id request
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .get("api/v1/products/"+id);

        // Assertions
        response.then()
                .body("id", not(emptyOrNullString()))
                .statusCode(200);

        // Parse response from request to JsonObject
        String responseBody = response.getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        // Convert JsonArray from request to String array
        JsonArray imagesArray = jsonObject.getAsJsonArray("images");
        String[] images = JsonUtils.createStringArrayFromJsonArray(imagesArray);

        // Get categoryId
        int categoryId = jsonObject.getAsJsonObject("category").get("id").getAsInt();

        // Create product transfer object from request body and return it
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

        // Parse string array to json array
        JsonArray imgUrlsArray = JsonUtils.createJsonArrayFromStringArray(productTO.getImgUrl());

        // Request parameters
        JsonObject productJSON = new JsonObject();
        productJSON.addProperty("title",productTO.title);
        productJSON.addProperty("price",productTO.price);
        productJSON.addProperty("description",productTO.description);
        productJSON.addProperty("categoryId",productTO.categoryId);
        productJSON.add("images", imgUrlsArray);

        // POST project request
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(productJSON.toString())
                    .log().all()
                .when()
                    .post("api/v1/products/");

        // Assertions
        response
                .then()
                .statusCode(201)
                .body("id", not(emptyOrNullString()));

        // Parse response to JsonObject
        String responseBody = response.getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        // Return id of created object
        return jsonObject.get("id").getAsInt();
    }

    public static void updateProductTitle(int id, String title, String token){

        // Request properties
        JsonObject updateProductJSON = new JsonObject();
        updateProductJSON.addProperty("title", title);

        // Update product title request (PUT project)
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(updateProductJSON.toString())
                    .log().all()
                .when()
                .put("api/v1/products/"+id);

        // Assertions
        response
                .then()
                .statusCode(200);

    }

    public static void deleteProduct(int id, String token){

        // DELETE product request
        Response response = RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                .delete("api/v1/products/"+id);

        // Assertions
        response
                .then()
                .statusCode(200);

    }

    public static void getNonExistingProduct(int id, String token){

        // GET product by id request
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .get("api/v1/products/"+id);

        // Assertions
        response.then()
                // Assert there will be error as expected
                .statusCode(400);

        String responseBody = response.getBody().asString();

    }

}
