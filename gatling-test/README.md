# Gatling Bundle

The Gatling Bundle gives you a quick start to your Gatling journey.
It is based on the [Gatling Maven Plugin demo, in Java](https://github.com/gatling/gatling-maven-plugin-demo-java).

## Run current test locally

```console
mvn gatling:test -Dgatling.simulationClass=kafkahighload.HighloadDbSimulation -DUSERS=10 -DRAMP_DURATION=20
```

## Run a test locally

```console
mvn gatling:test
```

## Run the Gatling Recorder

```console
mvn gatling:recorder
```

## To go further

* [our scripting introduction](https://docs.gatling.io/tutorials/scripting-intro/)
* [the Gatling Maven plugin documentation](https://docs.gatling.io/reference/extensions/build-tools/maven-plugin/)
