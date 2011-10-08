package com.calclab.hablar.vcard.client;

import com.calclab.emite.core.client.browser.PageAssist;

public class VCardConfig {
	/**
	 * <meta name="hablar.vcard.read-only" value="{true|false}" />
	 * 
	 */
	public boolean vCardReadOnly = false;

	public static VCardConfig getFromMeta() {
		VCardConfig config = new VCardConfig();
		config.vCardReadOnly = PageAssist.getMetaBoolean("hablar.vcard.read-only", false);
		return config;
	}

}
