package com.wojbeg.boredapp.ui.Home;

public interface HomePresenterMvp {

    void saveIdea();

    void getIdeaFromApi();

    void validateInputs(String type, String numberOfParticipants,
                        float accessibilityMin, float accessibilityMax,
                        float priceMin, float priceMax, boolean isChecked);
}
