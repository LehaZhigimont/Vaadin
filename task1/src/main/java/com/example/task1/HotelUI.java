package com.example.task1;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class HotelUI extends UI {

	final VerticalLayout layout = new VerticalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<Hotel>(Hotel.class);
	final TextField filter = new TextField("Search hotels by name:");
	final TextField filter1 = new TextField("Search hotels by address:");
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	private HotelEditForm form = new HotelEditForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(layout);
		setWidth(100, Unit.PERCENTAGE);
		HorizontalLayout controls = new HorizontalLayout();
		HorizontalLayout controls2 = new HorizontalLayout();
		HorizontalLayout formss = new HorizontalLayout();
		controls.addComponents(filter, filter1);
		form.setVisible(false);
		deleteHotel.setEnabled(false);
		HorizontalLayout content = new HorizontalLayout();
		formss.addComponents(content, form);

		form.setWidth(400, Unit.PIXELS);
		layout.addComponents(controls, formss, controls2);
		controls2.addComponents(addHotel, deleteHotel);
		content.addComponents(hotelGrid);
		content.setWidth(100, Unit.PERCENTAGE);
		layout.setWidth(100, Unit.PERCENTAGE);

		
		hotelGrid.setColumnOrder("name", "address", "rating", "category");
		hotelGrid.removeColumn("url");
		hotelGrid.setWidth(100, Unit.PERCENTAGE);
		hotelGrid.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() != null) {
				deleteHotel.setEnabled(true);
				form.setHotel(e.getValue());
			}
		});

		deleteHotel.addClickListener(e -> {
			Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
			hotelService.delete(delCandidate);
			deleteHotel.setEnabled(false);
			updateList();
			form.setVisible(false);
		});
		updateList();
		filter.addValueChangeListener(e -> updateList());
		filter1.addValueChangeListener(e -> updateList());
		filter1.setValueChangeMode(ValueChangeMode.LAZY);
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		addHotel.addClickListener(e -> form.setHotel(new Hotel()));

	}

	public void updateList() {
		List<Hotel> hotelList = hotelService.findAll();
		if (filter.getValue() != "") {
			hotelList = hotelService.findAll(filter.getValue(), "name");
		} else {
			hotelList = hotelService.findAll(filter1.getValue(), "address");
		}
		if (filter1.getValue() != "" && filter.getValue() != "") {
			hotelList = hotelService.findAll(filter.getValue(), filter1.getValue(), "addressname");
		}
		hotelGrid.setItems(hotelList);

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
	public static class HotelUIServlet extends VaadinServlet {
	}
}