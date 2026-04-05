package ai.mlxdroid.selfmassage.navigation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import ai.mlxdroid.selfmassage.MainActivity
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun zoneListScreen_isStartDestination() {
        composeRule.onNodeWithText("Self Massage").assertIsDisplayed()
        composeRule.onNodeWithText("Neck").assertIsDisplayed()
        composeRule.onNodeWithText("Shoulders").assertIsDisplayed()
        composeRule.onNodeWithText("Arms").assertIsDisplayed()
    }

    @Test
    fun tapNeck_navigatesToMassageListWithNeckTitle() {
        composeRule.onNodeWithText("Neck").performClick()
        composeRule.onNodeWithText("Suboccipital Release").assertIsDisplayed()
        composeRule.onNodeWithText("Lateral Neck Stretch & Friction").assertIsDisplayed()
    }

    @Test
    fun tapTechnique_navigatesToDetailScreen() {
        composeRule.onNodeWithText("Neck").performClick()
        composeRule.onNodeWithText("Suboccipital Release").performClick()
        composeRule.onNodeWithText("Releases tension at the base of the skull").assertIsDisplayed()
        composeRule.onNodeWithText("Steps").assertIsDisplayed()
    }

    @Test
    fun backFromDetail_returnsToMassageList() {
        composeRule.onNodeWithText("Neck").performClick()
        composeRule.onNodeWithText("Suboccipital Release").performClick()
        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("Suboccipital Release").assertIsDisplayed()
        composeRule.onNodeWithText("Upper Trapezius Kneading").assertIsDisplayed()
    }

    @Test
    fun backFromMassageList_returnsToZoneList() {
        composeRule.onNodeWithText("Neck").performClick()
        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("Self Massage").assertIsDisplayed()
        composeRule.onNodeWithText("Shoulders").assertIsDisplayed()
    }

    @Test
    fun fullFlow_armsZone_webTechnique_stepsVisible() {
        composeRule.onNodeWithText("Arms").performClick()
        composeRule.onNodeWithText("Hand Web & Thumb Base Massage").performClick()
        composeRule.onNodeWithText("Steps").assertIsDisplayed()
        // Step 1 badge
        composeRule.onNodeWithText("1").assertIsDisplayed()
    }
}
