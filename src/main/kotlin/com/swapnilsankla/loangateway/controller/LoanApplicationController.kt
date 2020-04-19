package com.swapnilsankla.loangateway.controller

import com.swapnilsankla.loangateway.service.LoanApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanApplicationController(@Autowired val loanApplicationService: LoanApplicationService) {
    @GetMapping("/apply")
    fun apply(@RequestParam customerId: String): ResponseEntity<String> {
        return ResponseEntity.accepted().body(loanApplicationService.apply(customerId))
    }

    @GetMapping("/track")
    fun track(@RequestParam applicationNumber: String): ResponseEntity<String> {
        return ResponseEntity.ok(applicationNumber)
    }
}