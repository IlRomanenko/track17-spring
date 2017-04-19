package track.messenger.messages.responses;

import track.messenger.messages.Message;
import track.messenger.messages.Type;

/**
 * Tehnotrack
 * track.messenger.messages
 * <p>
 * Created by ilya on 13.04.17.
 */
public class StatusMessage extends Message {

    public enum Status {
        OK, NOT_FOUND, FAIL;
    }

    private Status status;

    public StatusMessage(Status status) {
        super(-1L, Type.MSG_STATUS);
        this.status = status;
    }

    public StatusMessage(Status status, Type type) {
        super(-1L, type);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StatusMessage{ " + status.toString() + " }";
    }
}
