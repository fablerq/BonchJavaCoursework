package com.bonch.kursach.ui.pages;

import com.bonch.kursach.authentication.AccessControl;
import com.bonch.kursach.authentication.AccessControlFactory;
import com.bonch.kursach.ui.HomeViewAdmin;
import com.bonch.kursach.ui.HomeViewClient;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout implements BeforeEnterObserver {

    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("Hint: same as username"));

        // layout to center login form when there is sufficient screen space
        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        add(centeringLayout);
    }

    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            if (accessControl.isUserAdmin())
                getUI().get().navigate(HomeViewAdmin.class);
            else
                getUI().get().navigate(HomeViewClient.class);
        } else {
            event.getSource().setError(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (accessControl.isUserSignedIn()) {
            if (accessControl.isUserAdmin())
                getUI().get().navigate(HomeViewAdmin.class);
            else
                event.rerouteTo(HomeViewClient.class);
        }
    }
}
