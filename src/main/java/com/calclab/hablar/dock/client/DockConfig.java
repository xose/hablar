package com.calclab.hablar.dock.client;

import com.calclab.emite.core.client.browser.PageAssist;


public class DockConfig {
	public static DockConfig getFromMeta() {
		final DockConfig dockConfig = new DockConfig();
		final String rosterDock = PageAssist.getMetaString("hablar.dock.roster", "left");
		dockConfig.rosterDock = "right".equalsIgnoreCase(rosterDock) ? "right" : "left";
		return dockConfig;
	}

	public String rosterDock = "left";
	public double rosterWidth = 250;
	public int headerSize = 24;

}
