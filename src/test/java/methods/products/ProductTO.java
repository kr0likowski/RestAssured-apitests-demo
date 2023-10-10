package methods.products;
/*
@Author: jkrolikowski
@Date: 10/10/2023
*/

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTO {

    String title;

    String price;

    String description;

    String categoryId;

    String imgUrl;

}
