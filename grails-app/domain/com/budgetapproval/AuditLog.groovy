package com.budgetapproval

class AuditLog {
    String action
    Long entityId
    String entityType
    String oldValue
    String newValue
    String changedBy
    Date timestamp = new Date()

    static constraints = {
        action blank: false
        entityType inList: ['BudgetRequest', 'Department']
    }
    static mapping = {
        version false
    }
}