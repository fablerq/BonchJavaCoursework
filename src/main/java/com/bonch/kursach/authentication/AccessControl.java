package com.bonch.kursach.authentication;

import java.io.Serializable;

public interface AccessControl extends Serializable {

    String ADMIN_ROLE_NAME = "admin";
    String ADMIN_USERNAME = "admin";

    boolean signIn(String username, String password);

    boolean isUserSignedIn();

    boolean isUserAdmin();

    String getPrincipalName();

    void signOut();
}
