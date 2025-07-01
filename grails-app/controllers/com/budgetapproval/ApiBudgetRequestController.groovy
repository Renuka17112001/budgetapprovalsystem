package com.budgetapproval

import grails.converters.JSON

class ApiBudgetRequestController {

    BudgetService budgetService

    def submit() {
        try {
            def body = request.JSON
            Long deptId = body.deptId as Long
            BigDecimal requestedAmount = body.requestedAmount as BigDecimal
            String purpose = body.purpose


            String requestedBy = body.requestedBy ?: (request.userPrincipal?.name ?: 'anonymous')

            def req = budgetService.submitRequest(deptId, requestedAmount, purpose, requestedBy)
            render status: 200, req as JSON

        } catch (IllegalArgumentException | IllegalStateException e) {
            render status: 400, contentType: "application/json", text: [error: e.message] as JSON
        } catch (Exception e) {
            render status: 500, contentType: "application/json", text: [error: "Unexpected error: ${e.message}"] as JSON
        }
    }


    def approve(Long id) {
        try {
            def req = budgetService.approve(id, request.userPrincipal?.name ?: 'manager')
            render status: 200, req as JSON
        } catch (IllegalArgumentException | IllegalStateException e) {
            render status: 400, [error: e.message] as JSON
        } catch (Exception e) {
            render status: 500, [error: "Unexpected error: ${e.message}"] as JSON
        }
    }

    def reject(Long id) {
        try {
            def json = request.JSON
            def reason = json?.reason ?: "No reason provided"
            def req = budgetService.reject(id, request.userPrincipal?.name ?: 'manager', reason)
            render status: 200, req as JSON
        } catch (IllegalArgumentException | IllegalStateException e) {
            render status: 400, [error: e.message] as JSON
        } catch (Exception e) {
            render status: 500, [error: "Unexpected error: ${e.message}"] as JSON
        }
    }

    def pending() {
        try {
            def list = BudgetRequest.findAllByStatus(RequestStatus.PENDING)
            if (!list || list.isEmpty()) {
                render status: 200, [message: "No pending budget requests"] as JSON
            } else {
                render status: 200, list as JSON
            }
        } catch (Exception e) {
            render status: 500, [error: "Failed to fetch pending requests: ${e.message}"] as JSON
        }
    }

}
