package com.swapnilsankla.loangateway.service

import com.swapnilsankla.loangateway.publisher.NeedLoanEventPublisher
import com.swapnilsankla.loangateway.repository.LoanApplication
import com.swapnilsankla.loangateway.repository.LoanGatewayRepository
import io.opentracing.Tracer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class LoanApplicationService(@Autowired val needLoanEventPublisher: NeedLoanEventPublisher,
                             @Autowired val loanApplicationRepository: LoanGatewayRepository) {
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    fun apply(customerId: String): Mono<String> {
        val applicationNumber = UUID.randomUUID().toString()
        needLoanEventPublisher.publish(customerId, applicationNumber)
        val loanApplication = LoanApplication(applicationNumber = applicationNumber, customerId = customerId)
        return loanApplicationRepository.save(loanApplication).map {
            val result = "The loan is accepted. You can track it with application number $applicationNumber"
            logger.info(result)
            result
        }
    }
}