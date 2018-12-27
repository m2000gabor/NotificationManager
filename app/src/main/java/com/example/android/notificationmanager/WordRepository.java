package com.example.android.notificationmanager;
import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class WordRepository {

    private OneDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public Word getOneItem(int pos) {
        List<Word> words = mAllWords.getValue();
        return words.get(pos);
    }


    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }
    private static class insertAsyncTask extends android.os.AsyncTask<Word, Void, Void> {

        private OneDao mAsyncTaskDao;

        insertAsyncTask(OneDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //deleteAll
    public void del() {
        new delAsyncTask(mWordDao).execute();
    }
    private static class delAsyncTask extends android.os.AsyncTask<Word, Void, Void> {

        private OneDao mAsyncTaskDao;

        delAsyncTask(OneDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    //update
    public void updateOneSingleItem(Word newW) {
        new updateAsyncTask(mWordDao).execute(newW);
    }
    private static class updateAsyncTask extends android.os.AsyncTask<Word, Void, Void> {
        private OneDao mAsyncTaskDao;
        updateAsyncTask(OneDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.updateOne(params[0]);
            return null;
        }
    }

    //delete one
    public void deleteOneSingleItem(Word oldWord){new delOneAsyncTask(mWordDao).execute(oldWord);}
    private static class delOneAsyncTask extends android.os.AsyncTask<Word, Void, Void> {
        private OneDao mAsyncTaskDao;
        delOneAsyncTask(OneDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.deleteOne(params[0]);
            return null;
        }
    }
}

