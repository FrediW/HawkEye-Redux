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

package org.cubeville.hawkeye;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.cubeville.hawkeye.model.DatabaseEntry;
import org.cubeville.hawkeye.model.Entry;
import org.cubeville.lib.jnbt.ByteArrayTag;
import org.cubeville.lib.jnbt.CompoundTag;
import org.cubeville.lib.jnbt.DoubleTag;
import org.cubeville.lib.jnbt.NBTInputStream;
import org.cubeville.lib.jnbt.StringTag;
import org.cubeville.lib.jnbt.Tag;

public class EntryImporter implements Runnable {

	public EntryImporter() {

	}

	@Override
	public void run() {
		importData();
	}

	private void importData() {
		File[] files = new File("plugins/HawkEye/data/").listFiles();
		if (files == null || files.length == 0) return;

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().toLowerCase().endsWith(".hawk")) {
				Tag tag = readFile(file);
				if (!(tag instanceof CompoundTag)) return;
				readTag((CompoundTag) tag);
				file.delete();
			}
		}
	}

	private Tag readFile(File file) {
		NBTInputStream nbt = null;

		try {
			nbt = new NBTInputStream(new FileInputStream(file));
			return nbt.readTag();
		} catch (FileNotFoundException ignore) {
		} catch (IOException e) {
			HawkEye.getLogger().error("Unable to read data file", e);
		} finally {
			try {
				if (nbt != null) nbt.close();
			} catch (IOException ignore) { }
		}

		return null;
	}

	private void readTag(CompoundTag data) {
		Map<String, Tag> entries = data.getValue();

		for (Map.Entry<String, Tag> tag : entries.entrySet()) {
			if (!(tag.getValue() instanceof CompoundTag)) continue;

			try {
				Entry entry = createEntry((CompoundTag) tag.getValue());
				if (entry != null) HawkEye.getConsumer().addEntry(entry);
			} catch (Exception ignore) {
			}
		}
	}

	private Entry createEntry(CompoundTag tag) {
		Map<String, Tag> tags = tag.getValue();

		UUID player = UUID.fromString(((StringTag) tags.get("player")).getValue());
		String action = ((StringTag) tags.get("action")).getValue();
		Timestamp date = Timestamp.valueOf(((StringTag) tags.get("date")).getValue());
		String world = ((StringTag) tags.get("world")).getValue();
		double x = ((DoubleTag) tags.get("x")).getValue();
		double y = ((DoubleTag) tags.get("y")).getValue();
		double z = ((DoubleTag) tags.get("z")).getValue();
		String data = ((StringTag) tags.get("data")).getValue();
		byte[] nbt = ((ByteArrayTag) tags.get("nbt")).getValue();

		int playerId = HawkEye.getDataManager().getPlayerId(player);
		int worldId = HawkEye.getDataManager().getWorldId(world);

		DatabaseEntry entry = new DatabaseEntry(-1, playerId, action, date, worldId, x, y, z, data, nbt);
		return entry.toEntry();
	}

}
