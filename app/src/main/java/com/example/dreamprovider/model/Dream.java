package com.example.dreamprovider.model;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dream_tbl")
public class Dream {
    private static final String DREAM_ID = "id";
    private static final String DREAM_NAME = "name";
    private static final String DREAM_DESCRIPTION = "description";

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Dream fromContentValues(ContentValues contentValues) {
        Dream dream = new Dream();

        if (contentValues.containsKey(DREAM_ID)) {
            dream.setId(contentValues.getAsInteger(DREAM_ID));
        } if (contentValues.containsKey(DREAM_NAME)) {
            dream.setName(contentValues.getAsString(DREAM_NAME));
        } if (contentValues.containsKey(DREAM_DESCRIPTION)) {
            dream.setDescription(contentValues.getAsString(DREAM_DESCRIPTION));
        }

        return dream;

    }
}
