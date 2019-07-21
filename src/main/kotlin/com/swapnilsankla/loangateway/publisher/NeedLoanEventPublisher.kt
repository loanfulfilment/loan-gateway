package com.swapnilsankla.loangateway.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.swapnilsankla.loangateway.model.NeedLoanEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NeedLoanEventPublisher(@Autowired val kafkaTemplate: KafkaTemplate<String, String>,
                             @Autowired val objectMapper: ObjectMapper) {
    fun publish(customerId: String, applicationNumber: String) {
        kafkaTemplate.send("needLoanEvent", objectMapper.writeValueAsString(NeedLoanEvent(customerId, applicationNumber)))
    }
}