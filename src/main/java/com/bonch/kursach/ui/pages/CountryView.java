package com.bonch.kursach.ui.pages;

import com.bonch.kursach.entities.Country;
import com.bonch.kursach.services.CountryService;
import com.bonch.kursach.ui.MainLayout;
import com.bonch.kursach.ui.forms.CountryForm;
import com.bonch.kursach.ui.forms.LocationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@PageTitle("Страны")
@Route(value = "countries", layout = MainLayout.class)
public class CountryView extends Main {

    @Autowired
    private CountryService countryService;

    private Grid<Country> countryGrid;
    private CountryForm countryForm;
    private Button addButton;

    public CountryView(CountryService countryService) {
        configureGrid();
        configureButton();
        configureForm();
        closeEditor();
        countryGrid.setItems(countryService.getAll());

        add(addButton, countryForm, countryGrid);
    }

    private void configureButton() {
        addButton = new Button("Добавить страну");
        addButton.addClickListener(click -> addCountry());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureGrid() {
        countryGrid = new Grid<>(Country.class);
        countryGrid.setColumns("countryId", "title");
        countryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        countryGrid.asSingleSelect().addValueChangeListener(event -> editCountry(event.getValue()));
    }

    private void configureForm() {
        countryForm = new CountryForm();
        countryForm.addListener(CountryForm.SaveEvent.class, this::saveCountry);
        countryForm.addListener(CountryForm.DeleteEvent.class, this::deleteCountry);
        countryForm.addListener(CountryForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteCountry(CountryForm.DeleteEvent event) {
        countryService.delete(event.getCountry());
        countryGrid.setItems(countryService.getAll());
        closeEditor();
    }

    private void saveCountry(CountryForm.SaveEvent event) {
        if (event.getCountry().getTitle().isEmpty()) {
            errorNotification("Неверно заполнены поля!");
        } else {
            countryService.save(event.getCountry());
            countryGrid.setItems(countryService.getAll());
            closeEditor();
        }
    }

    private void addCountry() {
        countryGrid.asSingleSelect().clear();
        editCountry(new Country());
    }

    private void editCountry(Country country) {
        if (country == null) {
            closeEditor();
        } else {
            countryForm.setCountry(country);
            countryForm.setVisible(true);
        }
    }

    private void closeEditor() {
        countryForm.setCountry(null);
        countryForm.setVisible(false);
    }

}
