package ru.khananov.models.domains.registration;

public class WaitingName {
    private static WaitingName INSTANCE;

    private boolean isWaitingNameText;

    private WaitingName() {
    }

    public static WaitingName getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WaitingName();
        }

        return INSTANCE;
    }

    public boolean getWaitingNameText() {
        return isWaitingNameText;
    }

    public void setWaitingNameText(boolean waitingNameText) {
        isWaitingNameText = waitingNameText;
    }
}
