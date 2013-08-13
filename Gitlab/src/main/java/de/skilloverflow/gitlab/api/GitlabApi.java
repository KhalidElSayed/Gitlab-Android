package de.skilloverflow.gitlab.api;

import android.content.Context;

public class GitlabApi {
    private GitlabApi() {
    }

    private static GitlabRequestBuilder mBuilder;


    public static RequestBuilder init(Context context) {
        return getBuilder(context);
    }

    public static GitlabRequestBuilder getBuilder(Context context) {
        if (mBuilder == null) {
            mBuilder = new GitlabRequestBuilder(context);
        }

        return mBuilder;
    }
}
