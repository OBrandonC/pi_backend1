package com.example.application.views.inicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Ingreso de Notas")
@Route(value = "ingreso-notas", layout = MainLayout.class)
@Menu(order = 3, icon = LineAwesomeIconUrl.EDIT_SOLID)
public class IngresoNotasView extends VerticalLayout implements BeforeEnterObserver {

    private ComboBox<Estudiante> estudianteCombo;
    private NumberField notaField;
    private Grid<NotaRegistro> gridNotas;
    private List<NotaRegistro> notasRegistradas;
    private Span infoEstudiante;

    public IngresoNotasView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        notasRegistradas = new ArrayList<>();

        H2 titulo = new H2("Ingreso de Notas");
        titulo.getStyle().set("text-align", "center").set("color", "#1e40af");

        // Información del estudiante seleccionado
        infoEstudiante = new Span();
        infoEstudiante.getStyle()
                .set("background-color", "#dbeafe")
                .set("padding", "10px")
                .set("border-radius", "5px")
                .set("display", "block")
                .set("margin-bottom", "10px");
        infoEstudiante.setVisible(false);

        // Formulario
        estudianteCombo = new ComboBox<>("Seleccionar Estudiante");
        estudianteCombo.setItems(RegistroEstudiantesView.getEstudiantes());
        estudianteCombo.setItemLabelGenerator(
                est -> est.getId() + " - " + est.getNombreCompleto() + " (" + est.getMateria() + ")");
        estudianteCombo.setPlaceholder("Selecciona un estudiante...");
        estudianteCombo.setWidthFull();
        estudianteCombo.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                actualizarInfoEstudiante(event.getValue());
                cargarNotasEstudiante(event.getValue());
            } else {
                infoEstudiante.setVisible(false);
                notasRegistradas.clear();
                gridNotas.setItems(notasRegistradas);
            }
        });

        notaField = new NumberField("Calificación");
        notaField.setPlaceholder("Ingresa la nota");
        notaField.setStep(0.1);
        notaField.setMin(0.0);
        notaField.setMax(5.0);
        notaField.setHelperText("Rango: 0.0 a 5.0");
        notaField.setWidthFull();

        Button btnAgregar = new Button("Agregar Nota", event -> agregarNota());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnLimpiar = new Button("Limpiar", event -> limpiar());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botones = new HorizontalLayout(btnAgregar, btnLimpiar);
        botones.setSpacing(true);

        FormLayout form = new FormLayout(estudianteCombo, notaField, botones);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));
        form.setColspan(botones, 2);

        // Grid de notas registradas
        gridNotas = new Grid<>(NotaRegistro.class, false);
        gridNotas.addColumn(NotaRegistro::getNumero).setHeader("#").setAutoWidth(true);
        gridNotas.addColumn(NotaRegistro::getNota).setHeader("Calificación").setAutoWidth(true);

        gridNotas.addComponentColumn(notaReg -> {
            Button btnEliminar = new Button("Eliminar", event -> eliminarNota(notaReg));
            btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return btnEliminar;
        }).setHeader("Acciones").setAutoWidth(true);

        gridNotas.setItems(notasRegistradas);
        gridNotas.setWidthFull();

        add(titulo, infoEstudiante, form, new H2("Notas Registradas"), gridNotas);
    }

    private void actualizarInfoEstudiante(Estudiante estudiante) {
        infoEstudiante.setText(String.format(
                "📚 Estudiante: %s | ID: %s | Materia: %s | Notas actuales: %d | Promedio: %.2f",
                estudiante.getNombreCompleto(),
                estudiante.getId(),
                estudiante.getMateria(),
                estudiante.getCantidadNotas(),
                estudiante.getPromedio()));
        infoEstudiante.setVisible(true);
    }

    private void cargarNotasEstudiante(Estudiante estudiante) {
        notasRegistradas.clear();
        List<Double> notas = estudiante.getNotas();
        for (int i = 0; i < notas.size(); i++) {
            notasRegistradas.add(new NotaRegistro(i + 1, notas.get(i)));
        }
        gridNotas.setItems(notasRegistradas);
    }

    private void agregarNota() {
        Estudiante estudiante = estudianteCombo.getValue();
        Double nota = notaField.getValue();

        if (estudiante == null) {
            Notification notification = Notification.show("Debes seleccionar un estudiante");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (nota == null || nota < 0.0 || nota > 5.0) {
            Notification notification = Notification.show("Ingresa una nota válida entre 0.0 y 5.0");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        estudiante.agregarNota(nota);
        cargarNotasEstudiante(estudiante);
        actualizarInfoEstudiante(estudiante);

        Notification notification = Notification.show("Nota agregada exitosamente");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        notaField.clear();
    }

    private void limpiar() {
        estudianteCombo.clear();
        notaField.clear();
        infoEstudiante.setVisible(false);
        notasRegistradas.clear();
        gridNotas.setItems(notasRegistradas);
    }

    private void eliminarNota(NotaRegistro notaReg) {
        Estudiante estudiante = estudianteCombo.getValue();
        if (estudiante == null) {
            return;
        }

        // Eliminar la nota del estudiante (índice es número - 1)
        estudiante.eliminarNota(notaReg.getNumero() - 1);

        // Recargar las notas y actualizar la información
        cargarNotasEstudiante(estudiante);
        actualizarInfoEstudiante(estudiante);

        Notification notification = Notification.show("Nota eliminada exitosamente");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Boolean autenticado = VaadinSession.getCurrent().getAttribute("autenticado") != null
                ? (Boolean) VaadinSession.getCurrent().getAttribute("autenticado")
                : false;

        if (!autenticado) {
            event.rerouteTo("login");
            Notification.show("Debes iniciar sesión para acceder");
        }
    }

    public static class NotaRegistro {
        private int numero;
        private double nota;

        public NotaRegistro(int numero, double nota) {
            this.numero = numero;
            this.nota = nota;
        }

        public int getNumero() {
            return numero;
        }

        public double getNota() {
            return nota;
        }
    }
}