package com.swapnilsankla.loangateway.publisher

import com.swapnilsankla.loangateway.model.NeedLoanEvent
import com.swapnilsankla.tracestarter.CustomKafkaTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NeedLoanEventPublisher(@Autowired val kafkaTemplate: CustomKafkaTemplate) {
    fun publish(customerId: String, applicationNumber: String) {
        kafkaTemplate.publish("needLoanEvent", NeedLoanEvent(customerId, applicationNumber))
    }
}