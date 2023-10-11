package utils;

/*
@Author: jkrolikowski
@Date: 10/11/2023
*/

import com.google.gson.JsonArray;
import methods.products.ProductTO;

public class JsonUtils {

    public static JsonArray createJsonArrayFromStringArray(String[] stringArray){

        // Create a JsonArray to represent the array of strings for images
        JsonArray imgUrlsArray = new JsonArray();
        for (String imgUrl : stringArray) {
            imgUrlsArray.add(imgUrl);
        }
        return imgUrlsArray;
    }

    public static String[] createStringArrayFromJsonArray(JsonArray imagesArray){

        // Convert the JsonArray to a string array
        String[] images = new String[imagesArray.size()];
        for (int i = 0; i < imagesArray.size(); i++) {
            images[i] = imagesArray.get(i).getAsString();
        }

        return images;
    }

}
