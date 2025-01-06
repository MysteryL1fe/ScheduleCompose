package com.example.schedule.compose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.theme.Theme
import com.example.schedule.compose.view.model.screen.SettingsScreenViewModel

private lateinit var buttonColors: ButtonColors
private lateinit var sliderColors: SliderColors

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
    )
    sliderColors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.secondary,
        activeTrackColor = MaterialTheme.colorScheme.primary,
        activeTickColor = MaterialTheme.colorScheme.primary,
        inactiveTrackColor = MaterialTheme.colorScheme.primary,
        inactiveTickColor = MaterialTheme.colorScheme.primary
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        TextButton(
            onClick = { viewModel.showChooseThemeDialog = true },
            modifier = Modifier.padding(25.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = stringResource(id = R.string.choose_theme),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(25.dp)
        ) {
            Text(
                text = stringResource(id = R.string.font_size),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Slider(
                value = viewModel.textSize.value,
                onValueChange = { viewModel.updateTextSize(it.sp) },
                valueRange = 8f..32f,
                steps = 5,
                colors = sliderColors
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(25.dp)
        ) {
            Text(
                text = stringResource(R.string.display_mode),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = { viewModel.updateDisplayModeFull(!viewModel.displayModeFull) },
                modifier = Modifier.padding(top = 25.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(25.dp),
                colors = buttonColors
            ) {
                Text(
                    text = stringResource(id = if (viewModel.displayModeFull) R.string.full_display_mode else R.string.compact_display_mode),
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

        TextButton(
            onClick = { viewModel.finishActivity() },
            modifier = Modifier.padding(25.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = stringResource(id = R.string.change_flow),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
        }

        TextButton(
            onClick = { viewModel.updateUseServer(!viewModel.useServer) },
            modifier = Modifier.padding(25.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = stringResource(id = if (viewModel.useServer) R.string.deactivate_server else R.string.activate_server),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
        }

        TextButton(
            onClick = { viewModel.clearDB() },
            modifier = Modifier.padding(25.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = stringResource(id = R.string.clear_db),
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center
            )
        }
    }

    if (viewModel.showChooseThemeDialog) {
        ShowChooseThemeDialog(viewModel)
    }
}

@Composable
private fun ShowChooseThemeDialog(
    viewModel: SettingsScreenViewModel
) {
    Dialog(
        onDismissRequest = { viewModel.showChooseThemeDialog = false }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.choose_theme),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize
                )
                TextButton(
                    onClick = { viewModel.updateTheme(Theme.SYSTEM) },
                    colors = buttonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.system_theme),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize
                    )
                }
                TextButton(
                    onClick = { viewModel.updateTheme(Theme.LIGHT) },
                    colors = buttonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.light_theme),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize
                    )
                }
                TextButton(
                    onClick = { viewModel.updateTheme(Theme.DARK) },
                    colors = buttonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dark_theme),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize
                    )
                }
            }
        }
    }
}
