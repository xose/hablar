package com.calclab.hablar.html.client;

import com.calclab.emite.core.client.browser.PageAssist;

public class HtmlConfig {

    public static HtmlConfig getFromMeta() {
	final HtmlConfig config = new HtmlConfig();
	config.hasLogin = PageAssist.getMetaBoolean("hablar.login", false);
	config.inline = PageAssist.getMetaString("hablar.inline", null);
	config.width = PageAssist.getMetaString("hablar.width", "100%");
	config.height = PageAssist.getMetaString("hablar.height", "100%");
	return config;
    }

    /**
     * Width
     */
    public String width = "100%";

    /**
     * Height
     */
    public String height = "100%";

    /**
     * Install Logger module
     */
    public boolean hasLogger = false;

    /**
     * Show or not login panel
     */
    public boolean hasLogin = false;

    /**
     * If not null, show 'hablar' inside the div with the id given
     */
    public String inline = null;
}
