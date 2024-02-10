package com.mvcguru.risiko.maven.eclipse.model.player;

public class LoggedPlayer extends Player {
    private boolean isLoggedIn;

    public LoggedPlayer(String name, int playerId) {
        super(name, playerId);
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
