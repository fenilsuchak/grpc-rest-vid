package grpcvhttp2.callEndpoints;


import grpcvhttp2.controllers.UserNotificationEndpointController;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("v1")
public class OptimizeBasicHttpEndpoint {


    private final UserNotificationEndpointController userNotificationEndpointController;

    public OptimizeBasicHttpEndpoint(UserNotificationEndpointController userNotificationEndpointController) {
        this.userNotificationEndpointController = userNotificationEndpointController;
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
