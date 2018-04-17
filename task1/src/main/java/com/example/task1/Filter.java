package com.example.task1;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class Filter extends FormLayout {

	private HotelUI ui;
	final TextField filterByName = new TextField("Search hotel by name:");
	final TextField filterByAddress = new TextField("Search hotel by address:");
	private String choice = null;

	public void Filter(HotelUI ui) {
		HorizontalLayout searchForm = new HorizontalLayout();
		searchForm.addComponents(filterByName, filterByAddress);
		addComponents(searchForm);
	
		filterByName.addValueChangeListener(e -> ui.updateList());
		filterByAddress.addValueChangeListener(e -> ui.updateList());
		filterByName.setValueChangeMode(ValueChangeMode.LAZY);
		filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
	}
}
