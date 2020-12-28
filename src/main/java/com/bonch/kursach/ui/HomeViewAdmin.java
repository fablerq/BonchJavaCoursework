package com.bonch.kursach.ui;

import com.bonch.kursach.authentication.AccessControl;
import com.bonch.kursach.authentication.AccessControlFactory;
import com.bonch.kursach.ui.pages.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;

@PageTitle("Информационная панель")
@Route(value = "", layout = MainLayout.class)
public class HomeViewAdmin extends Main implements BeforeEnterObserver {

	public HomeViewAdmin() {
		add(new Section(new Paragraph(
				"Это приложение демонстрирует пример работы информационной системы по навигации морских судов"),
				new H1("Добро пожаловать, Администратор!"),
				new Span("Исходный код приложения находится "),
				new Anchor("https://github.com/fablerq/BonchJavaCoursework", "здесь.")));

		add(new Section(
				new H2(new RouterLink("Контрольная панель кораблей", ShipView.class)),
				new Paragraph(
						"Информационная панель с имеющимися кораблями в наличии")));

		add(new Section(
				new H2(new RouterLink("Контрольная панель с местонахождениями", LocationView.class)),
				new Paragraph(
						"Информационная панель с последними данными по местанахождению кораблей")));

		add(new Section(
				new H2(new RouterLink("Контрольная панель с маршрутами", RouteView.class)),
				new Paragraph(
						"Информационная панель с маршрутами кораблей")));

		add(new Section(
				new H2(new RouterLink("Контрольная панель со странами", CountryView.class)),
				new Paragraph(
						"Информационная панель с данными по имеющимся странам в системе")));

		add(new Section(
				new H2(new RouterLink("Констрольная панель с типами кораблей", TypeView.class)),
				new Paragraph(
						"Информационная панель с данными по имеющимся типам кораблей")));

	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
		if (!accessControl.isUserSignedIn() || !accessControl.isUserAdmin()) {
			event.rerouteTo(LoginScreen.class);
		}
	}
}
