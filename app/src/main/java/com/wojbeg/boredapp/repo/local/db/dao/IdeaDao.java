package com.wojbeg.boredapp.repo.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wojbeg.boredapp.model.Idea;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface IdeaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Idea idea);

    @Delete
    Single<Integer> deleteIdea(Idea idea);

    @Query("SELECT * FROM ideas")
    Flowable<List<Idea>> getSavedIdeas();

    @Query("SELECT COUNT(*) FROM ideas")
    Single<Integer> getCount();

}
