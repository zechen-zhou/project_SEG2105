package com.example.projecttraining;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    @UiThreadTest
    public void checkEmail() throws Exception {
        assertNotNull(mActivity.findViewById(R.id.emailInputEditText));
        text = mActivity.findViewById(R.id.emailInputEditText);
        text.setText("abcEmail");
        String email = text.getText().toString();
        assertNotEquals("email", email);
    }

    @Test
    @UiThreadTest
    public void checkPassword() throws Exception {
        assertNotNull(mActivity.findViewById(R.id.passwordInputEditText));
        text = mActivity.findViewById(R.id.passwordInputEditText);
        text.setText("123456");
        String password = text.getText().toString();
        assertNotEquals("password", password);
    }
}