# Ping/Pong server

Test performance characteristics of various combinations of serialization and transport options,
for various combinations of messages.

## Build

    mvn clean package

This produces two jars under `target/`, `ping.jar` and `pong.jar`. Ping is the client, pong is the server.

## Run

Each run requires starting first the server, and then the client, with the same transport/serialization config for both.

    java -jar pong.jar <transport> <serializer>
    java -jar ping.jar <transport> <serializer> <host:port> <runtimeseconds>

For example

    java -jar pong.jar oio sbe_small
    java -jar ping.jar oio sbe_small localhost:7474 30

For available transports and serializers, see `Serialization` and `Transports`.


## Messages

### small

A minimal message, containing only the singular message id.

### stmt_response

A larger message, representing a statement from the client, including parameters, and then a response from the server
of 100 rows of 'nodes' with properties, labels and ids.

