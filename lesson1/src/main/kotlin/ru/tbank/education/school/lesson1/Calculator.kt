package ru.tbank.education.school.lesson1

/**
 * Метод для вычисления простых арифметических операций.
 */
fun calculate(a: Double, b: Double, operation: OperationType): Double? {
    return when (operation) {
        OperationType.DIVIDE -> if (b != 0.0) a / b else {
            println("Cannot divide by zero!")
            null }
        OperationType.MULTIPLY -> a*b
        OperationType.SUBTRACT -> a-b
        OperationType.ADD -> a+b
    }
}

/**
 * Функция вычисления выражения, представленного строкой
 * @return результат вычисления строки или null, если вычисление невозможно
 * @sample "5 * 2".calculate()
 */

@Suppress("ReturnCount")
fun String.calculate(): Double? {
    for (i in 0..this.length - 1) {
        if (this[i] == '+' || this[i] == '-' || this[i] == '*' || this[i] == '/') {
            return this.substring(0 until i).trim().toDoubleOrNull()?.let { a ->
                this.substring(i + 1).trim().toDoubleOrNull()?.let { b ->
                    when (this[i]) {
                        '+' -> a + b
                        '-' -> a - b
                        '*' -> a * b
                        '/' -> if (b != 0.0) a / b else {
                            println("Cannot divide by zero!")
                            null
                        }
                        else -> null
                    }
                }
            }
        }
    }
    return null
}

//fun main(){
//    println("5 * 2".calculate())
//    println(calculate(10.0,10.10,OperationType.ADD))
//}