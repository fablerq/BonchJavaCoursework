package com.bonch.kursach.ui.forms;

import com.bonch.kursach.entities.Route;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import static com.bonch.kursach.utils.Notifications.errorNotification;

public class RouteForm extends FormLayout {

    private TextField source;
    private TextField destination;
    private Route route;

    private Binder<Route> binder;
    private HorizontalLayout layout;

    private Button save;
    private Button delete;
    private Button close;

    public RouteForm() {
        configureElements();

        binder = new BeanValidationBinder<>(Route.class);
        binder.bindInstanceFields(this);

        layout = configureLayout();

        add(source, destination, layout);
    }

    private void configureElements() {
        source = new TextField("Место отправки");
        destination = new TextField("Место назначения");

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, route)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(route);
            fireEvent(new SaveEvent(this, route));
        } catch (ValidationException e) {
            errorNotification("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setRoute(Route route) {
        this.route = route;
        binder.readBean(route);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class routeFormEvent extends ComponentEvent<RouteForm> {
        private final Route route;

        protected routeFormEvent(RouteForm source, Route route) {
            super(source, false);
            this.route = route;
        }

        public Route getRoute() {
            return route;
        }
    }

    public static class SaveEvent extends routeFormEvent {
        public SaveEvent(RouteForm source, Route route) {
            super(source, route);
        }
    }

    public static class DeleteEvent extends routeFormEvent {
        public DeleteEvent(RouteForm source, Route route) {
            super(source, route);
        }
    }

    public static class CloseEvent extends routeFormEvent {
        public CloseEvent(RouteForm source) {
            super(source, null);
        }
    }
}
