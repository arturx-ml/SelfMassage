package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.domain.GetTechniquesForZoneUseCase
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageListViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MassageListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun viewModel(zoneId: String) = MassageListViewModel(
        savedStateHandle = SavedStateHandle(mapOf("zoneId" to zoneId)),
        getTechniquesForZone = GetTechniquesForZoneUseCase(MassageRepository)
    )

    private fun setScreen(
        zoneId: String = "neck",
        onTechniqueClick: (String, String) -> Unit = { _, _ -> },
        onBack: () -> Unit = {}
    ) {
        composeRule.setContent {
            SelfMassageTheme {
                MassageListScreen(
                    onTechniqueClick = onTechniqueClick,
                    onBack = onBack,
                    viewModel = viewModel(zoneId)
                )
            }
        }
    }

    @Test
    fun zoneTitle_displayedInTopBar() {
        setScreen(zoneId = "neck")
        composeRule.onNodeWithText("Neck").assertIsDisplayed()
    }

    @Test
    fun allNeckTechniquesListed() {
        setScreen(zoneId = "neck")
        composeRule.onNodeWithText("Suboccipital Release").assertIsDisplayed()
        composeRule.onNodeWithText("Lateral Neck Stretch & Friction").assertIsDisplayed()
        composeRule.onNodeWithText("Upper Trapezius Kneading").assertIsDisplayed()
    }

    @Test
    fun durationChipVisible_forSuboccipitalRelease() {
        setScreen(zoneId = "neck")
        composeRule.onNodeWithText("3 min").assertIsDisplayed()
    }

    @Test
    fun clickingTechnique_invokesCallbackWithCorrectIds() {
        var capturedZoneId: String? = null
        var capturedTechniqueId: String? = null
        setScreen(
            zoneId = "neck",
            onTechniqueClick = { zoneId, techniqueId ->
                capturedZoneId = zoneId
                capturedTechniqueId = techniqueId
            }
        )
        composeRule.onNodeWithText("Suboccipital Release").performClick()
        assertEquals("neck", capturedZoneId)
        assertEquals("neck_suboccipital", capturedTechniqueId)
    }

    @Test
    fun backButton_invokesOnBack() {
        var backCalled = false
        setScreen(onBack = { backCalled = true })
        composeRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backCalled)
    }

    @Test
    fun unknownZoneId_showsEmptyList() {
        setScreen(zoneId = "invalid_zone")
        composeRule.onNodeWithText("Suboccipital Release").assertDoesNotExist()
        composeRule.onNodeWithText("Upper Trapezius Kneading").assertDoesNotExist()
    }
}
