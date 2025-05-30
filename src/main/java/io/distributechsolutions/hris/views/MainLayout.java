package io.distributechsolutions.hris.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import io.distributechsolutions.hris.dtos.admin.UserDTO;
import io.distributechsolutions.hris.services.admin.UserService;
import io.distributechsolutions.hris.services.attendance.EmployeeLeaveFilingService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.views.reference.DepartmentListView;
import io.distributechsolutions.hris.views.reference.PositionListView;
import io.distributechsolutions.hris.views.admin.UserListView;
import io.distributechsolutions.hris.views.attendance.EmployeeShiftListView;
import io.distributechsolutions.hris.views.attendance.EmployeeLeaveApprovalsListView;
import io.distributechsolutions.hris.views.common.DashboardView;
import io.distributechsolutions.hris.views.common.LeaveFilingView;
import io.distributechsolutions.hris.views.common.TimeAttendanceView;
import io.distributechsolutions.hris.views.compenben.*;
import io.distributechsolutions.hris.views.common.EmployeeInfoView;
import io.distributechsolutions.hris.views.attendance.EmployeeTimesheetListView;
import io.distributechsolutions.hris.views.payroll.PayrollGeneratorView;
import io.distributechsolutions.hris.views.profile.EmployeeDepartmentListView;
import io.distributechsolutions.hris.views.profile.EmployeeListView;
import io.distributechsolutions.hris.views.profile.EmployeePositionListView;
import io.distributechsolutions.hris.views.reference.CalendarHolidaysListView;

import jakarta.annotation.Resource;

import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    @Resource private final EmployeeLeaveFilingService employeeLeaveFilingService;

    private UserDTO userDTO;
    private H1 viewTitle;
    private String fullName;

    public MainLayout(UserService userService,
                      EmployeeLeaveFilingService employeeLeaveFilingService) {
        this.employeeLeaveFilingService = employeeLeaveFilingService;

        // Gets the user data transfer object based from the logged in user.
        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
            fullName = userDTO.getEmployeeDTO().getFirstName() + " " + userDTO.getEmployeeDTO().getLastName();
        }

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);
        viewTitle.getStyle().setWidth("50%");

        // This will show the user's avatar if logged in the application.
        if (userDTO != null) {
            Avatar userAvatar = new Avatar(fullName);
            userAvatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
            userAvatar.setColorIndex((int) (Math.random() * 7) + 1);

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(userAvatar);
            contextMenu.setOpenOnClick(true);
            contextMenu.add(this.createProfileDiv());
            contextMenu.add(new Hr());

            MenuItem changePasswordMenuItem = contextMenu.addItem("Change Password");
            changePasswordMenuItem.add(new Icon(VaadinIcon.PASSWORD));

            contextMenu.add(new Hr());
            contextMenu.addItem("Logout", menuItemClickEvent -> SecurityUtil.logout());

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(userAvatar);
            verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, userAvatar);

            addToNavbar(true, toggle, viewTitle, verticalLayout);
        } else {
            addToNavbar(true, toggle, viewTitle);
        }
    }

    private void addDrawerContent() {
        Span appName = new Span("WHIZ Services HRIS");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(this.createNavigationLayout());

        addToDrawer(header, scroller, createFooter());
    }

    private Div createProfileDiv() {
        Span profileGreetingsSpan = new Span("Welcome ".concat(fullName).concat("!"));
        profileGreetingsSpan.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        Div profileDiv = new Div();
        profileDiv.add(profileGreetingsSpan);
        profileDiv.getStyle().setPaddingLeft("15px");
        profileDiv.getStyle().setPaddingRight("15px");
        profileDiv.getStyle().setPaddingTop("5px");
        profileDiv.getStyle().setPaddingBottom("5px");

        return profileDiv;
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("My Dashboard", DashboardView.class, LineAwesomeIcon.CHART_BAR_SOLID.create()));
        nav.addItem(new SideNavItem("My Profile", EmployeeInfoView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
        nav.addItem(new SideNavItem("My Leave Filings", LeaveFilingView.class, LineAwesomeIcon.DOOR_OPEN_SOLID.create()));
        nav.addItem(new SideNavItem("My Time Attendance", TimeAttendanceView.class, LineAwesomeIcon.CLOCK.create()));

        if (!userDTO.getRole().equals("ROLE_EMPLOYEE")) {
            nav.addItem(this.createEmployeeNavigation(),
                        this.createAttendanceNavigation(),
                        this.createCompenbenNavigation(),
                        this.createPayrollNavigation(),
                        this.createReferenceNavigation(),
                        this.createAdminNavigation());
        }

        return nav;
    }

    private SideNavItem createEmployeeNavigation() {
        SideNavItem navItem = new SideNavItem("Employee Details");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_HR_EMPLOYEE")) {
            navItem.addItem(new SideNavItem("Employees", EmployeeListView.class, LineAwesomeIcon.ID_BADGE_SOLID.create()));
            navItem.addItem(new SideNavItem("Assign Position", EmployeePositionListView.class, LineAwesomeIcon.USER_CHECK_SOLID.create()));
            navItem.addItem(new SideNavItem("Assign Department", EmployeeDepartmentListView.class, LineAwesomeIcon.USER_CIRCLE_SOLID.create()));
        }

        return navItem;
    }

    private SideNavItem createAttendanceNavigation() {
        SideNavItem navItem = new SideNavItem("Attendance");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR")) {
            navItem.addItem(new SideNavItem("Employee Shift", EmployeeShiftListView.class, LineAwesomeIcon.CALENDAR_DAY_SOLID.create()));
        }

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_HR_EMPLOYEE")) {
            navItem.addItem(new SideNavItem("Timesheets", EmployeeTimesheetListView.class, LineAwesomeIcon.CALENDAR_WEEK_SOLID.create()));
        }

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_MANAGER") ||
                userDTO.getRole().equals("ROLE_SUPERVISOR")) {
            // Get the count of pending leaves to approved. Check every 5 seconds.
            int pendingLeaveCounts = employeeLeaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()).size();

            // Show a notification badge that displays the count of leaves to be approved.
            Span counter = new Span(String.valueOf(pendingLeaveCounts));
            counter.getElement().getThemeList().add("badge pill medium error primary");
            counter.getStyle().set("margin-inline-start", "var(--lumo-space-s)");

            // Create the navigation item for leave approvals.
            SideNavItem leaveApprovalNavItem = new SideNavItem("Leave Approvals", EmployeeLeaveApprovalsListView.class, LineAwesomeIcon.CALENDAR_CHECK.create());
            if (pendingLeaveCounts > 0) leaveApprovalNavItem.setSuffixComponent(counter);

            navItem.addItem(leaveApprovalNavItem);
        }

        return navItem;
    }

    private SideNavItem createCompenbenNavigation() {
        SideNavItem navItem = new SideNavItem("Compenben");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR")) {
            navItem.addItem(new SideNavItem("Rates", RatesListView.class, LineAwesomeIcon.MONEY_CHECK_SOLID.create()));
            navItem.addItem(new SideNavItem("Allowances", AllowanceListView.class, LineAwesomeIcon.COINS_SOLID.create()));
            navItem.addItem(new SideNavItem("Contributions", GovernmentContributionsListView.class, LineAwesomeIcon.HAND_HOLDING_USD_SOLID.create()));
            navItem.addItem(new SideNavItem("Loan Deductions", LoanDeductionListView.class, LineAwesomeIcon.MONEY_BILL_WAVE_SOLID.create()));
            navItem.addItem(new SideNavItem("Leave Benefits", LeaveBenefitsListView.class, LineAwesomeIcon.DOOR_OPEN_SOLID.create()));
        }

        return navItem;
    }

    private SideNavItem createPayrollNavigation() {
        SideNavItem navItem = new SideNavItem("Payroll");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_HR_EMPLOYEE")) {
            navItem.addItem(new SideNavItem("Payroll Generator", PayrollGeneratorView.class, LineAwesomeIcon.FILE_INVOICE_DOLLAR_SOLID.create()));
        }

        return navItem;
    }

    private SideNavItem createReferenceNavigation() {
        SideNavItem navItem = new SideNavItem("Reference");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR")) {
            navItem.addItem(new SideNavItem("Calendar Holidays", CalendarHolidaysListView.class, LineAwesomeIcon.CALENDAR.create()));
            navItem.addItem(new SideNavItem("Positions", PositionListView.class, LineAwesomeIcon.SITEMAP_SOLID.create()));
            navItem.addItem(new SideNavItem("Departments", DepartmentListView.class, LineAwesomeIcon.BUILDING_SOLID.create()));
        }

        return navItem;
    }

    private SideNavItem createAdminNavigation() {
        SideNavItem navItem = new SideNavItem("Administration");
        navItem.setExpanded(false);

        if (userDTO.getRole().equals("ROLE_ADMIN") || userDTO.getRole().equals("ROLE_HR_MANAGER")) {
            navItem.addItem(new SideNavItem("Users", UserListView.class, LineAwesomeIcon.USER_LOCK_SOLID.create()));
        }

        return navItem;
    }

    private VerticalLayout createNavigationLayout() {
        VerticalLayout navigationLayout = new VerticalLayout();

        navigationLayout.add(this.createNavigation());
        navigationLayout.setSpacing(true);
        navigationLayout.setSizeUndefined();

        return navigationLayout;
    }

    private Footer createFooter() {
        return new Footer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
