package com.victorylimited.hris.views.compenben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;

import com.vaadin.flow.router.Route;
import com.victorylimited.hris.dtos.compenben.LeaveBenefitsDTO;
import com.victorylimited.hris.services.compenben.LeaveBenefitsService;
import com.victorylimited.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Leave Benefits List")
@Route(value = "leave-benefits-list", layout = MainLayout.class)
public class LeaveBenefitsListView extends VerticalLayout {
    @Resource private final LeaveBenefitsService leaveBenefitsService;

    private Grid<LeaveBenefitsDTO> leaveBenefitsDTOGrid;
    private TextField searchFilterTextField;

    public LeaveBenefitsListView(LeaveBenefitsService leaveBenefitsService) {
        this.leaveBenefitsService = leaveBenefitsService;

        this.add(buildHeaderToolbar(), buildLeaveBenefitsDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public HorizontalLayout buildHeaderToolbar() {
        HorizontalLayout headerToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateLeaveBenefitsDTOGrid());

        Button addButton = new Button("Add Leave Benefit");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(LeaveBenefitsFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<LeaveBenefitsDTO> buildLeaveBenefitsDTOGrid() {
        leaveBenefitsDTOGrid = new Grid<>(LeaveBenefitsDTO.class, false);

        leaveBenefitsDTOGrid.addColumn(leaveBenefitsDTO -> leaveBenefitsDTO.getEmployeeDTO().getEmployeeNumber())
                            .setHeader("Employee No.")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(leaveBenefitsDTO -> leaveBenefitsDTO.getEmployeeDTO().getFirstName()
                                                                                            .concat(" ")
                                                                                            .concat(leaveBenefitsDTO.getEmployeeDTO().getLastName())
                                                                                            .concat(leaveBenefitsDTO.getEmployeeDTO().getSuffix() != null ? leaveBenefitsDTO.getEmployeeDTO().getSuffix() : ""))
                            .setHeader("Employee Name")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveCode)
                            .setHeader("Leave Code")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveType)
                            .setHeader("Leave Type")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveCount)
                            .setHeader("Leave Count")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveForYear)
                            .setHeader("For Year")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, leaveBenefitsDTO) -> {
                                            String theme = String.format("badge %s", leaveBenefitsDTO.isLeaveActive() ? "success" : "error");

                                            Span activeSpan = new Span();
                                            activeSpan.getElement().setAttribute("theme", theme);
                                            activeSpan.setText(leaveBenefitsDTO.isLeaveActive() ? "Yes" : "No");

                                            layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                            layout.add(activeSpan);
                                        }))
                            .setHeader("Is Leave Active?")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        leaveBenefitsDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                              GridVariant.LUMO_COLUMN_BORDERS,
                                              GridVariant.LUMO_WRAP_CELL_CONTENT);
        leaveBenefitsDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        leaveBenefitsDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        leaveBenefitsDTOGrid.setAllRowsVisible(true);
        leaveBenefitsDTOGrid.setEmptyStateText("No benefit records found.");
        leaveBenefitsDTOGrid.setItems((query -> leaveBenefitsService.getAll(query.getPage(), query.getPageSize()).stream()));

        return leaveBenefitsDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Leave Benefit");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LeaveBenefitsDTO selectedLeaveBenefitsDTO = leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LeaveBenefitsDetailsView.class, selectedLeaveBenefitsDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Leave Benefit");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LeaveBenefitsDTO selectedLeaveBenefitsDTO = leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LeaveBenefitsFormView.class, selectedLeaveBenefitsDTO.getId().toString());
            }
        }));

        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Employee Rates");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        rowToolbarLayout.add(viewButton, editButton, deleteButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateLeaveBenefitsDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            leaveBenefitsDTOGrid.setItems(leaveBenefitsService.findByParameter(searchFilterTextField.getValue()));
        } else {
            leaveBenefitsDTOGrid.setItems(query -> leaveBenefitsService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
