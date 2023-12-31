package ru.khananov.models.domains;

public enum Command {
    START_COMMAND("/start"),
    CART_COMMAND("\uD83D\uDECD Корзина"),
    CATALOG_COMMAND("\uD83D\uDCD6 Каталог"),
    PROFILE_COMMAND("\uD83D\uDC64 Профиль"),

    WILD_FLOWERS_COMMAND("\uD83C\uDF38 Полевые"),
    GARDEN_FLOWERS_COMMAND("\uD83C\uDF39 Садовые"),

    BUY_COMMAND("\uD83D\uDCB3 Купить"),

    PAY_ORDER_COMMAND("\u2705 Оформить заказ"),
    CLEAR_ORDER_COMMAND("\u267B Отчистить корзину"),
    CANCEL_ORDER_COMMAND("Отменить заказ"),

    PLUS_AMOUNT_COMMAND("\u2795"),
    MINUS_AMOUNT_COMMAND("\u2796"),

    REGISTRATION_COMMAND("\u270F Регистрация"),
    VERIFY_EMAIL_COMMAND("\u2705 Подтвердить email"),
    CANCEL_CODE_COMMAND("\uD83D\uDEAB Отмена"),
    CHANGE_PROFILE_COMMAND("\uD83D\uDCDD Изменить информацию"),
    DELETE_PROFILE_COMMAND("\u274C Удалить профиль"),

    GET_ORDERS_COMMAND("\uD83D\uDCD1 Список заказов"),

    CONFIRM_COMMAND("Да"),
    REJECT_COMMAND("Нет"),

    MAIN_MENU_COMMAND("\uD83D\uDEAA Главная");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}