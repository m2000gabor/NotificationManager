package com.example.android.databasebasic;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "word_table")
public class Word {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    private String mWord;
    // public Word(String word) {this.mWord = word;}

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo (name="prise")
    private int mNumber;

    @ColumnInfo
    private boolean mNotificationBoolean;

    public void setWord(@NonNull String mWord) {
        this.mWord = mWord;
    }
    public String getWord(){return this.mWord;}
    public int getId(){return this.id;}
    public void setId(int mID){this.id=mID;}
    public void setCategory(@NonNull String mCategory) {this.mCategory=mCategory;}
    public String getCategory(){return this.mCategory;}
    public void setNumber(int Number) {this.mNumber=Number;}
    public int getNumber(){return this.mNumber;}
    public boolean getNotificationBoolean() {
        return mNotificationBoolean;
    }
    public void setNotificationBoolean(boolean mNotificationBoolean) {
        this.mNotificationBoolean = mNotificationBoolean;
    }

}