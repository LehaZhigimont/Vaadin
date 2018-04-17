package com.example.task1;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class HotelEditForm extends FormLayout {

	private HorizontalLayout nameRating = new HorizontalLayout();
	private HorizontalLayout addressLayout = new HorizontalLayout();
	private HorizontalLayout dateCategory = new HorizontalLayout();
	private HorizontalLayout urlLayout = new HorizontalLayout();
	private HorizontalLayout descriptionLayout = new HorizontalLayout();
	private HorizontalLayout buttons = new HorizontalLayout();
	private HotelService hotelService = HotelService.getInstance();
	private Hotel hotel;
	private HotelUI ui;
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Date");
	private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	private Button save = new Button("Save");
	private Button close = new Button("Close");
	private Button delete = new Button("Delete");
	private Binder<Hotel> binder = new Binder<>(Hotel.class);

	public HotelEditForm(HotelUI hotelUI) {
		this.setVisible(false);
		this.ui = hotelUI;
		/*
		 * Configurations layers
		 */
		nameRating.addComponents(name, rating);
		dateCategory.addComponents(operatesFrom, category);
		addressLayout.addComponent(address);
		urlLayout.addComponent(url);
		descriptionLayout.addComponent(description);
		buttons.addComponents(save, close, delete);
		addComponents(nameRating, addressLayout, dateCategory, urlLayout, descriptionLayout, buttons);
		/*
		 * Configurations all fields
		 */
		name.setWidth(330, Unit.PIXELS);
		address.setWidth(450, Unit.PIXELS);
		operatesFrom.setWidth(300, Unit.PIXELS);
		category.setWidth(200, Unit.PIXELS);
		rating.setWidth(108, Unit.PIXELS);
		url.setWidth(450, Unit.PIXELS);
		description.setWidth(450, Unit.PIXELS);

		binder.bindInstanceFields(this);
		category.setItems(HotelCategory.values());
		/*
		 * Configurations Listeners
		 * 
		 */
		save.addClickListener(e -> {
			save();
			ui.editHotel.setVisible(true);
			ui.deleteHotel.setVisible(true);
			ui.filterForm.setVisible(true);
			ui.addHotel.setVisible(true);
			ui.addHotel.setEnabled(true);
			ui.hotelGrid.setHeightByRows(10);
		});

		close.addClickListener(e -> {
			this.setVisible(false);
			ui.filterForm.setVisible(true);
			ui.editHotel.setVisible(true);
			ui.deleteHotel.setVisible(true);
			ui.addHotel.setVisible(true);
			ui.addHotel.setEnabled(true);
			ui.hotelGrid.setHeightByRows(10);
		});

		delete.addClickListener(e -> {
			Hotel delCandidate = ui.hotelGrid.getSelectedItems().iterator().next();
			hotelService.delete(delCandidate);
			ui.deleteHotel.setEnabled(false);
			ui.editHotel.setEnabled(false);
			this.setVisible(false);
			ui.deleteHotel.setVisible(true);
			ui.filterForm.setVisible(true);
			ui.editHotel.setVisible(true);
			ui.addHotel.setVisible(true);
			ui.updateList();
			ui.hotelGrid.setHeightByRows(10);
		});
	}

	private void save() {
		hotelService.save(hotel);
		ui.updateList();
		ui.filterForm.setVisible(true);
		ui.addHotel.setVisible(true);
		ui.deleteHotel.setEnabled(false);
		ui.editHotel.setEnabled(false);
		setVisible(false);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(this.hotel);
		setVisible(true);
	}
	
	public Button getDelete() {
		return delete;
	}
	
}
