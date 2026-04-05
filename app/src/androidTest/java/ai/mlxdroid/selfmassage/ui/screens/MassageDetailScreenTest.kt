package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageDetailViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MassageDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun viewModel(zoneId: String = "neck", techniqueId: String) = MassageDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("zoneId" to zoneId, "techniqueId" to techniqueId)),
        getTechniqueDetail = GetTechniqueDetailUseCase(MassageRepository)
    )

    private fun setScreen(techniqueId: String, onBack: () -> Unit = {}) {
        val vm = viewModel(techniqueId = techniqueId)
        composeRule.setContent {
            SelfMassageTheme {
                MassageDetailScreen(
                    onBack = onBack,
                    viewModel = vm
                )
            }
        }
    }

    @Test
    fun techniqueName_displayedInTopBar() {
        setScreen("neck_suboccipital")
        composeRule.onNodeWithText("Suboccipital Release").assertIsDisplayed()
    }

    @Test
    fun summaryText_displayed() {
        setScreen("neck_suboccipital")
        composeRule.onNodeWithText("Releases tension at the base of the skull").assertIsDisplayed()
    }

    @Test
    fun stepsHeaderVisible() {
        setScreen("neck_suboccipital")
        composeRule.onNodeWithText("Steps").assertIsDisplayed()
    }

    @Test
    fun stepOrderBadge_stepOne_visible() {
        setScreen("neck_suboccipital")
        composeRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun durationChip_stepOne_30sVisible() {
        setScreen("neck_suboccipital")
        // Steps 1, 2 and 4 all have 30s duration → 3 matching chips
        composeRule.onAllNodesWithText("30s").assertCountEquals(3)
    }

    @Test
    fun backButton_invokesOnBack() {
        var backCalled = false
        setScreen("neck_suboccipital", onBack = { backCalled = true })
        composeRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backCalled)
    }

    @Test
    fun invalidTechniqueId_screenIsEmpty() {
        setScreen("nonexistent_id")
        composeRule.onNodeWithText("Steps").assertDoesNotExist()
        composeRule.onNodeWithText("Back").assertDoesNotExist()
    }
}
