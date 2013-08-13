package de.skilloverflow.gitlab.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.skilloverflow.gitlab.R;

public class Snippets extends Fragment {

    public static Snippets newInstance() {
        return new Snippets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview, container, false);

        return v;
    }
}
