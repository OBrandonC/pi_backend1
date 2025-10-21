
package com.example.application.views.inicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Login Docente")
@Route(value = "login")
public class LoginView extends VerticalLayout {

    private TextField usuario;
    private PasswordField contrasena;
    private Button btnLogin;

    public LoginView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("background-color", "#f0f4f8");

        // Crear contenedor
        VerticalLayout loginForm = new VerticalLayout();
        loginForm.setWidth("400px");
        loginForm.setPadding(true);
        loginForm.setSpacing(true);
        loginForm.getStyle()
                .set("background-color", "white")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)");

        H1 titulo = new H1("Acceso Docente");
        titulo.getStyle().set("margin", "0").set("color", "#1e40af");

        Paragraph descripcion = new Paragraph("Ingresa tus credenciales para continuar");
        descripcion.getStyle().set("color", "#6b7280").set("margin-top", "0");

        usuario = new TextField("Usuario");
        usuario.setPlaceholder("Ingresa tu usuario");
        usuario.setWidthFull();
        usuario.setClearButtonVisible(true);

        contrasena = new PasswordField("Contraseña");
        contrasena.setPlaceholder("Ingresa tu contraseña");
        contrasena.setWidthFull();
        contrasena.setClearButtonVisible(true);

        btnLogin = new Button("Iniciar Sesión", event -> login());
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLogin.setWidthFull();

        Paragraph ayuda = new Paragraph("Usuario de prueba: docente / Contraseña: 1234");
        ayuda.getStyle()
                .set("font-size", "12px")
                .set("color", "#9ca3af")
                .set("margin-top", "10px");

        loginForm.add(titulo, descripcion, usuario, contrasena, btnLogin, ayuda);
        add(loginForm);
    }

    private void login() {
        String user = usuario.getValue().trim();
        String pass = contrasena.getValue().trim();

        // Validación simple (en producción usar base de datos)
        if (user.equals("docente") && pass.equals("1234")) {
            VaadinSession.getCurrent().setAttribute("usuario", user);
            VaadinSession.getCurrent().setAttribute("autenticado", true);

            Notification notification = Notification.show("Bienvenido, " + user);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            getUI().ifPresent(ui -> ui.navigate("registro-estudiantes"));
        } else {
            Notification notification = Notification.show("Usuario o contraseña incorrectos");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(3000);
        }
    }
}