package net.opgenorth.prdc11.scotchy;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import net.opgenorth.prdc11.intro.R;

public class Scotchy extends ListActivity {

    public static final String TAG = "Scotchy";
    private int _urlColumnIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] projections = new String[]{
                ScotchProvider.Columns._ID,
                ScotchProvider.Columns.COLUMN_NAME_BRAND,
                ScotchProvider.Columns.COLUMN_NAME_URL
        };
        String[] displayFields = new String[]{ScotchProvider.Columns.COLUMN_NAME_BRAND};
        int[] displayViews = new int[]{R.id.scotch_brand};

        Cursor scotchCursor = managedQuery(ScotchProvider.CONTENT_URI,
                projections,
                null,
                null,
                null);
        _urlColumnIndex = scotchCursor.getColumnIndex(ScotchProvider.Columns.COLUMN_NAME_URL);
        setListAdapter(new SimpleCursorAdapter(this, R.layout.row, scotchCursor, displayFields, displayViews));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "Display the URL for _ID " + id + ", position " + position);

        Cursor cur =  (Cursor) getListAdapter().getItem(position);
        String url = cur.getString(_urlColumnIndex);

        Log.d(TAG, "Display information at " + url);

        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        startActivity(browserIntent);
    }
}
