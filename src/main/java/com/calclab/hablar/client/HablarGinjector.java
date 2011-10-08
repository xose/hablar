package com.calclab.hablar.client;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.emite.xep.mucchatstate.client.MUCChatStateManager;
import com.calclab.emite.xep.mucdisco.client.RoomDiscoveryManager;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.storage.client.PrivateStorageManager;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ HablarModule.class })
public interface HablarGinjector extends Ginjector {

	XmppConnection getXmppConnection();

	XmppSession getXmppSession();

	XmppRoster getXmppRoster();
	
	Services getServices();

	PairChatManager getPairChatManager();

	RoomChatManager getRoomChatManager();

	ChatStateManager getChatStateManager();

	RoomDiscoveryManager getRoomDiscoveryManager();

	MUCChatStateManager getMUCChatStateManager();

	PresenceManager getPresenceManager();

	SearchManager getSearchManager();

	VCardManager getVCardManager();

	SubscriptionHandler getSubscriptionHandler();

	PrivateStorageManager getPrivateStorageManager();

}