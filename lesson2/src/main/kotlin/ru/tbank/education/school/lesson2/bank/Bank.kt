package ru.tbank.education.school.lesson2.bank

class Bank {
    var accountSeq = 1
    var clientSeq = 1
    private val clients: MutableList<Client> = mutableListOf()
    private val accounts: MutableList<Account> = mutableListOf()

    fun addClient(clientFullName: String) {
        val newClient = Client(
            id = "C-${clientSeq}",
            fullName = clientFullName
        )
        clients.add(newClient)
    }

    fun addAccount(clientID: String) {
        val newAccount = Account(
            id = "A-${accountSeq}",
            balance = 0.0,
            customerID = clientID
        )
        accountSeq++
        accounts.add(newAccount)
    }

    fun transfer(fromAccountID: String, toAccountID: String, amount: Double){
        val fromAccount = accounts.find { it.id == fromAccountID }!!
        val toAccount = accounts.find { it.id == toAccountID }!!

        val hasEnoughMoney = fromAccount.withdraw(amount)

        if (hasEnoughMoney) {
            toAccount.deposit(amount)
        }
    }

}