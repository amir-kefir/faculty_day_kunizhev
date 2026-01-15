package ru.tbank.education.school.lesson2.homework.task_manager

fun main() {

    val manager = TaskManager()

    // Create a project
    val project = manager.addProject(
        title = "Educational Project",
        description = "Example task structure",
        priority = 1
    )

    // Create tasks
    val task = manager.addTask(
        title = "Create basic structure",
        description = "Create Task and Project classes",
        priority = 1,
        user = User("Amir", "Kunizhev", "amir@example.com"),
    )

    val simpleTask = manager.addSimpleTask(
        title = "Implement status methods",
        priority = 2,
        user = User("Bob", "Ivanov", "bob@example.com"),
    )

    // Add tasks to project
    manager.addTaskToProject(task.getId(), project.getId())
    manager.addTaskToProject(simpleTask.getId(), project.getId())

    println("=== Project Start ===")
    println("Progress: ${project.calculateProgress(true)}%")

    // Change status of one task
    task.changeStatus(StatusType.InProgress)
    project.updateStatus()

    println("\nAfter updating t1 status:")
    println("Project status: ${project.getCurrentStatusInString()}")
    println("Progress: ${project.calculateProgress(true)}%")

    // Complete both tasks
    task.changeStatus(StatusType.Done)
    simpleTask.changeStatus(StatusType.Done)
    project.updateStatus()

    println("\nAfter completing all tasks:")
    println("Project status: ${project.getCurrentStatusInString()}")
    println("Progress: ${project.calculateProgress(true)}%")

    // Display all project users
    println("\nProject assignees: ${project.getAllAssignedUsers()}")
}