package com.example.schedule.compose.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Cabinet
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Subject
import com.example.schedule.compose.entity.Teacher
import com.example.schedule.compose.repo.CabinetRepo
import com.example.schedule.compose.repo.FlowRepo
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.repo.ScheduleDBHelper
import com.example.schedule.compose.repo.SubjectRepo
import com.example.schedule.compose.repo.TeacherRepo
import com.example.schedule.compose.retrofit.RetrofitService
import com.example.schedule.compose.theme.ScheduleComposeTheme
import com.example.schedule.compose.theme.ThemeManager
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.view.model.activity.MainActivityViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var flowRepo: FlowRepo
    private lateinit var subjectRepo: SubjectRepo
    private lateinit var teacherRepo: TeacherRepo
    private lateinit var cabinetRepo: CabinetRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE)
        SettingsStorage.init(saves)
        ThemeManager.setTheme(SettingsStorage.getTheme(saves))

        val curFlow = SettingsStorage.getCurFlow(saves)

        val scheduleDBHelper = ScheduleDBHelper(this)
        flowRepo = FlowRepo(scheduleDBHelper)
        subjectRepo = SubjectRepo(scheduleDBHelper)
        teacherRepo = TeacherRepo(scheduleDBHelper)
        cabinetRepo = CabinetRepo(scheduleDBHelper)
        val homeworkRepo = HomeworkRepo(scheduleDBHelper, flowRepo, subjectRepo)

        homeworkRepo.deleteAllBeforeDate(LocalDate.now())

        if (SettingsStorage.useServer) {
            val retrofitService = RetrofitService.getInstance()
            retrofitService.getAllActiveFlows(::updateFlows)
            retrofitService.getAllSubjects(::updateSubjects)
            retrofitService.getAllTeachers(::updateTeachers)
            retrofitService.getAllCabinets(::updateCabinets)
        }

        viewModel = MainActivityViewModel(flowRepo, saves, this)
        viewModel.educationLevel = curFlow.educationLevel
        viewModel.course = curFlow.course
        viewModel.group = curFlow.group
        viewModel.subgroup = curFlow.subgroup
        viewModel.onContinue()

        setContent {
            ScheduleComposeTheme(
                theme = ThemeManager.theme.collectAsState().value
            ) {
                ScheduleApp(viewModel)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.update()
    }

    private fun updateFlows(flows: List<Flow>) {
        val curFlows = flowRepo.findAll()
        flows.filter { flow -> curFlows.any { it.educationLevel == flow.educationLevel && it.course == flow.course && it.group == flow.group && it.subgroup == flow.subgroup && (it.lessonsStartDate != flow.lessonsStartDate || it.sessionStartDate != flow.sessionStartDate || it.sessionEndDate != flow.sessionEndDate || it.active != flow.active) } }.forEach {
            flowRepo.update(it.educationLevel, it.course, it.group, it.subgroup, it.lessonsStartDate, it.sessionStartDate, it.sessionEndDate, it.active!!)
        }
        flows.filter { flow -> curFlows.none { it.educationLevel == flow.educationLevel && it.course == flow.course && it.group == flow.group && it.subgroup == flow.subgroup } }.forEach {
            flowRepo.add(
                educationLevel = it.educationLevel,
                course = it.course,
                group = it.group,
                subgroup = it.subgroup,
                lessonsStartDate = it.lessonsStartDate,
                sessionStartDate = it.sessionStartDate,
                sessionEndDate = it.sessionEndDate,
                active = it.active!!
            )
        }
        viewModel.update()
    }

    private fun updateSubjects(subjects: List<Subject>) {
        val curSubjects = subjectRepo.findAll()
        subjects.filter { subject -> curSubjects.none { it.subject == subject.subject } }.forEach {
            subjectRepo.add(it.subject)
        }
    }

    private fun updateTeachers(teachers: List<Teacher>) {
        val curTeachers = teacherRepo.findAll()
        teachers.filter { teacher -> curTeachers.none { it.surname == teacher.surname && it.name == teacher.name && it.patronymic == teacher.patronymic } }.forEach {
            teacherRepo.add(it.surname, it.name, it.patronymic)
        }
    }

    private fun updateCabinets(cabinets: List<Cabinet>) {
        val curCabinets = cabinetRepo.findAll()
        cabinets.filter { cabinet -> curCabinets.any { it.cabinet == cabinet.cabinet && it.building == cabinet.building && it.address != cabinet.address } }.forEach {
            cabinetRepo.update(it.cabinet, it.building, it.address)
        }
        cabinets.filter { cabinet -> curCabinets.none { it.cabinet == cabinet.cabinet && it.building == cabinet.building } }.forEach {
            cabinetRepo.add(it.cabinet, it.building, it.address)
        }
    }
}

private lateinit var buttonColors: ButtonColors
private lateinit var textFieldColors: TextFieldColors

@Composable
fun ScheduleApp(viewModel: MainActivityViewModel) {
    buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    )
    textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.tertiary,
        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
        cursorColor = MaterialTheme.colorScheme.tertiary,
        focusedLabelColor = MaterialTheme.colorScheme.secondary,
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        focusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
    )

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlowLvlButton(viewModel)
            Spacer(modifier = Modifier.height(32.dp))
            CourseButton(viewModel)
            Spacer(modifier = Modifier.height(32.dp))
            GroupButton(viewModel)
            Spacer(modifier = Modifier.height(32.dp))
            SubgroupButton(viewModel)
            Spacer(modifier = Modifier.height(64.dp))
            ContinueButton(viewModel)
        }
    }
}

@Composable
private fun FlowLvlButton(viewModel: MainActivityViewModel) {
    val flowLvlStr = listOf("Бакалавриат/Специалитет", "Магистратура", "Аспирантура")
    TextButton(
        onClick = {
            viewModel.educationLevel = (viewModel.educationLevel % 3) + 1
            viewModel.course = 0
            viewModel.group = 0
            viewModel.subgroup = 0
        },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors
    ) {
        Text(
            text = flowLvlStr[viewModel.educationLevel - 1],
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CourseButton(viewModel: MainActivityViewModel) {
    TextButton(
        onClick = { viewModel.chooseCourse() },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors
    ) {
        Text(
            text = if (viewModel.course == 0) stringResource(R.string.choose_course) else "${viewModel.course} курс",
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            textAlign = TextAlign.Center
        )
    }
    if (viewModel.showChooseCourseDialog) {
        ChooseDialog(
            title = stringResource(R.string.choose_course),
            items = viewModel.items,
            onDismissRequest = { viewModel.showChooseCourseDialog = false },
            onClick = viewModel::onCourseChosen,
            viewModel = viewModel
        )
    }
    if (viewModel.showNewCourseDialog) {
        NewFlowDialog(
            label = stringResource(R.string.new_course),
            onDismissRequest = { viewModel.showNewCourseDialog = false },
            onDone = viewModel::onCourseCreated,
            viewModel = viewModel
        )
    }
}

@Composable
private fun GroupButton(viewModel: MainActivityViewModel) {
    TextButton(
        onClick = viewModel::chooseGroup,
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors
    ) {
        Text(
            text = if (viewModel.group == 0) stringResource(R.string.choose_group) else "${viewModel.group} группа",
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            textAlign = TextAlign.Center
        )
    }
    if (viewModel.showChooseGroupDialog) {
        ChooseDialog(
            title = stringResource(R.string.choose_group),
            items = viewModel.items,
            onDismissRequest = { viewModel.showChooseGroupDialog = false },
            onClick = viewModel::onGroupChosen,
            viewModel = viewModel
        )
    }
    if (viewModel.showNewGroupDialog) {
        NewFlowDialog(
            label = stringResource(R.string.new_group),
            onDismissRequest = { viewModel.showNewGroupDialog = false },
            onDone = viewModel::onGroupCreated,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SubgroupButton(viewModel: MainActivityViewModel) {
    TextButton(
        onClick = viewModel::chooseSubgroup,
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors
    ) {
        Text(
            text = if (viewModel.subgroup == 0) stringResource(R.string.choose_subgroup) else "${viewModel.subgroup} подгруппа",
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            textAlign = TextAlign.Center
        )
    }
    if (viewModel.showChooseSubgroupDialog) {
        ChooseDialog(
            title = stringResource(R.string.choose_subgroup),
            items = viewModel.items,
            onDismissRequest = { viewModel.showChooseSubgroupDialog = false },
            onClick = viewModel::onSubgroupChosen,
            viewModel = viewModel
        )
    }
    if (viewModel.showNewSubgroupDialog) {
        NewFlowDialog(
            label = stringResource(R.string.new_group),
            onDismissRequest = { viewModel.showNewSubgroupDialog = false },
            onDone = viewModel::onSubgroupCreated,
            viewModel = viewModel
        )
    }
}

@Composable
private fun ContinueButton(viewModel: MainActivityViewModel) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = viewModel::onContinue,
        colors = buttonColors
    ) {
        Text(
            text = stringResource(R.string.next),
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChooseDialog(title: String, items: SnapshotStateList<String>, onDismissRequest: () -> Unit, onClick: (index: Int, item: String) -> Unit, viewModel: MainActivityViewModel) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Column {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize
                )
                LazyColumn {
                    itemsIndexed(items) { index, item ->
                        TextButton(
                            onClick = { onClick(index, item) },
                            colors = buttonColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = viewModel.textSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewFlowDialog(label: String, onDismissRequest: () -> Unit, onDone: (result: Int) -> Unit, viewModel: MainActivityViewModel) {
    val regex = Regex("^[1-9](\\d+)?\$")
    var text by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TextField(
                    value = text,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(regex)) {
                            text = it
                        }
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = viewModel.textSize
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = viewModel.textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                Row {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { if (text.isNotEmpty()) onDone(text.toInt()) },
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize
                        )
                    }
                }
            }
        }
    }
}
