package ru.khananov.models.domains.registration;

public final class WaitingAddress {
    private static WaitingAddress INSTANCE;

    private boolean isWaitingAddressText;

    private WaitingAddress() {
    }

    public static WaitingAddress getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WaitingAddress();
        }

        return INSTANCE;
    }

    public boolean getWaitingAddressText() {
        return isWaitingAddressText;
    }

    public void setWaitingAddressText(boolean waitingAddressText) {
        isWaitingAddressText = waitingAddressText;
    }
}
