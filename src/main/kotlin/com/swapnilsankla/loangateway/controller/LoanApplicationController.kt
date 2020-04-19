package com.swapnilsankla.loangateway.controller

import com.swapnilsankla.loangateway.publisher.NeedLoanEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/loans")
class LoanApplicationController(@Autowired val needLoanEventPublisher: NeedLoanEventPublisher) {
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    @GetMapping("/apply")
    fun apply(@RequestParam customerId: String): ResponseEntity<String> {
        val applicationNumber = UUID.randomUUID().toString()
        needLoanEventPublisher.publish(customerId, applicationNumber)
        val result = "The loan is accepted. You can track it with application number $applicationNumber"
        logger.info(result)
        return ResponseEntity.accepted().body(result)
    }

    @GetMapping("/track")
    fun track(@RequestParam applicationNumber: String): ResponseEntity<String> {
        return ResponseEntity.ok(applicationNumber)
    }
}