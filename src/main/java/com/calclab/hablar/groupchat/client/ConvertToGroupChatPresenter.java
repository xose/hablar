package com.calclab.hablar.groupchat.client;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.events.ChatStatusChangedEvent;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.rooms.client.open.EditRoomPresenter;
import com.calclab.hablar.rooms.client.open.EditRoomWidget;

public class ConvertToGroupChatPresenter extends EditRoomPresenter {

	private static final String TYPE = "ConvertToGroupChat";
	private PairChat chat;
	private final String roomsService;
	private final XmppSession session;
	private final XmppRoster roster;
	private final PairChatManager chatManager;
	private final RoomChatManager roomManager;

	public ConvertToGroupChatPresenter(final XmppSession session, final XmppRoster roster, final PairChatManager chatManager, final RoomChatManager roomManager,
			final String roomsService, final HablarEventBus eventBus, final EditRoomWidget openRoomWidget) {
		super(TYPE, eventBus, openRoomWidget);
		this.session = session;
		this.roster = roster;
		this.chatManager = chatManager;
		this.roomManager = roomManager;
		this.roomsService = roomsService;
		display.setPageTitle(GroupChatMessages.msg.convertPageTitle());
		display.setAcceptText(GroupChatMessages.msg.convertToGroupAction());
	}

	public void setChat(final PairChat chat) {
		this.chat = chat;
	}

	@Override
	protected void onAccept() {

		final XmppURI currentJid = session.getCurrentUserURI().getJID();
		final String roomName = display.getRoomName().getValue();
		final XmppURI roomUri = XmppURI.uri(roomName, roomsService, currentJid.getNode());

		final ChatProperties newProperties = new ChatProperties(roomUri, chat.getProperties());

		final RoomChat room = roomManager.openChat(newProperties, true);

		room.addChatStatusChangedHandler(true, new ChatStatusChangedEvent.Handler() {
			@Override
			public void onChatStatusChanged(final ChatStatusChangedEvent event) {
				if (event.is(ChatStatus.ready)) {
					sendInvitations(room);
					chatManager.close(chat);
				}
			}
		});
	}

	@Override
	protected void onPageOpen() {

		final XmppURI currentJid = session.getCurrentUserURI().getJID();
		final XmppURI chatJid = chat.getURI();
		String name = GroupChatMessages.msg.defaultRoomName(currentJid.getNode(), chatJid.getNode());
		name = name.replaceAll("[^-a-zA-Z0-9 ]", "-");
		display.getRoomName().setValue(name);

		display.clearList();
		for (final RosterItem item : roster.getItems()) {
			if (chatJid.equals(item.getJID())) {
				createItem(item, true);
			} else {
				createItem(item, false);
			}
		}
	}
}
