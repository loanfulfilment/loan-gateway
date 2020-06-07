package com.swapnilsankla.loangateway.repository

import com.swapnilsankla.loangateway.model.Offer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface LoanGatewayRepository: ReactiveMongoRepository<LoanApplication, String> {
    fun findByApplicationNumberAndCustomerId(applicationNumber: String, customerId: String): Mono<LoanApplication>
}

@Document(collection = "LoanApplication")
data class LoanApplication(
    @Id
    val applicationNumber: String,
    val customerId: String,
    val offer: Offer? = null
)