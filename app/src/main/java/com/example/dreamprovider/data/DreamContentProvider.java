package com.example.dreamprovider.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamprovider.DreamDao;
import com.example.dreamprovider.model.Dream;

import java.net.URI;
import java.util.Objects;

public class DreamContentProvider extends ContentProvider {
    private DreamDao dreamDao;

    public static final String TAG = DreamContentProvider.class.getName();

    public static final String AUTHORITY ="com.example.dreamprovider";
    public static final String DREAM_TABLE = "dream_tbl";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";

    public static final int DREAMS = 1;
    public static final int DREAM_ID = 2;

    public static final String URL = "content://" + AUTHORITY + "/" + DREAM_TABLE;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,
                DREAM_TABLE, DREAMS);
        uriMatcher.addURI(AUTHORITY,
                DREAM_TABLE + "/#", DREAM_ID);
    }

    @Override
    public boolean onCreate() {
        dreamDao = ApplicationDatabase.getInstance(getContext())
                .getDreamDao();

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Log.d(TAG, "query: ");
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case DREAMS:
                cursor = dreamDao.findAll();
                if (getContext() != null) {
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DREAMS:
                return "vnd.android.cursor.dir/" + DREAM_TABLE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert: ");
        switch (uriMatcher.match(uri)) {
            case DREAMS:
                if (getContext() != null) {
                    if (values != null) {
                        long id = dreamDao.insert(Dream.fromContentValues(values));
                        if (id != 0) {
                            getContext().getContentResolver()
                                    .notifyChange(uri, null);
                            return ContentUris.withAppendedId(uri, id);
                        }
                    }
                }
            case DREAM_ID:
                throw new IllegalArgumentException("Invalid UI: Insert failed" + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
       }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count;
        Log.d(TAG, "delete: ");
        switch (uriMatcher.match(uri)) {
            case DREAMS:
                if (getContext() != null) {
                    count = dreamDao.deleteAll();
                    getContext().getContentResolver()
                            .notifyChange(uri, null);
                    return count;
                }
            case DREAM_ID:
                if (getContext() != null) {
                    count = dreamDao.delete(ContentUris.parseId(uri));
                    getContext().getContentResolver()
                            .notifyChange(uri, null);
                    return count;
                }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update: ");
        switch (uriMatcher.match(uri)) {
            case DREAMS:
                if (getContext() != null) {
                    int count = dreamDao
                            .update(Dream.fromContentValues(Objects.requireNonNull(values)));
                    if (count != 0) {
                        getContext().getContentResolver()
                                .notifyChange(uri, null);
                        return count;
                    }
                }
            case DREAM_ID:
                throw new IllegalArgumentException("Invalid URI: cannot update");
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }
}
