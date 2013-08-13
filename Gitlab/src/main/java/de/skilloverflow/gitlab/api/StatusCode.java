package de.skilloverflow.gitlab.api;

public class StatusCode {
    public static int RESPONSE;

    // Status codes.
    public static final int NULL = 0;
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int CONFLICT = 409;
    public static final int SERVER_ERROR = 500;

    public StatusCode(String response) {
        super();
        RESPONSE = buildStatusCode(response);
    }

    private int buildStatusCode(String response) {
        if ("200 Ok".equals(response)) {
            return OK;
        } else if ("401 Unauthorized".equals(response)) {
            return UNAUTHORIZED;
        } else if ("403 Forbidden".equals(response)) {
            return FORBIDDEN;
        }

        return NULL;
    }
}
