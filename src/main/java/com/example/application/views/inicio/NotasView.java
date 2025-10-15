package com.example.application.views.inicio;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Notas")
@Route(value = "notas", layout = MainLayout.class)
@Menu(order = 1, icon = LineAwesomeIconUrl.BOOK_SOLID)
public class NotasView extends VerticalLayout {

    private final Grid<Nota> grid = new Grid<>(Nota.class, false);
    private final List<Nota> notas = new ArrayList<>();

    public NotasView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Gestión de Notas");
        titulo.getStyle().set("text-align", "center");

        grid.addColumn(Nota::getEstudiante).setHeader("Estudiante");
        grid.addColumn(Nota::getAsignatura).setHeader("Asignatura");
        grid.addColumn(Nota::getCalificacion).setHeader("Calificación");
        grid.setItems(notas);
        grid.setWidthFull();

        TextField nombre = new TextField("Nombre del estudiante");
        TextField materia = new TextField("Asignatura");
        NumberField nota = new NumberField("Calificación (0 a 5)");
        nota.setStep(0.1);
        nota.setMin(0);
        nota.setMax(5);

        Button guardar = new Button("Guardar nota", event -> {
            if (nombre.isEmpty() || materia.isEmpty() || nota.isEmpty()) {
                Notification.show("Por favor completa todos los campos");
                return;
            }
            Nota nueva = new Nota(nombre.getValue(), materia.getValue(), nota.getValue());
            notas.add(nueva);
            grid.setItems(notas);
            nombre.clear();
            materia.clear();
            nota.clear();
            Notification.show("Nota guardada con éxito");
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout form = new FormLayout(nombre, materia, nota, guardar);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));

        add(titulo, form, grid);
    }

    public static class Nota {
        private String estudiante;
        private String asignatura;
        private Double calificacion;

        public Nota() {
        }

        public Nota(String estudiante, String asignatura, Double calificacion) {
            this.estudiante = estudiante;
            this.asignatura = asignatura;
            this.calificacion = calificacion;
        }

        public String getEstudiante() {
            return estudiante;
        }

        public String getAsignatura() {
            return asignatura;
        }

        public Double getCalificacion() {
            return calificacion;
        }
    }
}