package com.calclab.hablar.chat.client;

import java.util.HashMap;
import java.util.Map.Entry;

import com.calclab.emite.core.client.stanzas.Presence.Show;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.events.PairChatChangedEvent;
import com.calclab.emite.im.client.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.hablar.chat.client.ui.ChatDisplay;
import com.calclab.hablar.chat.client.ui.ChatWidget;
import com.calclab.hablar.chat.client.ui.PairChatPage;
import com.calclab.hablar.chat.client.ui.PairChatPresenter;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;

public class HablarChatManager {

	/**
	 * Factory to create a chat display
	 */
	public static interface ChatPageFactory {
		/**
		 * Create a ChatDisplay instance.
		 * 
		 * @param sendButtonVisible
		 *            whether the "Send" button should be visible.
		 * @return the ChatDisplay instance.
		 */
		ChatDisplay create(boolean sendButtonVisible);
	}

	public static interface PairChatPresenterFactory {
		/**
		 * Create a ChatPresenter instance.
		 * 
		 * @param sendButtonVisible
		 *            whether the "Send" button should be visible.
		 * @return the ChatDisplay instance.
		 */
		PairChatPresenter create(HablarEventBus eventBus, PairChat chat, ChatDisplay display);
	}

	private final HashMap<PairChat, PairChatPage> chatPages;

	private final ChatPageFactory chatPageFactory;
	private final PairChatPresenterFactory chatPresenterFactory;
	private final boolean sendButtonVisible;

	private final Hablar hablar;

	private final XmppRoster roster;

	public HablarChatManager(final XmppRoster roster, final PairChatManager chatManager, final Hablar hablar, final ChatConfig config) {
		this(roster, chatManager, hablar, config, new ChatPageFactory() {
			@Override
			public ChatDisplay create(final boolean sendButtonVisible) {
				return new ChatWidget(sendButtonVisible);
			}
		}, new PairChatPresenterFactory() {

			@Override
			public PairChatPresenter create(final HablarEventBus eventBus, final PairChat chat, final ChatDisplay display) {
				return new PairChatPresenter(roster, eventBus, chat, display);
			}
		});
	}

	public HablarChatManager(final XmppRoster roster, final PairChatManager chatManager, final Hablar hablarPresenter, final ChatConfig config,
			final ChatPageFactory chatPageFactory, final PairChatPresenterFactory chatPresenterFactory) {
		this.roster = roster;
		hablar = hablarPresenter;

		this.chatPageFactory = chatPageFactory;
		this.chatPresenterFactory = chatPresenterFactory;
		chatPages = new HashMap<PairChat, PairChatPage>();

		if (config.openChat != null) {
			chatManager.open(config.openChat);
		}

		chatManager.addPairChatChangedHandler(new PairChatChangedEvent.Handler() {

			@Override
			public void onPairChatChanged(final PairChatChangedEvent event) {
				if (event.isCreated()) {
					createChat(event.getChat(), Visibility.notFocused);
				}

				if (event.isOpened()) {
					final PairChatPage page = chatPages.get(event.getChat());
					if (page != null) {
						page.requestVisibility(Visibility.focused);
					} else {
						createChat(event.getChat(), Visibility.notFocused);
					}
				}

				if (event.isClosed()) {
					final PairChatPage page = chatPages.get(event.getChat());
					page.requestVisibility(Visibility.hidden);
				}
			}
		});

		roster.addRosterItemChangedHandler(new RosterItemChangedEvent.Handler() {

			@Override
			public void onRosterItemChanged(final RosterItemChangedEvent event) {
				if (event.isModified()) {
					final XmppURI jid = event.getRosterItem().getJID();
					for (final Entry<PairChat, PairChatPage> entry : chatPages.entrySet()) {
						if (entry.getKey().getURI().equalsNoResource(jid)) {
							entry.getValue().setPresence(event.getRosterItem().isAvailable(), event.getRosterItem().getShow());
						}
					}
				}
			}
		});

		sendButtonVisible = config.sendButtonVisible;

	}

	private void createChat(final PairChat chat, final Visibility visibility) {
		final ChatDisplay display = chatPageFactory.create(sendButtonVisible);
		final PairChatPresenter presenter = chatPresenterFactory.create(hablar.getEventBus(), chat, display);
		chatPages.put(chat, presenter);
		hablar.addPage(presenter);

		final RosterItem item = roster.getItemByJID(chat.getURI().getJID());
		final Show show = item != null ? item.getShow() : Show.unknown;
		final boolean available = item != null ? item.isAvailable() : false;
		presenter.setPresence(available, show);
	}

}
