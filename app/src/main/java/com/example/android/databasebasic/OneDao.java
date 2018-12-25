package com.example.android.databasebasic;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OneDao {

    @Insert
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * from word_table")
    LiveData<List<Word>> getAllWords();

    @Update
    void updateOne(Word newWord);

    @Delete
    void deleteOne(Word oldWord);
}
