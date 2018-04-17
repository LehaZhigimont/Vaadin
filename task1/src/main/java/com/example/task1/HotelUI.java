package com.example.task1;

import java.util.List;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class HotelUI extends UI {

	final HorizontalLayout filterForm = new HorizontalLayout();
	final HorizontalLayout contentAll = new HorizontalLayout();
	final HorizontalLayout content = new HorizontalLayout();
	final HorizontalLayout buttons = new HorizontalLayout();
	final VerticalLayout layout = new VerticalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<Hotel>(Hotel.class);
	final TextField filterByName = new TextField("Search hotel by name:");
	final TextField filterByAddress = new TextField("Search hotel by address:");
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	final Button editHotel = new Button("Edit hotel");
	private HotelEditForm editForm = new HotelEditForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(layout);
		// Add all layouts
		layout.addComponents(filterForm, hotelGrid, editForm, buttons);

		filterForm.addComponents(filterByName, filterByAddress);
		buttons.addComponents(addHotel, editHotel, deleteHotel);

		/*
		 * Configuration for all contents
		 */
		contentAll.setWidth(100, Unit.PERCENTAGE);
		hotelGrid.setWidth(100, Unit.PERCENTAGE);
		editForm.setWidth(100, Unit.PERCENTAGE);
		deleteHotel.setEnabled(false);
		editHotel.setEnabled(false);
		editForm.setVisible(false);

		hotelGrid.setColumnOrder("name", "address", "rating", "category");
		hotelGrid.addColumn(hotel -> "<a href='" + hotel.getUrl() + "' target='_top'>" + hotel.getName() + "</a>",
				new HtmlRenderer()).setCaption("Web site");
		hotelGrid.removeColumn("url");

		/*
		 * Buttons settings
		 */
		// SelectTable and EditButton configurations
		hotelGrid.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() != null && !editForm.isVisible()) {
				deleteHotel.setEnabled(true);
				editHotel.setEnabled(true);
				// EditButton ClickListener
				editHotel.addClickListener(event -> {
					editForm.setHotel(e.getValue());
					hotelGrid.setHeightByRows(4);
					editForm.getDelete().setVisible(true);
					deleteHotel.setVisible(false);
					//filterForm.setVisible(false);
					editHotel.setVisible(false);
					addHotel.setVisible(false);
				});
			} else if (editForm.isVisible()) {
				editForm.setHotel(e.getValue());
			}
		});
		// DeleteButton configuration
		deleteHotel.addClickListener(e -> {
			Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
			hotelService.delete(delCandidate);
			deleteHotel.setEnabled(false);
			editHotel.setEnabled(false);
			editForm.setVisible(false);
			filterForm.setVisible(true);
			editHotel.setVisible(true);
			addHotel.setVisible(true);
			updateList();
		});
		// AddButton configuration
		addHotel.addClickListener(e -> {
			editForm.getDelete().setVisible(false);
			hotelGrid.setHeightByRows(4);
			deleteHotel.setVisible(false);
			//filterForm.setEnabled(false);
			editHotel.setVisible(false);
			addHotel.setVisible(false);
			editForm.setHotel(new Hotel());
		});

		/*
		 * Filters settings
		 */
		updateList();
		filterByName.addValueChangeListener(e -> updateList());
		filterByAddress.addValueChangeListener(e -> updateList());
		filterByName.setValueChangeMode(ValueChangeMode.LAZY);
		filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
	}

	public void updateList() {
		String choice = null;
		List<Hotel> hotelList = hotelService.findAll();
		if (filterByName.getValue() != "") {
			choice = "name";
			hotelList = hotelService.findAll(filterByName.getValue(), choice);
		} else {
			choice = "address";
			hotelList = hotelService.findAll(filterByAddress.getValue(), choice);
		}
		if (filterByName.getValue() != "" && filterByAddress.getValue() != "") {
			choice = "addressname";
			hotelList = hotelService.findAll(filterByName.getValue(), filterByAddress.getValue(), choice);
		}
		hotelGrid.setItems(hotelList);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
	public static class HotelUIServlet extends VaadinServlet {
	}
}
