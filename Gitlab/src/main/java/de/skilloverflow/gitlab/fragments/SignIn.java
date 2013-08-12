package de.skilloverflow.gitlab.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;

import de.skilloverflow.gitlab.R;

public class SignIn extends Fragment {

    public static SignIn newInstance() {
        return new SignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_signin, container, false);

        Context context = getActivity();

        // Create TypeFace from .ttf files in Assets directory for use with the button.
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        signInButton.setTypeface(typefaceBold);
        signInButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        signInButton.setOnClickListener(new SignInClickListener(context, v));

        return v;
    }

    private final class SignInClickListener implements View.OnClickListener {
        private final Context mContext;
        private final View mView;

        public SignInClickListener(Context context, View view) {
            super();
            this.mContext = context;
            this.mView = view;
        }

        @Override
        public void onClick(View v) {
            EditText gitlabUrlEditText = (EditText) mView.findViewById(R.id.gitlab_url_edittext);
            EditText privateTokenEditText = (EditText) mView.findViewById(R.id.private_token_edittext);

            String gitlabUrl = gitlabUrlEditText.getText().toString();
            String privateToken = privateTokenEditText.getText().toString();

            boolean inputIsValid;
            Matcher matcher = Patterns.WEB_URL.matcher(gitlabUrl);

            inputIsValid = !TextUtils.isEmpty(gitlabUrl) && matcher.matches();
            if (!inputIsValid) {
                gitlabUrlEditText.setError(mContext.getResources().getString(R.string.url_invalid));
            }

            inputIsValid = !TextUtils.isEmpty(privateToken);
            if (!inputIsValid) {
                privateTokenEditText.setError(mContext.getResources().getString(R.string.token_invalid));
            }

            if (inputIsValid) {
                // Sign user in.
            }
        }
    }

}
