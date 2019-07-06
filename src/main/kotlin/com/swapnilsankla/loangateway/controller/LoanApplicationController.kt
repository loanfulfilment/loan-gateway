package com.swapnilsankla.loangateway.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanApplicationController {
    @PostMapping("")
    fun apply(@RequestParam customerId: String) = ResponseEntity.accepted().body("")
}