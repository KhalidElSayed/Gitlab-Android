package de.skilloverflow.gitlab.api;

import de.skilloverflow.gitlab.api.responses.SessionResponse;

public interface RequestBuilder {

    /**
     * Login to get private token
     *
     * @param email    The email of user
     * @param password Valid password
     * @return {@link SessionResponse} to register a {@link de.skilloverflow.gitlab.api.responses.SessionResponse.CompletedListener} on.
     */
    public SessionResponse querySession(String email, String password);
}
