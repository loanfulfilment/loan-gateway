package com.swapnilsankla.loangateway.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.swapnilsankla.loangateway.model.LoanGrantedEvent
import com.swapnilsankla.loangateway.publisher.LoanProcessedPublisher
import com.swapnilsankla.loangateway.repository.LoanApplication
import com.swapnilsankla.loangateway.repository.LoanGatewayRepository
import com.swapnilsankla.tracestarter.TraceIdExtractor
import io.opentracing.Span
import io.opentracing.Tracer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class LoanGrantedEventListener(
    @Autowired val loanGatewayRepository: LoanGatewayRepository,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val traceIdExtractor: TraceIdExtractor,
    @Autowired val loanProcessedPublisher: LoanProcessedPublisher,
    @Autowired val tracer: Tracer
) {
    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @KafkaListener(topics = ["loanGranted"])
    fun loanGrantedEventListener(loanGrantedEventString: String, @Headers headers: Map<String, Any>) {
        val loanApplication = objectMapper.readValue(loanGrantedEventString, LoanGrantedEvent::class.java)
        val logString = "loanGranted event received for customer ${loanApplication.customerId}"
        logger.info(logString)
        val activeSpan: Span? = tracer.activeSpan()
        activeSpan?.log(logString)

        loanGatewayRepository.findByApplicationNumberAndCustomerId(
            loanApplication.applicationNumber,
            loanApplication.customerId
        ).doOnSuccess { loanApplicationInDB ->
            val updatedLoanApplication = LoanApplication(
                customerId = loanApplicationInDB.customerId,
                applicationNumber = loanApplicationInDB.applicationNumber,
                offer = loanApplication.offer
            )
            val trace = traceIdExtractor.fromKafkaHeaders(headers)
            loanProcessedPublisher.publish(updatedLoanApplication, trace)
            loanGatewayRepository.save(updatedLoanApplication).subscribe()
        }.subscribe()
    }
}
