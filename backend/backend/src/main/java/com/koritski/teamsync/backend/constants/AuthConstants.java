package com.koritski.teamsync.backend.constants;

public final class AuthConstants {
    public static final String EMAIL_CLAIMS_KEY = "email";
    public static final String IS_USER_ROLE_EXIST = "is_user_role";
    public static final String IS_MODERATOR_ROLE_EXIST = "is_moderator_role";
    public static final String IS_ADMIN_ROLE_EXIST = "is_admin_role";
    public static final String ID_CLAIMS_KEY = "id";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PERMISSION_DESCRIPTION
            = "You don't have permission to do this. Please, try again later.";

    private AuthConstants() {
    }
}
