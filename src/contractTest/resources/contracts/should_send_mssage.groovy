package com.example.bankservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description "should send kafka message"
    label 'transactionController.sendMessage'

    input {
        triggeredBy 'createBankTransaction()'
    }

    outputMessage {
        sentTo 'transactionEvents'

        headers {
            header('contentType', 'application/json')
        }

        body([
                amount     : anyNonBlankString(),
                iban       : anyNonBlankString(),
                description: anyNonBlankString()
        ])


    }
}
