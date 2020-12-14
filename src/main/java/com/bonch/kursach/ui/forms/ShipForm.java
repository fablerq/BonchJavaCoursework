package com.bonch.kursach.ui.forms;

import com.bonch.kursach.entities.Country;
import com.bonch.kursach.entities.Route;
import com.bonch.kursach.entities.Ship;
import com.bonch.kursach.entities.Type;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import static com.bonch.kursach.utils.Notifications.errorNotification;

public class ShipForm extends FormLayout {

    private TextField title;
    private ComboBox<Country> country;
    private ComboBox<Type> type;
    private ComboBox<Route> route;
    private Ship ship;

    private HorizontalLayout layout;
    private Binder<Ship> binder;

    private Button save;
    private Button delete;
    private Button close;

    public ShipForm(List<Country> countries, List<Type> types, List<Route> routes) {
        configureElements(countries, types, routes);

        binder = new BeanValidationBinder<>(Ship.class);
        binder.bindInstanceFields(this);

        layout = configureLayout();

        add(title, type, route, country, layout);
    }

    private void configureElements(List<Country> countries, List<Type> types, List<Route> routes) {
        title = new TextField("Название корабля");

        country = new ComboBox<>("Страна корабля");
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getTitle);

        type = new ComboBox<>("Тип корабля");
        type.setItems(types);
        type.setItemLabelGenerator(Type::getTitle);

        route = new ComboBox<>("Маршрут корабля");
        route.setItems(routes);
        route.setItemLabelGenerator(r -> r.getSource() + " - " + r.getDestination());

        save = new Button("Сохранить");
        delete = new Button("Удалить");
        close = new Button("Убрать");
    }

    private HorizontalLayout configureLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, ship)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        binder.readBean(ship);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(ship);
            fireEvent(new SaveEvent(this, ship));
        } catch (ValidationException e) {
            errorNotification("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class ShipFormEvent extends ComponentEvent<ShipForm> {
        private final Ship ship;

        protected ShipFormEvent(ShipForm source, Ship ship) {
            super(source, false);
            this.ship = ship;
        }

        public Ship getShip() {
            return ship;
        }
    }

    public static class SaveEvent extends ShipFormEvent {
        public SaveEvent(ShipForm source, Ship ship) {
            super(source, ship);
        }
    }

    public static class DeleteEvent extends ShipFormEvent {
        public DeleteEvent(ShipForm source, Ship ship) {
            super(source, ship);
        }
    }

    public static class CloseEvent extends ShipFormEvent {
        public CloseEvent(ShipForm source) {
            super(source, null);
        }
    }

}
