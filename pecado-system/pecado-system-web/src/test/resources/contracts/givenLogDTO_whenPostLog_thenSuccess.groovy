package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'givenLogDTO_whenPostLog_thenSuccess'
    request {
        method POST()
        url('/log')
        headers {
            header('from', 'Y')
            contentType(applicationJson())
        }
        body(
            httpRequestMethod: $(consumer(anyNonBlankString()), producer('POST')),
            className: $(consumer(anyNonBlankString()), producer('me.batizhao.system.web.LogController')),
            classMethod: $(consumer(anyNonBlankString()), producer('save')),
            description: $(consumer(anyNonBlankString()), producer('插入日志')),
            parameter: $(consumer(optional(anyNonBlankString())), producer('{userId=1}')),
            result: $(consumer(optional(anyNonBlankString())), producer('R(code=0, message=ok)')),
            spend: $(consumer(anyPositiveInt()), producer('100')),
            clientId: $(consumer(anyUuid()), producer('client_app')),
            username: $(consumer(anyOf('admin', 'tom')), producer('admin')),
            url: $(consumer(anyUrl()), producer('http://localhost:5000/role')),
            ip: $(consumer(anyIpAddress()), producer('127.0.0.1')),
            time: $(consumer(anyDateTime()), producer('2020-04-02T12:32:10'))
        )
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
            "code" : 0,
            "message" : "ok",
            "data" : true
        )
    }
}