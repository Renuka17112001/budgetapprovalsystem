package com.budgetapproval

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BudgetRequestSpec extends Specification implements DomainUnitTest<BudgetRequest> {

     void "test domain constraints"() {
        when:
        BudgetRequest domain = new BudgetRequest()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
