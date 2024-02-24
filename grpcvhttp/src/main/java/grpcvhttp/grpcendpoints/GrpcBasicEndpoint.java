package grpcvhttp.grpcendpoints;

import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Singleton;
import proto.EmailIDGrpc;
import proto.Emailid;

@Singleton
@GrpcService
public class GrpcBasicEndpoint extends EmailIDGrpc.EmailIDImplBase {
    @Override
    public void getEmailID(
            Emailid.UserIDRequest request,
            StreamObserver<Emailid.EmailIDResponse> responseObserver){
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException ignored){
            System.out.println("Ignored");
        }

        Emailid.EmailIDResponse response =
                Emailid.EmailIDResponse.newBuilder()
                        .setEmailD("fenu1998@gmail.com").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
