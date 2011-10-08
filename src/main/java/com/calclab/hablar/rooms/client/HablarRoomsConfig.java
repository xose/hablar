package com.calclab.hablar.rooms.client;

import com.calclab.emite.core.client.browser.PageAssist;

public class HablarRoomsConfig {

	public static HablarRoomsConfig getFromMeta() {
		final HablarRoomsConfig config = new HablarRoomsConfig();
		config.roomsService = PageAssist.getMetaString("hablar.roomService", null);
		config.sendButtonVisible = PageAssist.getMetaBoolean("hablar.sendButton", true);
		return config;
	}

	/**
	 * Show or not the send button in ui
	 */
	public boolean sendButtonVisible;

	/**
	 * The room service name
	 */
	public String roomsService;

}
