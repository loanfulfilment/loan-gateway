package com.swapnilsankla.loangateway.publisher

import com.swapnilsankla.loangateway.model.NeedLoanEvent
import com.swapnilsankla.tracestarter.CustomKafkaTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.swapnilsankla.loangateway.repository.LoanApplication
import com.fasterxml.jackson.databind.ObjectMapper
import com.swapnilsankla.tracestarter.Trace

@Component
class LoanProcessedPublisher(
    @Autowired val kafkaTemplate: CustomKafkaTemplate,
    @Autowired val objectMapper: ObjectMapper) {
    fun publish(loanApplication: LoanApplication, trace: Trace) {
        kafkaTemplate.publish(
            "loanProcessed", 
            loanApplication, 
            trace)
    }
}