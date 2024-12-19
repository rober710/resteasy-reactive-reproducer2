# Quarkus Reproducer

This project is a simple reproducer for a NPE when using the `resteasy-reactive` extension.

To reproduce NPE when a timeout happens and we have a filter, call the `/api/test-timeout` endpoint:

```shell
curl http://127.0.0.1:8080/api/test-timeout
```

You'll see the following error:
```
Details:
        Error id ba11afe4-322d-4de6-b9ae-beb3410473de-1, jakarta.ws.rs.ProcessingException: java.lang.NullPointerException: Cannot invoke "org.jboss.resteasy.reactive.client.impl.ClientRequestContextImpl.getRestClientRequestContext()" because "requestContext" is null
Stack:
        jakarta.ws.rs.ProcessingException: java.lang.NullPointerException: Cannot invoke "org.jboss.resteasy.reactive.client.impl.ClientRequestContextImpl.getRestClientRequestContext()" because "requestContext" is null
        at org.jboss.resteasy.reactive.client.handlers.ClientResponseFilterRestHandler.handle(ClientResponseFilterRestHandler.java:25)
        at org.jboss.resteasy.reactive.client.handlers.ClientResponseFilterRestHandler.handle(ClientResponseFilterRestHandler.java:10)
        at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.invokeHandler(AbstractResteasyReactiveContext.java:231)
        at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.run(AbstractResteasyReactiveContext.java:147)
        at org.jboss.resteasy.reactive.client.impl.RestClientRequestContext$1.lambda$execute$0(RestClientRequestContext.java:324)
        at io.vertx.core.impl.ContextInternal.dispatch(ContextInternal.java:270)
        at io.vertx.core.impl.ContextInternal.dispatch(ContextInternal.java:252)
        at io.vertx.core.impl.ContextInternal.lambda$runOnContext$0(ContextInternal.java:50)
        at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:173)
        at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:166)
        at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:569)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.NullPointerException: Cannot invoke "org.jboss.resteasy.reactive.client.impl.ClientRequestContextImpl.getRestClientRequestContext()" because "requestContext" is null
        at com.example.CustomRestClientResponseFilter.filter(CustomRestClientResponseFilter.java:21)
        at org.jboss.resteasy.reactive.client.handlers.ClientResponseFilterRestHandler.handle(ClientResponseFilterRestHandler.java:21)
        ... 15 more
```
