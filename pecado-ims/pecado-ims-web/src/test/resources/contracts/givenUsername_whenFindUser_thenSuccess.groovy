package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'givenUsername_whenFindUser_thenSuccess'
    request {
        method GET()
        url('/user') {
            queryParameters {
                parameter('username', 'admin')
            }
            headers {
                header('from', 'Y')
            }
        }
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(file("userResponse.json"))
    }
}