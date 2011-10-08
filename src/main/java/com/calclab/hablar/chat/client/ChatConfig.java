package com.calclab.hablar.chat.client;

import com.calclab.emite.core.client.browser.PageAssist;
import com.calclab.emite.core.client.stanzas.XmppURI;


/**
 * Configuration of chat module. It can be configured by html meta tags <br/>
 * Meta tags available:
 * 
 * "hablar.chatWidget"
 * 
 */
public class ChatConfig {

	public static ChatConfig getFromMeta() {
		ChatConfig config = new ChatConfig();
		String chatURI = PageAssist.getMetaString("hablar.chatWidget", null);
		config.openChat = XmppURI.uri(chatURI);
		config.sendButtonVisible = PageAssist.getMetaBoolean("hablar.sendButton", true);
		config.enableEmoticons = PageAssist.getMetaBoolean("hablar.enableEmoticons", true);
		return config;
	}

	/**
	 * Hablar module will open automatically this chat page when load
	 */
	public XmppURI openChat;
	/**
	 * A Send button in the chat pages
	 */
	public boolean sendButtonVisible = true;

    /**
     * Whether to enable emoticons. Default to <code>true</code>.
     */
    public boolean enableEmoticons = true;
}
