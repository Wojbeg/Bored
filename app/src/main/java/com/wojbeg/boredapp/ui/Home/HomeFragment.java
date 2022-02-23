package com.wojbeg.boredapp.ui.Home;


import static com.wojbeg.boredapp.utils.Constants.*;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment implements HomeView {

    private static final String TAG = "HomeFragment";

    @BindView(R.id.loadingProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.savingProgressBar)
    ProgressBar savingProgressBar;

    @BindView(R.id.getIdeaBtn)
    Button getIdeaBtn;

    @BindView(R.id.IdeaContainer)
    ConstraintLayout ideaContainer;

    @BindView(R.id.TitleHome)
    TextView ideaTitle;
    @BindView(R.id.LinkTitleHome)
    TextView linkTitle;
    @BindView(R.id.linkTextHome)
    TextView ideaLink;
    @BindView(R.id.ideaTypeHome)
    TextView ideaType;
    @BindView(R.id.participantsTextHome)
    TextView ideaParticipants;
    @BindView(R.id.accessibilityTextHome)
    TextView ideaAccessibility;
    @BindView(R.id.PriceTextHome)
    TextView ideaPrice;

    @BindView(R.id.randomPeopleAmount)
    MaterialCheckBox randomPeopleCheckBox;

    @BindView(R.id.settingsBtnContainer)
    RadioGroup radioGroup;

    @BindView(R.id.typeDropdown)
    AutoCompleteTextView typeSelector;
    @BindView(R.id.participantsNumber)
    TextInputEditText participantsNumber;
    @BindView(R.id.outlinedTextFieldParticipants)
    TextInputLayout participantsLayout;

    @BindView(R.id.priceSlider)
    RangeSlider priceSlider;
    @BindView(R.id.accessibilitySlider)
    RangeSlider accessibilitySlider;

    @BindView(R.id.settingsContainer)
    ConstraintLayout settingsContainer;

    @BindView(R.id.saveBtn)
    ImageView saveIdeaBtn;

    @BindView(R.id.ideaSearchSettings)
    CardView cardView;


    @Inject
    HomePresenter homePresenter;

    @Inject
    public HomeFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        setupCheckBox();
        setupSliders();
        setupTypeSelector();
        firstSetupTypeSelector();
        setupSettingsRadioGroup();

        setupGetIdeaBtn();

        addParticipantsChangeListener();

        return view;
    }

    private void setupGetIdeaBtn(){
        getIdeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.RandomBtn:
                        homePresenter.getIdeaFromApi();
                        break;
                    case R.id.CustomBtn:

                        validateInputs();
                        break;
                }

            }
        });
    }

    private void setupSettingsRadioGroup(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.RandomBtn:
                        hideSettingsFrame();
                        break;
                    case R.id.CustomBtn:
                        showSettingsFrame();
                        break;
                }
            }
        });
    }

    private void setupCheckBox(){
        randomPeopleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    participantsNumber.setEnabled(false);
                }else {
                    participantsNumber.setEnabled(true);
                }
            }
        });
    }

    private void validateInputs() {

        List<Float> priceSliderValues = priceSlider.getValues();
        List<Float> accessibilitySliderValues = accessibilitySlider.getValues();

        String selected = "";
        if(typeSelector.getEditableText().toString().compareToIgnoreCase(typeSelector.getAdapter().getItem(0).toString()) != 0){
            selected = typeSelector.getEditableText().toString();
        }
        homePresenter.validateInputs( selected, participantsNumber.getText().toString(), accessibilitySliderValues.get(0), accessibilitySliderValues.get(1), priceSliderValues.get(0), priceSliderValues.get(1),  randomPeopleCheckBox.isChecked());

    }

    private void setupSliders(){
        priceSlider.setValueFrom(MIN_PRICE);
        priceSlider.setValueTo(MAX_PRICE);
        priceSlider.setValues(MIN_INITIAL_VALUE, MAX_INITIAL_VALUE);
        priceSlider.setStepSize(INCREMENTATION_STEP);

        accessibilitySlider.setValueFrom(MIN_ACCESSIBILITY);
        accessibilitySlider.setValueTo(MAX_ACCESSIBILITY);
        accessibilitySlider.setValues(MIN_INITIAL_VALUE, MAX_INITIAL_VALUE);
        accessibilitySlider.setStepSize(INCREMENTATION_STEP);
    }

    private void setupTypeSelector(){
        String[] types = getResources().getStringArray(R.array.ideaTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, types);
        typeSelector.setAdapter(adapter);
        adapter.getFilter().filter(null);
    }

    private void firstSetupTypeSelector(){
        typeSelector.setText(typeSelector.getAdapter().getItem(0).toString(), false);
    }

    private void addParticipantsChangeListener(){
        participantsNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideParticipantsError();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTypeSelector();
        homePresenter.subscribe(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homePresenter.onDetach();
    }

    @Override
    public void showFrame() {
        ideaContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFrame() {
        ideaContainer.setVisibility(View.GONE);
    }

    @Override
    public void clearFrame() {

        hideLink();
        ideaType.setVisibility(View.GONE);
        ideaParticipants.setVisibility(View.GONE);
        ideaAccessibility.setVisibility(View.GONE);
        ideaPrice.setVisibility(View.GONE);

        saveIdeaBtn.setVisibility(View.GONE);
        saveIdeaBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_border);
    }

    private void hideLink(){
        linkTitle.setVisibility(View.GONE);
        ideaLink.setText("");
        ideaLink.setVisibility(View.GONE);
        ideaLink.setMovementMethod(null);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSaving() {
        savingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSaving() {
        savingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSettingsFrame() {
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSettingsFrame() {
        cardView.setVisibility(View.GONE);
    }

    @Override
    public void showIdeaDetails(Idea idea, boolean wasSaved) {
        hideLink();

        ideaTitle.setText(idea.activity);

        if(idea.link!=null && (!idea.link.trim().isEmpty())){
            ideaLink.setText(idea.link);
            ideaLink.setMovementMethod(LinkMovementMethod.getInstance());
            ideaLink.setClickable(true);

            linkTitle.setVisibility(View.VISIBLE);
            ideaLink.setVisibility(View.VISIBLE);
        }

        ideaType.setText(idea.type);
        ideaType.setVisibility(View.VISIBLE);

        ideaParticipants.setText(String.valueOf(idea.participants));
        ideaParticipants.setVisibility(View.VISIBLE);

        ideaAccessibility.setText( String.valueOf(idea.accessibility) );
        ideaAccessibility.setVisibility(View.VISIBLE);

        ideaPrice.setText( String.valueOf(idea.price));
        ideaPrice.setVisibility(View.VISIBLE);

        saveIdeaBtn.setVisibility(View.VISIBLE);

        if(wasSaved){
            setSaveBtnSaved();
        }else{
            setSaveBtnUnSaved();
        }

        saveIdeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.saveIdea();
            }
        });

    }

    @Override
    public void showParticipantsError() {
        participantsLayout.setErrorEnabled(true);
        participantsLayout.setError(getString(R.string.incorrectNumberOfParticipants));
    }

    @Override
    public void hideParticipantsError() {
        participantsLayout.setErrorEnabled(false);
    }

    @Override
    public void showSavedInfo(){
        setSaveBtnSaved();
        Toast.makeText(getContext(), getString(R.string.taskSaved), Toast.LENGTH_LONG).show();
    }

    private void setSaveBtnSaved(){
        saveIdeaBtn.setBackgroundResource(R.drawable.ic_twotone_blue_favorite_24);
    }

    private void setSaveBtnUnSaved(){
        saveIdeaBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_border);
    }

    @Override
    public void showNetworkUnavailable(){
        Toast.makeText(getContext(), getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(){
        Toast.makeText(getContext(), getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorSavingMessage(){
        Toast toast = Toast.makeText(getContext(), getString(R.string.taskNotSaved), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    @Override
    public void showEmptyIdeaMessage(){
        Toast.makeText(getContext(), getString(R.string.noActivityFound), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        homePresenter.onDetach();
        super.onSaveInstanceState(outState);
    }
}
