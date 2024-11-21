package com.example.schedule.compose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.R
import com.example.schedule.compose.ScheduleDBHelper
import com.example.schedule.compose.screens.ChangeScheduleScreen
import com.example.schedule.compose.screens.ScheduleScreen
import com.example.schedule.compose.repo.FlowRepo
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.view.model.ChangeScheduleViewModel
import com.example.schedule.compose.view.model.ScheduleViewModel
import com.example.schedule.compose.view.model.factory.ChangeScheduleViewModelFactory
import com.example.schedule.compose.view.model.factory.ScheduleViewModelFactory
import kotlinx.coroutines.launch

class ScheduleActivity : ComponentActivity() {
    private var flowLvl: Int = 0
    private var course: Int = 1
    private var group: Int = 1
    private var subgroup: Int = 1
    private var activeMenuItem by mutableStateOf(MenuItem.SCHEDULE)
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var changeScheduleViewModel: ChangeScheduleViewModel
    private val textSize = 18.sp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flowLvl = intent.getIntExtra("flowLvl", 0)
        course = intent.getIntExtra("course", 1)
        group = intent.getIntExtra("group", 1)
        subgroup = intent.getIntExtra("subgroup", 1)

        val scheduleDBHelper = ScheduleDBHelper(this)
        val flowRepo = FlowRepo(scheduleDBHelper)
        val lessonRepo = LessonRepo(scheduleDBHelper)
        val scheduleRepo = ScheduleRepo(scheduleDBHelper, flowRepo, lessonRepo)

        val scheduleViewModelFactory = ScheduleViewModelFactory(
            lessonRepo, scheduleRepo, flowLvl, course, group, subgroup
        )
        scheduleViewModel = scheduleViewModelFactory.create(ScheduleViewModel::class.java)

        val changeScheduleViewModelFactory = ChangeScheduleViewModelFactory(
            lessonRepo, scheduleRepo, flowLvl, course, group, subgroup
        )
        changeScheduleViewModel = changeScheduleViewModelFactory.create(ChangeScheduleViewModel::class.java)

        setContent {
            ScheduleApp()
        }
    }

    @Composable
    private fun ScheduleApp() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { ScheduleDrawerContent() }
        ) {
            Scaffold(
                topBar = {
                    ScheduleTopBar {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                },
                containerColor = colorResource(R.color.background),
                contentColor = colorResource(R.color.primary)
            ) { innerPadding ->
                when (activeMenuItem) {
                    MenuItem.SCHEDULE -> {
                        scheduleViewModel.updateData()
                        ScheduleScreen(
                            viewModel = scheduleViewModel,
                            textSize = textSize,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    MenuItem.CHANGE_SCHEDULE -> {
                        changeScheduleViewModel.updateData()
                        ChangeScheduleScreen(
                            viewModel = changeScheduleViewModel,
                            textSize = textSize,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    else -> Text(
                        text = "Not implemented yet",
                        color = colorResource(R.color.text),
                        fontSize = textSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    @Composable
    private fun ScheduleDrawerContent() {
        ModalDrawerSheet(
            drawerContainerColor = colorResource(R.color.background)
        ) {
            DrawerItem(
                label = stringResource(R.string.schedule),
                menuItem = MenuItem.SCHEDULE,
                painter = painterResource(R.drawable.ic_schedule)
            )
            DrawerItem(
                label = stringResource(R.string.change_schedule),
                menuItem = MenuItem.CHANGE_SCHEDULE,
                painter = painterResource(R.drawable.ic_rename)
            )
            DrawerItem(
                label = stringResource(R.string.temp_schedule),
                menuItem = MenuItem.TEMP_SCHEDULE,
                painter = painterResource(R.drawable.ic_temporary)
            )
            DrawerItem(
                label = stringResource(R.string.homework),
                menuItem = MenuItem.HOMEWORK,
                painter = painterResource(R.drawable.ic_homework)
            )
            DrawerItem(
                label = stringResource(R.string.find_teacher),
                menuItem = MenuItem.FIND_TEACHER,
                painter = painterResource(R.drawable.ic_search)
            )
            DrawerItem(
                label = stringResource(R.string.settings),
                menuItem = MenuItem.SETTINGS,
                painter = painterResource(R.drawable.ic_settings)
            )
        }
    }

    @Composable
    private fun DrawerItem(label: String, menuItem: MenuItem, painter: Painter) {
        val navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = colorResource(R.color.secondary),
            unselectedContainerColor = colorResource(R.color.primary),
            selectedIconColor = colorResource(R.color.text),
            unselectedIconColor = colorResource(R.color.text),
            selectedTextColor = colorResource(R.color.text),
            unselectedTextColor = colorResource(R.color.text)
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = label,
                    fontSize = textSize
                )
            },
            selected = activeMenuItem == menuItem,
            onClick = { activeMenuItem = menuItem },
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
    private fun ScheduleTopBar(onClick: () -> Unit) {
        val topAppBarColors = TopAppBarColors(
            containerColor = colorResource(R.color.primary),
            scrolledContainerColor = colorResource(R.color.primary),
            navigationIconContentColor = colorResource(R.color.text),
            titleContentColor = colorResource(R.color.text),
            actionIconContentColor = colorResource(R.color.text)
        )

        TopAppBar(
            title = {
                Text(
                    text = "Группа $course.$group.$subgroup",
                    fontSize = textSize,
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

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        ScheduleApp()
    }

    private enum class MenuItem {
        SCHEDULE,
        CHANGE_SCHEDULE,
        TEMP_SCHEDULE,
        HOMEWORK,
        FIND_TEACHER,
        SETTINGS
    }
}
