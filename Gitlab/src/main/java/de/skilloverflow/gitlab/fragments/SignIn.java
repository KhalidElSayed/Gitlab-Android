package de.skilloverflow.gitlab.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

import de.skilloverflow.gitlab.MainActivity;
import de.skilloverflow.gitlab.R;
import de.skilloverflow.gitlab.api.CredentialsProvider;
import de.skilloverflow.gitlab.api.GitlabApi;
import de.skilloverflow.gitlab.api.responses.TokenResponse;
import de.skilloverflow.gitlab.utils.App;
import de.skilloverflow.gitlab.utils.User;

public class SignIn extends Fragment {

    public static SignIn newInstance() {
        return new SignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_signin, container, false);

        Activity activity = getActivity();

        // Create TypeFace from .ttf files in Assets directory for use with the button.
        Typeface typefaceBold = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");

        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        signInButton.setTypeface(typefaceBold);
        signInButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        signInButton.setOnClickListener(new SignInClickListener(activity, v));

        return v;
    }

    private final class SignInClickListener implements View.OnClickListener {
        private final Activity mActivity;
        private final View mView;

        public SignInClickListener(Activity activity, View view) {
            super();
            this.mActivity = activity;
            this.mView = view;
        }

        @Override
        public void onClick(View v) {
            EditText gitlabUrlEditText = (EditText) mView.findViewById(R.id.gitlab_url_edittext);
            EditText emailEditText = (EditText) mView.findViewById(R.id.email_edittext);
            EditText passwordEditText = (EditText) mView.findViewById(R.id.password_edittext);

            String gitlabUrl = gitlabUrlEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean inputIsValid;
            Matcher urlMatcher = Patterns.WEB_URL.matcher(gitlabUrl);

            inputIsValid = !TextUtils.isEmpty(gitlabUrl) && urlMatcher.matches();
            if (!inputIsValid) {
                gitlabUrlEditText.setError(mActivity.getResources().getString(R.string.url_invalid));
            }

            Matcher emailMatcher = Patterns.EMAIL_ADDRESS.matcher(email);
            inputIsValid = !TextUtils.isEmpty(email) && emailMatcher.matches();
            if (!inputIsValid) {
                emailEditText.setError(mActivity.getResources().getString(R.string.email_invalid));
            }

            inputIsValid = !TextUtils.isEmpty(password);
            if (!inputIsValid) {
                passwordEditText.setError(mActivity.getResources().getString(R.string.password_invalid));
            }

            if (inputIsValid) {
                CredentialsProvider.setBaseUrl(mActivity, gitlabUrl);
                // TODO Add ProgressBar somewhere for showing progress.
                GitlabApi.init(mActivity).querySession(email, password).setCallback(new TokenResponse.CompletedListener() {
                    @Override
                    public void onCompleted(JSONObject jsonObject) {
                        if (jsonObject == null) {
                            // TODO Crouton to inform about the Failure and start again.
                            return;
                        }
                        try {
                            new User(mActivity, jsonObject.getString("username"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("created_at"));
                            CredentialsProvider.setPrivateToken(mActivity, jsonObject.getString("private_token"));
                            App.setWizardCompleted(mActivity);

                            startActivity(new Intent(mActivity, MainActivity.class));
                            mActivity.finish();

                        } catch (JSONException e) {
                            Log.d("TAG", "JSONException", e);
                            // TODO Crouton to inform about the Failure and start again.
                        }
                    }
                });
            }
        }
    }

}
