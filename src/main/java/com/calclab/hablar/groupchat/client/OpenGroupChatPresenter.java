package com.calclab.hablar.groupchat.client;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.rooms.client.RoomMessages;
import com.calclab.hablar.rooms.client.open.EditRoomDisplay;
import com.calclab.hablar.rooms.client.open.OpenRoomPresenter;

public class OpenGroupChatPresenter extends OpenRoomPresenter {

	private static final String TYPE = "OpenGroupChat";
	private String groupName;
	private final XmppRoster roster;

	public OpenGroupChatPresenter(XmppSession session, RoomChatManager roomManager, XmppRoster roster, final String roomsService, final HablarEventBus eventBus,
			final EditRoomDisplay display) {
		super(session, roomManager, TYPE, eventBus, display, roomsService);
		this.roster = roster;
	}

	public void setGroupName(final String groupName) {
		this.groupName = groupName;
	}

	@Override
	protected void onPageOpen() {
		display.getRoomName().setValue(groupName);
		display.setPageTitle(RoomMessages.msg.openNewGroupChat());
		display.setAcceptText(RoomMessages.msg.openNewGroupChatAction());
		setItems(roster.getItemsByGroup(groupName), true);
		roomNameValidator.validate();
	}

}
