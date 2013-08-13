package de.skilloverflow.gitlab.api;

import de.skilloverflow.gitlab.api.responses.TokenResponse;

public interface RequestBuilder {
    public TokenResponse querySession(String email, String password);
}
