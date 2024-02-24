package grpcvhttp2.controllers;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import proto.EmailIDGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Emailid;


@Singleton
public class UserNotificationEndpointController {

    private static final String USER_SERVICE_BASE_URL = "http://localhost:8081/";

    private static final String EMAIL_SERVICE_ENDPOINT = "http://email-service-api/";

    private static final ManagedChannel myChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

    CompletableFuture<Emailid.EmailIDResponse> responseFuture = new CompletableFuture<>();

    private final StreamObserver<Emailid.EmailIDResponse> responseObserver = new StreamObserver<Emailid.EmailIDResponse>() {
        @Override
        public void onNext(Emailid.EmailIDResponse response) {
            // When a response is received, complete the CompletableFuture with the response
            responseFuture.complete(response);
        }

        @Override
        public void onError(Throwable t) {
            // If an error occurs, complete the CompletableFuture exceptionally with the error
            responseFuture.completeExceptionally(t);
        }

        @Override
        public void onCompleted() {
            // This method is called when the RPC call is complete
            // You can perform any cleanup here if needed
        }
    };

    public CompletableFuture<Void> sendNotificationToUserAsync(String userId, String message, String callType) {
        if(Objects.equals(callType, "HTTP")) {
            return getUserEmailAsyncHTTP(userId)
                    .thenCompose(userEmail -> sendNotificationRequest(userEmail, message));
        }
        else {
            return getUserEmailAsyncGrpc(userId)
                        .thenCompose(userEmail -> sendNotificationRequest(responseFuture.toString(), message));
        }
    }

    private CompletableFuture<String> getUserEmailAsyncHTTP(String userId) {
        // Simulate fetching user email via HTTP asynchronously
        String userApiUrl = USER_SERVICE_BASE_URL + "v1/getemailid/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userApiUrl))
                .GET()
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(throwable -> {
                    System.out.println("Error " + throwable.toString());
                    return null;
                });
    }

    private CompletableFuture<Emailid.EmailIDResponse> getUserEmailAsyncGrpc(String userId) {
        // Simulate fetching user email via Grpc asynchronously

        EmailIDGrpc.EmailIDStub emailIDStub = EmailIDGrpc.newStub(myChannel); // default is async.

        Emailid.UserIDRequest userIDRequest =  Emailid.UserIDRequest.newBuilder().setUserID("randomUserID").build();

        emailIDStub.getEmailID(userIDRequest, responseObserver);

        return responseFuture;
    }



    private CompletableFuture<Void> sendNotificationRequest(String userEmail, String message) {
        // Simulate sending a notification via HTTP asynchronously

        System.out.println("userEmail obtained from Async call is " + userEmail);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EMAIL_SERVICE_ENDPOINT))
                .POST(HttpRequest.BodyPublishers.ofString("userEmail=" + userEmail + "&message=" + message))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> System.out.println("Notification API response: " + responseBody))
                .exceptionally(throwable -> {
                    System.out.println("Failed with Error which is caught here but passed on --> " + throwable.toString());
                    throw new RuntimeException(throwable);
                });
    }

}
