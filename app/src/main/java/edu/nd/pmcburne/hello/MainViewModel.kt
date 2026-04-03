package edu.nd.pmcburne.hello

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.data.AppDatabase
import edu.nd.pmcburne.hello.data.Placemark
import edu.nd.pmcburne.hello.data.PlacemarkApi
import edu.nd.pmcburne.hello.data.PlacemarkTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val tags: List<String> = emptyList(),
    val selectedTag: String = "core",
    val placemarks: List<Placemark> = emptyList(),
    val isLoading: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).placemarkDao()
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val response = PlacemarkApi.instance.getPlacemarks()
                val placemarks = response.mapNotNull { item ->
                    val center = item.visualCenter ?: return@mapNotNull null
                    Placemark(
                        id = item.id,
                        name = item.name,
                        description = item.description ?: "",
                        latitude = center.latitude,
                        longitude = center.longitude
                    )
                }
                dao.insertPlacemarks(placemarks)

                val tags = response.flatMap { item ->
                    item.tagList.map { tag -> PlacemarkTag(placemarkId = item.id, tag = tag) }
                }
                dao.insertTags(tags)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to fetch from API", e)
            }

            val allTags = dao.getAllTags()
            val defaultTag = if (allTags.contains("core")) "core" else allTags.firstOrNull() ?: ""
            val filtered = dao.getPlacemarksByTag(defaultTag)
            _uiState.value = MapUiState(
                tags = allTags,
                selectedTag = defaultTag,
                placemarks = filtered,
                isLoading = false
            )
        }
    }

    fun selectTag(tag: String) {
        viewModelScope.launch {
            val filtered = dao.getPlacemarksByTag(tag)
            _uiState.value = _uiState.value.copy(
                selectedTag = tag,
                placemarks = filtered
            )
        }
    }
}
