package ru.tbank.education.school.lesson2.homework.task_manager

open class Task (
    val id: String,
    protected var user: User?,
    private var title: String,
    private var description: String,
    var priority: Int = 0,
    protected var status: StatusType,
    var failureReason: String,
    val parent: Project?,
) {
    // user - start
    open fun assignUser(user: User?): Boolean {
        if (user != null) {
            this.user = user
            return true
        } else return false
    }

    open fun unassignUser(): Boolean {
        this.user = null
        return true
    }

    open fun getAssignedUser(): User? {
        return this.user
    }
    // user - end

    // description/title - start
    open fun newDescription(text: String = "-"): Boolean {
        if (text.length > DescriptionLimit) {
            description = "${text.take(DescriptionLimit+1)}..."
        } else{
            description = text
        }
        return true
    }

    fun newTitle(text: String = "-"): Boolean {
        if (text.length > TitleLimit) {
            title = "${text.take(TitleLimit+1)}..."
        } else{
            title = text
        }
        return true
    }

    open fun getDescription(): String {
        return this.description
    }

    fun getTitle(): String {
        return this.title
    }
    // description/title - end

    open fun changeStatus(type: StatusType? = null): Boolean {
        if (type != null) {
            this.status = type
            parent?.updateStatus()
            return true
        } else return false
    }

    fun markAsFailed(reason: String = "???"): Boolean {
        this.changeStatus(StatusType.Failed)
        this.failureReason = reason
        return true
    }

    fun getStatus(): StatusType {
        return this.status
    }
}
