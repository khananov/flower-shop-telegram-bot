package ru.khananov.dispatchers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.controllers.impl.*;
import ru.khananov.controllers.impl.registration.RegistrationAddressController;
import ru.khananov.controllers.impl.registration.RegistrationCodeController;
import ru.khananov.controllers.impl.registration.RegistrationEmailController;
import ru.khananov.controllers.impl.registration.RegistrationNameController;
import ru.khananov.exceptions.UnsupportedMessageTypeException;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class UpdateDispatcher {
    private final StartController startController;
    private final CatalogController catalogController;
    private final CategoryController categoryController;
    private final CartController cartController;
    private final BuyController buyController;
    private final ProductInOrderController productInOrderController;
    private final ProfileController profileController;
    private final RegistrationNameController registrationNameController;
    private final RegistrationAddressController registrationAddressController;
    private final RegistrationEmailController registrationEmailController;
    private final RegistrationCodeController registrationCodeController;
    private final PaymentController paymentController;

    @Autowired
    public UpdateDispatcher(StartController startController,
                            CatalogController catalogController,
                            CategoryController categoryController,
                            CartController cartController,
                            BuyController buyController,
                            ProductInOrderController productInOrderController,
                            ProfileController profileController,
                            RegistrationNameController registrationNameController,
                            RegistrationAddressController registrationAddressController,
                            RegistrationEmailController registrationEmailController,
                            RegistrationCodeController registrationCodeController,
                            PaymentController paymentController) {
        this.startController = startController;
        this.catalogController = catalogController;
        this.categoryController = categoryController;
        this.cartController = cartController;
        this.buyController = buyController;
        this.productInOrderController = productInOrderController;
        this.profileController = profileController;
        this.registrationNameController = registrationNameController;
        this.registrationAddressController = registrationAddressController;
        this.registrationEmailController = registrationEmailController;
        this.registrationCodeController = registrationCodeController;
        this.paymentController = paymentController;
    }

    private List<TelegramController> getControllers() {
        return Arrays.asList(
                startController,
                catalogController,
                categoryController,
                cartController,
                buyController,
                productInOrderController,
                profileController,
                registrationNameController,
                registrationAddressController,
                registrationEmailController,
                registrationCodeController,
                paymentController);
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage() || update.hasCallbackQuery() || update.hasPreCheckoutQuery())
            distributeMessageByCommand(update);
        else
            log.error(new UnsupportedMessageTypeException("Неподдерживаемый тип сообщения"));
    }

    private void distributeMessageByCommand(Update update) {
        getControllers().stream()
                .filter(controller -> controller.support(update))
                .findFirst()
                .ifPresent(controller -> controller.execute(update));
    }
}