package ru.khanin.dmitrii.schedule.compose.view.model.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.khanin.dmitrii.schedule.compose.activity.ScheduleActivity
import ru.khanin.dmitrii.schedule.compose.repo.FlowRepo
import ru.khanin.dmitrii.schedule.compose.utils.SettingsStorage

class MainActivityViewModel(
    private val flowRepo: FlowRepo,
    private val saves: SharedPreferences,
    private val context: Context
) : ViewModel() {
    var educationLevel by mutableIntStateOf(1)
    var course by mutableIntStateOf(0)
    var group by mutableIntStateOf(0)
    var subgroup by mutableIntStateOf(0)
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var items = mutableStateListOf<String>()
    var showChooseCourseDialog by mutableStateOf(false)
    var showNewCourseDialog by mutableStateOf(false)
    var showChooseGroupDialog by mutableStateOf(false)
    var showNewGroupDialog by mutableStateOf(false)
    var showChooseSubgroupDialog by mutableStateOf(false)
    var showNewSubgroupDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
    }

    fun chooseCourse() {
        val courses = flowRepo.findDistinctActiveCourseByEducationLevel(educationLevel)
        items.clear()
        courses.forEach { items.add(it.toString()) }
        items.add("...")
        showChooseCourseDialog = true
    }

    fun onCourseChosen(index: Int, item: String) {
        if (item == "...") {
            showNewCourseDialog = true
        } else {
            course = items[index].toInt()
            group = 0
            subgroup = 0
        }
        showChooseCourseDialog = false
    }

    fun onCourseCreated(course: Int) {
        this.course = course
        group = 0
        subgroup = 0
        showNewCourseDialog = false
    }

    fun chooseGroup() {
        if (course > 0) {
            val groups = flowRepo.findDistinctActiveGroupByEducationLevelAndCourse(educationLevel, course)
            items.clear()
            groups.forEach { items.add(it.toString()) }
            items.add("...")
            showChooseGroupDialog = true
        }
    }

    fun onGroupChosen(index: Int, item: String) {
        if (item == "...") {
            showNewGroupDialog = true
        } else {
            group = items[index].toInt()
            subgroup = 0
        }
        showChooseGroupDialog = false
    }

    fun onGroupCreated(group: Int) {
        this.group = group
        subgroup = 0
        showNewGroupDialog = false
    }

    fun chooseSubgroup() {
        if (group > 0) {
            val subgroups = flowRepo.findDistinctActiveSubgroupByEducationLevelAndCourseAndGroup(
                educationLevel,
                course,
                group
            )
            items.clear()
            subgroups.forEach { items.add(it.toString()) }
            items.add("...")
            showChooseSubgroupDialog = true
        }
    }

    fun onSubgroupChosen(index: Int, item: String) {
        if (item == "...") {
            showNewSubgroupDialog = true
        } else {
            subgroup = items[index].toInt()
        }
        showChooseSubgroupDialog = false
    }

    fun onSubgroupCreated(subgroup: Int) {
        this.subgroup = subgroup
        showNewSubgroupDialog = false
    }

    fun onContinue() {
        if (educationLevel in 1..3 && course in 1..5 && group > 0 && subgroup > 0) {
            val intent = Intent(context, ScheduleActivity::class.java)

            intent.putExtra("educationLevel", educationLevel)
            intent.putExtra("course", course)
            intent.putExtra("group", group)
            intent.putExtra("subgroup", subgroup)
            val flow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(
                educationLevel, course, group, subgroup
            )
            if (flow == null) flowRepo.add(educationLevel, course, group, subgroup)
            else if (!flow.active!!) flowRepo.update(
                educationLevel,
                course,
                group,
                subgroup,
                true
            )
            SettingsStorage.saveCurFlow(educationLevel, course, group, subgroup, saves)
            context.startActivity(intent)
        }
    }
}