package com.wojbeg.boredapp.ui.Main;

import static com.wojbeg.boredapp.utils.FragmentsEnum.*;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.ui.Favourite.FavPresenter;
import com.wojbeg.boredapp.ui.Favourite.FavouriteFragment;
import com.wojbeg.boredapp.ui.Home.HomeFragment;
import com.wojbeg.boredapp.ui.Home.HomePresenter;
import com.wojbeg.boredapp.utils.FragmentsEnum;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";

    private static final String RESTORED_IDEA_KEY = "RESTORED_IDEA";
    private static final String RESTORED_SAVED_KEY = "RESTORED_SAVED_BOOLEAN";
    private static final String RESTORED_NAVIGATION_KEY = "RESTORED_NAVIGATION";

    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    @Inject
    MainPresenter mainPresenter;

    @Inject
    HomeFragment homeFragment;

    @Inject
    HomePresenter homePresenter;

    @Inject
    FavouriteFragment favouriteFragment;

    @Inject
    FavPresenter favPresenter;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigation.setOnItemSelectedListener(navListener);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).addToBackStack(null).commit();

        mainPresenter.subscribe(this);

        if(savedInstanceState != null){

            if(savedInstanceState.containsKey(RESTORED_IDEA_KEY)){
                Idea restoredIdea = (Idea) savedInstanceState.getParcelable(RESTORED_IDEA_KEY);
                boolean wasSaved = savedInstanceState.getBoolean(RESTORED_SAVED_KEY);
                homePresenter.retrieveData(restoredIdea, wasSaved);
            }

            mainPresenter.selectFragment( (FragmentsEnum) savedInstanceState.getSerializable(RESTORED_NAVIGATION_KEY));

        }

    }


    private NavigationBarView.OnItemSelectedListener navListener = new
            NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.homeFragment:
                            mainPresenter.setSelectedFragment(HOME_FRAGMENT);
                            switchToHomeFragment();
                            break;

                        case R.id.favouriteFragment:
                            mainPresenter.setSelectedFragment(FAVOURITE_FRAGMENT);
                            switchToFavFragment();
                            break;
                    }

                    return true;
                }
            };

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!mainPresenter.isViewAttached()){
            mainPresenter.subscribe(this);
        }
    }

    @Override
    public void switchToHomeFragment() {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.enter_left_to_right,
                        R.anim.exit_left_to_right,
                        R.anim.enter_right_to_left,
                        R.anim.exit_right_to_left
                )
                .replace(R.id.nav_host_fragment, homeFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public void switchToFavFragment() {

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.enter_right_to_left,
                        R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right,
                        R.anim.exit_left_to_right
                )
                .replace(R.id.nav_host_fragment, favouriteFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(homePresenter.haveCurrentIdea()){
            outState.putParcelable(RESTORED_IDEA_KEY, homePresenter.getCurrentIdea());
            outState.putBoolean(RESTORED_SAVED_KEY, homePresenter.isCurrentSaved());
        }

        outState.putSerializable(RESTORED_NAVIGATION_KEY, mainPresenter.getSelectedFragment());

        mainPresenter.onDetach();

    }

    @Override
    public void onBackPressed() {

        if(bottomNavigation.getSelectedItemId() == R.id.homeFragment){
            super.onBackPressed();
            finish();
        }else{
            bottomNavigation.setSelectedItemId(R.id.homeFragment);
        }

    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDetach();
        super.onDestroy();
    }
}
