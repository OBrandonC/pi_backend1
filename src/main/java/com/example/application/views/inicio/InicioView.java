package com.example.application.views.inicio;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
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

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
