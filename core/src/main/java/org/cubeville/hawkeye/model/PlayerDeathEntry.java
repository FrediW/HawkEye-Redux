/*
 * HawkEye Redux
 * Copyright (C) 2012-2013 Cubeville <http://www.cubeville.org> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.cubeville.hawkeye.model;

import java.util.UUID;

import org.cubeville.hawkeye.Action;
import org.cubeville.hawkeye.entity.Entity;
import org.cubeville.hawkeye.location.Location;

public class PlayerDeathEntry extends AbstractEntityEntry {

	public PlayerDeathEntry(DatabaseEntry entry) {
		super(entry.getAction(), entry);
	}

	public PlayerDeathEntry(Action action, UUID player, Location location, Entity killer) {
		super(action, player, location, killer);
	}

}
