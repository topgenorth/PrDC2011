package net.opgenorth.prdc11.scotchy;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class ScotchProvider extends ContentProvider {
    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "net.opgenorth.prdc11.scotchy";
    private static final String PATH_SCOTCH = "/scotch";
    private static final String PATH_SCOTCH_ID = "/scotch/";
    private static final int SCOTCH_ID_PATH_POSITION = 1;

    private static final String DATABASE_NAME = "scotch.db3";
    private static final int DATABASE_VERSION = 2;

    private static final int SCOTCHES = 1;
    private static final int SCOTCH_ID = 2;

    public static final String TAG = "ScotchProvider";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.opgenorth.prdc11.scotch";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.opgenorth.prdc11.scotch";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_SCOTCH);
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_SCOTCH_ID);
    public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_SCOTCH_ID + "/#");


    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> sScotchProjectionMap;

    private DatabaseHelper _openHelper;

    public static final class Columns implements BaseColumns {

        private Columns() {
        }

        public static final String TABLE_NAME = "singlemalt";
        public static final String COLUMN_NAME_BRAND = "brand";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_CREATE_DATE = "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";
        public static final String DEFAULT_SORT_ORDER = "brand";

    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        private long getTimeStamp() {
            return Long.valueOf(System.currentTimeMillis());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder sql = new StringBuilder("CREATE TABLE ");
            sql.append(Columns.TABLE_NAME);
            sql.append(" (");
            sql.append(Columns._ID);
            sql.append(" INTEGER PRIMARY KEY, ");
            sql.append(Columns.COLUMN_NAME_BRAND);
            sql.append(" TEXT, ");
            sql.append(Columns.COLUMN_NAME_URL);
            sql.append(" TEXT, ");
            sql.append(Columns.COLUMN_NAME_CREATE_DATE);
            sql.append(" INTEGER, ");
            sql.append(Columns.COLUMN_NAME_MODIFICATION_DATE);
            sql.append(" INTEGER");
            sql.append(");");

            Log.d(TAG, "About to create table with SQL " + sql.toString());
            db.execSQL(sql.toString());

            seedDatabaseWithStartupData(db);

        }

        private void seedDatabaseWithStartupData(SQLiteDatabase db) {
            long timestamp = this.getTimeStamp();
            db.execSQL("INSERT INTO singlemalt (brand, url, created, modified) VALUES ('Cragganmore', 'http://en.wikipedia.org/wiki/Cragganmore', " + timestamp + "," + timestamp + ");");
            db.execSQL("INSERT INTO singlemalt (brand, url, created, modified) VALUES ('Oban', 'http://en.wikipedia.org/wiki/Oban_%28whisky%29', " + timestamp + "," + timestamp + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from v" + oldVersion
                    + " to v" + newVersion + " which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS singlemalt");
            onCreate(db);
        }
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "scotch", SCOTCHES);
        sUriMatcher.addURI(AUTHORITY, "scotch/#", SCOTCH_ID);

        sScotchProjectionMap = new HashMap<String, String>();
        sScotchProjectionMap.put(Columns._ID, Columns._ID);
        sScotchProjectionMap.put(Columns.COLUMN_NAME_BRAND, Columns.COLUMN_NAME_BRAND);
        sScotchProjectionMap.put(Columns.COLUMN_NAME_URL, Columns.COLUMN_NAME_URL);
        sScotchProjectionMap.put(Columns.COLUMN_NAME_CREATE_DATE, Columns.COLUMN_NAME_CREATE_DATE);
        sScotchProjectionMap.put(Columns.COLUMN_NAME_MODIFICATION_DATE, Columns.COLUMN_NAME_MODIFICATION_DATE);
    }

    @Override
    public boolean onCreate() {
        _openHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String orderBy = getOrderBy(sortOrder);

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Columns.TABLE_NAME);

        setProjectionMapOnQueryBuilder(qb, uri);

        SQLiteDatabase db = _openHelper.getReadableDatabase();
        Cursor c = qb.query(
                db,
                projection,         // columns to return from query
                selection,          // columns for the WHERE clause
                selectionArgs,      // values for the WHERE clause
                null,               // nothing to group by
                null,               // nothing to filter by.
                orderBy             // ORDER BY clause
        );

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private void setProjectionMapOnQueryBuilder(SQLiteQueryBuilder qb, Uri uri) {
        qb.setProjectionMap(sScotchProjectionMap);
        switch (sUriMatcher.match(uri)) {
            case SCOTCHES:
                break;
            case SCOTCH_ID:
                final String idOfScotchToFetch = uri.getPathSegments().get(SCOTCH_ID_PATH_POSITION);
                qb.appendWhere(Columns._ID + "=" + idOfScotchToFetch);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    private String getOrderBy(String sortOrder) {
        String orderBy;
        if (TextUtils.isEmpty(sortOrder))
            orderBy = Columns.DEFAULT_SORT_ORDER;
        else
            orderBy = sortOrder;
        return orderBy;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SCOTCHES:
                return CONTENT_TYPE;
            case SCOTCH_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (sUriMatcher.match(uri) != SCOTCHES)
            throw new IllegalArgumentException("Unknown URI " + uri);

        ContentValues valuesToInsert = getContentValuesToInsert(contentValues);

        SQLiteDatabase db = _openHelper.getWritableDatabase();
        long rowId = db.insert(Columns.TABLE_NAME, Columns.COLUMN_NAME_BRAND, valuesToInsert);
        if (rowId > 0) {
            Uri scotchUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, rowId);
            return scotchUri;
        }

        // TODO: should throw an SQLException
        return null;
    }

    private ContentValues getContentValuesToInsert(ContentValues contentValues) {
        long timestamp = getTimeStamp();

        ContentValues valuesToInsert;

        if (contentValues == null) {
            valuesToInsert = new ContentValues();
        } else {
            valuesToInsert = new ContentValues(contentValues);
        }

        if (valuesToInsert.containsKey(Columns.COLUMN_NAME_MODIFICATION_DATE)) {
            valuesToInsert.remove(Columns.COLUMN_NAME_MODIFICATION_DATE );
        }
        valuesToInsert.put(Columns.COLUMN_NAME_MODIFICATION_DATE, timestamp);

        if (!valuesToInsert.containsKey(Columns.COLUMN_NAME_CREATE_DATE)) {
            valuesToInsert.put(Columns.COLUMN_NAME_CREATE_DATE, timestamp);
        }
        if (!valuesToInsert.containsKey(Columns.COLUMN_NAME_BRAND)) {
            valuesToInsert.put(Columns.COLUMN_NAME_BRAND, "");
        }
        return valuesToInsert;
    }

    private long getTimeStamp() {
        return Long.valueOf(System.currentTimeMillis());
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case SCOTCHES:
                count = db.delete(Columns.TABLE_NAME, where, whereArgs);
                break;
            case SCOTCH_ID:
                String scotchId = uri.getPathSegments().get(SCOTCH_ID_PATH_POSITION);
                String finalWhere = Columns._ID + "=" + scotchId;
                if (where != null) {
                    finalWhere += " AND " + where;
                }
                count = db.delete(Columns.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {
            case SCOTCHES:
                count = db.update(Columns.TABLE_NAME,
                        values,
                        where,
                        whereArgs
                );
                break;
            case SCOTCH_ID:
                String scotchId = uri.getPathSegments().get(SCOTCH_ID_PATH_POSITION);
                finalWhere = Columns._ID + " = " + scotchId;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(Columns.TABLE_NAME,
                        values,
                        finalWhere,
                        whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }
}
