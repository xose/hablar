package com.calclab.hablar.console.client;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.StanzaReceivedEvent;
import com.calclab.emite.core.client.events.StanzaSentEvent;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter;
import com.calclab.hablar.icons.client.IconsBundle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HasText;

public class ConsolePresenter extends PagePresenter<ConsoleDisplay> {
	private static final String TYPE = "Logger";
	

	public ConsolePresenter(final XmppConnection connection, final XmppSession session, final HablarEventBus eventBus, final ConsoleDisplay display, final Services services) {
		super(TYPE, eventBus, display);
		model.init(IconsBundle.bundle.consoleIcon(), "Console", "Console");
		setVisibility(Visibility.hidden);
		model.setCloseable(true);

		connection.addStanzaReceivedHandler(new StanzaReceivedEvent.Handler() {
			@Override
			public void onStanzaReceived(final StanzaReceivedEvent event) {
				display.add(event.getStanza().toString(), "input", session.getStatus().toString());
			}
		});
		connection.addStanzaSentHandler(new StanzaSentEvent.Handler() {
			@Override
			public void onStanzaSent(final StanzaSentEvent event) {
				display.add(event.getStanza().toString(), "output", session.getStatus().toString());
			}
		});

		display.getClear().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.clear();
			}
		});

		display.getInput().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					final HasText input = display.getInputText();
					if (event.getNativeEvent().getCtrlKey()) {
						// With ctrl + Enter insert a newline (Can be useful
						// also for chat -implemented in other clients-)
						final String newText = input.getText() + "\n";
						input.setText(newText);
						display.setInputCursorPos(newText.length());
					} else {
						final String packet = input.getText();
						final boolean isReady = SessionStatus.ready.equals(session.getStatus());
						if (isReady && !packet.isEmpty()) {
							session.send(services.toXML(packet));
							input.setText("");
						}
					}
				}
			}
		});
	}
}
