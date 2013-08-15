package de.skilloverflow.gitlab.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.skilloverflow.gitlab.MainActivity;
import de.skilloverflow.gitlab.R;
import de.skilloverflow.gitlab.api.CredentialsProvider;
import de.skilloverflow.gitlab.api.GitlabApi;
import de.skilloverflow.gitlab.api.responses.SessionResponse;
import de.skilloverflow.gitlab.utils.App;
import de.skilloverflow.gitlab.utils.User;
import de.skilloverflow.gitlab.utils.misc.CustomCrouton;

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

        CheckBox passwordVisiblityCheckBox = (CheckBox) v.findViewById(R.id.password_visibility_switch);
        passwordVisiblityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText passwordEditText = (EditText) v.findViewById(R.id.password_edittext);
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

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
        public void onClick(final View v) {
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
                CredentialsProvider.setUrl(mActivity, gitlabUrl);
                final ProgressBar signInProgressBar = (ProgressBar) mView.findViewById(R.id.signin_progressBar);
                signInProgressBar.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);

                GitlabApi.init(mActivity).querySession(email, password).setCallback(new SessionResponse.CompletedListener() {
                    @Override
                    public void onCompleted(JSONObject jsonObject) {
                        if (jsonObject == null) {
                            signInProgressBar.setVisibility(View.GONE);
                            v.setVisibility(View.VISIBLE);
                            CustomCrouton.show(mActivity, R.string.crouton_login_wrong, CustomCrouton.ALERT);
                            return;
                        }

                        new CreateUserTask(mActivity, jsonObject, v, signInProgressBar).execute();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        // Workaround for Crouton issue #24 (https://github.com/keyboardsurfer/Crouton/issues/24).
        Crouton.clearCroutonsForActivity(getActivity());
        super.onDestroyView();
    }

    private final class CreateUserTask extends AsyncTask<Boolean, Void, Boolean> {
        private final Activity mActivity;
        private final JSONObject mJSONObject;
        private final View mClickedView;
        private final ProgressBar mSignInProgressBar;

        public CreateUserTask(Activity activity, JSONObject jsonObject, View clickedView, ProgressBar signInProgressBar) {
            super();
            this.mActivity = activity;
            this.mJSONObject = jsonObject;
            this.mClickedView = clickedView;
            this.mSignInProgressBar = signInProgressBar;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            try {
                new User(mActivity, mJSONObject.getString("username"),
                        mJSONObject.getString("email"),
                        mJSONObject.getString("name"),
                        mJSONObject.getString("created_at"));
                return CredentialsProvider.setPrivateToken(mActivity, mJSONObject.getString("private_token"));

            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool == null) {
                mSignInProgressBar.setVisibility(View.GONE);
                mClickedView.setVisibility(View.VISIBLE);
                CustomCrouton.show(mActivity, R.string.crouton_login_wrong, CustomCrouton.ALERT);
                return;
            }

            App.setWizardCompleted(mActivity);

            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            mActivity.finish();
        }
    }
}
