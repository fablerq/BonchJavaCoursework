package com.bonch.kursach.ui;

import com.bonch.kursach.authentication.AccessControl;
import com.bonch.kursach.authentication.AccessControlFactory;
import com.bonch.kursach.ui.pages.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;

@PageTitle("Информационная панель")
@Route(value = "client", layout = MainLayout.class)
public class HomeViewClient extends Main implements BeforeEnterObserver {

	public HomeViewClient() {
		add(new Section(new Paragraph(
				"Это приложение демонстрирует пример работы информационной системы по навигации морских судов"),
				new H1("Добро пожаловать, Клиент!"),
				new Span("Исходный код приложения находится "),
				new Anchor("https://github.com/fablerq/BonchJavaCoursework", "здесь.")));

		add(new Section(
				new H2(new RouterLink("Контрольная панель кораблей", RestrictedShipView.class)),
				new Paragraph(
						"Информационная панель с имеющимися кораблями в наличии")));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
		if (!accessControl.isUserSignedIn()) {
			event.rerouteTo(LoginScreen.class);
		}
	}
}
