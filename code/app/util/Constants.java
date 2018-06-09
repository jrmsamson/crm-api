package util;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final Map<String, String> IMAGE_CONTENT_TYPE_EXTENSIONS = new HashMap<String, String>()
    {
        {
            put("image/jpeg", ".jpg");
            put("image/png", ".png");
        }
    };

    public static final String REQUEST_TRANSACTION_DSL_CONTEXT = "transactionDslContext";
    public static final String REQUEST_CONTEXT_USER_ID = "currentUserId";
    public static final String USER_ID_SESSION_KEY = "userId";
    public static final String ROLE_SESSION_KEY = "role";
    public static final String TOKEN_SESSION_KEY = "token";

}
