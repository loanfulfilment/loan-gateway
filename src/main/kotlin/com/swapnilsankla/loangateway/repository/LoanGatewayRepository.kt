package com.swapnilsankla.loangateway.repository

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

interface LoanGatewayRepository: MongoRepository<LoanApplication, String>

@Document(collection = "LoanApplication")
data class LoanApplication(val customerId: String, val applicationNumber: String)