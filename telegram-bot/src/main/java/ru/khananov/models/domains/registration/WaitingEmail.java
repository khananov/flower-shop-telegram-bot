package ru.khananov.models.domains.registration;

public class WaitingEmail {
    private static WaitingEmail INSTANCE;

    private boolean isWaitingEmailText;

    private WaitingEmail() {
    }

    public static WaitingEmail getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WaitingEmail();
        }

        return INSTANCE;
    }

    public boolean getWaitingEmailText() {
        return isWaitingEmailText;
    }

    public void setWaitingEmailText(boolean waitingEmailText) {
        isWaitingEmailText = waitingEmailText;
    }
}
