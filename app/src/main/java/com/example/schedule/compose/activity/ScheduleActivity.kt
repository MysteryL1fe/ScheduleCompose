package com.example.schedule.compose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.repo.CabinetRepo
import com.example.schedule.compose.repo.FlowRepo
import com.example.schedule.compose.repo.ScheduleDBHelper
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.SubjectRepo
import com.example.schedule.compose.repo.TeacherRepo
import com.example.schedule.compose.screen.ChangeScheduleScreen
import com.example.schedule.compose.screen.ScheduleScreen
import com.example.schedule.compose.screen.SettingsScreen
import com.example.schedule.compose.ui.theme.ScheduleComposeTheme
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.view.model.ThemeManager
import com.example.schedule.compose.view.model.activity.ScheduleActivityViewModel
import com.example.schedule.compose.view.model.screen.ChangeScheduleScreenViewModel
import com.example.schedule.compose.view.model.screen.ScheduleScreenViewModel
import com.example.schedule.compose.view.model.screen.SettingsScreenViewModel
import kotlinx.coroutines.launch

class ScheduleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val educationLevel = intent.getIntExtra("educationLevel", 1)
        val course = intent.getIntExtra("course", 1)
        val group = intent.getIntExtra("group", 1)
        val subgroup = intent.getIntExtra("subgroup", 1)

        val scheduleDBHelper = ScheduleDBHelper(this)
        val flowRepo = FlowRepo(scheduleDBHelper)
        val subjectRepo = SubjectRepo(scheduleDBHelper)
        val teacherRepo = TeacherRepo(scheduleDBHelper)
        val cabinetRepo = CabinetRepo(scheduleDBHelper)
        val scheduleRepo = ScheduleRepo(scheduleDBHelper, flowRepo, subjectRepo, teacherRepo, cabinetRepo)

        val saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE)

        val scheduleActivityViewModel = ScheduleActivityViewModel(course, group, subgroup)
        val scheduleScreenViewModel = ScheduleScreenViewModel(scheduleRepo, educationLevel, course, group, subgroup)
        val changeScheduleScreenViewModel = ChangeScheduleScreenViewModel(subjectRepo, teacherRepo, cabinetRepo, scheduleRepo, educationLevel, course, group, subgroup)
        val settingsScreenViewModel = SettingsScreenViewModel(scheduleDBHelper, scheduleActivityViewModel, saves, this)

        setContent {
            ScheduleComposeTheme(
                theme = ThemeManager.theme.collectAsState().value
            ) {
                ScheduleApp(
                    scheduleActivityViewModel,
                    scheduleScreenViewModel,
                    changeScheduleScreenViewModel,
                    settingsScreenViewModel
                )
            }
        }
    }
}

enum class MenuItem {
    SCHEDULE,
    CHANGE_SCHEDULE,
    TEMP_SCHEDULE,
    HOMEWORK,
    FIND_TEACHER,
    SETTINGS
}

private lateinit var navigationDrawerItemColors: NavigationDrawerItemColors
@OptIn(ExperimentalMaterial3Api::class)
private lateinit var topAppBarColors: TopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleApp(
    viewModel: ScheduleActivityViewModel,
    scheduleScreenViewModel: ScheduleScreenViewModel,
    changeScheduleScreenViewModel: ChangeScheduleScreenViewModel,
    settingsScreenViewModel: SettingsScreenViewModel
) {
    navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = MaterialTheme.colorScheme.secondary,
        unselectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedIconColor = MaterialTheme.colorScheme.tertiary,
        unselectedIconColor = MaterialTheme.colorScheme.tertiary,
        selectedTextColor = MaterialTheme.colorScheme.tertiary,
        unselectedTextColor = MaterialTheme.colorScheme.tertiary
    )
    topAppBarColors = TopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        scrolledContainerColor = MaterialTheme.colorScheme.primary,
        navigationIconContentColor = MaterialTheme.colorScheme.tertiary,
        titleContentColor = MaterialTheme.colorScheme.tertiary,
        actionIconContentColor = MaterialTheme.colorScheme.tertiary
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ScheduleDrawerContent(viewModel) }
    ) {
        Scaffold(
            topBar = {
                ScheduleTopBar(
                    viewModel = viewModel,
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) { innerPadding ->
            when (viewModel.activeMenuItem) {
                MenuItem.SCHEDULE -> {
                    scheduleScreenViewModel.update()
                    ScheduleScreen(
                        viewModel = scheduleScreenViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MenuItem.CHANGE_SCHEDULE -> {
                    changeScheduleScreenViewModel.update()
                    ChangeScheduleScreen(
                        viewModel = changeScheduleScreenViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MenuItem.SETTINGS -> {
                    SettingsScreen(
                        viewModel = settingsScreenViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                else -> Text(
                    text = "Not implemented yet",
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ScheduleDrawerContent(viewModel: ScheduleActivityViewModel) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background
    ) {
        DrawerItem(
            label = stringResource(R.string.schedule),
            menuItem = MenuItem.SCHEDULE,
            painter = painterResource(R.drawable.ic_schedule),
            viewModel = viewModel
        )
        DrawerItem(
            label = stringResource(R.string.change_schedule),
            menuItem = MenuItem.CHANGE_SCHEDULE,
            painter = painterResource(R.drawable.ic_rename),
            viewModel = viewModel
        )
        DrawerItem(
            label = stringResource(R.string.temp_schedule),
            menuItem = MenuItem.TEMP_SCHEDULE,
            painter = painterResource(R.drawable.ic_temporary),
            viewModel = viewModel
        )
        DrawerItem(
            label = stringResource(R.string.homework),
            menuItem = MenuItem.HOMEWORK,
            painter = painterResource(R.drawable.ic_homework),
            viewModel = viewModel
        )
        DrawerItem(
            label = stringResource(R.string.find_teacher),
            menuItem = MenuItem.FIND_TEACHER,
            painter = painterResource(R.drawable.ic_search),
            viewModel = viewModel
        )
        DrawerItem(
            label = stringResource(R.string.settings),
            menuItem = MenuItem.SETTINGS,
            painter = painterResource(R.drawable.ic_settings),
            viewModel = viewModel
        )
    }
}

@Composable
private fun DrawerItem(label: String, menuItem: MenuItem, painter: Painter, viewModel: ScheduleActivityViewModel) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontSize = viewModel.textSize
            )
        },
        selected = viewModel.activeMenuItem == menuItem,
        onClick = { viewModel.activeMenuItem = menuItem },
        icon = {
            Icon(
                painter = painter,
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
        },
        colors = navigationDrawerItemColors,
        modifier = Modifier.padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTopBar(onClick: () -> Unit, viewModel: ScheduleActivityViewModel) {
    TopAppBar(
        title = {
            Text(
                text = "Группа ${viewModel.course}.${viewModel.group}.${viewModel.subgroup}",
                fontSize = viewModel.textSize,
                modifier = Modifier.padding(8.dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        colors = topAppBarColors
    )
}