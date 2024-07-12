package kafkahighload;

import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HighloadDbSimulation extends Simulation {
    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));
    private static final Random random = new Random();

    @Override
    public void before() {
        System.out.printf("Starting simulation with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
    }

    // HTTP CONFIGURATION
    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080")
            .acceptHeader("application/json;charset=UTF-8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0");

    // FEEDER FOR TESTTING
    Iterator<Map<String, Object>> feeder = Stream.generate((Supplier<Map<String, Object>>) () -> {
        var id = random.nextInt(100) + 1;
        return Map.of("id", id);
    }).iterator();

    ScenarioBuilder calculations = scenario("Highload kafka test")
            .feed(feeder)
            .exec(http("GET request to /api/v1/calculate")
                    .get("/api/v1/calculation")
                    .queryParam("id", "#{id}"));

    {
        setUp(
                calculations.injectOpen(
                        nothingFor(5), // do nothing for 5 seconds. To warm up test
                        rampUsers(USER_COUNT).during(RAMP_DURATION)))
                .protocols(httpProtocol);
    }
}
