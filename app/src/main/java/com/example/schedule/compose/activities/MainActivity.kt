package com.example.schedule.compose.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.SettingsStorage
import com.example.schedule.compose.repo.FlowRepo

class MainActivity : ComponentActivity() {
    private lateinit var saves: SharedPreferences
    private var flowLvl by mutableIntStateOf(1)
    private var course by mutableIntStateOf(0)
    private var group by mutableIntStateOf(0)
    private var subgroup by mutableIntStateOf(0)
    private lateinit var flowRepo: FlowRepo
    private lateinit var buttonColors: ButtonColors
    private lateinit var textFieldColors: TextFieldColors
    private val textSize = 18.sp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saves = getSharedPreferences(SettingsStorage.SCHEDULE_SAVES, Context.MODE_PRIVATE)
        /*SettingsStorage.updateDisplayMode(saves)
        SettingsStorage.updateUseServer(saves)
        SettingsStorage.updateTextSize(saves)*/

        flowRepo = FlowRepo(this)

        /*val curFlow = SettingsStorage.getCurFlow(saves)
        flowLvl = curFlow[0]
        course = curFlow[1]
        group = curFlow[2]
        subgroup = curFlow[3]*/

        setContent {
            ScheduleApp()
        }
    }

    @Composable
    fun ScheduleApp() {
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primary),
            contentColor = colorResource(R.color.secondary)
        )
        textFieldColors = TextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.text),
            unfocusedTextColor = colorResource(R.color.text),
            cursorColor = colorResource(R.color.text),
            focusedLabelColor = colorResource(R.color.secondary),
            unfocusedLabelColor = colorResource(R.color.secondary),
            focusedContainerColor = colorResource(R.color.primary),
            unfocusedContainerColor = colorResource(R.color.primary),
            focusedIndicatorColor = colorResource(R.color.secondary),
            unfocusedIndicatorColor = colorResource(R.color.secondary)
        )


        Scaffold (
            modifier = Modifier.fillMaxSize(),
            containerColor = colorResource(R.color.background),
            contentColor = colorResource(R.color.primary)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FlowLvlButton()
                Spacer(modifier = Modifier.height(32.dp))
                CourseButton()
                Spacer(modifier = Modifier.height(32.dp))
                GroupButton()
                Spacer(modifier = Modifier.height(32.dp))
                SubgroupButton()
                Spacer(modifier = Modifier.height(64.dp))
                ContinueButton()
            }
        }
    }

    @Composable
    fun FlowLvlButton() {
        val flowLvlStr = listOf("Бакалавриат/Специалитет", "Магистратура", "Аспирантура")
        TextButton(
            onClick = {
                flowLvl = (flowLvl % 3) + 1
                course = 0
                group = 0
                subgroup = 0
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text(
                text = flowLvlStr[flowLvl - 1],
                modifier = Modifier.padding(8.dp),
                color = colorResource(R.color.text),
                fontSize = textSize
            )
        }
    }

    @Composable
    fun CourseButton() {
        var showChooseCourseDialog by remember { mutableStateOf(false) }
        var showNewCourseDialog by remember { mutableStateOf(false) }
        val items = remember { mutableStateListOf<String>() }
        TextButton(
            onClick = {
                val courses = flowRepo.findDistinctActiveCourseByFlowLvl(flowLvl)
                items.clear()
                courses.forEach { items.add(it.toString()) }
                items.add("...")
                showChooseCourseDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text(
                text = if (course == 0) getString(R.string.choose_course) else "$course курс",
                modifier = Modifier.padding(8.dp),
                color = colorResource(R.color.text),
                fontSize = textSize
            )
        }
        if (showChooseCourseDialog) {
            ChooseDialog(
                title = stringResource(R.string.choose_course),
                items = items,
                onDismissRequest = { showChooseCourseDialog = false }
            ) { index, item ->
                if (item == "...") {
                    showNewCourseDialog = true
                } else {
                    course = items[index].toInt()
                    group = 0
                    subgroup = 0
                }
                showChooseCourseDialog = false
            }
        }
        if (showNewCourseDialog) {
            NewFlowDialog(
                label = stringResource(R.string.new_course),
                onDismissRequest = { showNewCourseDialog = false }
            ) { result ->
                course = result
                group = 0
                subgroup = 0
                showNewCourseDialog = false
            }
        }
    }

    @Composable
    fun GroupButton() {
        var showChooseGroupDialog by remember { mutableStateOf(false) }
        var showNewGroupDialog by remember { mutableStateOf(false) }
        val items = remember { mutableStateListOf<String>() }
        TextButton(
            onClick = {
                if (course > 0) {
                    val groups = flowRepo.findDistinctActiveFlowByFlowLvlAndCourse(flowLvl, course)
                    items.clear()
                    groups.forEach { items.add(it.toString()) }
                    items.add("...")
                    showChooseGroupDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text(
                text = if (group == 0) getString(R.string.choose_group) else "$group группа",
                modifier = Modifier.padding(8.dp),
                color = colorResource(R.color.text),
                fontSize = textSize
            )
        }
        if (showChooseGroupDialog) {
            ChooseDialog(
                title = stringResource(R.string.choose_group),
                items = items,
                onDismissRequest = { showChooseGroupDialog = false }
            ) { index, item ->
                if (item == "...") {
                    showNewGroupDialog = true
                } else {
                    group = items[index].toInt()
                    subgroup = 0
                }
                showChooseGroupDialog = false
            }
        }
        if (showNewGroupDialog) {
            NewFlowDialog(
                label = stringResource(R.string.new_group),
                onDismissRequest = { showNewGroupDialog = false }
            ) { result ->
                group = result
                subgroup = 0
                showNewGroupDialog = false
            }
        }
    }

    @Composable
    fun SubgroupButton() {
        var showChooseSubgroupDialog by remember { mutableStateOf(false) }
        var showNewSubgroupDialog by remember { mutableStateOf(false) }
        val items = remember { mutableStateListOf<String>() }
        TextButton(
            onClick = {
                if (group > 0) {
                    val subgroups = flowRepo.findDistinctActiveSubgroupByFlowLvlAndCourseAndFlow(
                        flowLvl,
                        course,
                        group
                    )
                    items.clear()
                    subgroups.forEach { items.add(it.toString()) }
                    items.add("...")
                    showChooseSubgroupDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text(
                text = if (subgroup == 0) getString(R.string.choose_subgroup) else "$subgroup подгруппа",
                modifier = Modifier.padding(8.dp),
                color = colorResource(R.color.text),
                fontSize = textSize
            )
        }
        if (showChooseSubgroupDialog) {
            ChooseDialog(
                title = stringResource(R.string.choose_subgroup),
                items = items,
                onDismissRequest = { showChooseSubgroupDialog = false }
            ) { index, item ->
                if (item == "...") {
                    showNewSubgroupDialog = true
                } else {
                    subgroup = items[index].toInt()
                }
                showChooseSubgroupDialog = false
            }
        }
        if (showNewSubgroupDialog) {
            NewFlowDialog(
                label = stringResource(R.string.new_group),
                onDismissRequest = { showNewSubgroupDialog = false }
            ) { result ->
                subgroup = result
                showNewSubgroupDialog = false
            }
        }
    }

    @Composable
    fun ContinueButton() {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (flowLvl in 1..3 && course in 1..5 && group > 0 && subgroup > 0) {
                    intent = Intent(this@MainActivity, ScheduleActivity::class.java)

                    intent.putExtra("flowLvl", flowLvl)
                    intent.putExtra("course", course)
                    intent.putExtra("group", group)
                    intent.putExtra("subgroup", subgroup)
                    val flow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                        flowLvl, course, group, subgroup
                    )
                    if (flow == null) flowRepo.add(flowLvl, course, group, subgroup)
                    else if (!flow.active) flowRepo.update(
                        flowLvl,
                        course,
                        group,
                        subgroup,
                        true
                    )
                    startActivity(intent)
                }
            },
            colors = buttonColors
        ) {
            Text(
                text = getString(R.string.next),
                modifier = Modifier.padding(8.dp),
                color = colorResource(R.color.text),
                fontSize = textSize
            )
        }
    }

    @Composable
    fun ChooseDialog(title: String, items: SnapshotStateList<String>, onDismissRequest: () -> Unit, onClick: (index: Int, item: String) -> Unit) {
        Dialog(
            onDismissRequest = { onDismissRequest() },
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.background),
                border = BorderStroke(1.dp, colorResource(R.color.secondary))
            ) {
                Column {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = colorResource(R.color.text),
                        fontSize = textSize
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
                                    color = colorResource(R.color.text),
                                    fontSize = textSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NewFlowDialog(label: String, onDismissRequest: () -> Unit, onDone: (result: Int) -> Unit) {
        val regex = Regex("^[1-9](\\d+)?\$")
        var text by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.background),
                border = BorderStroke(1.dp, colorResource(R.color.secondary)),
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
                                fontSize = textSize
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors,
                        textStyle = TextStyle(fontSize = textSize),
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
                                color = colorResource(R.color.text),
                                fontSize = textSize
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
                                color = colorResource(R.color.text),
                                fontSize = textSize
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ScheduleApp()
    }
}
