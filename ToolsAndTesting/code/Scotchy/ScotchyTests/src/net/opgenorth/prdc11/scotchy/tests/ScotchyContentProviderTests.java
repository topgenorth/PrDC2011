package net.opgenorth.prdc11.scotchy.tests;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import net.opgenorth.prdc11.scotchy.ScotchProvider;

public class ScotchyContentProviderTests extends ProviderTestCase2<net.opgenorth.prdc11.scotchy.ScotchProvider> {
    Context _context;
    private String[] SCOTCH_COLUMNS_PROJECTION = new String[]{
            ScotchProvider.Columns._ID,
            ScotchProvider.Columns.COLUMN_NAME_BRAND,
            ScotchProvider.Columns.COLUMN_NAME_URL,
            ScotchProvider.Columns.COLUMN_NAME_CREATE_DATE,
            ScotchProvider.Columns.COLUMN_NAME_MODIFICATION_DATE
    };

    public ScotchyContentProviderTests() {
        super(ScotchProvider.class, ScotchProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _context = getContext();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void test_getType_ShouldParseContentItemTypeURI() throws Exception {
        String type = getProvider().getType(Uri.parse("content://net.opgenorth.prdc11.scotchy/scotch/1"));
        assertEquals(ScotchProvider.CONTENT_ITEM_TYPE, type);
    }

    public void test_getType_ShouldParseContentTypeURI() throws Exception {
        String type = getProvider().getType(Uri.parse("content://net.opgenorth.prdc11.scotchy/scotch"));
        assertEquals(ScotchProvider.CONTENT_TYPE, type);
    }

    public void test_getType_CannotMatchURI_ThrowsException() throws Exception {
        try {
            getProvider().getType(Uri.parse("BAD URI"));
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }

    public void test_delete_ExistingScotch_ShouldBeRemoved() throws Exception {
        ScotchProvider provider = getProvider();
        Uri uri = ContentUris.withAppendedId(ScotchProvider.CONTENT_URI, 1);
        int result = provider.delete(uri, null, null);

        assertEquals(1, result);    // We shold have deleted one record.

        Cursor c = provider.query(uri, SCOTCH_COLUMNS_PROJECTION, null, null, null);
        c.moveToLast();

        assertEquals(0, c.getCount());

    }

    public void test_insert_newScotch_ShouldSucceed() throws Exception {
        ContentValues values = new ContentValues();
        values.put(ScotchProvider.Columns.COLUMN_NAME_BRAND, "Teachers");
        values.put(ScotchProvider.Columns.COLUMN_NAME_URL, "http://www.teacherswhisky.com/");
        Uri newUri = getProvider().insert(ScotchProvider.CONTENT_URI, values);

        assertNotNull(newUri);

        Uri expectedUri = ContentUris.withAppendedId(ScotchProvider.CONTENT_URI, 3);
        assertEquals(expectedUri, newUri);
    }

    public void test_query_ScotchWithId1_ShouldBeFound() {
        Uri uri = Uri.withAppendedPath(ScotchProvider.CONTENT_URI, "1");
        Cursor c = getProvider().query(uri, SCOTCH_COLUMNS_PROJECTION, null, null, null);
        c.moveToFirst();

        int brandColumn = c.getColumnIndex(ScotchProvider.Columns.COLUMN_NAME_BRAND);
        assertEquals("Cragganmore", c.getString(brandColumn));
    }

    public void test_update_WithBadURI_ShouldThrowException() {
        Uri badUri = Uri.parse(ScotchProvider.CONTENT_TYPE + "/4/dddd");
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(ScotchProvider.Columns.COLUMN_NAME_BRAND, "Oban Double Distilled");
        try {
            getProvider().update(badUri, updatedValues, null, null);
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }

    public void test_update_ShouldUpdatedBrand() {
        Uri uri = ContentUris.withAppendedId(ScotchProvider.CONTENT_URI, 2);

        // Perform the actual update
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(ScotchProvider.Columns.COLUMN_NAME_BRAND, "Oban Double Distilled");
        int result = getProvider().update(uri, updatedValues, null, null);
        assertEquals(1, result);    // we should have updated one record.

        // Retrieve the record and inspect the value we just updated.
        Cursor c = getProvider().query(uri, SCOTCH_COLUMNS_PROJECTION, null, null, null);
        c.moveToFirst();

        // Check that the Brand was updated
        int brandColumn = c.getColumnIndex(ScotchProvider.Columns.COLUMN_NAME_BRAND);
        assertEquals("Oban Double Distilled", c.getString(brandColumn));
    }
}
