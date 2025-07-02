package com.budgetapproval

import grails.gorm.transactions.Transactional

import grails.plugins.mail.MailService

@Transactional
class BudgetService {


    MailService mailService
    //@Cacheable('deptBudget')
    Department getDepartment(Long deptId) {
        Department.get(deptId)
    }

    def submitRequest(Long deptId, BigDecimal amount, String purpose, String requester) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive")

        def cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -7)
        Date sevenDaysAgo = cal.time

        def recent = BudgetRequest.createCriteria().list {
            eq('purpose', purpose)
            eq('requestedBy', requester)
            ge('dateCreated', sevenDaysAgo)
        }

        if (recent) throw new IllegalStateException("Duplicate request for same purpose within 7 days")

        def dept = getDepartment(deptId)
        if (!dept) throw new IllegalArgumentException("Invalid department ID")

        def req = new BudgetRequest(
                department: dept,
                requestedAmount: amount,
                purpose: purpose,
                requestedBy: requester,
                status: RequestStatus.PENDING
        )
        req.save(flush: true, failOnError: true)
        req.refresh() 

        logAudit('CREATE', req.id, 'BudgetRequest', null, req.toString(), requester)

        return req
    }


    def approve(Long reqId, String manager) {
        def req = BudgetRequest.get(reqId)
        if (!req) {
            throw new IllegalArgumentException("Invalid BudgetRequest ID: ${reqId}")
        }

        if (req.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Request is not in PENDING state")
        }

        def dept = getDepartment(req.department.id)
        if (req.requestedAmount > dept.yearlyAllocation * 0.1) {
            throw new IllegalStateException("Exceeds 10% of yearly allocation")
        }
        if (req.requestedAmount > dept.currentBudget) {
            throw new IllegalStateException("Insufficient department budget")
        }

        def oldBudget = dept.currentBudget
        dept.currentBudget -= req.requestedAmount
        dept.save(flush: true)

        req.status = RequestStatus.APPROVED
        req.approvedBy = manager
        req.save(flush: true)

        logAudit('UPDATE', req.id, 'BudgetRequest', 'PENDING', 'APPROVED', manager)
        logAudit('UPDATE', dept.id, 'Department', oldBudget.toString(), dept.currentBudget.toString(), manager)

        sendEmail(req.requestedBy, "Budget Approved", """
            Hello ${req.requestedBy},
            
            Your budget request has been approved.
            
            Details:
            - Purpose: ${req.purpose}
            - Amount: ${req.requestedAmount}
            - Approved by: ${manager}
            
            Regards,
            Budget Approval System
            """)

        return req
    }


    def reject(Long id, String approver, String reason) {
        def req = BudgetRequest.get(id)
        if (!req) throw new IllegalArgumentException("Invalid request ID")

        println "Rejecting request #$id with current status: ${req.status}"

        if (req.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Request status must be PENDING to reject. Current: ${req.status}")
        }

        req.status = RequestStatus.REJECTED
        req.approvedBy = approver
        req.save(flush: true)

        logAudit('REJECT', req.id, 'BudgetRequest', RequestStatus.PENDING.name(), RequestStatus.REJECTED.name(), approver)

        sendEmail(req.requestedBy, "Budget Rejected", """
            Hello ${req.requestedBy},
            
            Your budget request has been rejected.
            
            Details:
            - Purpose: ${req.purpose}
            - Amount: ${req.requestedAmount}
            - Rejected by: ${approver}
            - Reason: ${reason}
            
            Regards,
            Budget Approval System
            """)
        return req
    }


    def logAudit(String action, Long entityId, String entityType, String oldVal, String newVal, String user) {
        new AuditLog(action: action, entityId: entityId, entityType: entityType,
                oldValue: oldVal, newValue: newVal, changedBy: user).save(flush: true)
    }

    private void sendEmail(String toEmail, String subjectText, String bodyText) {
        if (!toEmail?.contains("@")) {
            log.warn "Invalid email address: $toEmail"
            return
        }

        mailService.sendMail {
            to toEmail
            from "renuka17112001@gmail.com"
            subject subjectText
            body bodyText
        }
    }



}