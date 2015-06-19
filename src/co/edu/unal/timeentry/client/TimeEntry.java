package co.edu.unal.timeentry.client;

import co.edu.unal.timeentry.server.TimeEntryEntity;
import co.edu.unal.timeentry.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.i18n.client.NumberFormat;
import com.googlecode.objectify.ObjectifyService;

import java.util.Date;
import java.util.Vector;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TimeEntry implements EntryPoint {
	
//	static {
//		ObjectifyService.register(TimeEntryEntity.class);
//	}
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private AbsolutePanel totalPanel = new AbsolutePanel();
	private DockPanel navPanel = new DockPanel();
	private HorizontalPanel topPanel = new HorizontalPanel();
	private Label totalLabel = new Label("0.00");
	private FlexTable flexEntryTable = new FlexTable();
	private FlexTable flexCurrentTable = new FlexTable();
	private Image logo = new Image();
	// tracks the current row and column in the grid
	private int currentRow = 0;
	private int currentColumn = 0;
	private Date startDate;
	private LoginInfo loginInfo = null;
	// create the data service
	private final DataServiceAsync dataService = GWT.create(DataService.class);
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		logo.setUrl("images/logo.png");
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {}
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					loadMainUI();
				} else {
					loadLoginUI();
				}
			}
		});
	}
	
	protected void loadLoginUI() {
		VerticalPanel loginPanel = new VerticalPanel();
		Anchor loginLink = new Anchor("Iniciar sesion");
		loginLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(logo);
		loginPanel.add(new Label("Por favor inicia sesion con tu cuenta de Google."));
		loginPanel.add(loginLink);
		RootPanel.get("timeentryUI").add(loginPanel);
	}

	public void loadMainUI() {
		
		logo.setUrl("images/logo.png");
		
		HorizontalPanel userPanel = new HorizontalPanel();
		Anchor logOutLink = new Anchor("Cerrar sesion");
		logOutLink.setHref(loginInfo.getLogoutUrl());
		Label separator = new Label("|");
		separator.setStyleName("separator");
		userPanel.add(new Label(loginInfo.getEmailAddress()));
		userPanel.add(separator);
		userPanel.add(logOutLink);
		
		topPanel.setWidth("1000px");
		topPanel.add(logo);
		topPanel.add(userPanel);
		topPanel.setCellHorizontalAlignment(userPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		
		// set up a horizontal panel to hold the date picker
		HorizontalPanel leftNav = new HorizontalPanel();
		leftNav.setSpacing(5);
		leftNav.add(new Label("Fecha comienzo de semana"));
		DateBox dateBox = new DateBox();
		dateBox.setWidth("100px");
		dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("M/d/yyyy")));
		leftNav.add(dateBox);
		
		// set up horizontal panel to hold the Add and Save buttons
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		Button addRowButton = new Button("Agregar Fila");
		Button saveButton = new Button("Guardar");
		buttonPanel.add(addRowButton);
		buttonPanel.add(saveButton);
		
		// set up another horizontal panel to dock all of the buttons to the right
		final HorizontalPanel rightNav = new HorizontalPanel();
		rightNav.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		rightNav.setWidth("100%");
		rightNav.add(buttonPanel);
		
		// add all of the navigation panels to the dock panel
		navPanel.setWidth("1000px");
		navPanel.add(leftNav, DockPanel.WEST);
		navPanel.add(rightNav, DockPanel.EAST);
		
		// set up horizontal panel to hold the grand total
		totalPanel.setSize("1000px", "50px");
		totalPanel.add(new Label("Total:"), 900, 25);
		totalPanel.add(totalLabel, 950, 25);
		
		// listen for mouse events on the save button
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveEntries();
				removeAllRows();
				getCurrentEntries();
			}
		});
		
		// listen for mouse events on the add new row button
		addRowButton.addClickHandler(new ClickHandler() {
					
			@Override
			public void onClick(ClickEvent event) {
				addRow();				
			}
		});
	
		// listen for the changes in the value of the date
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				startDate = event.getValue();
				renameColumns();
				// show the main parts of the UI now
				flexEntryTable.setVisible(true);
				rightNav.setVisible(true);
				totalPanel.setVisible(true);
			}
		});
		
		// set the width of the table to expand the size of the navPanel
		flexEntryTable.setWidth("100%");
		// set the style for the table to be accessed in the css
		flexEntryTable.setStylePrimaryName("timeEntryTable");
		
		// add the column headers
		flexEntryTable.setText(0, 0, "Proyecto");
		flexEntryTable.setText(0, 1, "Objetivo");
		flexEntryTable.setText(0, 2, "Facturable");
		flexEntryTable.setText(0, 3, "Lun");
		flexEntryTable.setText(0, 4, "Mar");
		flexEntryTable.setText(0, 5, "Mie");
		flexEntryTable.setText(0, 6, "Jue");
		flexEntryTable.setText(0, 7, "Vie");
		flexEntryTable.setText(0, 8, "Sab");
		flexEntryTable.setText(0, 9, "Dom");
		flexEntryTable.setText(0, 10, "Total");
		
		// set the style for the table to be accessed in the css
		flexCurrentTable.setStylePrimaryName("existingEntryTable");
		// add the column headers
		flexCurrentTable.setText(0, 0, "Proyecto");
		flexCurrentTable.setText(0, 1, "Objetivo");
		flexCurrentTable.setText(0, 2, "Facturable");
		flexCurrentTable.setText(0, 3, "Fecha");
		flexCurrentTable.setText(0, 4, "Horas");
		
		VerticalPanel tab1Content = new VerticalPanel();
		tab1Content.add(navPanel);
		tab1Content.add(flexEntryTable);
		tab1Content.add(totalPanel);
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("100%");
		tabPanel.setAnimationEnabled(true);
		tabPanel.add(tab1Content, "Ingresa tus horas");
		tabPanel.add(flexCurrentTable, "Horas Guardadas");
		tabPanel.selectTab(0);
		
		// add the navpanel and flex table to the main panel
		mainPanel.add(topPanel);
		mainPanel.add(tabPanel);
		
		// get the current entries for the user
		getCurrentEntries();
		
		// associate the main panel with the HTML host page.
		RootPanel.get("timeentryUI").add(mainPanel);
		
		addRow();
		
		// hide the main parts of the UI until they choose a date
		flexEntryTable.setVisible(false);
		rightNav.setVisible(false);
		totalPanel.setVisible(false);
		
	}
	
	private void addRow() {
		int row = flexEntryTable.getRowCount();
		
		final ListBox lbMilestones = new ListBox(false);
		final ListBox lbProjects = new ListBox(false);
		lbProjects.addItem("-- Selecciona un proyecto --");
		
		// create the time input fields for all 7 days
		final TextBox day1 = new TextBox();
		day1.setValue("0");
		day1.setWidth("50px");
		day1.setEnabled(false);
		final TextBox day2 = new TextBox();
		day2.setValue("0");
		day2.setWidth("50px");
		day2.setEnabled(false);
		final TextBox day3 = new TextBox();
		day3.setValue("0");
		day3.setWidth("50px");
		day3.setEnabled(false);
		final TextBox day4 = new TextBox();
		day4.setValue("0");
		day4.setWidth("50px");
		day4.setEnabled(false);
		final TextBox day5 = new TextBox();
		day5.setValue("0");
		day5.setWidth("50px");
		day5.setEnabled(false);
		final TextBox day6 = new TextBox();
		day6.setValue("0");
		day6.setWidth("50px");
		day6.setEnabled(false);
		final TextBox day7 = new TextBox();
		day7.setValue("0");
		day7.setWidth("50px");
		day7.setEnabled(false);
		
		// add all of the widgets to the flex table
		flexEntryTable.setWidget(row, 0, lbProjects);
		flexEntryTable.setWidget(row, 1, lbMilestones);
		flexEntryTable.setWidget(row, 2, new CheckBox());
		flexEntryTable.setWidget(row, 3, day1);
		flexEntryTable.setWidget(row, 4, day2);
		flexEntryTable.setWidget(row, 5, day3);
		flexEntryTable.setWidget(row, 6, day4);
		flexEntryTable.setWidget(row, 7, day5);
		flexEntryTable.setWidget(row, 8, day6);
		flexEntryTable.setWidget(row, 9, day7);
		flexEntryTable.setWidget(row, 10, new Label("0.00"));
		
		flexEntryTable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable.Cell cellForEvent = flexEntryTable.getCellForEvent(event);
				currentRow = cellForEvent.getRowIndex();
				currentColumn = cellForEvent.getCellIndex();
			}
		});
		
		day1.addValueChangeHandler(timeChangeHandler);
		day2.addValueChangeHandler(timeChangeHandler);
		day3.addValueChangeHandler(timeChangeHandler);
		day4.addValueChangeHandler(timeChangeHandler);
		day5.addValueChangeHandler(timeChangeHandler);
		day6.addValueChangeHandler(timeChangeHandler);
		day7.addValueChangeHandler(timeChangeHandler);
		
		lbProjects.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// remove all of the current items in the milestone list
				for(int i=lbMilestones.getItemCount()-1; i>=0; i--)
					lbMilestones.removeItem(i);
				// get all the milestones for the project
				dataService.getMilestones(lbProjects.getItemText(lbProjects.getSelectedIndex()),
						new AsyncCallback<String[]>() {
							public void onFailure(Throwable caught) {
								handleError(caught);
							}
							public void onSuccess(String[] results) {
								for(int i=0; i<results.length; i++)
									lbMilestones.addItem(results[i]);
							}
						});
			}
		});
		
		// get all of the projects for the user
		dataService.getProjects(new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(String[] results) {
				for(int i=0; i<results.length; i++)
					lbProjects.addItem(results[i]);
				day1.setEnabled(true);
				day2.setEnabled(true);
				day3.setEnabled(true);
				day4.setEnabled(true);
				day5.setEnabled(true);
				day6.setEnabled(true);
				day7.setEnabled(true);
			}
		});
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if(error instanceof NotLoggedInException)
			Window.Location.replace(loginInfo.getLogoutUrl());
	}
	
	private ValueChangeHandler<String> timeChangeHandler = new ValueChangeHandler<String>() {
		public void onValueChange(ValueChangeEvent<String> event) {
			try {
				double t = Double.parseDouble(event.getValue());
				if(t > 24) {
					Window.alert("No puedes trabajar mas de 24 horas al dia");
				} else {
					totalRow();
				}
			} catch (NumberFormatException e) {
				TextBox tb = (TextBox)flexEntryTable.getWidget(currentRow, currentColumn);
				tb.setValue("0");
				flexEntryTable.setWidget(currentRow, currentColumn, tb);
				Window.alert("Not a valid number");
			}
		}
	};
	
	private void totalRow() {
		double rowTotal = 0.00;
		for(int cell=3; cell<=9; cell++) {
			TextBox timeWidget = (TextBox)flexEntryTable.getWidget(currentRow, cell);
			double t = Double.parseDouble(timeWidget.getValue());
			rowTotal = rowTotal + t;
		}
		flexEntryTable.setWidget(currentRow, 10, 
				new Label(NumberFormat.getFormat(".00").format(rowTotal)));
		totalGrid();
	}
	
	private void totalGrid() {
		double grandTotal = 0.00;
		for(int row=1; row<flexEntryTable.getRowCount(); row++) {
			Label rowTotalWidget = (Label)flexEntryTable.getWidget(row,10);
			double rowTotal = Double.parseDouble(rowTotalWidget.getText());
			grandTotal = grandTotal + rowTotal;
		}
		totalLabel.setText(NumberFormat.getFormat(".00").format(grandTotal));
	}
	
	
	private void renameColumns() {
		flexEntryTable.setText(0, 3, formatDate(startDate));
		flexEntryTable.setText(0, 4, formatDate(addDays(startDate, 1)));
		flexEntryTable.setText(0, 5, formatDate(addDays(startDate, 2)));
		flexEntryTable.setText(0, 6, formatDate(addDays(startDate, 3)));
		flexEntryTable.setText(0, 7, formatDate(addDays(startDate, 4)));
		flexEntryTable.setText(0, 8, formatDate(addDays(startDate, 5)));
		flexEntryTable.setText(0, 9, formatDate(addDays(startDate, 6)));
	}
	
	@SuppressWarnings("deprecation")
	private Date addDays(Date d, int numberOfDays) {
		int day = d.getDate();
		int month = d.getMonth();
		int year = d.getYear();
		return new Date(year, month, day+numberOfDays);
	}
	
	@SuppressWarnings("deprecation")
	private String formatDate(Date d) {
		return (d.getMonth()+1) + "/" + d.getDate() + "(" + d.toString().substring(0,2) + ")";
	}
	
	private void saveEntries() {
		Vector<TimeEntryData> entries = new Vector<TimeEntryData>();
		
		for(int row=1; row<flexEntryTable.getRowCount(); row++) {
			ListBox projectWidget = (ListBox)flexEntryTable.getWidget(row, 0);
			ListBox milestoneWidget = (ListBox)flexEntryTable.getWidget(row, 1);
			CheckBox billableWidget = (CheckBox)flexEntryTable.getWidget(row, 2);
			for(int column=3; column<10; column++) {
				// get the current box for the day
				TextBox textBox = (TextBox)flexEntryTable.getWidget(row, column);
				double hours = Double.parseDouble(textBox.getValue());
				if(hours > 0) {
					TimeEntryData ted = new TimeEntryData();
					ted.setHours(hours);
					ted.setMilestone(milestoneWidget.getItemText(
							milestoneWidget.getSelectedIndex()));
					ted.setProject(projectWidget.getItemText(projectWidget.getSelectedIndex()));
					ted.setBillable(billableWidget.getValue());
					ted.setDate(addDays(startDate, column-3));
					entries.add(ted);
				}
			}
		}
		
		if(!entries.isEmpty()) {
			// submit the entries to the server
			dataService.addEntries(entries, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					handleError(caught);
				}
				
				public void onSuccess(String message) {
					Window.alert(message);
					getCurrentEntries();
				}
			});
		}
	}
	
	private void removeAllRows() {
		// remove all of the rows from the flex table
		for(int row=flexEntryTable.getRowCount()-1; row>0; row--)
			flexEntryTable.removeRow(row);
		
		// reset the total
		totalLabel.setText("0.00");
		// add a new blank row to the flex table
		addRow();
	}
	
	private void getCurrentEntries() {
		// get all the milestones for the project
		dataService.getEntries(new AsyncCallback<Vector<TimeEntryData>>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(Vector<TimeEntryData> entries) {
				int row = flexEntryTable.getRowCount();
				for(TimeEntryData ted : entries) {
					row++;
					flexCurrentTable.setText(row, 0, ted.getProject());
					flexCurrentTable.setText(row, 1, ted.getMilestone());
					flexCurrentTable.setText(row, 2, ted.getBillable() ? "Yes" : "No");
					flexCurrentTable.setText(row, 3, DateTimeFormat.getShortDateFormat().
							format(ted.getDate()));
					flexCurrentTable.setText(row, 4, String.valueOf(NumberFormat.
							getFormat(".00").format(ted.getHours())));
				}
			}
		});
	}
	
}