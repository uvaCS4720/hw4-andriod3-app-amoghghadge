package edu.nd.pmcburne.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.text.font.FontWeight
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MapScreen(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TagDropdown(
            tags = uiState.tags,
            selectedTag = uiState.selectedTag,
            onTagSelected = { viewModel.selectTag(it) }
        )
        MapView(placemarks = uiState.placemarks, modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagDropdown(
    tags: List<String>,
    selectedTag: String,
    onTagSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = selectedTag,
            onValueChange = {},
            readOnly = true,
            label = { Text("Filter by Tag") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag) },
                    onClick = {
                        onTagSelected(tag)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MapView(placemarks: List<edu.nd.pmcburne.hello.data.Placemark>, modifier: Modifier = Modifier) {
    val uvaCenter = LatLng(38.0336, -78.5080)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        placemarks.forEach { placemark ->
            MarkerInfoWindowContent(
                state = MarkerState(position = LatLng(placemark.latitude, placemark.longitude)),
                title = placemark.name
            ) {
                Column(modifier = Modifier.widthIn(max = 250.dp).padding(8.dp)) {
                    Text(text = placemark.name, fontWeight = FontWeight.Bold)
                    Text(text = placemark.description)
                }
            }
        }
    }
}
