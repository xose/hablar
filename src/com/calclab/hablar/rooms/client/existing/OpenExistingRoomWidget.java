package com.calclab.hablar.rooms.client.existing;

import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.hablar.core.client.ui.selectionlist.Selectable;
import com.calclab.hablar.core.client.ui.selectionlist.SelectionList;
import com.calclab.hablar.rooms.client.RoomsMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OpenExistingRoomWidget extends Composite implements OpenExistingRoomDisplay {

    private static OpenExistingRoomWidgetUiBinder uiBinder = GWT.create(OpenExistingRoomWidgetUiBinder.class);

    private static RoomsMessages messages;

    public static void setMessages(final RoomsMessages messages) {
	OpenExistingRoomWidget.messages = messages;
    }

    public static RoomsMessages i18n() {
	return messages;
    }

    @UiField
    SpanElement title;

    @UiField
    LabelElement availableRoomsLabel;

    @UiField
    SelectionList roomList;

    @UiField
    Button accept, cancel;

    interface OpenExistingRoomWidgetUiBinder extends UiBinder<Widget, OpenExistingRoomWidget> {
    }

    public OpenExistingRoomWidget() {
	initWidget(uiBinder.createAndBindUi(this));
	title.setInnerText(i18n().openExistingRoom());
	availableRoomsLabel.setInnerText(i18n().availableRoomsLabelText());
	accept.setText(i18n().acceptAction());
	cancel.setText(i18n().cancelAction());
	roomList.addSelectionHandler(new SelectionHandler<Selectable>() {

	    @Override
	    public void onSelection(SelectionEvent<Selectable> event) {
		accept.setEnabled(true);
	    }
	});
    }

    @Override
    public void setRooms(List<ExistingRoom> rooms) {
	accept.setEnabled(false);
	roomList.clear();
	for (ExistingRoom uri : rooms) {
	    roomList.add(new RoomSelectable(uri));
	}
    }

    @Override
    public XmppURI getSelectedRoom() {
	RoomSelectable selectable = (RoomSelectable) roomList.getSelectedSelectable();
	if (selectable != null) {
	    return ((ExistingRoom) selectable.getItem()).getUri();
	}
	return null;
    }

    @Override
    public HasClickHandlers getAccept() {
	return accept;
    }

    @Override
    public HasClickHandlers getCancel() {
	return cancel;
    }

    @Override
    public Widget asWidget() {
	return this;
    }
}
