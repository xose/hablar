package com.calclab.hablar.login.client;

import com.calclab.emite.core.client.browser.PageAssist;

public class LoginConfig {
	public static LoginConfig getFromMeta() {
		LoginConfig config = new LoginConfig();
		config.userName = PageAssist.getMetaString("hablar.user", null);
		config.password = PageAssist.getMetaString("hablar.password", null);

		return config;
	}

	public String password;
	public String userName;
}
