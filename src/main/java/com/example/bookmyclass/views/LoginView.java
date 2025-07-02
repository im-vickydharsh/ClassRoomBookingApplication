package com.example.bookmyclass.views;

import com.example.bookmyclass.entity.User;
import com.example.bookmyclass.repository.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout {

    private final UserRepository userRepository;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    @Autowired
    public LoginView(UserRepository userRepository) {
        this.userRepository = userRepository;

        // Set full viewport height
        setSizeFull();

        // Background image and styling
        getStyle().set("background-image", "url('images/background2.jpg')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        // Heading with gradient effect
        H1 heading = new H1("Login");
        heading.getStyle()
               .set("color", "blue")
               .set("-webkit-background-clip", "text")
               .set("-moz-background-clip", "text")
               .set("background-clip", "text");

        // Input fields with modern styling
        usernameField = new TextField("Username");
        usernameField.setWidth("300px");
        usernameField.getStyle()
                     .set("padding", "10px")
                     .set("border", "2px solid #4facfe")
                     .set("border-radius", "5px");

        passwordField = new PasswordField("Password");
        passwordField.setWidth("300px");
        passwordField.getStyle()
                     .set("padding", "10px")
                     .set("border", "2px solid #4facfe")
                     .set("border-radius", "5px");

        // Button with hover and focus effects
        loginButton = new Button("Login", e -> handleLogin());
        loginButton.getStyle()
                   .set("cursor", "pointer")
                   .set("background", "#4facfe")
                   .set("color", "white")
                   .set("border", "none")
                   .set("padding", "10px 20px")
                   .set("border-radius", "25px")
                   .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)")
                   .set("transition", "transform 0.2s, box-shadow 0.2s");

        loginButton.addClickListener(e -> loginButton.getStyle()
                                                     .set("box-shadow", "0 6px 8px rgba(0, 0, 0, 0.2)")
                                                     .set("transform", "scale(1.05)"));

        loginButton.addFocusListener(e -> loginButton.getStyle()
                                                     .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.3)"));

        // Layout container for form elements
        VerticalLayout formLayout = new VerticalLayout(heading, usernameField, passwordField, loginButton);
        formLayout.getStyle()
                  .set("background", "rgba(255, 255, 255, 0.85)")
                  .set("padding", "30px")
                  .set("border-radius", "10px")
                  .set("box-shadow", "0 8px 15px rgba(0, 0, 0, 0.2)");
        formLayout.setSpacing(true);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Center form on the page
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(formLayout);
    }

    private void handleLogin() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            login(user.getId());
            if ("STUDENT".equals(user.getRole())) {
                getUI().ifPresent(ui -> ui.navigate("student-dashboard"));
            } else if ("APPROVER".equals(user.getRole())) {
                getUI().ifPresent(ui -> ui.navigate("approver-dashboard"));
            }
        } else {
            Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
        }
    }

    public void login(Long userId) {
        UI.getCurrent().getSession().setAttribute("userId", userId);
        System.out.println("User ID set in session: " + userId);
        Notification.show("User ID set in session: " + userId, 3000, Notification.Position.MIDDLE);
    }
}
