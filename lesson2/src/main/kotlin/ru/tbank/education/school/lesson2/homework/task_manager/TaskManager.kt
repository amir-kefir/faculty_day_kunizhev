package ru.tbank.education.school.lesson2.homework.task_manager

class TaskManager {
    var userSeq = 1
    var taskSeq = 1
    var projectSeq = 1
    private val users: MutableList<User> = mutableListOf()
    private val tasks: MutableList<Task> = mutableListOf()
    private val projects: MutableList<Project> = mutableListOf()

    fun addUser(name: String, email: String) {
        val newUser = User(
            id = "U-${userSeq++}",
            name = name,
            email = email
        )
        users.add(newUser)
    }

    fun addTask(
        user: User?,
        title: String,
        description: String,
        priority: Int = 0,) {
            val newTask = Task(
                id = "T-${taskSeq++}",
                user = user,
                title = title,
                description = description,
                priority = priority,
            )
            tasks.add(newTask)
    }

    fun addProject(
        title: String,
        description: String,
        priority: Int = 0,) {
            val newProject = Project(
                id = "P-${projectSeq++}",
                title = title,
                description = description,
                priority = priority,
            )
            projects.add(newProject)
    }

    fun getUserById(id: String): User? = users.find { it.getId() == id }
    fun getTaskById(id: String): Task? = tasks.find { it.getId() == id }
    fun getProjectById(id: String): Project? = projects.find { it.getId() == id }

    fun assignTaskToUser(taskId: String, userId: String): Boolean {
        val task = getTaskById(taskId)
        val user = getUserById(userId)
        task?.assignUser(user)
        return true
    }

    fun addTaskToProject(taskId: String, projectId: String): Boolean {
        val task = getTaskById(taskId)
        val project = getProjectById(projectId)
        if (task != null && project != null) {
            task.parent = project
            return true
        }
        return false
    }

    fun deleteTask(taskToDelete: Task) {
        for (task in tasks.filter { it.getId() > taskToDelete.getId() }) {
            task.changeId("T-${task.getId().drop(2).toInt()-1}")
        }
        tasks.remove(taskToDelete)
        taskSeq--
    }

    fun deleteProject(projectToDelete: Project) {
        for (project in projects.filter { it.getId() > projectToDelete.getId() }) {
            project.changeId("P-${project.getId().drop(2).toInt()-1}")
        }
        projects.remove(projectToDelete)
        projectSeq--
    }

    fun deleteUser(userToDelete: User) {
        for (user in users.filter { it.getId() > userToDelete.getId() }) {
            user.changeId("U-${user.getId().drop(2).toInt()-1}")
        }
        users.remove(userToDelete)
        userSeq--
    }

    fun getAllUsers(): List<User> = users.toList()
    fun getAllTasks(): List<Task> = tasks.toList()
    fun getAllProjects(): List<Project> = projects.toList()
}