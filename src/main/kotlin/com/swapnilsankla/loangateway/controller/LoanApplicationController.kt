package com.swapnilsankla.loangateway.controller

import com.swapnilsankla.loangateway.publisher.NeedLoanEventPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanApplicationController(@Autowired val loanEventPublisher: NeedLoanEventPublisher) {
    @PostMapping("")
    fun apply(@RequestParam customerId: String): ResponseEntity<String> {
        loanEventPublisher.publish(customerId)
        return ResponseEntity.accepted().body("")
    }
}