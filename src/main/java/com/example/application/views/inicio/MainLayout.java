package com.example.application.views.inicio;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        // Botón de cerrar sesión
        Button btnCerrarSesion = new Button("Cerrar Sesión", event -> cerrarSesion());
        btnCerrarSesion.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);

        // Mostrar nombre de usuario si está autenticado
        String usuario = VaadinSession.getCurrent().getAttribute("usuario") != null
                ? (String) VaadinSession.getCurrent().getAttribute("usuario")
                : "";

        Span usuarioSpan = new Span("👤 " + usuario);
        usuarioSpan.getStyle().set("margin-right", "10px");

        HorizontalLayout userLayout = new HorizontalLayout(usuarioSpan, btnCerrarSesion);
        userLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        userLayout.setSpacing(true);

        // Solo mostrar si está autenticado
        Boolean autenticado = VaadinSession.getCurrent().getAttribute("autenticado") != null
                ? (Boolean) VaadinSession.getCurrent().getAttribute("autenticado")
                : false;
        userLayout.setVisible(autenticado);

        addToNavbar(true, toggle, viewTitle, userLayout);
    }

    private void cerrarSesion() {
        VaadinSession.getCurrent().setAttribute("usuario", null);
        VaadinSession.getCurrent().setAttribute("autenticado", null);

        Notification.show("Sesión cerrada exitosamente");

        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    private void addDrawerContent() {
        Span appName = new Span("Proyecto Integrador");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.add(new Span("© 2025 Proyecto Integrador - Todos los derechos reservados"));
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}