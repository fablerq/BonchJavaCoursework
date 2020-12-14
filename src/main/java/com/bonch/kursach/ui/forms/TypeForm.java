package com.bonch.kursach.ui.forms;

import com.bonch.kursach.entities.Type;
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
public class TypeForm extends FormLayout {

    TextField title;
    Type type;

    Binder<Type> binder;
    HorizontalLayout layout;

    Button save;
    Button delete;
    Button close;

    public TypeForm() {
        configureElements();

        binder = new BeanValidationBinder<>(Type.class);
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, type)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(type);
            fireEvent(new SaveEvent(this, type));
        } catch (ValidationException e) {
            errorNotification("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setType(Type type) {
        this.type = type;
        binder.readBean(type);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class TypeFormEvent extends ComponentEvent<TypeForm> {
        private final Type type;

        protected TypeFormEvent(TypeForm source, Type Type) {
            super(source, false);
            this.type = Type;
        }

        public Type getType() {
            return type;
        }
    }

    public static class SaveEvent extends TypeFormEvent {
        public SaveEvent(TypeForm source, Type Type) {
            super(source, Type);
        }
    }

    public static class DeleteEvent extends TypeFormEvent {
        public DeleteEvent(TypeForm source, Type Type) {
            super(source, Type);
        }
    }

    public static class CloseEvent extends TypeFormEvent {
        public CloseEvent(TypeForm source) {
            super(source, null);
        }
    }
}
