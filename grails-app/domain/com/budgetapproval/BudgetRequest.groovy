package com.budgetapproval

import com.budgetapproval.Department

enum RequestStatus {
    PENDING, APPROVED, REJECTED
}

class BudgetRequest {
    BigDecimal requestedAmount
    String purpose
    RequestStatus status = RequestStatus.PENDING
    String requestedBy
    String approvedBy
    Date dateCreated
    Date lastUpdated

    static belongsTo = [department: Department]

    static constraints = {
        requestedAmount nullable: false, min: 0.01G
        purpose blank: false
        requestedBy blank: false
        approvedBy nullable: true
        status nullable: false
    }
    static mapping = {
        version false
    }
}
