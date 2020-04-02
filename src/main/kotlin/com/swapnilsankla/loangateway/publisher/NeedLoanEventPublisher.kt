package com.swapnilsankla.loangateway.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.swapnilsankla.loangateway.model.NeedLoanEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NeedLoanEventPublisher(@Autowired val kafkaTemplate: KafkaTemplate<String, String>,
                             @Autowired val objectMapper: ObjectMapper) {
    fun publish(customerId: String, applicationNumber: String) {
        kafkaTemplate.send(buildMessage(customerId, applicationNumber))
    }

    private fun buildMessage(customerId: String, applicationNumber: String): ProducerRecord<String, String> {
        val message = ProducerRecord<String, String>(
            "needLoanEvent",
            objectMapper.writeValueAsString(NeedLoanEvent(customerId, applicationNumber))
        )
        message.headers().remove("uber-trace-id")
        message.headers().add("uber-trace-id", applicationNumber.toByteArray())
        return message
    }
}