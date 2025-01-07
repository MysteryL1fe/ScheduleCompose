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
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.TempSchedule
import com.example.schedule.compose.repo.CabinetRepo
import com.example.schedule.compose.repo.FlowRepo
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.repo.ScheduleDBHelper
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.SubjectRepo
import com.example.schedule.compose.repo.TeacherRepo
import com.example.schedule.compose.repo.TempScheduleRepo
import com.example.schedule.compose.retrofit.RetrofitService
import com.example.schedule.compose.screen.ChangeScheduleScreen
import com.example.schedule.compose.screen.FindTeacherScreen
import com.example.schedule.compose.screen.HomeworkScreen
import com.example.schedule.compose.screen.ScheduleScreen
import com.example.schedule.compose.screen.SettingsScreen
import com.example.schedule.compose.screen.TempScheduleScreen
import com.example.schedule.compose.theme.ScheduleComposeTheme
import com.example.schedule.compose.theme.ThemeManager
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.view.model.activity.ScheduleActivityViewModel
import com.example.schedule.compose.view.model.screen.ChangeScheduleScreenViewModel
import com.example.schedule.compose.view.model.screen.FindTeacherScreenViewModel
import com.example.schedule.compose.view.model.screen.HomeworkScreenViewModel
import com.example.schedule.compose.view.model.screen.ScheduleScreenViewModel
import com.example.schedule.compose.view.model.screen.SettingsScreenViewModel
import com.example.schedule.compose.view.model.screen.TempScheduleScreenViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ScheduleActivity : ComponentActivity() {
    private var educationLevel = 1
    private var course = 1
    private var group = 1
    private var subgroup = 1
    private lateinit var flowRepo: FlowRepo
    private lateinit var scheduleRepo: ScheduleRepo
    private lateinit var tempScheduleRepo: TempScheduleRepo
    private lateinit var homeworkRepo: HomeworkRepo
    private lateinit var retrofitService: RetrofitService
    private lateinit var scheduleScreenViewModel: ScheduleScreenViewModel
    private lateinit var changeScheduleScreenViewModel: ChangeScheduleScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        educationLevel = intent.getIntExtra("educationLevel", 1)
        course = intent.getIntExtra("course", 1)
        group = intent.getIntExtra("group", 1)
        subgroup = intent.getIntExtra("subgroup", 1)

        val scheduleDBHelper = ScheduleDBHelper(this)
        flowRepo = FlowRepo(scheduleDBHelper)
        val subjectRepo = SubjectRepo(scheduleDBHelper)
        val teacherRepo = TeacherRepo(scheduleDBHelper)
        val cabinetRepo = CabinetRepo(scheduleDBHelper)
        scheduleRepo = ScheduleRepo(scheduleDBHelper, flowRepo, subjectRepo, teacherRepo, cabinetRepo)
        tempScheduleRepo = TempScheduleRepo(scheduleDBHelper, flowRepo, subjectRepo, teacherRepo, cabinetRepo)
        homeworkRepo = HomeworkRepo(scheduleDBHelper, flowRepo, subjectRepo)

        val saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, MODE_PRIVATE)

        if (SettingsStorage.useServer) {
            retrofitService = RetrofitService.getInstance()
            retrofitService.getFlow(educationLevel, course, group, subgroup, ::updateFlow)
            retrofitService.getAllTempSchedulesByFlow(educationLevel, course, group, subgroup, ::updateTempSchedules)
            retrofitService.getAllHomeworksByFlow(educationLevel, course, group, subgroup, ::updateHomeworks)
        }

        val scheduleActivityViewModel = ScheduleActivityViewModel(course, group, subgroup)
        scheduleScreenViewModel = ScheduleScreenViewModel(scheduleRepo, tempScheduleRepo, homeworkRepo, educationLevel, course, group, subgroup)
        changeScheduleScreenViewModel = ChangeScheduleScreenViewModel(subjectRepo, teacherRepo, cabinetRepo, scheduleRepo, educationLevel, course, group, subgroup)
        val tempScheduleScreenViewModel = TempScheduleScreenViewModel(subjectRepo, teacherRepo, cabinetRepo, scheduleRepo, tempScheduleRepo, educationLevel, course, group, subgroup)
        val homeworkScreenViewModel = HomeworkScreenViewModel(subjectRepo, scheduleRepo, tempScheduleRepo, homeworkRepo, educationLevel, course, group, subgroup)
        val findTeacherScreenViewModel = FindTeacherScreenViewModel(teacherRepo)
        val settingsScreenViewModel = SettingsScreenViewModel(scheduleDBHelper, scheduleActivityViewModel, saves, this)

        setContent {
            ScheduleComposeTheme(
                theme = ThemeManager.theme.collectAsState().value
            ) {
                ScheduleApp(
                    scheduleActivityViewModel,
                    scheduleScreenViewModel,
                    changeScheduleScreenViewModel,
                    tempScheduleScreenViewModel,
                    homeworkScreenViewModel,
                    findTeacherScreenViewModel,
                    settingsScreenViewModel
                )
            }
        }
    }

    private fun updateFlow(flow: Flow) {
        if (flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(flow.educationLevel, flow.course, flow.group, flow.subgroup)?.lastEdit?.isBefore(flow.lastEdit) != false) {
            retrofitService.getAllSchedulesByFlow(flow.educationLevel, flow.course, flow.group, flow.subgroup, ::updateSchedules)
        }
    }

    private fun updateSchedules(schedules: List<Schedule>) {
        if (schedules.isEmpty()) return
        val educationLevel = schedules[0].flow.educationLevel
        val course = schedules[0].flow.course
        val group = schedules[0].flow.group
        val subgroup = schedules[0].flow.subgroup
        val curSchedules = scheduleRepo.findAllByFlow(educationLevel, course, group, subgroup)
        curSchedules.filter { schedule -> schedules.none { it.dayOfWeek == schedule.dayOfWeek && it.lessonNum == schedule.lessonNum && it.numerator == schedule.numerator } }.forEach {
            scheduleRepo.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, it.dayOfWeek, it.lessonNum, it.numerator)
        }
        schedules.filter { schedule -> curSchedules.any { it.dayOfWeek == schedule.dayOfWeek && it.lessonNum == schedule.lessonNum && it.numerator == schedule.numerator && (it.subject.subject != schedule.subject.subject || it.teacher?.surname != schedule.teacher?.surname || it.teacher?.name != schedule.teacher?.name || it.teacher?.patronymic != schedule.teacher?.patronymic || it.cabinet?.cabinet != schedule.cabinet?.cabinet || it.cabinet?.building != schedule.cabinet?.building) } }.forEach {
            scheduleRepo.update(educationLevel, course, group, subgroup, it.dayOfWeek, it.lessonNum, it.numerator, it.subject.subject, it.teacher?.surname, it.teacher?.name, it.teacher?.patronymic, it.cabinet?.cabinet, it.cabinet?.building)
        }
        schedules.filter { schedule -> curSchedules.none { it.dayOfWeek == schedule.dayOfWeek && it.lessonNum == schedule.lessonNum && it.numerator == schedule.numerator } }.forEach {
            scheduleRepo.add(educationLevel, course, group, subgroup, it.dayOfWeek, it.lessonNum, it.numerator, it.subject.subject, it.teacher?.surname, it.teacher?.name, it.teacher?.patronymic, it.cabinet?.cabinet, it.cabinet?.building)
        }
        flowRepo.update(educationLevel, course, group, subgroup, LocalDateTime.now())
        scheduleScreenViewModel.update()
        changeScheduleScreenViewModel.update()
    }

    private fun updateTempSchedules(tempSchedules: List<TempSchedule>) {
        tempSchedules.forEach { tempScheduleRepo.addOrUpdate(it.flow.educationLevel, it.flow.course, it.flow.group, it.flow.subgroup, it.lessonDate, it.lessonNum, it.willLessonBe, it.subject?.subject, it.teacher?.surname, it.teacher?.name, it.teacher?.patronymic, it.cabinet?.cabinet, it.cabinet?.building) }
    }

    private fun updateHomeworks(homeworks: List<Homework>) {
        homeworks.forEach { homeworkRepo.addOrUpdate(it.flow.educationLevel, it.flow.course, it.flow.group, it.flow.subgroup, it.homework, it.lessonDate, it.lessonNum, it.subject.subject) }
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
    tempScheduleScreenViewModel: TempScheduleScreenViewModel,
    homeworkScreenViewModel: HomeworkScreenViewModel,
    findTeacherScreenViewModel: FindTeacherScreenViewModel,
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
                MenuItem.TEMP_SCHEDULE -> {
                    tempScheduleScreenViewModel.update()
                    TempScheduleScreen(
                        viewModel = tempScheduleScreenViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MenuItem.HOMEWORK -> {
                    homeworkScreenViewModel.update()
                    HomeworkScreen(
                        viewModel = homeworkScreenViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MenuItem.FIND_TEACHER -> {
                    findTeacherScreenViewModel.update()
                    FindTeacherScreen(
                        viewModel = findTeacherScreenViewModel,
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
                    fontSize = viewModel.textSize,
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
