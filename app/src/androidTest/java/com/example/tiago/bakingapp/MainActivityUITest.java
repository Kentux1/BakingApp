package com.example.tiago.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityUITest {

    private final static String RECIPE_NAME = "Yellow Cake";
    private final static String STEP_NAME = "0. Recipe Introduction";
    private final static String STEP_DESC = "Recipe Introduction";
    private final static String STEP_NUM_0 = "Step 0 of 12";
    private final static String STEP_NUM_1 = "Step 1 of 12";
    private final static int RECIPE_LIST_SCROLL_POSITION = 2;
    private final static int STEPS_WITH_MEDIA = 0;
    private final static int STEPS_WITHOUT_MEDIA = 1;

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testRecipeNameAtPostion() {
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions
                .scrollToPosition(RECIPE_LIST_SCROLL_POSITION));

        onView(withText(RECIPE_NAME))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testClickRecipeAtPosition() throws Exception {

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        onView(withId(R.id.ingredients_text_view))
                .check(matches(isDisplayed()));

        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions
                .scrollToPosition(STEPS_WITH_MEDIA));

        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));

        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(STEPS_WITH_MEDIA, click()));

        onView(withId(R.id.button_previous)).check(matches(isDisplayed()));
        onView(withId(R.id.button_next)).check(matches(isDisplayed()));
        onView(withId(R.id.step_desc_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_stepnum)).check(matches(isDisplayed()));
        onView(withId(R.id.step_desc_text_view)).check(matches(withText(STEP_DESC)));
        onView(withId(R.id.textview_stepnum)).check(matches(withText(STEP_NUM_0)));
        onView(withId(R.id.recipe_exoplayer_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickStepAtPosition() throws Exception {
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));
        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(STEPS_WITHOUT_MEDIA, click()));

        onView(withId(R.id.textview_stepnum))
                .check(matches(withText(STEP_NUM_1)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
