package de.skilloverflow.gitlab.api.responses;

import org.json.JSONObject;

import de.skilloverflow.gitlab.api.RequestBuilder;

public interface SessionResponse extends RequestBuilder {
    /**
     * @param completedListener The listener for receiving the callback.
     */
    public void setCallback(CompletedListener completedListener);

    public interface CompletedListener {
        /**
         * Return the POST /session object.
         *
         * @param jsonObject The POST /session object.
         */
        public void onCompleted(JSONObject jsonObject);
    }
}
