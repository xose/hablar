package com.calclab.hablar.client;

import com.calclab.emite.core.client.EmiteCoreModule;
import com.calclab.emite.im.client.IMModule;
import com.calclab.emite.xep.disco.client.DiscoveryModule;
import com.calclab.emite.xep.muc.client.MucModule;
import com.calclab.emite.xep.mucdisco.client.MucDiscoveryModule;
import com.calclab.emite.xep.search.client.SearchModule;
import com.calclab.emite.xep.vcard.client.VCardModule;
import com.google.gwt.inject.client.AbstractGinModule;

public class HablarModule extends AbstractGinModule {

	@Override
	protected void configure() {
		install(new EmiteCoreModule());
		install(new IMModule());
		install(new DiscoveryModule());
		install(new MucModule());
		install(new MucDiscoveryModule());
		install(new SearchModule());
		install(new VCardModule());
	}

}
