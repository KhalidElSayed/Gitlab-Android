package de.skilloverflow.gitlab.api;

import de.skilloverflow.gitlab.api.responses.TokenResponse;

public interface RequestBuilder {
    public TokenResponse authenticateUser(String email, String password);
}
