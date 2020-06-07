package com.swapnilsankla.loangateway

import com.swapnilsankla.loangateway.repository.LoanApplication
import com.swapnilsankla.loangateway.repository.LoanGatewayRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@EmbeddedKafka(
    partitions = 1, controlledShutdown = false,
    brokerProperties = ["listeners=PLAINTEXT://localhost:3333", "port=3333"]
)
@TestPropertySource(
    properties = [
        "spring.kafka.bootstrap-servers=\${spring.embedded.kafka.brokers}"
    ]
)
class LoanGrantedIntegrationTest(
    @Autowired val loanGatewayRepository: LoanGatewayRepository,
    @Autowired val kafkaTemplate: KafkaTemplate<String, String>
) {
    @Test
    fun `should update DB when received loan approval event`() {
        loanGatewayRepository.deleteAll()
        loanGatewayRepository.save(LoanApplication(customerId = "1", applicationNumber = "1234"))

        val loanGrantedEventString = """
            {
              "customerId": "1",
              "applicationNumber": "1234",
              "offer": {
                "interestRate": 8.5,
                "tenureInMonths": 60,
                "approvedLoanAmount": 500000.0,
                "foreclosureCharges": 0.0,
                "partPaymentCharges": 0.0
              },
              "fraudStatus": "CLEAR",
              "loanGranted": true
            }
        """.trimIndent()
        kafkaTemplate.send("loanGranted", loanGrantedEventString)

        val result = waitTill(5, false) {
            loanGatewayRepository.findByApplicationNumberAndCustomerId("1234", "1").block()!!.offer != null
        }

        result shouldBe true
    }

    private fun <T> waitTill(numberOfAttempts: Int, tillValue: T, f: () -> T): T? {
        var remainingNumberOfAttempts = numberOfAttempts
        var t: T? = null
        while (remainingNumberOfAttempts > 0) {
            t = f()
            if (t == tillValue) {
                remainingNumberOfAttempts--
                Thread.sleep(1000)
            } else break
        }
        return t
    }
}