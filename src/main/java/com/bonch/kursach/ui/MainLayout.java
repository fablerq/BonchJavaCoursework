package com.bonch.kursach.ui;

import com.bonch.kursach.authentication.AccessControl;
import com.bonch.kursach.authentication.AccessControlFactory;
import com.bonch.kursach.ui.pages.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

@CssImport("./styles/shared-styles.css")
@PWA(name = "Kursach", shortName = "Kursach", enableInstallPrompt = false)
public class MainLayout extends AppLayout implements AfterNavigationObserver {

	private final H1 pageTitle;

	private final RouterLink home;
	private final RouterLink shipsRouterLink;
	private final RouterLink locationsRouterLink;
	private final RouterLink routersRouterLink;
	private final RouterLink typesRouterLink;
	private final RouterLink countriesRouterLink;

	public MainLayout() {
		home = new RouterLink("Главная", HomeViewAdmin.class);
		shipsRouterLink = new RouterLink("Корабли", ShipView.class);
		locationsRouterLink = new RouterLink("Местонахождения", LocationView.class);
		routersRouterLink = new RouterLink("Маршруты", RouteView.class);
		typesRouterLink = new RouterLink("Типы кораблей", TypeView.class);
		countriesRouterLink = new RouterLink("Страны", CountryView.class);

		UnorderedList list;

		final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
		if (accessControl.isUserAdmin()) {
			 list = new UnorderedList(
					new ListItem(home), new ListItem(shipsRouterLink), new ListItem(locationsRouterLink),
					new ListItem(routersRouterLink), new ListItem(typesRouterLink), new ListItem(countriesRouterLink));
		} else { list = new UnorderedList(new ListItem(new RouterLink("Корабли для Клиента", RestrictedShipView.class))); }

		final Nav navigation = new Nav(list);

		addToDrawer(navigation);
		setPrimarySection(Section.DRAWER);
		setDrawerOpened(true);

		pageTitle = new H1("Home");
		final Header header = new Header(new DrawerToggle(), pageTitle);
		addToNavbar(header);
	}

	private RouterLink[] getRouterLinks() {
		return new RouterLink[] { home, shipsRouterLink, locationsRouterLink, routersRouterLink,
				typesRouterLink, countriesRouterLink};
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		for (final RouterLink routerLink : getRouterLinks()) {
			if (routerLink.getHighlightCondition().shouldHighlight(routerLink, event)) {
				pageTitle.setText(routerLink.getText());
			}
		}
	}


}
