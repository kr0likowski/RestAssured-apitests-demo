package methods.authorization;
/*
@Author: jkrolikowski
@Date: 10/10/2023
*/

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizationTO {

    String email;
    String password;

}
