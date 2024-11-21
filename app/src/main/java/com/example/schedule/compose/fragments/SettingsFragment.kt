/*
package com.example.schedule.compose.fragments

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.schedule.compose.R
import com.example.schedule.compose.ScheduleDBHelper
import com.example.schedule.compose.SettingsStorage
import com.example.schedule.compose.activities.ScheduleActivity

@Composable
fun SettingsScreen(
    context: Context,
    updateTextSize: (Int) -> Unit
) {
    val textSize = remember { mutableStateOf(SettingsStorage.textSize) }
    val displayMode = remember { mutableStateOf(SettingsStorage.displayModeFull) }
    val useServer = remember { mutableStateOf(SettingsStorage.useServer) }

    // UI Components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Choose Theme Button
        Button(
            onClick = { showChooseThemeDialog(context) },
            modifier = Modifier.fillMaxWidth().padding(25.dp)
        ) {
            Text(text = stringResource(id = R.string.choose_theme), fontSize = 20.sp)
        }

        // Font Size Controls
        Text(
            text = stringResource(id = R.string.font_size),
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        )
        SeekBarComponent(value = textSize.value) {
            textSize.value = it
            SettingsStorage.saveTextSize(it, context.getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE))
            updateTextSize(it)
        }

        // Display Mode Toggle
        Text(
            text = stringResource(id = R.string.display_mode),
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        )
        Switch(
            checked = displayMode.value,
            onCheckedChange = { newValue ->
                displayMode.value = newValue
                SettingsStorage.saveDisplayMode(newValue, context.getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        // Server Toggle
        Switch(
            checked = useServer.value,
            onCheckedChange = { newValue ->
                useServer.value = newValue
                SettingsStorage.saveUseServer(newValue, context.getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        // Clear DB Button
        Button(
            onClick = {
                val dbHelper = ScheduleDBHelper(context)
                dbHelper.clearData()
                (context as ScheduleActivity).finish()
            },
            modifier = Modifier.fillMaxWidth().padding(25.dp)
        ) {
            Text(text = stringResource(id = R.string.clear_db), fontSize = 20.sp)
        }
    }
}

@Composable
fun SeekBarComponent(value: Int, onValueChanged: (Int) -> Unit) {
    SeekBar(
        value = value.toFloat(),
        onValueChange = { onValueChanged(it.toInt()) },
        valueRange = 0f..2f,
        steps = 2,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    )
}

fun showChooseThemeDialog(context: Context) {
    val items = arrayOf(
        context.getString(R.string.system_theme),
        context.getString(R.string.light_theme),
        context.getString(R.string.dark_theme)
    )
    MaterialAlertDialogBuilder(context)
        .setTitle(context.getString(R.string.choose_theme))
        .setItems(items) { dialog, which ->
            when (which) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        .show()
}

class SettingsFragment : Fragment() {
    private lateinit var updateTextSize: (Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the updateTextSize function
        updateTextSize = { textSize ->
            (activity as? ScheduleActivity)?.updateTextSize(textSize)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreen(context = requireContext(), updateTextSize = updateTextSize)
            }
        }
    }
}
*/
