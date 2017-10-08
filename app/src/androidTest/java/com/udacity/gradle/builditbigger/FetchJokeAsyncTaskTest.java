package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class FetchJokeAsyncTaskTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingRegistry mIdlingRegistry;
    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        if (mIdlingRegistry == null) mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mIdlingResource);
    }

    // TODO [Android Test] UI Test to verify that a click on the tell joke and then displays a joke
    @Test
    public void testVerifyOnClickTellJokeDisplaysAJoke()
    {
        // Clicks the show joke button, should launch the asynctask that queries the GCE module for
        // joke, this operation is async so we must wait until the activity state returns to idle
        // before continuing the test.
        onView(withId(R.id.tell_joke_btn)).perform(click());

        if (BuildConfig.FLAVOR.equals("free")) {
            // TODO Handle if the product flavor is free, to also check if the interstitial ad displays, try to close it
            // Verify the interstitial ad shows, closes it
            ViewInteraction imageButton = onView(
                    allOf(withContentDescription("Interstitial close button"), isDisplayed()));
            imageButton.perform(click());
        }

        // Continue with the following assertions when the activity state returned to idled
        // this is marked when async task completes
        onView(withId(R.id.joke_display_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.joke_display_tv)).check(matches(withText(
                "The Java Librarian joke: how do oceans say hi to each other? Say no more, just wave")
                ));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }
}
