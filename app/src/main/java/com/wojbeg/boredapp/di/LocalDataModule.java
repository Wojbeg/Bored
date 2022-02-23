package com.wojbeg.boredapp.di;

import static com.wojbeg.boredapp.utils.Constants.DB_NAME;

import android.app.Application;

import androidx.room.Room;

import com.wojbeg.boredapp.di.scopes.AppScope;
import com.wojbeg.boredapp.repo.local.db.AppDatabase;
import com.wojbeg.boredapp.repo.local.db.dao.IdeaDao;
import com.wojbeg.boredapp.repo.local.prefs.SharedPrefHelper;
import com.wojbeg.boredapp.repo.local.prefs.SharedPreferenceUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataModule {

    @Provides
    @AppScope
    IdeaDao provideIdeaDao(AppDatabase appDatabase){
        return appDatabase.ideaDao();
    }

    @AppScope
    @Provides
    AppDatabase provideDb(Application context){
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    @Provides
    @AppScope
    SharedPrefHelper providePreferencesHelper(SharedPreferenceUtils appPreferences){
        return appPreferences;
    }

}
