package it.dgspa.skeleton.Exception;

public class PlayersException extends RuntimeException {

    private String nickname;

    public PlayersException(String message, String conflictingNickname) {
        super(message);
        this.nickname= nickname;
    }

    public String getConflictingNickname() {
        return nickname;
    }
}