package de.skilloverflow.gitlab.api;

import android.content.Context;

public class GitlabApi {
    private GitlabApi() {
    }

    private static GitlabRequestBuilder mBuilder;


    /**
     * The the default {@link RequestBuilder} instance and start building a request.
     *
     * @param context A Context of the activity building this request.
     * @return The default instance of the {@link RequestBuilder}.
     */
    public static RequestBuilder init(Context context) {
        return getBuilder(context);
    }

    /**
     * The default instance of the {@link GitlabRequestBuilder} class.
     *
     * @param context A Context of the activity building this request.
     * @return The default instance of the {@link GitlabRequestBuilder}.
     */
    public static GitlabRequestBuilder getBuilder(Context context) {
        if (mBuilder == null) {
            mBuilder = new GitlabRequestBuilder(context);
        }

        return mBuilder;
    }
}
