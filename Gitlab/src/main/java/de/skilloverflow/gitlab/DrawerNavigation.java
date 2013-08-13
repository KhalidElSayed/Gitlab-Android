package de.skilloverflow.gitlab;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import de.skilloverflow.gitlab.fragments.Dashboard;
import de.skilloverflow.gitlab.fragments.Issues;
import de.skilloverflow.gitlab.fragments.MergeRequests;
import de.skilloverflow.gitlab.fragments.Profile;
import de.skilloverflow.gitlab.fragments.Projects;
import de.skilloverflow.gitlab.fragments.Snippets;
import de.skilloverflow.gitlab.utils.misc.Interfaces;

public class DrawerNavigation extends ListFragment {

    private Context mContext;
    private View mSavedView;

    private Integer mScheduledPosition;
    private View mScheduledView;

    protected static NavigationTransactionListener sNavigationTransactionListener;

    // Menu Entries for the Navigation Drawer.
    private static final int MENU_DASHBOARD = 0;
    private static final int MENU_PROECTS = 1;
    private static final int MENU_ISSUES = 2;
    private static final int MENU_MERGE_REQUESTS = 3;
    private static final int MENU_SNIPPETS = 4;
    private static final int MENU_PROFILE = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slidingmenu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();

        // Get Menu icons and Menu strings for the Navigation Drawer.
        TypedArray menuIcons = getResources().obtainTypedArray(R.array.navigation_drawer_icons);
        String[] menuItems = getResources().getStringArray(R.array.navigation_drawer_strings);

        // Populate the ListView with a custom Adapter to include icons.
        CustomListAdapter mAdapter = new CustomListAdapter(getActivity(), R.layout.row_slidingmenu, R.id.slidingmenu_text, menuItems, menuIcons);
        setListAdapter(mAdapter);

        sNavigationTransactionListener = new NavigationTransactionListener();
    }


    @Override
    public void onListItemClick(ListView listview, View v, int position, long id) {
        mScheduledPosition = position;
        mScheduledView = v;

        if (MainActivity.sToggleListener != null) {
            MainActivity.sToggleListener.onShowAbove();
        }
    }

    private void switchFragments(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content_container, fragment)
                .commit();
    }

    private void highlightSelectedItem(View v) {
        setSelected(null, false);
        setSelected(v, true);
    }

    private void setSelected(View v, boolean selected) {
        View view;

        // If both are null, cancel the method call.
        if (v == null && mSavedView == null) {
            return;
        }

        if (v != null) {
            mSavedView = v;
            view = mSavedView;

        } else {
            view = mSavedView;
        }

        if (selected) {
            ViewCompat.setHasTransientState(view, true);
            view.setBackgroundColor(getResources().getColor(R.color.accent_color));

        } else {
            ViewCompat.setHasTransientState(view, false);
            view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    private void navigate(int scheduledPosition, View scheduledView) {
        switch (scheduledPosition) {
            case MENU_DASHBOARD:
                switchFragments(Dashboard.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            case MENU_PROECTS:
                switchFragments(Projects.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            case MENU_ISSUES:
                switchFragments(Issues.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            case MENU_MERGE_REQUESTS:
                switchFragments(MergeRequests.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            case MENU_SNIPPETS:
                switchFragments(Snippets.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            case MENU_PROFILE:
                switchFragments(Profile.newInstance());
                highlightSelectedItem(scheduledView);
                break;

            default:
                switchFragments(Dashboard.newInstance());
                highlightSelectedItem(scheduledView);
                break;
        }
    }

    public final class NavigationTransactionListener implements Interfaces.NavigationDrawerListener {

        @Override
        public void onDrawerClosed() {
            if (mScheduledPosition != null && mScheduledView != null) {
                navigate(mScheduledPosition, mScheduledView);

                mScheduledPosition = null;
                mScheduledView = null;
            }
        }

        @Override
        public void onDrawerOpened() {

        }
    }

    private final class CustomListAdapter extends ArrayAdapter<String> {
        private Activity mActivity;
        private TypedArray iconArray;
        private String[] textArray;

        /**
         * The Constructor for the CustomListAdapter.
         *
         * @param activity           The corresponding Activity.
         * @param resource           The layout for the {@link android.widget.ListView}.
         * @param textViewResourceId The {@link android.widget.TextView} Resource ID for the displayed Text.
         * @param text               The Text which shall be displayed.
         * @param icons              The Icons which shall be displayed.
         */
        public CustomListAdapter(Activity activity, int resource, int textViewResourceId, String[] text, TypedArray icons) {
            super(activity, resource, textViewResourceId, text);

            // Declare local class Variables.
            this.mActivity = activity;
            this.iconArray = icons;
            this.textArray = text;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mLayoutInflater = mActivity.getLayoutInflater();
            ViewHolder mViewHolder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.row_slidingmenu, null);

                mViewHolder = new ViewHolder();
                mViewHolder.icon = (ImageView) convertView.findViewById(R.id.slidingmenu_icon);
                mViewHolder.text = (TextView) convertView.findViewById(R.id.slidingmenu_text);

                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.icon.setImageDrawable(iconArray.getDrawable(position));
            mViewHolder.text.setText(textArray[position]);

            if (position == 0) {
                setSelected(convertView, true);
            }

            return convertView;
        }

    }

    /**
     * Simple ViewHolder.
     */
    private static class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
