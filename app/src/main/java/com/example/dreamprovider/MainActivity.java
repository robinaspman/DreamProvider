package com.example.dreamprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dreamprovider.data.DreamContentProvider;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.dreamName);
        textView = findViewById(R.id.res);
    }

    public void onClickAddDream(View view) {
        new InsertItem(getApplicationContext()).execute();
    }

    public void onClickShowDreams(View view) {
        new ShowDreams(getApplicationContext()).execute();
    }

    public void deleteAll(View view) {
        new DeleteAllItems(getApplicationContext()).execute();
    }

    private class InsertItem extends AsyncTask<Void, Void, Void> {
        ContentValues values = new ContentValues();


        private final WeakReference<Context> weakReference;

        public InsertItem(Context applicationContext) {
            this.weakReference = new WeakReference<>(applicationContext);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            values.put(DreamContentProvider.NAME, editText.getText().toString().trim());
            weakReference.get().getContentResolver().insert(DreamContentProvider.CONTENT_URI, values);
            return null;
        }

    }

    public class ShowDreams extends AsyncTask<Void, Void, String> {
        private final WeakReference<Context> weakReference;

        public ShowDreams(Context applicationContext) {
            this.weakReference = new WeakReference<>(applicationContext);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);
        }

        @SuppressLint("Range")
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder builder;

            try (Cursor cursor = weakReference.get().getContentResolver()
                    .query(Uri.parse(String.valueOf(DreamContentProvider.CONTENT_URI)),
                            null, null, null, null)) {
                assert cursor != null;
                cursor.moveToFirst();
                builder = new StringBuilder();
                while (!cursor.isAfterLast()) {
                    builder.append("\n")
                            .append(cursor.getString(cursor.getColumnIndex(DreamContentProvider.ID)))
                            .append(". ")
                            .append(cursor.getString(cursor.getColumnIndex(DreamContentProvider.NAME)));
                    cursor.moveToNext();
                }
            }
            return builder.toString();
        }
    }

    private static class DeleteAllItems extends AsyncTask<Void, Void, Void>{
        public final WeakReference<Context> weakReference;
        public DeleteAllItems(Context applicationContext) {
            this.weakReference = new WeakReference<>(applicationContext);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String selectionClause = DreamContentProvider.DREAM_ID + " id = ?";
            String[] selectArgs = {String.valueOf(Uri.parse(DreamContentProvider.ID))};

            try {
                int delete = weakReference.get().getContentResolver()
                        .delete(Uri.parse(String.valueOf(DreamContentProvider.CONTENT_URI + "/4")),
                                selectionClause, selectArgs);
            } catch (Exception e) {
                Log.d("DEL", "doInBackground: ");
            }
            return null;
        }
    }
}