package tests.producttests;

import io.restassured.RestAssured;
import methods.authorization.Authorization;
import methods.authorization.AuthorizationTO;
import methods.products.ProductTO;
import methods.products.Products;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
@Author: jkrolikowski
@Date: 10/10/2023

Test Case:
1. Authorize
2. Create product and save id from response
3. Get product from id
4. Compare products
Expected: Products are identical
5. Delete created product

*/

public class CreateProductTest {

    // Parameters of tests, might be sent in mvn (-Dparameter=xxx)
    protected String baseURL = System.getProperty("baseURL","https://api.escuelajs.co/");
    protected String email = System.getProperty("email","john@mail.com");
    protected String password = System.getProperty("password","changeme");

    private AuthorizationTO authTO;
    private ProductTO productTO;

    @BeforeSuite
    void BeforeSuite(){
        RestAssured.baseURI = baseURL;
    }

    @BeforeTest
    void BeforeTest(){
        buildTransferObjects();
    }
    @Test
    void Test(){

        String authToken = Authorization.authorize(authTO);

        int createdProductId = Products.createProduct(productTO, authToken);

        productTO.setId(createdProductId);

        ProductTO fromRequestTO = Products.getSingleProduct(productTO.getId(), authToken);

        Assert.assertEquals(productTO, fromRequestTO);

        Products.deleteProduct(productTO.getId(), authToken);
    }

    @AfterTest()
    void AfterTest(){

    }

    private void buildTransferObjects(){
        authTO = AuthorizationTO.builder()
                .email(email)
                .password(password)
                .build();

        productTO = ProductTO.builder()
                .title("Product")
                .price("1300")
                .description("description")
                .categoryId(12)
                .imgUrl(new String[]{"https://urltoimg.com/img.png"})
                .build();
    }

}
