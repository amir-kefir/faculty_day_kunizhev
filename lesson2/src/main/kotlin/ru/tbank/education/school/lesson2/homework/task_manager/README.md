# Task Manager System

Небольшая система управления задачами и проектами.
Проект демонстрирует работу с наследованием, иерархией объектов, статусами, приоритетами и прогрессом.

## Структура проекта

В системе есть два основных типа сущностей:

### Task
Обычная задача.  
Имеет:
- id
- заголовок
- описание
- приоритет
- статус (`ToDo`, `InProgress`, `Done`, `Failed`)
- причину провала
- родителя (проект)

### Simple Task
Упрощенная версия Task'a, у которого отсутствует описание и нет статуса `InProgress`.

### Project
Проект, который является задачей, но может включать в себя подзадачи.

Дополнительно:
- содержит список tasks
- может менять статусы дочерних задач
- рассчитывает прогресс

## Возможности проекта

### ✔ Управление статусами
- Задача может переходить между состояниями
- Проект синхронно обновляет статусы своих задач
- Есть автопересчёт статуса проекта по дочерним задачам

### ✔ Приоритеты
- Можно поднять задачу до максимального приоритета внутри проекта

### ✔ Прогресс
Расчёт:
- Done = 1.0
- InProgress = 0.5
- ToDo = 0.0  
Проект возвращает результат в процентах или долях.

### ✔ Работа с пользователями
- У задач может быть исполнитель
- Проект может получить список исполнителей всех своих подзадач

## ▶ Пример использования (main.kt)

```Kotlin
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
        user = User("Amir", "Kunizhev", "amir@example.com")
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