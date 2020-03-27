package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'givenUserId_whenFindRoles_thenSuccess'
    request {
        method GET()
        url('/role') {
            queryParameters {
                parameter('userId', $(consumer(number()), producer(1L)))
            }
            headers {
                header('Authorization', $(consumer(nonBlank()), producer(execute('adminAccessToken'))))
            }
        }
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(file("response.json"))
    }
}