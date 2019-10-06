/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jojoeasypixel.jojoeasypixeldungeon.levels.rooms.standard;

import com.jojoeasypixel.jojoeasypixeldungeon.items.Gold;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Heap;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Item;
import com.jojoeasypixel.jojoeasypixeldungeon.levels.Level;
import com.jojoeasypixel.jojoeasypixeldungeon.levels.Terrain;
import com.jojoeasypixel.jojoeasypixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class SuspiciousChestRoom extends EmptyRoom {

	@Override
	public int minWidth() {
		return Math.max(5, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(5, super.minHeight());
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		Item i = level.findPrizeItem();

		if ( i == null ){
			i = new Gold().random();
		}

		int center = level.pointToCell(center());

		Painter.set(level, center, Terrain.PEDESTAL);

		if (Random.Int(3) == 0) {
			level.drop(i, center).type = Heap.Type.MIMIC;
		} else {
			level.drop(i, center).type = Heap.Type.CHEST;
		}
	}
}
