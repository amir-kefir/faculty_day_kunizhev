package ru.tbank.education.school.lesson2.homework.task_manager

abstract class BaseTask {
    abstract fun calculateProgress(inPercents: Boolean? = false): Double
    abstract fun getObjectType(): String
}

open class Task (
    private var id: String,
    protected var user: User?,
    private var title: String,
    private var description: String,
    var priority: Int = 0,
    protected var status: StatusType,
    var failureReason: String,
    var parent: Project?,
) : BaseTask() {

    constructor(
        id: String,
        user: User?,
        title: String,
        description: String,
        priority: Int = 0,
    ) : this(
        id = id,
        user = user,
        title = title,
        description = description,
        priority = priority,
        status = StatusType.ToDo,
        failureReason = "-",
        parent = null,
    )

    fun getId(): String{
        return this.id
    }

    fun changeId(newId: String?): Boolean{
        if (newId != null) this.id = newId
        return true
    }

    // user - start
    open fun assignUser(user: User?): Boolean {
        if (user != null && this.getCurrentStatus() != StatusType.Done) {
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

    open fun changeStatus(type: StatusType? = null, updateParent: Boolean = true): Boolean {
        if (type != null) {
            this.status = type
            if (updateParent) parent?.updateStatus()
            return true
        } else return false
    }

    fun markAsFailed(reason: String = "???"): Boolean {
        this.changeStatus(StatusType.Failed)
        this.failureReason = reason
        return true
    }

    fun getCurrentStatus(): StatusType {
        return this.status
    }

    fun getCurrentStatusInString(): String {
        return when (this.status) {
            StatusType.Failed -> "Failed :("
            StatusType.ToDo -> "To do"
            StatusType.InProgress -> "In progress"
            StatusType.Done -> "Done"
        }
    }

    //abstract

    override fun calculateProgress(inPercents: Boolean?): Double {
        return when (this.getCurrentStatus()) {
            StatusType.Failed -> 0.0
            StatusType.ToDo -> 0.0
            StatusType.InProgress -> if (inPercents == true) 50.0 else 0.5
            StatusType.Done -> if (inPercents == true) 100.0 else 1.0
        }
    }

    override fun getObjectType(): String {
        return "Default Task"
    }
}
