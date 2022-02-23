package com.wojbeg.boredapp.repo.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.repo.local.db.dao.*;

@Database(entities = {Idea.class}, version = AppDatabase.VERSION, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract IdeaDao ideaDao();
}
