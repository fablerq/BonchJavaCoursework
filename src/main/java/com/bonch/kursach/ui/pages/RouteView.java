package com.bonch.kursach.ui.pages;

import com.bonch.kursach.entities.Route;
import com.bonch.kursach.services.RouteService;
import com.bonch.kursach.ui.MainLayout;
import com.bonch.kursach.ui.forms.RouteForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@PageTitle("Маршруты кораблей")
@com.vaadin.flow.router.Route(value = "routes", layout = MainLayout.class)
public class RouteView extends Main {

    @Autowired
    private RouteService routeService;

    private Grid<Route> routesGrid;
    private RouteForm routeForm;
    private Button addButton;

    public RouteView(RouteService routeService) {
        configureGrid();
        configureButton();
        configureForm();
        closeEditor();
        routesGrid.setItems(routeService.getAll());

        add(addButton, routeForm, routesGrid);
    }

    private void configureButton() {
        addButton = new Button("Добавить маршрут корабля");
        addButton.addClickListener(click -> addRoute());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void configureGrid() {
        routesGrid = new Grid<>(Route.class);
        routesGrid.setColumns("routeId", "source", "destination");
        routesGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        routesGrid.asSingleSelect().addValueChangeListener(event -> editRoute(event.getValue()));
    }

    private void configureForm() {
        routeForm = new RouteForm();
        routeForm.addListener(RouteForm.SaveEvent.class, this::saveRoute);
        routeForm.addListener(RouteForm.DeleteEvent.class, this::deleteRoute);
        routeForm.addListener(RouteForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteRoute(RouteForm.DeleteEvent event) {
        routeService.delete(event.getRoute());
        routesGrid.setItems(routeService.getAll());
        closeEditor();
    }

    private void saveRoute(RouteForm.SaveEvent event) {
        if (event.getRoute().getSource().isEmpty() || event.getRoute().getDestination().isEmpty()) {
            errorNotification("Неверно заполнены поля!");
        } else {
            routeService.save(event.getRoute());
            routesGrid.setItems(routeService.getAll());
            closeEditor();
        }
    }

    private void addRoute() {
        routesGrid.asSingleSelect().clear();
        editRoute(new Route());
    }

    private void editRoute(Route route) {
        if (route == null) {
            closeEditor();
        } else {
            routeForm.setRoute(route);
            routeForm.setVisible(true);
        }
    }

    private void closeEditor() {
        routeForm.setRoute(null);
        routeForm.setVisible(false);
    }

}
