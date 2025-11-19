package ru.tbank.education.school.lesson2.homework.task_manager

import ru.tbank.education.school.lesson2.homework.task_manager.Task

class SimpleTask (
    id: String,
    user: User?,
    title: String,
    description: String,
    priority: Int,
    status: StatusType,
    failureReason: String,
    parent: Project?,
) : Task (
    id,
    user,
    title,
    "",
    priority,
    status,
    failureReason,
    parent,
) {

    constructor(
        id: String,
        user: User?,
        title: String,
        priority: Int = 0,
    ) : this(
        id = id,
        user = user,
        title = title,
        description = "-",
        priority = priority,
        status = StatusType.ToDo,
        failureReason = "-",
        parent = null,
    )

    override fun newDescription(text: String): Boolean {
        println("Error: Simple tasks don't support descriptions")
        return false
    }

    override fun getDescription(): String {
        println("Warning: Simple tasks don't support descriptions. Returned blank string")
        return ""
    }

    override fun changeStatus(type: StatusType?): Boolean {
        if (type != null) {
            if (type != StatusType.InProgress) {
                this.status = type
                parent?.updateStatus()
                return true
            } else {
                println("Error: Simple tasks don't support \"InProgress\" status")
                return false
            }
        } else return false
    }

    //

    override fun calculateProgress(inPercents: Boolean?): Double {
        return when (this.getStatus()) {
            StatusType.Failed -> 0.0
            StatusType.ToDo -> 0.0
            StatusType.InProgress -> 0.0
            StatusType.Done -> if (inPercents == true) 100.0 else 1.0
        }
    }

    override fun getObjectType(): String {
        return "Simple Task"
    }
}