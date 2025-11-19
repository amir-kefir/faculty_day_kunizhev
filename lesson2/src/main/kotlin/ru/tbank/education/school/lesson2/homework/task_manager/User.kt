package ru.tbank.education.school.lesson2.homework.task_manager

data class User (
    private var id: String,
    val name: String,
    val email: String,
) {
    fun getId(): String{
        return this.id
    }

    fun changeId(newId: String?): Boolean{
        if (newId != null) this.id = newId
        return true
    }
}