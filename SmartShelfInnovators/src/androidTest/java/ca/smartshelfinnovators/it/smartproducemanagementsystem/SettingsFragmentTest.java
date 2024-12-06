package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.not;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public GrantPermissionRule notificationPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS);

    @Before
    public void setUp() {
        // Launch the activity hosting the fragment
        ActivityScenario.launch(MainActivity.class);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testDarkModeSwitchToggle() {
        // Verify dark mode switch toggling
        onView(withId(R.id.darkMode_switch)).perform(click());
        onView(withId(R.id.darkMode_switch)).check(matches(isChecked()));

        onView(withId(R.id.darkMode_switch)).perform(click());
        onView(withId(R.id.darkMode_switch)).check(matches(not(isChecked())));
    }

    @Test
    public void testLockScreenSwitchToggle() {
        // Verify lock screen orientation switch toggling
        onView(withId(R.id.lockScreen_switch)).perform(click());
        onView(withId(R.id.lockScreen_switch)).check(matches(isChecked()));

        onView(withId(R.id.lockScreen_switch)).perform(click());
        onView(withId(R.id.lockScreen_switch)).check(matches(not(isChecked())));
    }

    @Test
    public void testNotificationSwitchPermissionHandling() {
        // Enable the notification switch
        onView(withId(R.id.notification_switch)).perform(click());
        onView(withId(R.id.notification_switch)).check(matches(isChecked()));
    }

    @Test
    public void testFeedbackNavigation() {
        // Click the feedback TextView and verify navigation to FeedbackFragment
        onView(withId(R.id.feedback_textview)).perform(click());
        onView(withId(R.id.feedback_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogoutSnackbar() {
        // Verify logout Snackbar interaction
        onView(withId(R.id.logOut_textview)).perform(click());
        onView(withText(R.string.confirm_logout)).check(matches(isDisplayed()));

        // Click "Yes" on the Snackbar and verify navigation to SignUp activity
        onView(withText(R.string.yes)).perform(click());
        onView(withId(R.id.signup_activity)).check(matches(isDisplayed()));
    }
}
