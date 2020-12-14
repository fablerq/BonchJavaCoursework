package com.bonch.kursach.ui.pages;

import com.bonch.kursach.entities.Location;
import com.bonch.kursach.entities.Ship;
import com.bonch.kursach.services.LocationService;
import com.bonch.kursach.services.ShipService;
import com.bonch.kursach.ui.MainLayout;
import com.bonch.kursach.ui.forms.LocationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@PageTitle("Местоположения кораблей")
@Route(value = "locations", layout = MainLayout.class)
public class LocationView extends Main {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ShipService shipService;

    private Grid<Location> locationGrid;
    private LocationForm locationForm;
    private Button addButton;

    public LocationView(LocationService locationService, ShipService shipService) {
        configureGrid();
        configureButton();
        configureForm(shipService.getAll());
        closeEditor();
        locationGrid.setItems(locationService.getAll());

        add(addButton, locationForm, locationGrid);
    }

    private void configureButton() {
        addButton = new Button("Добавить местоположение корабля");
        addButton.addClickListener(click -> addLocation());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureGrid() {
        locationGrid = new Grid<>(Location.class);
        locationGrid.setColumns("locationId", "longitude", "latitude");

        locationGrid.addColumn(x -> x.getShip().getTitle()).setHeader("ship");

        locationGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        locationGrid.asSingleSelect().addValueChangeListener(event -> editLocation(event.getValue()));
    }

    private void configureForm(List<Ship> ships) {
        locationForm = new LocationForm(ships);
        locationForm.addListener(LocationForm.SaveEvent.class, this::saveLocation);
        locationForm.addListener(LocationForm.DeleteEvent.class, this::deleteLocation);
        locationForm.addListener(LocationForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteLocation(LocationForm.DeleteEvent event) {
        locationService.delete(event.getLocation());
        locationGrid.setItems(locationService.getAll());
        closeEditor();
    }

    private void saveLocation(LocationForm.SaveEvent event) {
        if (event.getLocation().getLatitude() == null || event.getLocation().getLongitude() == null) {
            errorNotification("Неверно заполнены поля!");
        } else {
            locationService.save(event.getLocation());
            locationGrid.setItems(locationService.getAll());
            closeEditor();
        }
    }

    private void addLocation() {
        locationGrid.asSingleSelect().clear();
        editLocation(new Location());
    }

    private void editLocation(Location location) {
        if (location == null) {
            closeEditor();
        } else {
            locationForm.setLocation(location);
            locationForm.setVisible(true);
        }
    }

    private void closeEditor() {
        locationForm.setLocation(null);
        locationForm.setVisible(false);
    }

}
