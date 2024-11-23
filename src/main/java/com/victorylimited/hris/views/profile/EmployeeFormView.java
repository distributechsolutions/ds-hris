package com.victorylimited.hris.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import com.victorylimited.hris.dtos.profile.EmployeeDTO;
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
@PageTitle("Employee Form")
@Route(value = "employee-form", layout = MainLayout.class)
public class EmployeeFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeeService employeeService;
    private EmployeeDTO employeeDTO;
    private UUID parameterId;

    private final FormLayout employeeDTOFormLayout = new FormLayout();
    private TextField employeeNoTextField,
                      biometricNoTextField,
                      lastNameTextField,
                      firstNameTextField,
                      middleNameTextField,
                      suffixTextField,
                      atmAccountNoTextField;
    private ComboBox<String> genderComboBox;
    private DatePicker dateHiredDatePicker;
    private Button saveButton, cancelButton;

    public EmployeeFormView(EmployeeService employeeService) {
        this.employeeService = employeeService;

        setSizeFull();
        setMargin(true);
        add(employeeDTOFormLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeDTO = employeeService.getById(parameterId);
        }

        buildEmployeeFormLayout();
    }

    private void buildEmployeeFormLayout() {
        employeeNoTextField = new TextField("Employee Number");
        employeeNoTextField.setRequired(true);
        employeeNoTextField.setRequiredIndicatorVisible(true);
        employeeNoTextField.setAllowedCharPattern("\\d*");
        employeeNoTextField.setMinLength(3);
        employeeNoTextField.setMaxLength(10);
        employeeNoTextField.setClearButtonVisible(true);
        if (employeeDTO != null) employeeNoTextField.setValue(employeeDTO.getEmployeeNumber());

        biometricNoTextField = new TextField("Biometric Number");
        biometricNoTextField.setRequired(true);
        biometricNoTextField.setRequiredIndicatorVisible(true);
        biometricNoTextField.setAllowedCharPattern("\\d*");
        biometricNoTextField.setMinLength(3);
        biometricNoTextField.setMaxLength(10);
        biometricNoTextField.setClearButtonVisible(true);
        if (employeeDTO != null) biometricNoTextField.setValue(employeeDTO.getBiometricsNumber());

        lastNameTextField = new TextField("Last Name");
        lastNameTextField.setRequired(true);
        lastNameTextField.setRequiredIndicatorVisible(true);
        lastNameTextField.setMinLength(2);
        lastNameTextField.setMaxLength(50);
        lastNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        lastNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) lastNameTextField.setValue(employeeDTO.getLastName());

        firstNameTextField = new TextField("First Name");
        firstNameTextField.setRequired(true);
        firstNameTextField.setRequiredIndicatorVisible(true);
        firstNameTextField.setMinLength(2);
        firstNameTextField.setMaxLength(50);
        firstNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        firstNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) firstNameTextField.setValue(employeeDTO.getFirstName());

        middleNameTextField = new TextField("Middle Name");
        middleNameTextField.setMinLength(2);
        middleNameTextField.setMaxLength(50);
        middleNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        middleNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) middleNameTextField.setValue(employeeDTO.getMiddleName());

        suffixTextField = new TextField("Suffix");
        suffixTextField.setPlaceholder("Sr, Jr, III, IV...");
        suffixTextField.setMinLength(2);
        suffixTextField.setMaxLength(5);
        suffixTextField.setAllowedCharPattern("[a-zA-Z]*");
        suffixTextField.setWidth("25%");
        suffixTextField.setClearButtonVisible(true);
        if (employeeDTO != null) suffixTextField.setValue(employeeDTO.getSuffix());

        genderComboBox = new ComboBox<>("Gender");
        genderComboBox.setRequired(true);
        genderComboBox.setRequiredIndicatorVisible(true);
        genderComboBox.setItems("Male", "Female");
        genderComboBox.setClearButtonVisible(true);
        if (employeeDTO != null) genderComboBox.setValue(employeeDTO.getGender());

        dateHiredDatePicker = new DatePicker("Date Hired");
        dateHiredDatePicker.setRequired(true);
        dateHiredDatePicker.setRequiredIndicatorVisible(true);
        dateHiredDatePicker.setClearButtonVisible(true);
        if (employeeDTO != null) dateHiredDatePicker.setValue(employeeDTO.getDateHired());

        atmAccountNoTextField = new TextField("ATM Account Number");
        atmAccountNoTextField.setRequired(true);
        atmAccountNoTextField.setRequiredIndicatorVisible(true);
        atmAccountNoTextField.setAllowedCharPattern("\\d*");
        atmAccountNoTextField.setMinLength(14);
        atmAccountNoTextField.setMaxLength(19);
        atmAccountNoTextField.setClearButtonVisible(true);
        atmAccountNoTextField.setHelperText("Set 000000000000000 if the employee doesn't have an ATM account number.");
        if (employeeDTO != null) atmAccountNoTextField.setValue(employeeDTO.getAtmAccountNumber());

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> saveOrUpdateEmployeeDTO());

        cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(EmployeeListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("768px");
        buttonLayout.setPadding(true);

        employeeDTOFormLayout.add(employeeNoTextField,
                                  biometricNoTextField,
                                  lastNameTextField,
                                  suffixTextField,
                                  firstNameTextField,
                                  middleNameTextField,
                                  genderComboBox,
                                  dateHiredDatePicker,
                                  atmAccountNoTextField,
                                  buttonLayout);
        employeeDTOFormLayout.setColspan(atmAccountNoTextField, 2);
        employeeDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDTOFormLayout.setMaxWidth("768px");
    }

    private void saveOrUpdateEmployeeDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            employeeDTO = employeeService.getById(parameterId);
        } else {
            employeeDTO = new EmployeeDTO();
            employeeDTO.setCreatedBy(loggedInUser);
        }

        employeeDTO.setEmployeeNumber(employeeNoTextField.getValue());
        employeeDTO.setBiometricsNumber(biometricNoTextField.getValue());
        employeeDTO.setLastName(lastNameTextField.getValue());
        employeeDTO.setSuffix(suffixTextField.getValue());
        employeeDTO.setFirstName(firstNameTextField.getValue());
        employeeDTO.setMiddleName(middleNameTextField.getValue());
        employeeDTO.setGender(genderComboBox.getValue());
        employeeDTO.setDateHired(dateHiredDatePicker.getValue());
        employeeDTO.setAtmAccountNumber(atmAccountNoTextField.getValue());
        employeeDTO.setUpdatedBy(loggedInUser);

        // Checks if the employee number and biometric number fields are not empty.
        if (employeeNoTextField.getValue().equals("")
                || biometricNoTextField.getValue().equals("")
                || lastNameTextField.getValue().equals("")
                || firstNameTextField.getValue().equals("")
                || atmAccountNoTextField.getValue().equals("")) {
            // Show notification message.
            Notification notification = Notification.show("You cannot save the employee record. Please fill up the required fields.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            employeeService.saveOrUpdate(employeeDTO);

            // Show notification message.
            Notification notification = Notification.show("You have successfully saved the employee record.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            saveButton.getUI().ifPresent(ui -> ui.navigate(EmployeeListView.class));
        }
    }
}
