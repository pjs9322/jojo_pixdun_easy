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

package com.jojoeasypixel.jojoeasypixeldungeon.items.rings;

import com.jojoeasypixel.jojoeasypixeldungeon.actors.Char;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;

import java.text.DecimalFormat;

public class RingOfFuror extends Ring {
	
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (Math.pow(1.105f, soloBonus()) - 1f)));
		} else {
			return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(10.5f));
		}
	}

	@Override
	protected RingBuff buff( ) {
		return new Furor();
	}
	
	public static float attackDelayMultiplier(Char target ){
		return 1f / (float)Math.pow(1.105, getBonus(target, Furor.class));
	}

	public class Furor extends RingBuff {
	}
}