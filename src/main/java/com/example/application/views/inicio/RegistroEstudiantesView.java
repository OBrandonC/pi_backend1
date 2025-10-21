package com.example.application.views.inicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Registro de Estudiantes")
@Route(value = "registro-estudiantes", layout = MainLayout.class)
@Menu(order = 2, icon = LineAwesomeIconUrl.USER_PLUS_SOLID)
public class RegistroEstudiantesView extends VerticalLayout implements BeforeEnterObserver {

    private final Grid<Estudiante> grid = new Grid<>(Estudiante.class, false);
    private static final List<Estudiante> estudiantes = new ArrayList<>();

    private TextField idField;
    private TextField nombreField;
    private TextField apellidoField;
    private TextField materiaField;

    public RegistroEstudiantesView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Registro de Estudiantes");
        titulo.getStyle().set("text-align", "center").set("color", "#1e40af");

        // Formulario de registro
        idField = new TextField("ID del Estudiante");
        idField.setPlaceholder("Ej: 2024001");
        idField.setRequired(true);

        nombreField = new TextField("Nombre");
        nombreField.setPlaceholder("Ej: Juan");
        nombreField.setRequired(true);

        apellidoField = new TextField("Apellido");
        apellidoField.setPlaceholder("Ej: Pérez");
        apellidoField.setRequired(true);

        materiaField = new TextField("Materia");
        materiaField.setPlaceholder("Ej: Matemáticas");
        materiaField.setRequired(true);

        Button btnGuardar = new Button("Registrar Estudiante", event -> registrarEstudiante());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnLimpiar = new Button("Limpiar", event -> limpiarFormulario());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botones = new HorizontalLayout(btnGuardar, btnLimpiar);
        botones.setSpacing(true);

        FormLayout form = new FormLayout(idField, nombreField, apellidoField, materiaField, botones);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));
        form.setColspan(botones, 2);

        // Grid de estudiantes
        grid.addColumn(Estudiante::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Estudiante::getApellido).setHeader("Apellido").setAutoWidth(true);
        grid.addColumn(Estudiante::getMateria).setHeader("Materia").setAutoWidth(true);
        grid.addColumn(Estudiante::getCantidadNotas).setHeader("Notas Registradas").setAutoWidth(true);

        grid.addComponentColumn(estudiante -> {
            Button btnEliminar = new Button("Eliminar", event -> eliminarEstudiante(estudiante));
            btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return btnEliminar;
        }).setHeader("Acciones").setAutoWidth(true);

        grid.setItems(estudiantes);
        grid.setWidthFull();

        add(titulo, form, grid);
    }

    private void registrarEstudiante() {
        String id = idField.getValue().trim();
        String nombre = nombreField.getValue().trim();
        String apellido = apellidoField.getValue().trim();
        String materia = materiaField.getValue().trim();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || materia.isEmpty()) {
            Notification notification = Notification.show("Por favor completa todos los campos");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Verificar si el ID ya existe
        boolean existe = estudiantes.stream().anyMatch(e -> e.getId().equals(id));
        if (existe) {
            Notification notification = Notification.show("Ya existe un estudiante con ese ID");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Estudiante nuevoEstudiante = new Estudiante(id, nombre, apellido, materia);
        estudiantes.add(nuevoEstudiante);
        grid.setItems(estudiantes);

        Notification notification = Notification.show("Estudiante registrado exitosamente");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        limpiarFormulario();
    }

    private void limpiarFormulario() {
        idField.clear();
        nombreField.clear();
        apellidoField.clear();
        materiaField.clear();
    }

    private void eliminarEstudiante(Estudiante estudiante) {
        estudiantes.remove(estudiante);
        grid.setItems(estudiantes);

        Notification notification = Notification.show(
                "Estudiante " + estudiante.getNombreCompleto() + " eliminado exitosamente");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static List<Estudiante> getEstudiantes() {
        return estudiantes;
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
}