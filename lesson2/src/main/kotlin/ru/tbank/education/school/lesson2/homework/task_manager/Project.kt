package ru.tbank.education.school.lesson2.homework.task_manager

class Project (
    id: String,
    description: String,
    title: String,
    priority: Int,
    status: StatusType,
    failureReason: String,
    parent: Project?,
    var tasks: Array<Task>,
) : Task (
    id,
    User(" "," "," "), // (rus) Не знаю как по-другому убрать юзера. Он не нужен
    title,
    description,
    priority,
    status,
    failureReason,
    parent,
) {
    // user-start
    override fun assignUser(user: User?): Boolean {
        println("Error: Cannot assign user to Project")
        return false
    }

    override fun unassignUser(): Boolean {
        println("Error: Cannot unassign user to Project")
        return false
    }

    override fun getAssignedUser(): User? {
        println("Warning: Only first user was returned (if there is one). Use getAllAssignedUsers()")
        for (task in tasks) {
            val taskUser: User? = task.getAssignedUser()
            if (taskUser != null) {
                return taskUser
            }
        }
        return null
    }

    fun getAllAssignedUsers(): List<User> {
        val users = mutableListOf<User>()
        for (task in tasks) {
            val taskUser: User? = task.getAssignedUser()
            if (taskUser != null) {
                users.add(taskUser)
            }
        }
        return users
    }
    // user-end

    fun calculateProgress(inPercents: Boolean? = false): Double{
        var progress: Double = 0.0
        for (task in tasks) {
            progress += when (task.getStatus()) {
                StatusType.Done -> 1.0
                StatusType.InProgress -> .5
                StatusType.ToDo -> 0.0
                else -> 0.0
            }
        }
        return when (inPercents) {
            true -> progress/tasks.size
            false -> progress/tasks.size*100
            null -> progress/tasks.size*100
        }
    }
    fun changePriorityToHighest(task: Task? = null): Boolean{
        if (task != null) {
            task.priority = 1
            for (siblingTask in tasks.filter {it != task}) {
                if (siblingTask.priority != 0) {
                    task.priority += 1
                }
            }
            return true
        } else return false
    }

    override fun changeStatus(type: StatusType?): Boolean {
        super.changeStatus(type)
        when (type) { // (rus) Да, я повторил кучу циклов, сори, но это никак не влияет на скорость. :sob:
            StatusType.Done -> {
                for (task in tasks) {
                    task.changeStatus(StatusType.Done)
                }
            }
            StatusType.ToDo -> {
                for (task in tasks) {
                    task.changeStatus(StatusType.ToDo)
                }
            }
            StatusType.Failed -> {
                for (task in tasks) {
                    task.markAsFailed("Its parent was marked as failed")
                }
            }
            else -> return false
        }
        return true
    }

    fun updateStatus(): Boolean {
        var currentStatus: StatusType = StatusType.ToDo
        var completedTasks: Int = 0
        for (task in tasks) {
            when (task.getStatus()) {
                StatusType.ToDo -> break
                StatusType.Failed -> break
                StatusType.InProgress -> {
                    currentStatus = StatusType.InProgress
                    break
                }
                StatusType.Done -> {
                    currentStatus = StatusType.InProgress
                    ++completedTasks
                }
            }
        }
        if (completedTasks == tasks.size) currentStatus = StatusType.Done
        changeStatus(currentStatus)
        return true
    }
}