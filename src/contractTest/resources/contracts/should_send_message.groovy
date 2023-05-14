package com.example.bankservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description "should send kafka message"
    label 'triggerTransactionCreatedEvent'

    input {
        triggeredBy('createBankTransaction()')
    }

    outputMessage {
        sentTo('transaction-events')

//        headers {
//            messagingContentType(applicationJson())
//        }

        body([
                amount     : anyNonBlankString(),
                iban       : anyUuid(),
                description: anyNonBlankString()
        ])


    }
}

