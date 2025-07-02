package com.example.bookmyclass.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomePage extends VerticalLayout {

    public HomePage() {
        // Set the size of the layout to take the full height of the viewport
        setSizeFull();
        
        // Add CSS styles for background image and center alignment
        getStyle().set("background-image", "url('images/background1.jpg')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        // Create the welcome button
        Button welcomeButton = new Button("Welcome to Book My Class", e -> {
            // Navigate to the login view when the button is clicked
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        
        // Apply CSS styles to the button
        welcomeButton.getStyle().set("cursor", "pointer"); // Change mouse pointer to hand
        welcomeButton.getStyle().set("background-color", "rgba(0, 0, 0, 0.5)"); // Transparent black background
        welcomeButton.getStyle().set("color", "white"); // Change text color to white
        welcomeButton.getStyle().set("border", "none"); // Remove border
        welcomeButton.getStyle().set("padding", "10px 20px"); // Add padding
        welcomeButton.getStyle().set("border-radius", "5px"); // Add rounded corners
        welcomeButton.getStyle().set("font-size", "24px"); // Increase font size

        // Center-align the button within the layout
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        // Add the button to the layout
        add(welcomeButton);
    }
}
