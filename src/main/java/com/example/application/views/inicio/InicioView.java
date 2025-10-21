package com.example.application.views.inicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Inicio")
@Route(value = "", layout = MainLayout.class)
@Menu(order = 0, icon = LineAwesomeIconUrl.HOME_SOLID)
public class InicioView extends VerticalLayout {

    public InicioView() {
        setSpacing(false);

        Image img = new Image("https://cdn-icons-png.flaticon.com/512/3135/3135768.png", "Logo Institucional");
        img.setWidth("120px");
        add(img);

        H2 header = new H2("Bienvenido al Sistema de Notas Institucional");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        add(new Paragraph("Gestiona y consulta las calificaciones de los estudiantes de manera fácil y rápida."));

        // Verificar si está autenticado
        Boolean autenticado = VaadinSession.getCurrent().getAttribute("autenticado") != null
                ? (Boolean) VaadinSession.getCurrent().getAttribute("autenticado")
                : false;

        if (autenticado) {
            String usuario = (String) VaadinSession.getCurrent().getAttribute("usuario");
            Paragraph bienvenida = new Paragraph("👋 Bienvenido, " + usuario);
            bienvenida.getStyle()
                    .set("font-size", "18px")
                    .set("color", "#1e40af")
                    .set("font-weight", "600");
            add(bienvenida);

            Button btnComenzar = new Button("Ir a Registro de Estudiantes", event -> {
                getUI().ifPresent(ui -> ui.navigate("registro-estudiantes"));
            });
            btnComenzar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
            add(btnComenzar);
        } else {
            Paragraph info = new Paragraph("Para acceder a las funcionalidades del sistema, debes iniciar sesión.");
            info.getStyle().set("color", "#6b7280");
            add(info);

            Button btnLogin = new Button("Iniciar Sesión", event -> {
                getUI().ifPresent(ui -> ui.navigate("login"));
            });
            btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
            add(btnLogin);
        }

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}