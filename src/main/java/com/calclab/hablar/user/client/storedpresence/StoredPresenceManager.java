package com.calclab.hablar.user.client.storedpresence;

import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.stanzas.Presence.Show;
import com.calclab.emite.xep.storage.client.PrivateStorageManager;
import com.calclab.emite.xep.storage.client.PrivateStorageResponseEvent;
import com.google.gwt.core.client.GWT;

public class StoredPresenceManager {
	private final PrivateStorageManager manager;

	public StoredPresenceManager(final PrivateStorageManager manager) {
		this.manager = manager;
	}

	public void add(final StoredPresence presence, final PrivateStorageResponseEvent.Handler handler) {
		manager.retrieve(StoredPresences.empty, new PrivateStorageResponseEvent.Handler() {
			@Override
			public void onStorageResponse(PrivateStorageResponseEvent event) {
				IQ response = event.getResponseIQ();
				if (IQ.isSuccess(response)) {
					final StoredPresences currentPresences = StoredPresences.parse(response);
					if (currentPresences.add(presence)) {
						manager.store(currentPresences, handler);
					}
				} else {
					checkError(response);
				}
			}
		});
	}

	public void add(final String status, final Show show, final PrivateStorageResponseEvent.Handler handler) {
		add(new StoredPresence(status, show), handler);
	}

	public void clearAll() {
		manager.store(StoredPresences.empty, new PrivateStorageResponseEvent.Handler() {
			@Override
			public void onStorageResponse(PrivateStorageResponseEvent event) {
				checkError(event.getResponseIQ());
			}
		});
	}

	public void get(final PrivateStorageResponseEvent.Handler handler) {
		manager.retrieve(StoredPresences.empty, handler);
	}

	private void checkError(final IQ response) {
		if (!IQ.isSuccess(response)) {
			GWT.log("Error clearing stored presences", null);
		}
	}
}
