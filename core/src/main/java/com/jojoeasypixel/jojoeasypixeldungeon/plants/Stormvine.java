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

package com.jojoeasypixel.jojoeasypixeldungeon.plants;

import com.jojoeasypixel.jojoeasypixeldungeon.actors.Char;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Levitation;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Vertigo;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.hero.Hero;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.hero.HeroSubClass;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;

public class Stormvine extends Plant {

	{
		image = 5;
	}

	@Override
	public void activate( Char ch ) {

		if (ch != null) {
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
				Buff.affect(ch, Levitation.class, 10f);
			} else {
				Buff.affect(ch, Vertigo.class, Vertigo.DURATION);
			}
		}
	}

	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_STORMVINE;

			plantClass = Stormvine.class;
		}
	}
}