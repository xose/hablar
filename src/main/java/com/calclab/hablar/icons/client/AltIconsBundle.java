package com.calclab.hablar.icons.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public interface AltIconsBundle extends IconsBundle {
	public static final AltIconsBundle bundle = GWT.create(AltIconsBundle.class);

	@Override
	@Source("alt/buddy-small.png")
	ImageResource buddyIcon();

	@Override
	@Source("alt/buddy-small-dnd.png")
	ImageResource buddyIconDnd();

	@Override
	@Source("alt/buddy-small-off.png")
	ImageResource buddyIconOff();

	@Override
	@Source("alt/buddy-small-on.png")
	ImageResource buddyIconOn();

	@Override
	@Source("alt/buddy-small-wait.png")
	ImageResource buddyIconWait();

	@Override
	@Source("alt/chat.png")
	ImageResource chatIcon();

	@Override
	@Source("alt/dnd.png")
	ImageResource dndIcon();

	@Override
	@Source("alt/menu.png")
	ImageResource menuIcon();

	@Override
	@Source("alt/off.png")
	ImageResource offIcon();

	@Override
	@Source("alt/on.png")
	ImageResource onIcon();

	@Override
	@Source("alt/roster.png")
	ImageResource rosterIcon();
}
