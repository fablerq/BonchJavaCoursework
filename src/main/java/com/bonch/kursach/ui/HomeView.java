package com.bonch.kursach.ui;

import com.bonch.kursach.ui.pages.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Информационная панель")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends Main {

	public HomeView() {
		add(new Section(new Paragraph(
				"Это приложение демонстрирует пример работы информационной системы по навигации морских судов"),
				new Span("Исходный код приложения находится "),
				new Anchor("https://github.com/vaadin/vaadin-rest-example", "здесь.")));

		add(new Section(
				new H2(new RouterLink("Ships Dashboard", ShipView.class)),
				new Paragraph(
						"Информационная панель с имеющимися кораблями в наличии")));

		add(new Section(
				new H2(new RouterLink("Locations Dashboard", LocationView.class)),
				new Paragraph(
						"Информационная панель с последними данными по местанахождению кораблей")));

		add(new Section(
				new H2(new RouterLink("Routes Dashboard", RouteView.class)),
				new Paragraph(
						"Информационная панель с маршрутами кораблей")));

		add(new Section(
				new H2(new RouterLink("Countries Dashboard", CountryView.class)),
				new Paragraph(
						"Информационная панель с данными по имеющимся странам в системе")));

		add(new Section(
				new H2(new RouterLink("Types Dashboard", TypeView.class)),
				new Paragraph(
						"Информационная панель с данными по имеющимся типам кораблей")));

	}
}
