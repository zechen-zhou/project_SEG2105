package com.example.projecttraining;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testNoEmailInput() {
        ViewInteraction materialButton = onView(withId(R.id.signIn));
        materialButton.perform(click());

        onView(withId(R.id.emailInputLayout)).check
                (matches(hasInputLayoutErrorText(mActivity.getString(R.string
                        .email_empty_message))));
    }

    @Test
    public void testInvalidEmail() {
        // Invalid email
        ViewInteraction appCompatEditText = onView(withId(R.id.emailInputEditText));
        appCompatEditText.perform(replaceText("Admin"));

        ViewInteraction materialButton = onView(withId(R.id.signIn));
        materialButton.perform(click());

        onView(withId(R.id.emailInputLayout)).check
                (matches(hasInputLayoutErrorText(mActivity.getString(R.string
                        .email_error_message))));
    }

    @Test
    public void testNoPasswordInput() {
        ViewInteraction materialButton = onView(withId(R.id.signIn));
        materialButton.perform(click());

        onView(withId(R.id.passwordInputLayout)).check
                (matches(hasInputLayoutErrorText(mActivity.getString(R.string
                        .password_empty_message))));
    }

/* Toast message assertions not working (https://github.com/android/android-test/issues/803)
    @Test
    public void testInvalidPassword() {
        // Select Admin user type
        ViewInteraction dropdownMenu = onView(withId(R.id.auto_complete_txt));
        dropdownMenu.perform(click());

        onView(withText("Admin"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // Valid email
        ViewInteraction emailInputEditText = onView(withId(R.id.emailInputEditText));
        emailInputEditText.perform(replaceText("admin@mealer.com"));

        // Invalid password
        ViewInteraction passwordInputEditText = onView(withId(R.id.passwordInputEditText));
        passwordInputEditText.perform(replaceText("123"));

        ViewInteraction materialButton = onView(withId(R.id.signIn));
        materialButton.perform(click());

        // Check toast message
        // https://stackoverflow.com/a/28606603
//        onView(withText(R.string.toast_text)).inRoot(withDecorView(Matchers.not((Validator) mActivity.getWindow().getDecorView()))).check(matches(isDisplayed()));
//        onView(withText("Wrong password")).inRoot(withDecorView((Matcher<View>) not((Validator) mActivity.getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
    */


    // https://stackoverflow.com/a/38874162
    public static Matcher<View> hasInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                // Get errorText of TextInputLayout
                CharSequence errorText = ((TextInputLayout) view).getError();

                if (errorText == null) {
                    return false;
                }

                String hint = errorText.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}

