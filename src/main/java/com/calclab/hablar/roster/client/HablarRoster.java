package com.calclab.hablar.roster.client;

import com.calclab.emite.core.client.events.SessionStatusChangedEvent;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionHandler.Behaviour;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.core.client.ui.menu.SimpleAction;
import com.calclab.hablar.core.client.util.NonBlockingCommandScheduler;
import com.calclab.hablar.roster.client.page.RosterPage;
import com.calclab.hablar.roster.client.page.RosterPresenter;
import com.calclab.hablar.roster.client.page.RosterWidget;

public class HablarRoster {

	private final RosterPage rosterPage;

	public HablarRoster(final Hablar hablar, final RosterConfig rosterConfig, final XmppSession session, final XmppRoster roster,
			final PairChatManager chatManager, final SubscriptionHandler subscriptionHandler) {

		final NonBlockingCommandScheduler commandQueue = new NonBlockingCommandScheduler();

		subscriptionHandler.setBehaviour(Behaviour.acceptAll);

		if ((rosterConfig.rosterItemClickAction == null) && rosterConfig.oneClickChat) {
			rosterConfig.rosterItemClickAction = new SimpleAction<RosterItem>(RosterMessages.msg.clickToChatWith(), "rosterItemClickAction") {
				@Override
				public void execute(final RosterItem item) {
					chatManager.open(item.getJID());
				}
			};
		}

		rosterPage = new RosterPresenter(session, roster, chatManager, hablar.getEventBus(), new RosterWidget(), rosterConfig, commandQueue);
		rosterPage.setVisibility(Visibility.notFocused);
		hablar.addPage(rosterPage);

		session.addSessionStatusChangedHandler(true, new SessionStatusChangedEvent.Handler() {
			@Override
			public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
				if (SessionStatus.ready.equals(event.getStatus())) {
					rosterPage.requestVisibility(Visibility.focused);
				}
			}
		});
		rosterPage.addHighPriorityActions();
	}

	public void addLowPriorityActions() {
		rosterPage.addLowPriorityActions();
	}

	public RosterPage getRosterPage() {
		return rosterPage;
	}
}
