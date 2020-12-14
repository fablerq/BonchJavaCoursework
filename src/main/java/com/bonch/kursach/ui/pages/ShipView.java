package com.bonch.kursach.ui.pages;

import com.bonch.kursach.entities.Country;
import com.bonch.kursach.entities.Ship;
import com.bonch.kursach.entities.Type;
import com.bonch.kursach.services.CountryService;
import com.bonch.kursach.services.RouteService;
import com.bonch.kursach.services.ShipService;
import com.bonch.kursach.services.TypeService;
import com.bonch.kursach.ui.MainLayout;
import com.bonch.kursach.ui.forms.ShipForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.bonch.kursach.utils.Notifications.errorNotification;

@PageTitle("Корабли")
@Route(value = "ships", layout = MainLayout.class)
public class ShipView extends Main {

	@Autowired
    private ShipService shipService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private RouteService routeService;

	private Grid<Ship> shipsGrid;
	private ShipForm shipForm;
	private Button addButton;

	public ShipView(ShipService shipService, CountryService countryService, TypeService typeService, RouteService routeService) {
		configureGrid();
		configureButton();
		configureForm(countryService.getAll(), typeService.getAll(), routeService.getAll());
		closeEditor();
		shipsGrid.setItems(shipService.getAll());

		add(addButton, shipForm, shipsGrid);
	}

	private void configureButton() {
		addButton = new Button("Добавить корабль");
		addButton.addClickListener(click -> addShip());
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	}

	private void configureGrid() {
		shipsGrid = new Grid<>(Ship.class);
		shipsGrid.setColumns("shipId");

		shipsGrid.addColumn(x -> x.getCountry().getTitle()).setHeader("country");
		shipsGrid.addColumn(x -> x.getType().getTitle()).setHeader("type");
		shipsGrid.addColumn(x -> x.getRoute().getSource() + " - " + x.getRoute().getDestination()).setHeader("route");

		shipsGrid.getColumns().forEach(column -> column.setAutoWidth(true));
		shipsGrid.asSingleSelect().addValueChangeListener(event -> editLocation(event.getValue()));
	}

	private void configureForm(List<Country> countries, List<Type> types, List<com.bonch.kursach.entities.Route> routes) {
		shipForm = new ShipForm(countries, types, routes);
		shipForm.addListener(ShipForm.SaveEvent.class, this::saveShip);
		shipForm.addListener(ShipForm.DeleteEvent.class, this::deleteShip);
		shipForm.addListener(ShipForm.CloseEvent.class, e -> closeEditor());
	}

	private void deleteShip(ShipForm.DeleteEvent event) {
		shipService.delete(event.getShip());
		shipsGrid.setItems(shipService.getAll());
		closeEditor();
	}

	private void saveShip(ShipForm.SaveEvent event) {
		if (event.getShip().getTitle().isEmpty()) {
			errorNotification("Неверно заполнены поля!");
		} else {
			shipService.save(event.getShip());
			shipsGrid.setItems(shipService.getAll());
			closeEditor();
		}
	}

	private void addShip() {
		shipsGrid.asSingleSelect().clear();
		editLocation(new Ship());
	}

	private void editLocation(Ship ship) {
		if (ship == null) {
			closeEditor();
		} else {
			shipForm.setShip(ship);
			shipForm.setVisible(true);
		}
	}

	private void closeEditor() {
		shipForm.setShip(null);
		shipForm.setVisible(false);
	}


}
