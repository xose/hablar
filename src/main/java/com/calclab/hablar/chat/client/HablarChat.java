package com.calclab.hablar.chat.client;

import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.hablar.chat.client.state.HablarChatStateManager;
import com.calclab.hablar.chat.client.ui.PairChatPage;
import com.calclab.hablar.chat.client.ui.PairChatPresenter;
import com.calclab.hablar.chat.client.ui.chatmessageformat.ChatMessageFormatter;
import com.calclab.hablar.chat.client.ui.chatmessageformat.StandardChatMessageFormatReplacements;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.container.PageAddedEvent;
import com.calclab.hablar.core.client.container.PageAddedHandler;
import com.calclab.hablar.core.client.ui.emoticons.EmoticonsChatMessageFormatReplacements;

public class HablarChat {

	public HablarChat(final Hablar hablar, final ChatConfig chatConfig, final XmppRoster roster, final PairChatManager chatManager, final ChatStateManager stateManager) {
        // Set up the message replacements
        ChatMessageFormatter.addReplacements(new StandardChatMessageFormatReplacements());
        
        if(chatConfig.enableEmoticons) {
                ChatMessageFormatter.addReplacements(new EmoticonsChatMessageFormatReplacements());
        }

        new HablarChatManager(roster, chatManager, hablar, chatConfig);

		hablar.addPageAddedHandler(new PageAddedHandler() {
			@Override
			public void onPageAdded(final PageAddedEvent event) {
				if (event.isType(PairChatPresenter.TYPE)) {
					final PairChatPage page = (PairChatPage) event.getPage();
					new HablarChatStateManager(stateManager, page);
				}
			}
		}, true);
	}

}
