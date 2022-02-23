package com.wojbeg.boredapp.repo.local;


import com.wojbeg.boredapp.repo.local.db.dao.IdeaDao;
import com.wojbeg.boredapp.repo.local.prefs.SharedPrefHelper;
import com.wojbeg.boredapp.repo.remote.BoredApi;
import com.wojbeg.boredapp.utils.onlineChecker.OnlineChecker;

public interface Repo extends OnlineChecker, IdeaDao, BoredApi, SharedPrefHelper {


}
