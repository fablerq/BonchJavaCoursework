package com.bonch.kursach.ui.forms;

import com.bonch.kursach.entities.Location;
import com.bonch.kursach.entities.Ship;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import static com.bonch.kursach.utils.Notifications.errorNotification;

public class LocationForm extends FormLayout {

    private NumberField longitude;
    private NumberField latitude;
    private ComboBox<Ship> ship;
    private Location location;

    private HorizontalLayout layout;
    private Binder<Location> binder;

    private Button save;
    private Button delete;
    private Button close;

    public LocationForm(List<Ship> ships) {
        configureElements(ships);

        binder = new BeanValidationBinder<>(Location.class);
        binder.bindInstanceFields(this);

        layout = configureLayout();

        add(longitude, latitude, ship, layout);
    }

    private void configureElements(List<Ship> ships) {
        longitude = new NumberField("Долгота");
        latitude = new NumberField("Широта");
        ship = new ComboBox<>("Корабль");

        ship.setItems(ships);
        ship.setItemLabelGenerator(Ship::getTitle);

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, location)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public void setLocation(Location location) {
        this.location = location;
        binder.readBean(location);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(location);
            fireEvent(new SaveEvent(this, location));
        } catch (ValidationException e) {
            errorNotification("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class LocationFormEvent extends ComponentEvent<LocationForm> {
        private final Location location;

        protected LocationFormEvent(LocationForm source, Location location) {
            super(source, false);
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class SaveEvent extends LocationFormEvent {
        public SaveEvent(LocationForm source, Location location) {
            super(source, location);
        }
    }

    public static class DeleteEvent extends LocationFormEvent {
        public DeleteEvent(LocationForm source, Location location) {
            super(source, location);
        }
    }

    public static class CloseEvent extends LocationFormEvent {
        public CloseEvent(LocationForm source) {
            super(source, null);
        }
    }

}
