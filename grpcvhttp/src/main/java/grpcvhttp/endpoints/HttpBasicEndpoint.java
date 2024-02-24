package grpcvhttp.endpoints;


import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("v1")
public class HttpBasicEndpoint {

    @Get("/helloworld")
    public String getHelloWorld(){
        return "Hello World";
    }


    @Get("/longrunningapi")
    public String getLongRunningApiCall() throws InterruptedException {
        Thread.sleep(20000);
        return "Sleepy Hello world";
    }

    @Get("/getemailid/{userID}")
    public String getEmailID(String userID) throws InterruptedException {
        Thread.sleep(1500); // This is a DB call let's say.
        System.out.println(userID);
        return "fenu1998@gmail.com";
    }

}
