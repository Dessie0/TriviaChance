package edu.floridapoly.mobiledeviceapps.fall22.api.socket;

public enum MessageType {

    UPDATE_PLAYER(Bound.CLIENTBOUND),
    START_GAME(Bound.CLIENTBOUND),
    KICKED(Bound.CLIENTBOUND),
    NEXT_QUESTION(Bound.CLIENTBOUND),

    SUBMIT_QUESTION(Bound.SERVERBOUND),
    KICK_PLAYER(Bound.SERVERBOUND),
    LEAVE_GAME(Bound.SERVERBOUND),
    JOIN_GAME(Bound.SERVERBOUND),
    HOST_START_GAME(Bound.SERVERBOUND);

    private final Bound bound;
    MessageType(Bound bound) {
        this.bound = bound;
    }

    public Bound getBound() {
        return bound;
    }
}
