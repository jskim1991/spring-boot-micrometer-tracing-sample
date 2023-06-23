package io.jay.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();

        SpringApplication.run(ServerApplication.class, args);
    }
}

@Controller
@ResponseBody
@RequestMapping("/customers")
class CustomerController {

    Logger log = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping
    public Flux<Customer> customers() {
        log.info("Generating new customers {} | {}", MDC.get("traceId"), MDC.get("spanId"));
        return Flux.fromStream(IntStream.range(0, 10).boxed())
                .flatMap(i -> Flux.just(new Customer(i, UUID.randomUUID().toString())));
    }
}

record Customer(int id, String name) {
}
