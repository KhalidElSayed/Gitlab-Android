package de.skilloverflow.gitlab.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import de.skilloverflow.gitlab.R;
import de.skilloverflow.gitlab.WizardActivity;
import de.skilloverflow.gitlab.utils.Utils;

public class WelcomeScreen extends Fragment {
    private static final String GITLAB_LOGO_URI = "assets://gitlab_logo.png";

    public static WelcomeScreen newInstance() {
        return new WelcomeScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        final Context context = getActivity();

        ImageView logoImageView = (ImageView) v.findViewById(R.id.gitlab_logo);
        ImageLoader.getInstance().displayImage(GITLAB_LOGO_URI, logoImageView);

        // Create TypeFace from .ttf files in Assets directory for use with the buttons.
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        Button tryItButton = (Button) v.findViewById(R.id.tryit_button);

        signInButton.setTypeface(typefaceBold);
        signInButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        tryItButton.setTypeface(typefaceThin);
        tryItButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WizardActivity.class);
                i.putExtra(Utils.INTENT_START_ID, Utils.START_SIGN_IN);
                startActivity(i);
            }
        });

        tryItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign in with demo credentials in the gitlab demo.
            }
        });

        return v;
    }
}
