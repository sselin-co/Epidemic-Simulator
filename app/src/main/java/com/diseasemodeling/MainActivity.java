package com.diseasemodeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.roacult.backdrop.BackdropLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    //Uses roiacult's Backdrop layout library: https://github.com/roiacult/BackdropLayout
    BackdropLayout backdropLayout;

    View layout_back, layout_front;

    //Front layout views
    private SimpleSimulationSketch simpleSimulationSketch;
    private GraphSketch graphSketch;
    private SimulationFragment simulationFragment;
    private GraphFragment graphFragment;
    private FrameLayout simulationFrame;
    public Snackbar backSnackbar;
    public Snackbar frontSnackbar;

    //Back layout views
    public Slider populationEditSlider;
    public TextView populationEditTextView;

    public Slider personSizeSlider;
    public TextView personSizeTextView;

    public Slider infectionChanceSlider;
    public TextView infectionChanceTextView;

    public Slider infectionRadiusSlider;
    public TextView infectionRadiusTextView;

    public Slider socialDistancingSlider;
    public TextView socialDistancingTextView;

    public Slider maskWearingSlider;
    public TextView maskWearingTextView;

    public Slider startingInfectedSlider;
    public TextView startingInfectedTextView;

    public Slider daysInfectedSlider;
    public TextView daysInfectedTextView;

    public Slider secondsPerDaySlider;
    public TextView secondsPerDayTextView;

    public FloatingActionButton playPause;

    private int populationNumber;
    private float personSize;
    private float infectionChance;
    private int radiusModifier;
    private int socialDistancingChance;
    private int maskWearingChance;
    private int startingInfected;
    private int daysInfected;
    private int secondsPerDay;

    public boolean isSliderClicked = false;

    public boolean isPaused = false;

    public boolean pauseOneTimeTrigger = false;
    public boolean resumeOneTimeTrigger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App component initialization
        initBackdropLayouts();
        initSimulationFragment();
        initGraphFragment();
        initBackDropListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumeOneTimeTrigger = false;
        if (pauseOneTimeTrigger) {
            simpleSimulationSketch.pauseSketch();
            graphSketch.pauseSketch();
        } else{
            pauseOneTimeTrigger = true;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        pauseOneTimeTrigger = false;
        if (resumeOneTimeTrigger) {
            simpleSimulationSketch.resumeSketch();
            graphSketch.resumeSketch();
        } else{
            resumeOneTimeTrigger = true;
        }
    }

    public void initBackdropLayouts(){
        backdropLayout = findViewById(R.id.container);
        layout_back = backdropLayout.getChildAt(0);
        layout_front = backdropLayout.getChildAt(1);
        //backdropLayout.setPeeckHeight(1800f);


        initPopulationSettingsCard();
        initPersonSizeCard();
        initSocialDistancingCard();
        initMaskWearingCard();
        initInfectionChanceCard();
        initInfectionRadiusCard();
        initStartingInfectedCard();
        initDaysInfectedCard();
        initDaysPerSecondCard();
    }

    public void initPopulationSettingsCard(){
        populationEditSlider = findViewById(R.id.populationSlider);
        populationEditTextView = findViewById(R.id.populationSizeTitle);
        populationEditTextView.setText(getString(R.string.population_number, (int)populationEditSlider.getValue()));
        populationEditSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                populationEditTextView.setText(getString(R.string.population_number, (int)populationEditSlider.getValue()));
                if(!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initPersonSizeCard(){
        personSizeSlider = findViewById(R.id.personSizeSlider);
        personSizeTextView = findViewById(R.id.personSizeTitle);
        personSizeTextView.setText(getString(R.string.person_size, personSizeSlider.getValue()));
        personSizeSlider.addOnChangeListener(new Slider.OnChangeListener() {
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                personSizeTextView.setText(getString(R.string.person_size, personSizeSlider.getValue()));
                if(!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    public void initSocialDistancingCard(){
        socialDistancingSlider = findViewById(R.id.socialDistancingSlider);
        socialDistancingTextView = findViewById(R.id.socialDistancingTextView);
        socialDistancingTextView.setText(getString(R.string.social_distancing_percent, (int)socialDistancingSlider.getValue()));
        socialDistancingSlider.addOnChangeListener(new Slider.OnChangeListener() {
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser){
                socialDistancingTextView.setText(getString(R.string.social_distancing_percent, (int)socialDistancingSlider.getValue()));
                if(!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    public void initMaskWearingCard(){
        maskWearingSlider = findViewById(R.id.maskWearingSlider);
        maskWearingTextView = findViewById(R.id.maskWearingTextView);
        maskWearingTextView.setText(getString(R.string.mask_wearing_percent, (int)maskWearingSlider.getValue()));
        maskWearingSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                maskWearingTextView.setText(getString(R.string.mask_wearing_percent, (int)maskWearingSlider.getValue()));
                if (!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    public void initInfectionChanceCard(){
        infectionChanceSlider = findViewById(R.id.infectionChanceSlider);
        infectionChanceTextView = findViewById(R.id.infectionChanceTitle);
        infectionChanceTextView.setText(getString(R.string.infection_chance, (int)(infectionChanceSlider.getValue() * 100)));
        infectionChanceSlider.addOnChangeListener(new Slider.OnChangeListener() {
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                infectionChanceTextView.setText(getString(R.string.infection_chance, (int)(infectionChanceSlider.getValue() * 100)));
                if(!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initInfectionRadiusCard(){
        infectionRadiusSlider = findViewById(R.id.infectionRadiusSlider);
        infectionRadiusTextView = findViewById(R.id.infectionRadiusTitle);
        infectionRadiusTextView.setText(getString(R.string.infection_radius, (int)infectionRadiusSlider.getValue()));
        infectionRadiusSlider.addOnChangeListener(new Slider.OnChangeListener() {
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                infectionRadiusTextView.setText(getString(R.string.infection_radius, (int)infectionRadiusSlider.getValue()));
                if(!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initStartingInfectedCard(){
        startingInfectedSlider = findViewById(R.id.startingInfectedSlider);
        startingInfectedTextView = findViewById(R.id.startingInfectedTextView);
        startingInfectedTextView.setText(getString(R.string.number_starting_infected, (int)startingInfectedSlider.getValue()));
        startingInfectedSlider.addOnChangeListener(new Slider.OnChangeListener() {
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                startingInfectedTextView.setText(getString(R.string.number_starting_infected, (int)startingInfectedSlider.getValue()));
                if (!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initDaysInfectedCard(){
        daysInfectedSlider = findViewById(R.id.daysInfectedSlider);
        daysInfectedTextView = findViewById(R.id.daysInfectedTextView);
        daysInfectedTextView.setText(getString(R.string.days_infected, (int)daysInfectedSlider.getValue()));
        daysInfectedSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                daysInfectedTextView.setText(getString(R.string.days_infected, (int)daysInfectedSlider.getValue()));
                if (!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initDaysPerSecondCard(){
        secondsPerDaySlider = findViewById(R.id.secondsPerDaySlider);
        secondsPerDayTextView = findViewById(R.id.secondsPerDayTextView);
        secondsPerDayTextView.setText(getString(R.string.days_per_second, (int) secondsPerDaySlider.getValue()));
        secondsPerDaySlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                secondsPerDayTextView.setText(getString(R.string.days_per_second, (int) secondsPerDaySlider.getValue()));
                if (!isSliderClicked){
                    displayBackSnackBar();
                }
                isSliderClicked = true;
            }
        });
    }

    public void initSimulationFragment(){
        playPause = findViewById(R.id.playPause);
        playPause.setColorFilter(getColor(R.color.textColor));
        getSettingsValues();
        simulationFrame = layout_front.findViewById(R.id.simulationFrame);
        simulationFrame.removeAllViews();
        simpleSimulationSketch = new SimpleSimulationSketch(populationNumber, personSize, infectionChance, radiusModifier
        , socialDistancingChance, maskWearingChance, startingInfected, daysInfected, secondsPerDay);
        simulationFragment = new SimulationFragment(simpleSimulationSketch);
        simulationFragment.setView(simulationFrame, this);
    }

    public void initGraphFragment(){
        FrameLayout graphFrame = findViewById(R.id.graphFrame);
        graphSketch = new GraphSketch();
        graphFragment = new GraphFragment(graphSketch);
        graphFragment.setView(graphFrame, this);
    }

    public void initBackDropListener(){
        backdropLayout.setOnBackdropChangeStateListener(new Function1<BackdropLayout.State, Unit>(){
            @Override
            public Unit invoke(BackdropLayout.State state) {
                Toolbar toolbar = findViewById(R.id.toolbar);
                if (state == BackdropLayout.State.OPEN)
                    toolbar.setTitle(getResources().getString(R.string.layout_back_toolbar_text));
                else
                    toolbar.setTitle(getResources().getString(R.string.layout_front_toolbar_text));
                if (isSliderClicked){
                    isSliderClicked = false;
                    resetSimulation();
                   }
                return Unit.INSTANCE;
            }
        });
    }

    public void resetSimulation(View v){
        try {
            if (isPaused) {
                isPaused = false;
                playPause.setImageDrawable(getDrawable(R.drawable.round_play_arrow_24));
                playPause.setColorFilter(getColor(R.color.textColor));
            }
            displayFrontSnackBar();
            simpleSimulationSketch.resetSketch();
            graphSketch.resetSketch();
        } catch (Exception e){
            System.out.println("resetSimulation has crashed");
        }
    }

    public void resetSimulation(){
        if (isPaused){
            isPaused = false;
            playPause.setImageDrawable(getDrawable(R.drawable.round_play_arrow_24));
            playPause.setColorFilter(getColor(R.color.textColor));
        }
        displayFrontSnackBar();
        getSettingsValues();
        graphSketch.resetSketch();
        simpleSimulationSketch.resetSketch(populationNumber, personSize, infectionChance, radiusModifier
                , socialDistancingChance, maskWearingChance, startingInfected, daysInfected, secondsPerDay);
    }

    public void playPauseSimulation(View v){
        if (!isPaused){
            isPaused = true;
            playPause.setImageDrawable(getDrawable(R.drawable.round_pause_24));
            playPause.setColorFilter(getColor(R.color.textColor));
            simpleSimulationSketch.pauseSketch();
            graphSketch.pauseSketch();
        }
        else{
            isPaused = false;
            playPause.setImageDrawable(getDrawable(R.drawable.round_play_arrow_24));
            playPause.setColorFilter(getColor(R.color.textColor));
            simpleSimulationSketch.resumeSketch();
            graphSketch.resumeSketch();
        }
    }

    public void consumeTouchEvent(View v){
        //nom nom nom :)
    }

    public void displayFrontSnackBar(){
        frontSnackbar = Snackbar.make(layout_front, R.string.simulation_reset_dialog, Snackbar.LENGTH_SHORT);
        frontSnackbar.setBackgroundTint(getColor(R.color.snackbarBackground));
        frontSnackbar.setAnchorView(playPause);
        frontSnackbar.show();
    }

    public void displayBackSnackBar(){
        backSnackbar = Snackbar.make(layout_back, R.string.settings_reset_dialog, Snackbar.LENGTH_INDEFINITE);
        backSnackbar.setBackgroundTint(getColor(R.color.snackbarBackground));
        backSnackbar.show();
    }

    public void getSettingsValues(){
        populationNumber = (int) populationEditSlider.getValue();
        personSize = personSizeSlider.getValue();
        infectionChance = infectionChanceSlider.getValue();
        radiusModifier = (int)infectionRadiusSlider.getValue();
        socialDistancingChance = (int)socialDistancingSlider.getValue();
        maskWearingChance = (int)maskWearingSlider.getValue();
        startingInfected = (int)startingInfectedSlider.getValue();
        daysInfected = (int)daysInfectedSlider.getValue();
        secondsPerDay = (int) secondsPerDaySlider.getValue();
    }
}
