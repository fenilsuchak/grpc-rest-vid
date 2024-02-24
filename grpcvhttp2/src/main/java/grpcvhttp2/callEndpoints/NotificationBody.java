package grpcvhttp2.callEndpoints;


import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable.Deserializable
public class NotificationBody {

    String userID;
    String userEmailContent;

    String callType;

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getUserID(){
        return this.userID;
    }

    public void setUserEmailContent(String userEmailContent){
        this.userEmailContent = userEmailContent;
    }

    public String getUserEmailContent(){
        return this.userEmailContent;
    }

    public void setCallType(String callType) { this.callType = callType; };

    public String getCallType() { return this.callType; };

}
