# Peeco: an OWB centric HTTP microserver

This repository is one of the results of a [GSOC 2020 (Google Summer of Code) project]((https://gist.github.com/a-rekkusu/b98ecd201d25102ca3e118a2fa38fbb4)) for [OpenWebBeans](https://openwebbeans.apache.org/) at ASF.
Its intention is to provide a very small and lightweight HTTP server with CDI functionalities at its core, that should hopefully become natively runnable soon, as a GraalVM native-image. 
For the server components, [Netty](https://netty.io/) is used.
This repository contains the API, implementation and a showcase in the respective sub-modules.

Here's an example for how to configure and build this server:
```java
public HttpServer init()
{
	return new HttpServer.Builder()
			.port(0) //applies a random valid port
			.ssl(false)
			.host("localhost")
			.build();
}
```

And here's how you can use the API with the `HttpHandler` annotation:
```java
@Inject private HttpServer httpServer;

@HttpHandler(method = {HttpMethod.GET}, url = "/hello/*", matching = Matching.WILDCARD)
public CompletionStage<Response> helloWorld(Request request)
{
	String responseContent = "Hello World from " + httpServer.getHost() + ":" + httpServer.getPort() + " !";
	ByteArrayInputStream output = new ByteArrayInputStream(responseContent.getBytes(StandardCharsets.UTF_8));

	return CompletableFuture.supplyAsync(() ->
	{
		Response response = new Response();
		response.addHeader("statusCode", "200");
		response.addHeader("content-type", "text/html");
		response.setOutput(output);
		return response;
	});
}
```

This is a reactive example with CompletionStage; you can also straight up use Response as the method return type.
The formal requirements of a valid handler method and annotation are:
- return type must be `CompletionStage<Response>` or `Response`
- first method parameter must be `Request`
- `Matching.WILDCARD` needs a star at the end of the given url

For more examples please refer to the respective [showcase.](https://github.com/a-rekkusu/owb-centric-microserver/blob/master/showcase/src/main/java/org/apache/peeco/showcase/HelloWorldHandler.java)
The implementation is found [here.](https://github.com/a-rekkusu/owb-centric-microserver/tree/master/impl/src/main/java/org/apache/peeco/impl)

[`PeecoExtension.java`](https://github.com/a-rekkusu/owb-centric-microserver/blob/master/impl/src/main/java/org/apache/peeco/impl/PeecoExtension.java#L25) collects all methods that are annotated with our `@HttpHandler` and starts up the Netty Server.
The flow of the request/response handling is then happening in [`PeecoChannelHandler.java`](https://github.com/a-rekkusu/owb-centric-microserver/blob/master/impl/src/main/java/org/apache/peeco/impl/PeecoChannelHandler.java#L44).