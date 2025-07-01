package com.budgetapproval

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class AuditLogSpec extends Specification implements DomainUnitTest<AuditLog> {

     void "test domain constraints"() {
        when:
        AuditLog domain = new AuditLog()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
