package ai.mlxdroid.selfmassage.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.mlxdroid.selfmassage.data.model.SessionStep
import ai.mlxdroid.selfmassage.domain.GetSessionStepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PREP_SECONDS = 5

data class SessionUiState(
    val steps: List<SessionStep>,
    val currentIndex: Int,
    val secondsRemaining: Int,
    val prepSecondsRemaining: Int,
    val isPlaying: Boolean,
    val isFinished: Boolean
) {
    val isPreparing: Boolean get() = prepSecondsRemaining > 0
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getSessionSteps: GetSessionStepsUseCase
) : ViewModel() {

    private val steps: List<SessionStep> = run {
        val tId: String? = savedStateHandle["techniqueId"]
        val rId: String? = savedStateHandle["routineId"]
        when {
            tId != null -> getSessionSteps.forTechnique(tId)
            rId != null -> getSessionSteps.forRoutine(rId)
            else -> emptyList()
        }
    }

    private val _uiState = MutableStateFlow(
        SessionUiState(
            steps = steps,
            currentIndex = 0,
            secondsRemaining = steps.firstOrNull()?.durationSeconds ?: 0,
            prepSecondsRemaining = PREP_SECONDS,
            isPlaying = false,
            isFinished = false
        )
    )
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun play() {
        if (_uiState.value.isFinished) return
        _uiState.value = _uiState.value.copy(isPlaying = true)
        startTicking()
    }

    fun pause() {
        timerJob?.cancel()
        timerJob = null
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    fun skipNext() {
        val wasPlaying = _uiState.value.isPlaying
        timerJob?.cancel()
        timerJob = null
        advanceStep(wasPlaying)
    }

    fun skipPrevious() {
        val wasPlaying = _uiState.value.isPlaying
        timerJob?.cancel()
        timerJob = null
        val current = _uiState.value
        if (current.currentIndex > 0) {
            val newIndex = current.currentIndex - 1
            _uiState.value = current.copy(
                currentIndex = newIndex,
                secondsRemaining = steps[newIndex].durationSeconds,
                prepSecondsRemaining = PREP_SECONDS,
                isPlaying = wasPlaying
            )
            if (wasPlaying) startTicking()
        }
    }

    private fun startTicking() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val current = _uiState.value
                if (!current.isPlaying) break
                when {
                    current.prepSecondsRemaining > 0 -> {
                        _uiState.value = current.copy(prepSecondsRemaining = current.prepSecondsRemaining - 1)
                    }
                    current.secondsRemaining > 1 -> {
                        _uiState.value = current.copy(secondsRemaining = current.secondsRemaining - 1)
                    }
                    else -> {
                        advanceStep(wasPlaying = true)
                        break
                    }
                }
            }
        }
    }

    private fun advanceStep(wasPlaying: Boolean) {
        val current = _uiState.value
        val nextIndex = current.currentIndex + 1
        if (nextIndex >= steps.size) {
            _uiState.value = current.copy(isPlaying = false, isFinished = true)
        } else {
            _uiState.value = current.copy(
                currentIndex = nextIndex,
                secondsRemaining = steps[nextIndex].durationSeconds,
                prepSecondsRemaining = PREP_SECONDS,
                isPlaying = wasPlaying
            )
            if (wasPlaying) startTicking()
        }
    }
}
