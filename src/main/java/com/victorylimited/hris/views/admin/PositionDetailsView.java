package com.victorylimited.hris.views.admin;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import com.victorylimited.hris.dtos.admin.PositionDTO;
import com.victorylimited.hris.services.admin.PositionService;
import com.victorylimited.hris.views.MainLayout;

import jakarta.annotation.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@PageTitle("Position Details")
@Route(value = "position-details", layout = MainLayout.class)
public class PositionDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final PositionService positionService;
    private PositionDTO positionDTO;

    private final FormLayout positionDetailsLayout = new FormLayout();

    public PositionDetailsView(PositionService positionService) {
        this.positionService = positionService;

        setSizeFull();
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        add(positionDetailsLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            positionDTO = positionService.getById(parameterId);
        }

        buildPositionDetailsLayout();
    }

    public void buildPositionDetailsLayout() {
        // To display the local date and time format.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");

        Span positionCodeLabelSpan = new Span("Code");
        positionCodeLabelSpan.getStyle().set("text-align", "right");

        Span positionCodeValueSpan = new Span(positionDTO.getCode());
        positionCodeValueSpan.getStyle().setFontWeight("bold");

        Span positionNameLabelSpan = new Span("Name");
        positionNameLabelSpan.getStyle().set("text-align", "right");

        Span positionNameValueSpan = new Span(positionDTO.getName());
        positionNameValueSpan.getStyle().setFontWeight("bold");

        Span createdByLabelSpan = new Span("Created by");
        createdByLabelSpan.getStyle().set("text-align", "right");

        Span createdByValueSpan = new Span(positionDTO.getCreatedBy());
        createdByValueSpan.getStyle().setFontWeight("bold");

        Span dateCreatedLabelSpan = new Span("Date created");
        dateCreatedLabelSpan.getStyle().set("text-align", "right");

        Span dateCreatedValueSpan = new Span(dateTimeFormatter.format(positionDTO.getDateAndTimeCreated()));
        dateCreatedValueSpan.getStyle().setFontWeight("bold");

        Span updatedByLabelSpan = new Span("Updated by");
        updatedByLabelSpan.getStyle().set("text-align", "right");

        Span updatedByValueSpan = new Span(positionDTO.getUpdatedBy());
        updatedByValueSpan.getStyle().setFontWeight("bold");

        Span dateUpdatedLabelSpan = new Span("Date updated");
        dateUpdatedLabelSpan.getStyle().set("text-align", "right");

        Span dateUpdatedValueSpan = new Span(dateTimeFormatter.format(positionDTO.getDateAndTimeUpdated()));
        dateUpdatedValueSpan.getStyle().setFontWeight("bold");

        positionDetailsLayout.add(positionCodeLabelSpan,
                positionCodeValueSpan,
                positionNameLabelSpan,
                positionNameValueSpan,
                createdByLabelSpan,
                createdByValueSpan,
                dateCreatedLabelSpan,
                dateCreatedValueSpan,
                updatedByLabelSpan,
                updatedByValueSpan,
                dateUpdatedLabelSpan,
                dateUpdatedValueSpan);
        positionDetailsLayout.setWidth("720px");
    }
}
