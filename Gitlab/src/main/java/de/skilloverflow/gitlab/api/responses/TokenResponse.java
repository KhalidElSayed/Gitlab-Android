package de.skilloverflow.gitlab.api.responses;

import org.json.JSONObject;

import de.skilloverflow.gitlab.api.RequestBuilder;

public interface TokenResponse extends RequestBuilder {
    public void setCallback(CompletedListener completedListener);

    public interface CompletedListener {
        public void onCompleted(JSONObject jsonObject);
    }
}
