package grpcvhttp2.callEndpoints;


import grpcvhttp2.controllers.UserNotificationEndpointController;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Controller("v1")
public class OptimizeBasicHttpEndpoint {


    private final UserNotificationEndpointController userNotificationEndpointController;

    public OptimizeBasicHttpEndpoint(UserNotificationEndpointController userNotificationEndpointController) {
        this.userNotificationEndpointController = userNotificationEndpointController;
    }

    @Get("/advanceHttpEndpoint")
    public String advancedHttpEndpoint() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/v1/basichttp/helloworld"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        return "Advanced  -- " + response.body().toString() + " -- Advanced";
    }

    @Get("/advanceHttpEndpointLong")
    public String advancedHttpEndpointLong() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/v1/basichttp/longrunningapi"))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> responseCompletableFuture = HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseCompletableFuture.thenAccept(response -> {
            System.out.println("HTTP Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        });

        System.out.println("Continuing with other unrelated work...");

        return "I have exited without my Future in place";

    }

    @Post("/sendEmailToUser")
    public io.micronaut.http.HttpResponse<String> sendNotificationToUser(@Body NotificationBody notificationBody){

        userNotificationEndpointController.sendNotificationToUserAsync(notificationBody.getUserID(), notificationBody.getUserEmailContent(),notificationBody.getCallType())
                                          .thenAccept(logResult -> System.out.println("Notification sent successfully"))
                                            .exceptionally(exception -> {System.out.println("Request Failed with Exception, Caught here!");
                                                return null;
                                            });


        return io.micronaut.http.HttpResponse.ok("Email notification Initiated");

    }

}
