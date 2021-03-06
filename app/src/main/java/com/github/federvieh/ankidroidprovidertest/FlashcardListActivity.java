/*
 * Copying and distribution of this file, with or without modification, are permitted in any
 * medium without royalty. This file is offered as-is, without any warranty.
 */
package com.github.federvieh.ankidroidprovidertest;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;


/**
 * An activity representing a list of Flash cards. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link FlashcardDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link FlashcardListFragment} and the item details
 * (if present) is a {@link FlashcardDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link FlashcardListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class FlashcardListActivity extends Activity
        implements FlashcardListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getName(), "onCreate");
        setContentView(R.layout.activity_flashcard_list);

        if (findViewById(R.id.flashcard_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((FlashcardListFragment) getFragmentManager()
                    .findFragmentById(R.id.flashcard_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link FlashcardListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     * @param id
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(FlashcardDetailFragment.ARG_ITEM_ID, id);
            FlashcardDetailFragment fragment = new FlashcardDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.flashcard_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, FlashcardDetailActivity.class);
            detailIntent.putExtra(FlashcardDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
