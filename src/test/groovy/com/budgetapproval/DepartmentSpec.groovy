package com.budgetapproval

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DepartmentSpec extends Specification implements DomainUnitTest<Department> {

     void "test domain constraints"() {
        when:
        Department domain = new Department()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
