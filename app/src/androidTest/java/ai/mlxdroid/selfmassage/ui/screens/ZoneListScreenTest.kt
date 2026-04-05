package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.domain.GetZonesUseCase
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.ZoneListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ZoneListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun setScreen(onZoneClick: (BodyZone) -> Unit = {}) {
        composeRule.setContent {
            SelfMassageTheme {
                ZoneListScreen(
                    onZoneClick = onZoneClick,
                    viewModel = ZoneListViewModel(GetZonesUseCase(MassageRepository))
                )
            }
        }
    }

    @Test
    fun topAppBarTitle_selfMassageVisible() {
        setScreen()
        composeRule.onNodeWithText("Self Massage").assertIsDisplayed()
    }

    @Test
    fun allThreeZoneNamesDisplayed() {
        setScreen()
        composeRule.onNodeWithText("Neck").assertIsDisplayed()
        composeRule.onNodeWithText("Shoulders").assertIsDisplayed()
        composeRule.onNodeWithText("Arms").assertIsDisplayed()
    }

    @Test
    fun techniqueCountSubtitle_forNeck_shows3() {
        setScreen()
        // Neck and Shoulders both have 3 techniques → 2 nodes with this text
        composeRule.onAllNodesWithText("3 techniques").assertCountEquals(2)
    }

    @Test
    fun techniqueCountSubtitle_forArms_shows2() {
        setScreen()
        composeRule.onNodeWithText("2 techniques").assertIsDisplayed()
    }

    @Test
    fun clickingZone_invokesOnZoneClickWithCorrectZone() {
        var clickedZone: BodyZone? = null
        setScreen(onZoneClick = { clickedZone = it })
        composeRule.onNodeWithText("Neck").performClick()
        assertEquals("neck", clickedZone?.id)
    }
}
