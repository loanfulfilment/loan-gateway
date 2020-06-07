package com.swapnilsankla.loangateway.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class LoanGrantedEvent(
    val customerId: String,
    val applicationNumber: String,
    val offer: Offer
)

class Offer(
    val interestRate: Double,
    val tenureInMonths: Int,
    val approvedLoanAmount: Double,
    val foreclosureCharges: Double,
    val partPaymentCharges: Double
)
