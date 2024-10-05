package com.victorylimited.hris.views.info;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.victorylimited.hris.dtos.admin.UserDTO;
import com.victorylimited.hris.dtos.info.AddressInfoDTO;
import com.victorylimited.hris.dtos.profile.EmployeeDTO;
import com.victorylimited.hris.dtos.reference.BarangayDTO;
import com.victorylimited.hris.dtos.reference.MunicipalityDTO;
import com.victorylimited.hris.dtos.reference.ProvinceDTO;
import com.victorylimited.hris.dtos.reference.RegionDTO;
import com.victorylimited.hris.services.admin.UserService;
import com.victorylimited.hris.services.info.AddressInfoService;
import com.victorylimited.hris.services.profile.EmployeeService;
import com.victorylimited.hris.services.reference.BarangayService;
import com.victorylimited.hris.services.reference.MunicipalityService;
import com.victorylimited.hris.services.reference.ProvinceService;
import com.victorylimited.hris.services.reference.RegionService;
import com.victorylimited.hris.utils.SecurityUtil;
import com.victorylimited.hris.views.common.DashboardView;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

public class AddressInfoForm extends VerticalLayout {
    @Resource private final AddressInfoService addressInfoService;
    @Resource private final UserService userService;
    @Resource private final EmployeeService employeeService;
    @Resource private final RegionService regionService;
    @Resource private final ProvinceService provinceService;
    @Resource private final MunicipalityService municipalityService;
    @Resource private final BarangayService barangayService;

    private List<AddressInfoDTO> addressInfoDTOList;
    private AddressInfoDTO addressInfoDTO;
    private UserDTO userDTO;
    private EmployeeDTO employeeDTO;

    private String loggedInUser;

    private Grid<AddressInfoDTO> addressInfoDTOGrid;
    private FormLayout addressInfoFormLayout;
    private RadioButtonGroup<String> addressTypeRadioButtonGroup;
    private TextField addressDetailTextField;
    private TextField streetTextField;
    private ComboBox<RegionDTO> regionDTOComboBox;
    private ComboBox<ProvinceDTO> provinceDTOComboBox;
    private ComboBox<MunicipalityDTO> municipalityDTOComboBox;
    private ComboBox<BarangayDTO> barangayDTOComboBox;
    private IntegerField postalCodeIntegerField;
    private Button saveButton;
    private Button cancelButton;
    private Button viewButton;
    private Button editButton;

    public AddressInfoForm(AddressInfoService addressInfoService,
                           UserService userService,
                           EmployeeService employeeService,
                           RegionService regionService,
                           ProvinceService provinceService,
                           MunicipalityService municipalityService,
                           BarangayService barangayService) {
        this.addressInfoService = addressInfoService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.regionService = regionService;
        this.provinceService = provinceService;
        this.municipalityService = municipalityService;
        this.barangayService = barangayService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.findByParameter(loggedInUser).get(0);
        }

        if (userDTO != null) {
            employeeDTO = userDTO.getEmployeeDTO();
        }

        if (employeeDTO != null) {
            addressInfoDTOList = addressInfoService.getByEmployeeDTO(employeeDTO);
        }

        addressInfoDTOGrid = new Grid<>(AddressInfoDTO.class, false);
        addressInfoFormLayout = new FormLayout();

        this.buildAddressInfoFormLayout();
        this.buildAddressInfoDTOGrid();

        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
        this.add(new Div(addressInfoFormLayout), addressInfoDTOGrid);
    }

    private void buildAddressInfoFormLayout() {
        addressTypeRadioButtonGroup = new RadioButtonGroup<>("Address Type");
        addressTypeRadioButtonGroup.setItems("Present", "Permanent");
        addressTypeRadioButtonGroup.setRequired(true);
        addressTypeRadioButtonGroup.setRequiredIndicatorVisible(true);

        addressDetailTextField = new TextField("Adddress Detail");
        addressDetailTextField.setHelperText("House number, lot or block number, or unit number.");
        addressDetailTextField.setRequired(true);
        addressDetailTextField.setRequiredIndicatorVisible(true);

        streetTextField = new TextField("Street Name");
        streetTextField.setRequired(true);
        streetTextField.setRequiredIndicatorVisible(true);

        regionDTOComboBox = new ComboBox<>("Region");
        regionDTOComboBox.setItems(regionService.findAllRegions());
        regionDTOComboBox.setItemLabelGenerator(RegionDTO::getRegionDescription);
        regionDTOComboBox.setRequired(true);
        regionDTOComboBox.setRequiredIndicatorVisible(true);
        regionDTOComboBox.addValueChangeListener(valueChangeEvent -> {
            provinceDTOComboBox.setItems(provinceService.getProvinceByRegion(regionDTOComboBox.getValue()));
            provinceDTOComboBox.setItemLabelGenerator(ProvinceDTO::getProvinceDescription);
        });

        provinceDTOComboBox = new ComboBox<>("Province");
        provinceDTOComboBox.setRequired(true);
        provinceDTOComboBox.setRequiredIndicatorVisible(true);
        provinceDTOComboBox.addValueChangeListener(valueChangeEvent -> {
            municipalityDTOComboBox.setItems(municipalityService.getMunicipalityByProvince(provinceDTOComboBox.getValue()));
            municipalityDTOComboBox.setItemLabelGenerator(MunicipalityDTO::getMunicipalityDescription);
        });

        municipalityDTOComboBox = new ComboBox<>("Municipality");
        municipalityDTOComboBox.setRequired(true);
        municipalityDTOComboBox.setRequiredIndicatorVisible(true);
        municipalityDTOComboBox.addValueChangeListener(valueChangeEvent -> {
            barangayDTOComboBox.setItems(barangayService.getBarangayByMunicipality(municipalityDTOComboBox.getValue()));
            barangayDTOComboBox.setItemLabelGenerator(BarangayDTO::getBarangayDescription);
        });

        barangayDTOComboBox = new ComboBox<>("Barangay");
        barangayDTOComboBox.setRequired(true);
        barangayDTOComboBox.setRequiredIndicatorVisible(true);


        postalCodeIntegerField = new IntegerField("Postal Code");
        postalCodeIntegerField.setRequired(true);
        postalCodeIntegerField.setRequiredIndicatorVisible(true);
        postalCodeIntegerField.setMin(0);
        postalCodeIntegerField.setMax(9999);

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            // Save the address and clear the fields.
            this.saveAddressInfoDTO();
            this.clearFields();

            // Show notification message.
            Notification notification = Notification.show("You have successfully saved your address information.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Update the address grid table.
            addressInfoDTOList = addressInfoService.getByEmployeeDTO(employeeDTO);
            addressInfoDTOGrid.setItems(addressInfoDTOList);
        });

        cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate(DashboardView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(true);

        addressInfoFormLayout.setColspan(buttonLayout, 2);
        addressInfoFormLayout.add(addressDetailTextField,
                                  streetTextField,
                                  regionDTOComboBox,
                                  provinceDTOComboBox,
                                  municipalityDTOComboBox,
                                  barangayDTOComboBox,
                                  postalCodeIntegerField,
                                  addressTypeRadioButtonGroup,
                                  buttonLayout);
        addressInfoFormLayout.setMaxWidth("75%");
    }

    private void buildAddressInfoDTOGrid() {
        addressInfoDTOGrid.addColumn(AddressInfoDTO::getAddressType)
                          .setHeader("Address Type");
        addressInfoDTOGrid.addColumn(addressDTO -> addressDTO.getAddressDetail()
                                                             .concat(" ")
                                                             .concat(addressDTO.getStreetName())
                                                             .concat(", ")
                                                             .concat(addressDTO.getBarangayDTO().getBarangayDescription()))
                          .setHeader("Address Details");
        addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getMunicipalityDTO().getMunicipalityDescription())
                          .setHeader("Municipality");
        addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getProvinceDTO().getProvinceDescription())
                          .setHeader("Province");
        addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getRegionDTO().getRegionDescription())
                          .setHeader("Region");
        addressInfoDTOGrid.addColumn(AddressInfoDTO::getPostalCode)
                          .setHeader("Postal Code");
        addressInfoDTOGrid.addComponentColumn(addressDTO -> this.buildAddressInfoRowToolbar())
                          .setHeader("Action");
        addressInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                            GridVariant.LUMO_COLUMN_BORDERS,
                                            GridVariant.LUMO_WRAP_CELL_CONTENT);
        addressInfoDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        addressInfoDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        addressInfoDTOGrid.setItems(addressInfoDTOList);
    }

    private Component buildAddressInfoRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        viewButton = new Button();
        viewButton.setTooltipText("View Address");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> this.loadAddressInfoDTO(true));

        editButton = new Button();
        editButton.setTooltipText("Edit Address");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(buttonClickEvent -> this.loadAddressInfoDTO(false));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void saveAddressInfoDTO() {
        if (addressInfoDTO == null) {
            addressInfoDTO = new AddressInfoDTO();
            addressInfoDTO.setEmployeeDTO(employeeDTO);
            addressInfoDTO.setCreatedBy(loggedInUser);
        }

        addressInfoDTO.setAddressType(addressTypeRadioButtonGroup.getValue());
        addressInfoDTO.setAddressDetail(addressDetailTextField.getValue());
        addressInfoDTO.setStreetName(streetTextField.getValue());
        addressInfoDTO.setRegionDTO(regionDTOComboBox.getValue());
        addressInfoDTO.setProvinceDTO(provinceDTOComboBox.getValue());
        addressInfoDTO.setMunicipalityDTO(municipalityDTOComboBox.getValue());
        addressInfoDTO.setBarangayDTO(barangayDTOComboBox.getValue());
        addressInfoDTO.setPostalCode(postalCodeIntegerField.getValue());
        addressInfoDTO.setUpdatedBy(loggedInUser);

        addressInfoService.saveOrUpdate(addressInfoDTO);
    }

    private void clearFields() {
        addressTypeRadioButtonGroup.clear();
        addressDetailTextField.clear();
        streetTextField.clear();
        regionDTOComboBox.clear();
        provinceDTOComboBox.clear();
        municipalityDTOComboBox.clear();
        barangayDTOComboBox.clear();
        postalCodeIntegerField.clear();
    }

    private void loadAddressInfoDTO(boolean readOnly) {
        addressInfoDTO = addressInfoDTOGrid.getSelectionModel().getFirstSelectedItem().get();

        addressDetailTextField.setValue(addressInfoDTO.getAddressDetail());
        addressDetailTextField.setReadOnly(readOnly);

        streetTextField.setValue(addressInfoDTO.getStreetName());
        streetTextField.setReadOnly(readOnly);

        regionDTOComboBox.setValue(addressInfoDTO.getRegionDTO());
        regionDTOComboBox.setReadOnly(readOnly);

        provinceDTOComboBox.setValue(addressInfoDTO.getProvinceDTO());
        provinceDTOComboBox.setReadOnly(readOnly);

        municipalityDTOComboBox.setValue(addressInfoDTO.getMunicipalityDTO());
        municipalityDTOComboBox.setReadOnly(readOnly);

        barangayDTOComboBox.setValue(addressInfoDTO.getBarangayDTO());
        barangayDTOComboBox.setReadOnly(readOnly);

        postalCodeIntegerField.setValue(addressInfoDTO.getPostalCode());
        postalCodeIntegerField.setReadOnly(readOnly);

        addressTypeRadioButtonGroup.setValue(addressInfoDTO.getAddressType());
        addressTypeRadioButtonGroup.setReadOnly(readOnly);
    }
}
