package com.mobile.usoz;

import android.support.test.rule.ActivityTestRule;

import com.mobile.usoz.UserActivities.EditUserDataActivities.EditUserDataActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ EditUserDataActivity.class})
public class EditUserDataActivityTest {

    @Rule
    public ActivityTestRule<EditUserDataActivity> rule = new ActivityTestRule<>(EditUserDataActivity.class);
    private EditUserDataActivity activity = null;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        onView(withId(R.id.editUserNameTextView)).perform(clearText());
        onView(withId(R.id.editUserLastNameTextView)).perform(clearText());
        onView(withId(R.id.editUserBirthdayDateTextView)).perform(clearText());
        onView(withId(R.id.editUserUniversityTextView)).perform(clearText());
        onView(withId(R.id.editUserPassionsTextView)).perform(clearText());
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }


    //UnitTests
    @Test
    public void onCreate_elementsNotNull() {
        assertNotNull(activity.findViewById(R.id.editUserNameTextView));
        assertNotNull(activity.findViewById(R.id.editUserLastNameTextView));
        assertNotNull(activity.findViewById(R.id.editUserBirthdayDateTextView));
        assertNotNull(activity.findViewById(R.id.editUserUniversityTextView));
        assertNotNull(activity.findViewById(R.id.editUserPassionsTextView));
        assertNotNull(activity.findViewById(R.id.saveUserDataButton));
    }

    @Test
    public void userNameTextDefault() {
        onView(withId(R.id.editUserNameTextView)).check(matches(withHint("Twoje imię")));
    }

    @Test
    public void lastNameTextDefault() {
        onView(withId(R.id.editUserLastNameTextView)).check(matches(withHint("Twoje nazwisko")));
    }

    @Test
    public void universityTextDefault() {
        onView(withId(R.id.editUserUniversityTextView)).check(matches(withHint("Twoja uczelnia")));
    }

    @Test
    public void birthdayTextDefault() {
        onView(withId(R.id.editUserBirthdayDateTextView)).check(matches(withHint("Twoja data urodzin")));
    }

    @Test
    public void passionsTextDefault() {
        onView(withId(R.id.editUserPassionsTextView)).check(matches(withHint("Twoje pasje")));
    }



    //funkcjonalność zawieszona
    /*@Test
    public void isCorrect_true() throws Exception {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        boolean result = Whitebox.invokeMethod(activity, "isCorrect");
        assertTrue(result);
    }

    @Test
    public void isCorrect_false() throws Exception {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        boolean result = Whitebox.invokeMethod(activity, "isCorrect");
        assertFalse(result);
    }

    @Test
    public void isCorrect_falseEmpty() throws Exception {
        boolean result = Whitebox.invokeMethod(activity, "isCorrect");
        assertFalse(result);
    }*/

    /*@Test
    public void saveData_successSomeFields1() throws Exception {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        Whitebox.invokeMethod(activity, "saveData");
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void saveData_successSomeFields2() throws Exception {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        Whitebox.invokeMethod(activity, "saveData");
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void saveData_successAllFields() throws Exception {
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserBirthdayDateTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserPassionsTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        Whitebox.invokeMethod(activity, "saveData");
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }*/

    //Instrumented Tests
    @Test
    public void onClick_successEmpty() {
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successSomeFields1() {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successSomeFields2() {
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successSomeFields3() {
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserBirthdayDateTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successSomeFields4() {
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserBirthdayDateTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserPassionsTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successSomeFields5() {
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onClick_successAllFields() {
        onView(withId(R.id.editUserUniversityTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserBirthdayDateTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserPassionsTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.editUserLastNameTextView)).perform(typeText("temp"), closeSoftKeyboard());
        onView(withId(R.id.saveUserDataButton)).perform(click());
        onView(withText("Dane Twojego profilu zostały zapisane")).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

}
