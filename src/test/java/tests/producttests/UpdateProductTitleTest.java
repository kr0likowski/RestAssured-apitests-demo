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
@Date: 10/11/2023

Test Case:
1. Authorize
2. Create product and save id from response
3. Update project change title
4. Get project and check title
Expected: Title was correctly changed
5. Delete created product

*/

public class UpdateProductTitleTest {

    protected String baseURL = System.getProperty("baseURL","https://api.escuelajs.co/");
    protected String email = System.getProperty("email","john@mail.com");
    protected String password = System.getProperty("password","changeme");

    protected String changedTitle = System.getProperty("title","SomeTitle");

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

        Products.updateProductTitle(productTO.getId(), changedTitle, authToken);

        ProductTO fromRequestTO = Products.getSingleProduct(productTO.getId(), authToken);

        Assert.assertEquals(fromRequestTO.getTitle(), changedTitle);

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
