package tests;
/*
@Author: jkrolikowski
@Date: 10/10/2023
*/

import methods.authorization.Authorization;
import methods.authorization.AuthorizationTO;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProductsTest {

    protected String baseURL = System.getProperty("baseURL","https://api.escuelajs.co/");
    protected String email = System.getProperty("email","john@mail.com");
    protected String password = System.getProperty("password","changeme");

    private AuthorizationTO authTO;
    @BeforeTest
    void BeforeTest(){
        buildTransferObjects();
    }
    @Test
    void Test(){

        String authToken = Authorization.authorize(authTO, baseURL);

    }

    @AfterTest()
    void AfterTest(){

    }

    private void buildTransferObjects(){
        authTO = AuthorizationTO.builder()
                .email(email)
                .password(password)
                .build();

    }
}
