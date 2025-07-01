package com.budgetapproval

class UrlMappings {

    static mappings = {

        // Budget Request Endpoints
        "/api/budget-request"(controller: 'apiBudgetRequest') {
            action = [POST: 'submit']
        }

        "/api/budget-request/pending"(controller: 'apiBudgetRequest', action: 'pending', method: 'GET')

        "/api/budget-request/$id/approve"(controller: 'apiBudgetRequest') {
            action = [PUT: 'approve']
        }

        "/api/budget-request/$id/reject"(controller: 'apiBudgetRequest') {
            action = [PUT: 'reject']
        }

        // Audit Logs Endpoint
        "/api/audit-logs"(controller: 'apiAuditLog') {
            action = [GET: 'index']
        }

        // Default Mappings
        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
