package de.skilloverflow.gitlab.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.skilloverflow.gitlab.R;

public class Projects extends Fragment {

    public static Projects newInstance() {
        return new Projects();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview, container, false);

        return v;
    }
}
