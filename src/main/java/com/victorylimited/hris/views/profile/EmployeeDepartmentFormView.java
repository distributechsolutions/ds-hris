package com.victorylimited.hris.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.victorylimited.hris.dtos.admin.DepartmentDTO;
import com.victorylimited.hris.dtos.profile.EmployeeDTO;
import com.victorylimited.hris.dtos.profile.EmployeeDepartmentDTO;
import com.victorylimited.hris.services.admin.DepartmentService;
import com.victorylimited.hris.services.profile.EmployeeDepartmentService;
import com.victorylimited.hris.services.profile.EmployeeService;
import com.victorylimited.hris.utils.SecurityUtil;
import com.victorylimited.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR",
        "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Department Form")
@Route(value = "employee-department-form", layout = MainLayout.class)
public class EmployeeDepartmentFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeeDepartmentService employeeDepartmentService;
    @Resource
    private final EmployeeService employeeService;
    @Resource
    private final DepartmentService departmentService;

    private EmployeeDepartmentDTO employeeDepartmentDTO;
    private UUID parameterId;

    private final FormLayout employeeDepartmentDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private ComboBox<DepartmentDTO> departmentDTOComboBox;
    private Checkbox currentDepartmentCheckbox;

    public EmployeeDepartmentFormView(EmployeeDepartmentService employeeDepartmentService,
                                    EmployeeService employeeService,
                                    DepartmentService departmentService) {
        this.employeeDepartmentService = employeeDepartmentService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;

        setSizeFull();
        setMargin(true);
        add(employeeDepartmentDTOFormLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeDepartmentDTO = employeeDepartmentService.getById(parameterId);
        }

        buildEmployeeDepartmentFormLayout();
    }

    private void buildEmployeeDepartmentFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                employeeService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (employeeDepartmentDTO != null) employeeDTOComboBox.setValue(employeeDepartmentDTO.getEmployeeDTO());

        // Create the query object that will do the pagination of position records in the combo box component.
        Query<DepartmentDTO, Void> departmentQuery = new Query<>();

        departmentDTOComboBox = new ComboBox<>("Department");
        departmentDTOComboBox.setItems((departmentDTO, filterString) -> departmentDTO.getName().toLowerCase().contains(filterString.toLowerCase()),
                departmentService.getAll(departmentQuery.getPage(), departmentQuery.getPageSize()));
        departmentDTOComboBox.setItemLabelGenerator(DepartmentDTO::getName);
        departmentDTOComboBox.setClearButtonVisible(true);
        departmentDTOComboBox.setRequired(true);
        departmentDTOComboBox.setRequiredIndicatorVisible(true);
        if (employeeDepartmentDTO != null) departmentDTOComboBox.setValue(employeeDepartmentDTO.getDepartmentDTO());

        currentDepartmentCheckbox = new Checkbox("Is Current Department?");
        if (employeeDepartmentDTO != null) currentDepartmentCheckbox.setValue(employeeDepartmentDTO.isCurrentDepartment());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeeDepartmentDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(EmployeeDepartmentListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(EmployeeDepartmentListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("768px");
        buttonLayout.setPadding(true);

        employeeDepartmentDTOFormLayout.add(employeeDTOComboBox,
                                            departmentDTOComboBox,
                                            currentDepartmentCheckbox,
                                            buttonLayout);
        employeeDepartmentDTOFormLayout.setColspan(currentDepartmentCheckbox, 2);
        employeeDepartmentDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDepartmentDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateEmployeeDepartmentDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            employeeDepartmentDTO = employeeDepartmentService.getById(parameterId);
        } else {
            employeeDepartmentDTO = new EmployeeDepartmentDTO();
            employeeDepartmentDTO.setCreatedBy(loggedInUser);
        }

        employeeDepartmentDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        employeeDepartmentDTO.setDepartmentDTO(departmentDTOComboBox.getValue());
        employeeDepartmentDTO.setCurrentDepartment(currentDepartmentCheckbox.getValue());
        employeeDepartmentDTO.setUpdatedBy(loggedInUser);

        employeeDepartmentService.saveOrUpdate(employeeDepartmentDTO);
    }
}
