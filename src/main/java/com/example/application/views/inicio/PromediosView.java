package com.example.application.views.inicio;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Promedios")
@Route(value = "promedios", layout = MainLayout.class)
@Menu(order = 4, icon = LineAwesomeIconUrl.CHART_BAR_SOLID)
public class PromediosView extends VerticalLayout implements BeforeEnterObserver {

    private final Grid<EstudiantePromedio> grid = new Grid<>(EstudiantePromedio.class, false);
    private TextField filtroField;
    private List<EstudiantePromedio> todosLosPromedios;

    public PromediosView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Promedios de Estudiantes");
        titulo.getStyle().set("text-align", "center").set("color", "#1e40af");

        // Filtro de búsqueda
        filtroField = new TextField();
        filtroField.setPlaceholder("Buscar por nombre, ID o materia...");
        filtroField.setPrefixComponent(new Span("🔍"));
        filtroField.setWidthFull();
        filtroField.setValueChangeMode(ValueChangeMode.LAZY);
        filtroField.addValueChangeListener(e -> aplicarFiltro(e.getValue()));

        // Configurar grid
        grid.addColumn(EstudiantePromedio::getId)
                .setHeader("ID")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(EstudiantePromedio::getNombreCompleto)
                .setHeader("Nombre Completo")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(EstudiantePromedio::getMateria)
                .setHeader("Materia")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(EstudiantePromedio::getCantidadNotas)
                .setHeader("Cantidad de Notas")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(EstudiantePromedio::getPromedioFormateado)
                .setHeader("Promedio")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(this::getEstadoAprobacion)
                .setHeader("Estado")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidthFull();

        // Cargar datos
        cargarPromedios();

        add(titulo, filtroField, grid);
    }

    private void cargarPromedios() {
        List<Estudiante> estudiantes = RegistroEstudiantesView.getEstudiantes();

        todosLosPromedios = estudiantes.stream()
                .map(est -> new EstudiantePromedio(
                        est.getId(),
                        est.getNombreCompleto(),
                        est.getMateria(),
                        est.getCantidadNotas(),
                        est.getPromedio()))
                .collect(Collectors.toList());

        grid.setItems(todosLosPromedios);

        if (todosLosPromedios.isEmpty()) {
            Notification.show("No hay estudiantes registrados. Registra estudiantes e ingresa notas primero.");
        }
    }

    private void aplicarFiltro(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            grid.setItems(todosLosPromedios);
            return;
        }

        String filtroLower = filtro.toLowerCase().trim();
        List<EstudiantePromedio> filtrados = todosLosPromedios.stream()
                .filter(ep -> ep.getId().toLowerCase().contains(filtroLower) ||
                        ep.getNombreCompleto().toLowerCase().contains(filtroLower) ||
                        ep.getMateria().toLowerCase().contains(filtroLower))
                .collect(Collectors.toList());

        grid.setItems(filtrados);
    }

    private String getEstadoAprobacion(EstudiantePromedio ep) {
        if (ep.getCantidadNotas() == 0) {
            return "Sin notas";
        }
        return ep.getPromedio() >= 3.0 ? "✅ Aprobado" : "❌ Reprobado";
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

    public static class EstudiantePromedio {
        private String id;
        private String nombreCompleto;
        private String materia;
        private int cantidadNotas;
        private double promedio;

        public EstudiantePromedio(String id, String nombreCompleto, String materia,
                int cantidadNotas, double promedio) {
            this.id = id;
            this.nombreCompleto = nombreCompleto;
            this.materia = materia;
            this.cantidadNotas = cantidadNotas;
            this.promedio = promedio;
        }

        public String getId() {
            return id;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public String getMateria() {
            return materia;
        }

        public int getCantidadNotas() {
            return cantidadNotas;
        }

        public double getPromedio() {
            return promedio;
        }

        public String getPromedioFormateado() {
            if (cantidadNotas == 0) {
                return "N/A";
            }
            return String.format("%.2f", promedio);
        }
    }
}