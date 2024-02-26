package grpcvhttp.endpoints;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("v1")
public class HttpBasicEndpoint {

    @Get("/getemailid/{userID}")
    public String getEmailID(String userID) throws InterruptedException {
        Thread.sleep(7000); // This is a DB call let's say.
        System.out.println(userID);
        return "fenu1998@gmail.com";
    }

}
