package com.calclab.hablar.vcard.client;

import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.calclab.emite.xep.vcard.client.VCardResponseEvent;
import com.calclab.hablar.client.HablarMessages;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class OthersVCardPresenter extends VCardPage {

	private final VCardManager vCardManager;

	public OthersVCardPresenter(final VCardManager vCardManager, final HablarEventBus eventBus, final VCardDisplay display) {
		super(eventBus, display);
		this.vCardManager = vCardManager;
		display.setAcceptVisible(false);
		display.setCancelVisible(true);
		display.setCancelText(HablarMessages.msg.closeAction());
		display.getCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				requestVisibility(Visibility.notFocused);
			}
		});
	}

	@Override
	protected void onBeforeFocus() {
		display.setLoadingVisible(true);
		display.setFormVisible(false);
	}

	public void setUser(final XmppURI jid) {
		display.setPageTitle(VCardMessages.msg.profileOfBuddy(jid.getShortName()));
		vCardManager.getUserVCard(jid, new VCardResponseEvent.Handler() {
			@Override
			public void onVCardResponse(final VCardResponseEvent event) {
				modelToDisplay(event.getVCardResponse().getVCard());
				display.setLoadingVisible(false);
				display.setFormVisible(true);
			}
		});

	}
}
