package com.calclab.hablar.login.client;

import com.calclab.emite.core.client.events.SessionStatusChangedEvent;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.page.Page;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;

public class HablarLogin {

	private static void setStatus(final Page<?> login, final SessionStatus sessionStatus) {
		if (SessionStatus.disconnected.equals(sessionStatus)) {
			login.requestVisibility(Visibility.focused);
		}
	}

	public HablarLogin(final Hablar hablar, final LoginConfig config, final XmppSession session) {
		final LoginPage login = new LoginPage(session, hablar.getEventBus(), new LoginWidget());
		hablar.addPage(login);

		session.addSessionStatusChangedHandler(true, new SessionStatusChangedEvent.Handler() {
			@Override
			public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
				setStatus(login, event.getStatus());
			}
		});

		login.getDisplay().getUser().setText(config.userName);
		login.getDisplay().getPassword().setText(config.password);
	}

}
