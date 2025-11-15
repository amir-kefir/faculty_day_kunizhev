package ru.tbank.education.school.lesson2.homework.task_manager

sealed class StatusType {
    object Failed: StatusType()
    object ToDo: StatusType()
    object InProgress: StatusType()
    object Done: StatusType()
}