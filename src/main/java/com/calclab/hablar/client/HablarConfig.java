package com.calclab.hablar.client;

import com.calclab.emite.core.client.browser.PageAssist;
import com.calclab.hablar.chat.client.ChatConfig;
import com.calclab.hablar.core.client.HablarDisplay;
import com.calclab.hablar.core.client.pages.tabs.TabsLayout;
import com.calclab.hablar.core.client.pages.tabs.TabsLayout.TabHeaderSize;
import com.calclab.hablar.dock.client.DockConfig;
import com.calclab.hablar.rooms.client.HablarRoomsConfig;
import com.calclab.hablar.roster.client.RosterConfig;
import com.calclab.hablar.search.client.SearchConfig;
import com.calclab.hablar.vcard.client.VCardConfig;

public class HablarConfig {

	private static void createTabHeaderSize(final HablarConfig config) {
		final String height = PageAssist.getMetaString("hablar.tabHeaderHeight", "23px");
		final String width = PageAssist.getMetaString("hablar.tabHeaderWidth", "120px");
		final int trim = PageAssist.getMetaInteger("hablar.tabHeaderTrim", 10);
		config.tabHeaderSize = TabHeaderSize.create(height, width, trim);
	}

	/**
	 * Retrieve Hablar configuration from meta tags in html
	 */
	public static HablarConfig getFromMeta() {
		final HablarConfig config = new HablarConfig();

		config.hasRoster = PageAssist.getMetaBoolean("hablar.roster", true);
		config.hasSearch = PageAssist.getMetaBoolean("hablar.search", true);
		config.hasSignals = PageAssist.getMetaBoolean("hablar.hasSignals", true);
		config.hasChat = PageAssist.getMetaBoolean("hablar.hasChats", true);
		config.hasVCard = PageAssist.getMetaBoolean("hablar.hasVCard", true);
		config.hasCopyToClipboard = PageAssist.getMetaBoolean("hablar.hasCopyToClipboard", true);
		config.hasSound = PageAssist.getMetaBoolean("hablar.hasSound", true);

		config.dockRoster = PageAssist.getMetaString("hablar.dockRoster", "left");

		final String layout = PageAssist.getMetaString("hablar.layout", null);
		if ("tabs".equals(layout)) {
			config.layout = HablarDisplay.Layout.tabs;
		} else {
			config.layout = HablarDisplay.Layout.accordion;
		}
		if (config.layout == HablarDisplay.Layout.tabs) {
			createTabHeaderSize(config);
		}

		config.roomsConfig = HablarRoomsConfig.getFromMeta();
		config.rosterConfig = RosterConfig.getFromMeta();
		config.searchConfig = SearchConfig.getFromMeta();
		config.vcardConfig = VCardConfig.getFromMeta();
		config.chatConfig = ChatConfig.getFromMeta();
		config.dockConfig = DockConfig.getFromMeta();
		return config;
	}

	public VCardConfig vcardConfig;

	/**
	 * The Rooms configuration
	 */
	public HablarRoomsConfig roomsConfig = new HablarRoomsConfig();

	/**
	 * Has ChatModule
	 */
	public boolean hasChat = true;

	/**
	 * Dock the roster: "left" or "right"
	 */
	public String dockRoster = "left";

	/**
	 * Choose a layout
	 */
	public HablarDisplay.Layout layout = HablarDisplay.Layout.tabs;

	/**
	 * The size of the header in tabs layout
	 */
	public TabsLayout.TabHeaderSize tabHeaderSize;

	/**
	 * Install Roster module
	 */
	public boolean hasRoster = true;

	/**
	 * Install Search module
	 */
	public boolean hasSearch = true;

	/**
	 * Install signals module
	 */
	public boolean hasSignals = true;

	/**
	 * Show user page docked on top
	 */
	public boolean dockUser = true;

	/**
	 * The Search module configuration
	 */
	public RosterConfig rosterConfig = new RosterConfig();

	/**
	 * The Search module configuration
	 */
	public SearchConfig searchConfig = new SearchConfig();

	/**
	 * Install the copy-to-clipboard module
	 */
	public boolean hasCopyToClipboard = true;

	/**
	 * Install the VCard module
	 */
	public boolean hasVCard = true;

	/**
	 * Install SoundSignalModule
	 */
	public boolean hasSound = true;

	public ChatConfig chatConfig = new ChatConfig();

	public DockConfig dockConfig;
}
