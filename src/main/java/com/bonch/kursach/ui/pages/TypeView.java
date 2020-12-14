package com.bonch.kursach.ui.pages;

import com.bonch.kursach.entities.Type;
import com.bonch.kursach.services.TypeService;
import com.bonch.kursach.ui.MainLayout;
import com.bonch.kursach.ui.forms.TypeForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@PageTitle("Типы кораблей")
@Route(value = "types", layout = MainLayout.class)
public class TypeView extends Main {

    @Autowired
    private TypeService typeService;

    private Grid<Type> typesGrid;
    private TypeForm typeForm;
    private Button addButton;

    public TypeView(TypeService typeService) {
        configureGrid();
        configureButton();
        configureForm();
        closeEditor();
        typesGrid.setItems(typeService.getAll());

        add(addButton, typeForm, typesGrid);
    }

    private void configureButton() {
        addButton = new Button("Добавить тип корабля");
        addButton.addClickListener(click -> addType());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureGrid() {
        typesGrid = new Grid<>(Type.class);
        typesGrid.setColumns("typeId", "title");
        typesGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        typesGrid.asSingleSelect().addValueChangeListener(event -> editType(event.getValue()));
    }

    private void configureForm() {
        typeForm = new TypeForm();
        typeForm.addListener(TypeForm.SaveEvent.class, this::saveType);
        typeForm.addListener(TypeForm.DeleteEvent.class, this::deleteType);
        typeForm.addListener(TypeForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteType(TypeForm.DeleteEvent event) {
        typeService.delete(event.getType());
        typesGrid.setItems(typeService.getAll());
        closeEditor();
    }

    private void saveType(TypeForm.SaveEvent event) {
        if (event.getType().getTitle().isEmpty()) {
            errorNotification("Неверно заполнены поля!");
        } else {
            typeService.save(event.getType());
            typesGrid.setItems(typeService.getAll());
            closeEditor();
        }
    }

    private void addType() {
        typesGrid.asSingleSelect().clear();
        editType(new Type());
    }

    private void editType(Type type) {
        if (type == null) {
            closeEditor();
        } else {
            typeForm.setType(type);
            typeForm.setVisible(true);
        }
    }

    private void closeEditor() {
        typeForm.setType(null);
        typeForm.setVisible(false);
    }

}
