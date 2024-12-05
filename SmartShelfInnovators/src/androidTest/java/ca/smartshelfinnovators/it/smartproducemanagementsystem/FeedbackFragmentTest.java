package ca.smartshelfinnovators.it.smartproducemanagementsystem;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FeedbackFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Navigate to the FeedbackFragment
        activityRule.getScenario().onActivity(activity -> {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FeedbackFragment());
            transaction.commit();
        });
    }

    @Test
    public void testSubmitButton_isInitiallyEnabled() {
        // Check if the submit button is initially enabled when feedback can be submitted
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }

    @Test
    public void testSubmitButton_isDisabledAfterSubmission() {
        // Submit the feedback with valid inputs
        onView(withId(R.id.et_name)).perform(ViewActions.typeText("John Doe"));
        onView(withId(R.id.et_phone)).perform(ViewActions.typeText("1234567890"));
        onView(withId(R.id.et_email)).perform(ViewActions.typeText("john@example.com"));
        onView(withId(R.id.ratingBar)).perform(ViewActions.click());  // First click (1)
        onView(withId(R.id.ratingBar)).perform(ViewActions.click());  // Second click (2)
        onView(withId(R.id.ratingBar)).perform(ViewActions.click());  // Third click (3)
        onView(withId(R.id.ratingBar)).perform(ViewActions.click()); // Fourth click (4)
        onView(withId(R.id.btn_submit)).perform(ViewActions.click());

        // Wait for progress bar to finish (simulate delay)
        onView(withId(R.id.progressBar)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // After submission, check if the button is disabled
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(ViewMatchers.isNotEnabled()));
    }

    @Test
    public void testSubmitFeedback_withEmptyFields_showsError() {
        // Attempt to submit with empty fields and check if a toast message appears
        onView(withId(R.id.btn_submit)).perform(ViewActions.click());

        // Check if the "Please fill all fields" toast message is displayed
        onView(withText("Please fill all fields and provide a rating"))
                .inRoot(isPlatformPopup())  // Match platform popups (including Toast)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSubmitButton_updatesWithRemainingTime() {
        // Simulate the feedback submission being disabled for 24 hours
        // Check that the submit button shows the "Wait X hours X min" message
        long remainingTime = 5000L;  // Simulate 5 seconds remaining time for testing

        // Trigger the method to display the remaining time
        onView(withId(R.id.btn_submit)).check(ViewAssertions.matches(withText("Wait 0 hours 5 min")));
    }

    @Test
    public void testBackButton_showsExitConfirmationDialog() {
        // Click the back button
        onView(withId(R.id.btn_back)).perform(ViewActions.click());

        // Check if the exit confirmation dialog appears
        onView(withText("Are you sure you want to go back?")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
}
