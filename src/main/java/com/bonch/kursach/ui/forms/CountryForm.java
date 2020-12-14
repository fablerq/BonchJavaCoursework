package com.bonch.kursach.ui.forms;

import com.bonch.kursach.entities.Country;
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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@FieldDefaults(level= AccessLevel.PRIVATE)
public class CountryForm extends FormLayout {

    TextField title;
    Country country;

    Binder<Country> binder;
    HorizontalLayout layout;

    Button save;
    Button delete;
    Button close;

    public CountryForm() {
        configureElements();

        binder = new BeanValidationBinder<>(Country.class);
        binder.bindInstanceFields(this);

        layout = configureLayout();

        add(title, layout);
    }

    private void configureElements() {
        title = new TextField("Title");

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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, country)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public void setCountry(Country country) {
        this.country = country;
        binder.readBean(country);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(country);
            fireEvent(new SaveEvent(this, country));
        } catch (ValidationException e) {
            errorNotification("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class CountryFormEvent extends ComponentEvent<CountryForm> {
        private final Country country;

        protected CountryFormEvent(CountryForm source, Country country) {
            super(source, false);
            this.country = country;
        }

        public Country getCountry() {
            return country;
        }
    }

    public static class SaveEvent extends CountryFormEvent {
        public SaveEvent(CountryForm source, Country country) {
            super(source, country);
        }
    }

    public static class DeleteEvent extends CountryFormEvent {
        public DeleteEvent(CountryForm source, Country country) {
            super(source, country);
        }
    }

    public static class CloseEvent extends CountryFormEvent {
        public CloseEvent(CountryForm source) {
            super(source, null);
        }
    }
}
