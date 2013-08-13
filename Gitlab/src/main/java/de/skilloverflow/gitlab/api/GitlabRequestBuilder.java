package de.skilloverflow.gitlab.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.skilloverflow.gitlab.api.responses.SessionResponse;

public class GitlabRequestBuilder implements RequestBuilder, SessionResponse {
    private final Context mContext;

    private String mEmail;
    private String mPass;

    public GitlabRequestBuilder(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public GitlabRequestBuilder querySession(String email, String password) {
        mEmail = email;
        // TODO Do it in a safer way!
        mPass = password;
        return GitlabApi.getBuilder(mContext);
    }

    @Override
    public void setCallback(CompletedListener completedListener) {
        new QuerySessionTask(mEmail, mPass, completedListener).execute();
        mEmail = null;
        mPass = null;
    }

    private final class QuerySessionTask extends AsyncTask<Boolean, Void, Boolean> {
        private final CompletedListener mCompletedListener;
        private final String mEmail;
        private final String mPassword;
        private JSONObject mJSONObject;

        public QuerySessionTask(String email, String password, CompletedListener completedListener) {
            super();
            this.mCompletedListener = completedListener;
            this.mEmail = email;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(CredentialsProvider.getUrl(mContext) + "/session");

            try {
                // Add the parameters.
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("email", mEmail));
                nameValuePairs.add(new BasicNameValuePair("password", mPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                    return false;
                }

                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);

                mJSONObject = new JSONObject(result);

                return true;

                // TODO Appropriate Error Handling.
            } catch (ClientProtocolException e) {
                Log.d("TAG", "ClientProtocolException", e);
                return false;
            } catch (IOException e) {
                Log.d("TAG", "IOException", e);
                return false;
            } catch (JSONException e) {
                Log.d("TAG", "JSONException", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            mCompletedListener.onCompleted(mJSONObject);
        }
    }
}
