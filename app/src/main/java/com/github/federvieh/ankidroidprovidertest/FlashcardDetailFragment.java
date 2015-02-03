/*
 * Copying and distribution of this file, with or without modification, are permitted in any
 * medium without royalty. This file is offered as-is, without any warranty.
 */
package com.github.federvieh.ankidroidprovidertest;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * A fragment representing a single Flash card detail screen.
 * This fragment is either contained in a {@link FlashcardListActivity}
 * in two-pane mode (on tablets) or a {@link FlashcardDetailActivity}
 * on handsets.
 */
public class FlashcardDetailFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private long noteId;
    private SimpleCursorAdapter mAdapter;
    private Cursor mCursor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FlashcardDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            noteId = getArguments().getLong(ARG_ITEM_ID);
        }

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.detail_list_item, null,
                new String[] {FlashCardsContract.DataColumns.MIMETYPE, FlashCardsContract.DataColumns.DATA1, FlashCardsContract.DataColumns.DATA2 },
                new int[] { R.id.textMimetype, R.id.textData1, R.id.textData2 }, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(mCursor!=null) {
                    mCursor.moveToPosition(position);
                    long noteId = mCursor.getLong(1);
                    String mimetype = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.MIMETYPE));
                    String data1 = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.DATA1));
                    String data2 = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.DATA2));
                    Log.i("FlashCardListFragment", "Item long clicked, noteID: " + noteId);
                    Log.i("FlashCardListFragment", "Item long clicked, MIME type: " + mimetype);
                    Log.i("FlashCardListFragment", "Item long clicked, DATA1: " + data1);
                    Log.i("FlashCardListFragment", "Item long clicked, DATA2: " + data2);

                    ContentValues values = new ContentValues();
                    values.put(FlashCardsContract.DataColumns.MIMETYPE, mimetype);
                    values.put(FlashCardsContract.DataColumns.DATA1, data1);
                    values.put(FlashCardsContract.DataColumns.DATA2, data2+"-");
                    ContentResolver cr = getActivity().getContentResolver();
                    Uri noteUri = Uri.withAppendedPath(FlashCardsContract.CONTENT_URI, Long.toString(noteId));
                    Uri dataUri = Uri.withAppendedPath(noteUri, "data");

                    cr.update(dataUri, values, null, null);
                }
                else{
                    Log.i("FlashCardListFragment", "Item clicked: " + position + ", but no cursor");
                }
                return true;
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(this.getClass().getName(), "onCreateLoader");
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri noteUri = Uri.withAppendedPath(FlashCardsContract.CONTENT_URI, Long.toString(noteId));
        Uri dataUri = Uri.withAppendedPath(noteUri, "data");

        return new CursorLoader(getActivity(), dataUri,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(this.getClass().getName(), "onLoadFinished");
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mCursor = data;
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mCursor = null;
        mAdapter.swapCursor(null);
    }
}
