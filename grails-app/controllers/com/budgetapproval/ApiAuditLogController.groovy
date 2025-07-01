package com.budgetapproval

import grails.converters.JSON

class ApiAuditLogController {
    def index(String entityType) {
        def logs = AuditLog.findAllByEntityType(entityType?.toString() ?: 'BudgetRequest', [sort: 'timestamp', order: 'desc'])
        render logs as JSON
    }
}
