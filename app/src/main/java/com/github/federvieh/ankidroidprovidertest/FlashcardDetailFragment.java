/*
 * Copying and distribution of this file, with or without modification, are permitted in any
 * medium without royalty. This file is offered as-is, without any warranty.
 */
package com.github.federvieh.ankidroidprovidertest;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private TextView mHeaderView;

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

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        if(mHeaderView == null){
            mHeaderView = new TextView(getActivity());
            ContentResolver cr = getActivity().getContentResolver();
            Cursor cursor = cr.query(Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteId)), null, null, null, null);
            StringBuilder noteContent = new StringBuilder();
            if(cursor.moveToFirst()){
                Log.i("FlashCardListFragment", "Cursor for: " + noteId);
                String[] columnNames = cursor.getColumnNames();
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.i("FlashCardListFragment", "Column: " + columnNames[i]);
                    noteContent.append(columnNames[i]);
                    noteContent.append(": ");
                    noteContent.append(cursor.getString(i));
                    noteContent.append("\n");
                }
                noteContent.deleteCharAt(noteContent.length()-1);
            }
            else {
                noteContent.append("Whoopsie, no result!?");
            }
            mHeaderView.setText(noteContent);
        }
        getListView().addHeaderView(mHeaderView);

        if(mAdapter==null){
            mAdapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.detail_list_item, null,
                    new String[] {FlashCardsContract.DataColumns.MIMETYPE, FlashCardsContract.DataColumns.DATA1, FlashCardsContract.DataColumns.DATA2 },
                    new int[] { R.id.textMimetype, R.id.textData1, R.id.textData2 }, 0);
        }

        setListAdapter(mAdapter);
    }

    private void showEditDataDialog(int position){
        if (mCursor != null) {
            mCursor.moveToPosition(position - 1);//position 0 is mHeaderView
            final long noteId = mCursor.getLong(1);
            final String mimetype = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.MIMETYPE));
            final String data1 = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.DATA1));
            final String data2 = mCursor.getString(mCursor.getColumnIndex(FlashCardsContract.DataColumns.DATA2));
            Log.i("FlashCardListFragment", "Item long clicked, noteID: " + noteId);
            Log.i("FlashCardListFragment", "Item long clicked, MIME type: " + mimetype);
            Log.i("FlashCardListFragment", "Item long clicked, DATA1: " + data1);
            Log.i("FlashCardListFragment", "Item long clicked, DATA2: " + data2);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.change_card_data, null);
            final EditText editTextMimeType = (EditText) dialogView.findViewById(R.id.editTextMimeType);
            editTextMimeType.setText(mimetype);
            final EditText editTextData1 = (EditText) dialogView.findViewById(R.id.editTextData1);
            editTextData1.setText(data1);
            final EditText editTextData2 = (EditText) dialogView.findViewById(R.id.editTextData2);
            editTextData2.setText(data2);
            builder.setView(dialogView);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put(FlashCardsContract.DataColumns.MIMETYPE, editTextMimeType.getText().toString());
                    values.put(FlashCardsContract.DataColumns.DATA1, editTextData1.getText().toString());
                    values.put(FlashCardsContract.DataColumns.DATA2, editTextData2.getText().toString());
                    ContentResolver cr = getActivity().getContentResolver();
                    Uri noteUri = Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteId));
                    Uri dataUri = Uri.withAppendedPath(noteUri, "data");

                    cr.update(dataUri, values, null, null);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing, just close dialog
                }
            });
            builder.create().show();
        } else {
            Log.i("FlashCardListFragment", "Item clicked: " + position + ", but no cursor");
        }
    }

    private void showEditMainDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ScrollView dialogView = (ScrollView) inflater.inflate(R.layout.change_card_main, null);
        final LinearLayout list = (LinearLayout) dialogView.findViewById(R.id.blabla);

//        final ListView listViewEditMain = (ListView) dialogView.findViewById(R.id.listViewMainEdit);

//        final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        final ContentResolver cr = getActivity().getContentResolver();
        final Cursor cursor = cr.query(Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteId)), null, null, null, null);
        if(cursor.moveToFirst()){
            Log.i("FlashCardListFragment", "Cursor for: " + noteId);
            String[] columnNames = cursor.getColumnNames();
            for(int i = 0; i < cursor.getColumnCount(); i++) {
                View eme = inflater.inflate(R.layout.edit_main_entry, null);
                ((EditText)eme.findViewById(R.id.editTextColumnContent)).setText(cursor.getString(i));
                ((TextView)eme.findViewById(R.id.textViewColumnName)).setText(columnNames[i]);
                list.addView(eme);
                Log.i("FlashCardListFragment", "Column: " + columnNames[i]);
//                list.add(putData(columnNames[i], cursor.getString(i)));
            }
        }

        builder.setView(dialogView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check which items have changed and send update
                ContentValues values = new ContentValues();
                for(int i = 0; i < list.getChildCount(); i++){
                    View v = list.getChildAt(i);
                    String newText = ((EditText)v.findViewById(R.id.editTextColumnContent)).getText().toString();
                    String origText = cursor.getString(i);
                    String columnName = cursor.getColumnName(i);
                    if(origText.equals(newText)){
                        Log.i("FlashCardListFragment", columnName+" wasn't changed");
                    }
                    else{
                        Log.i("FlashCardListFragment", columnName+" was changed from '"+ origText +"' to '" + newText + "'.");
                        values.put(columnName, newText);
                    }
                }
                if (values.size()>0){
                    Uri noteUri = Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteId));

                    cr.update(noteUri, values, null, null);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing, just close dialog
            }
        });
        builder.create().show();
    }

    private HashMap<String, String> putData(String columnName, String content) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("columnName", columnName);
        item.put("content", content);
        return item;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        setListAdapter(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position>0) {
            showEditDataDialog(position);
        }
        else if (position == 0) {
            showEditMainDialog();
        }
        //else WTF!?
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(this.getClass().getName(), "onCreateLoader");
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri noteUri = Uri.withAppendedPath(FlashCardsContract.Note.CONTENT_URI, Long.toString(noteId));
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
