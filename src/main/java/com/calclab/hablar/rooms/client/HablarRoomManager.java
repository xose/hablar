package com.calclab.hablar.rooms.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatChangedEvent;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.emite.xep.muc.client.RoomInvitationReceivedEvent;
import com.calclab.hablar.chat.client.ui.ChatMessage;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.rooms.client.room.RoomDisplay;
import com.calclab.hablar.rooms.client.room.RoomPresenter;
import com.calclab.hablar.rooms.client.room.RoomWidget;

public class HablarRoomManager {

	public static interface RoomPageFactory {
		RoomDisplay create(boolean sendButtonVisible);
	}

	public static interface RoomPresenterFactory {
		RoomPresenter create(HablarEventBus eventBus, RoomChat room, RoomDisplay display);
	}

	private final Hablar hablar;
	private final RoomPageFactory factory;
	private final RoomPresenterFactory presenterFactory;
	private final HashMap<XmppURI, RoomPresenter> roomPages;
	private final ArrayList<RoomInvitation> acceptedInvitations;

	public HablarRoomManager(final RoomChatManager rooms, final Hablar hablar, final HablarRoomsConfig config, final RoomPageFactory factory,
			final RoomPresenterFactory presenterFactory) {
		this.hablar = hablar;
		this.factory = factory;
		this.presenterFactory = presenterFactory;
		acceptedInvitations = new ArrayList<RoomInvitation>();
		roomPages = new HashMap<XmppURI, RoomPresenter>();

		rooms.addRoomChatChangedHandler(new RoomChatChangedEvent.Handler() {
			@Override
			public void onRoomChatChanged(final RoomChatChangedEvent event) {
				if (event.isCreated()) {
					createRoom(event.getChat());
				} else if (event.isOpened()) {
					openRoom(event.getChat());
				}
			}
		});

		rooms.addRoomInvitationReceivedHandler(new RoomInvitationReceivedEvent.Handler() {
			@Override
			public void onRoomInvitationReceived(final RoomInvitationReceivedEvent event) {
				final RoomInvitation invitation = event.getRoomInvitation();
				acceptedInvitations.add(invitation);
				rooms.acceptRoomInvitation(invitation);
			}
		});
	}

	public HablarRoomManager(final XmppSession session, final XmppRoster roster, final RoomChatManager rooms, final Hablar hablar, final HablarRoomsConfig config) {
		this(rooms, hablar, config, new RoomPageFactory() {
			@Override
			public RoomDisplay create(final boolean sendButtonVisible) {
				return new RoomWidget(sendButtonVisible);
			}
		}, new RoomPresenterFactory() {
			@Override
			public RoomPresenter create(final HablarEventBus eventBus, final RoomChat room, final RoomDisplay display) {
				return new RoomPresenter(session, roster, eventBus, room, display);
			}

		});
	}

	protected void createRoom(final RoomChat room) {
		final RoomDisplay display = factory.create(true);
		final RoomPresenter presenter = presenterFactory.create(hablar.getEventBus(), room, display);
		roomPages.put(room.getURI(), presenter);
		hablar.addPage(presenter);
	}

	protected RoomInvitation getInvitation(final XmppURI uri) {
		for (final RoomInvitation invitation : acceptedInvitations) {
			if (invitation.getRoomURI().getNode().equals(uri.getNode())) {
				return invitation;
			}
		}
		return null;
	}

	protected void openRoom(final RoomChat room) {
		final RoomPresenter roomPage = roomPages.get(room.getURI());
		assert roomPage != null : "Error in room pages - HablarRoomManager";
		final RoomInvitation invitation = getInvitation(room.getURI());
		if (invitation != null) {
			acceptedInvitations.remove(invitation);
			String message = "You have been invited to this group chat";
			message += invitation.getInvitor() != null ? " by " + invitation.getInvitor().getNode() : "";
			message += invitation.getReason() != null ? ": " + invitation.getReason() : "";
			roomPage.addMessage(new ChatMessage(message));
			roomPage.requestVisibility(Visibility.notFocused);
		} else {
			roomPage.requestVisibility(Visibility.focused);
		}
	}
}
