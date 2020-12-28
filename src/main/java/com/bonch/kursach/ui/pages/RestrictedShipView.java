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
@Route(value = "read_ships", layout = MainLayout.class)
public class RestrictedShipView extends Main {

	private Grid<Ship> shipsGrid;

	public RestrictedShipView(ShipService shipService, CountryService countryService, TypeService typeService, RouteService routeService) {
		configureGrid();
		shipsGrid.setItems(shipService.getAll());

		add(shipsGrid);
	}

	private void configureGrid() {
		shipsGrid = new Grid<>(Ship.class);
		shipsGrid.setColumns("shipId", "title");

		shipsGrid.addColumn(x -> x.getCountry().getTitle()).setHeader("country");
		shipsGrid.addColumn(x -> x.getType().getTitle()).setHeader("type");
		shipsGrid.addColumn(x -> x.getRoute().getSource() + " - " + x.getRoute().getDestination()).setHeader("route");

		shipsGrid.getColumns().forEach(column -> column.setAutoWidth(true));
	}

}
