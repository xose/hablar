package com.calclab.hablar.rooms.client.open;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.calclab.emite.im.client.events.ChatStatusChangedEvent;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.groupchat.client.OpenGroupChatPresenter;
import com.calclab.hablar.rooms.client.OpenNewRoomPresenter;
import com.calclab.hablar.rooms.client.RoomName;

/**
 * An abstract open room presenter. You need to subclass this one.
 * 
 * @see OpenGroupChatPresenter
 * @see OpenNewRoomPresenter
 */
public abstract class OpenRoomPresenter extends EditRoomPresenter {
	protected final String roomsService;
	protected final XmppSession session;
	protected final RoomChatManager roomManager;

	public OpenRoomPresenter(final XmppSession session, final RoomChatManager roomManager, final String type, final HablarEventBus eventBus,
			final EditRoomDisplay display, final String roomsService) {
		super(type, eventBus, display);
		this.session = session;
		this.roomManager = roomManager;
		this.roomsService = roomsService;

	}

	@Override
	protected void onAccept() {
		final XmppURI user = session.getCurrentUserURI();
		final String roomName = RoomName.encode(display.getRoomName().getValue(), user.getResource());
		final XmppURI roomUri = XmppURI.uri(roomName, roomsService, user.getNode());
		final RoomChat room = roomManager.open(roomUri);

		room.addChatStatusChangedHandler(true, new ChatStatusChangedEvent.Handler() {
			@Override
			public void onChatStatusChanged(final ChatStatusChangedEvent event) {
				if (event.is(ChatStatus.ready)) {
					sendInvitations(room);
				}
			}
		});
	}
}
