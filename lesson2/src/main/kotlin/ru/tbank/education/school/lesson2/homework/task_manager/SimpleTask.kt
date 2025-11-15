package ru.tbank.education.school.lesson2.homework.task_manager

class SimpleTask (
    id: String,
    user: User,
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
}