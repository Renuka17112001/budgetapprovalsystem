package com.budgetapproval

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class ApiAuditLogControllerSpec extends Specification implements ControllerUnitTest<ApiAuditLogController> {

     void "test index action"() {
        when:
        controller.index()

        then:
        status == 200

     }
}
