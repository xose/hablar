package com.calclab.hablar.login.client;

import com.calclab.emite.core.client.events.SessionStatusChangedEvent;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter;
import com.calclab.hablar.core.client.page.events.UserMessageEvent;
import com.calclab.hablar.icons.client.IconsBundle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;

public class LoginPage extends PagePresenter<LoginDisplay> {
	public static final String LOGIN_MESSAGE = "LoginMessage";
	private static int index = 0;
	private final XmppSession session;

	public LoginPage(final XmppSession session, final HablarEventBus eventBus, final LoginDisplay display) {
		super("Login", "" + ++index, eventBus, display);
		this.session = session;
		getState().setPageIcon(IconsBundle.bundle.offIcon());

		display.getAction().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (SessionStatus.disconnected.equals(session.getStatus())) {
					login();
				} else {
					logout();
				}
			}
		});

		session.addSessionStatusChangedHandler(true, new SessionStatusChangedEvent.Handler() {
			@Override
			public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
				setStatus(event.getStatus());
			}
		});

	}

	private void fireUserMessage(final SessionStatus state) {
		final String message = LoginMessages.msg.sessionState(state.toString());
		eventBus.fireEvent(new UserMessageEvent(this, message, LOGIN_MESSAGE));
	}

	protected void login() {
		final String user = display.getUser().getText();
		final String password = display.getPassword().getText();
		session.login(XmppURI.uri(user), password);
	}

	protected void logout() {
		session.logout();
	}

	private void setStatus(final SessionStatus status) {
		String actionText, pageTitle;
		ImageResource icon;
		boolean actionEnabled;
		if (SessionStatus.isReady(status)) {
			actionText = LoginMessages.msg.logout();
			actionEnabled = true;
			final String userName = session.getCurrentUserURI().getNode();
			pageTitle = LoginMessages.msg.connectedAs(userName);
			icon = IconsBundle.bundle.onIcon();
		} else if (SessionStatus.isDisconnected(status)) {
			actionText = LoginMessages.msg.login();
			actionEnabled = true;
			pageTitle = LoginMessages.msg.disconnected();
			icon = IconsBundle.bundle.offIcon();
		} else {
			pageTitle = actionText = LoginMessages.msg.waitDots();
			actionEnabled = false;
			icon = IconsBundle.bundle.offIcon();
		}
		display.getActionText().setText(actionText);
		display.setActionEnabled(actionEnabled);
		display.addMessage(LoginMessages.msg.sessionStateMessage(status.toString()));
		getState().setPageTitle(pageTitle);
		getState().setPageTitleTooltip(pageTitle);
		getState().setPageIcon(icon);
		fireUserMessage(status);
	}
}
