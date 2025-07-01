package com.budgetapproval

class Department {
    String name
    BigDecimal currentBudget
    BigDecimal yearlyAllocation
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name unique: true
        currentBudget nullable: false, min: 0.0G
        yearlyAllocation nullable: false, min: 0.0G
    }
    static mapping = {
        version false
    }
}
