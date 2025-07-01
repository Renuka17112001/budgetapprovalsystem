package com.budgetapproval

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class ApiBudgetRequestControllerSpec extends Specification implements ControllerUnitTest<ApiBudgetRequestController> {

     void "test index action"() {
        when:
        controller.index()

        then:
        status == 200

     }
}
