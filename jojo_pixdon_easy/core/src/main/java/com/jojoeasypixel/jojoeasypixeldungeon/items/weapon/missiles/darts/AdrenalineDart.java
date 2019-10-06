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

package com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.missiles.darts;

import com.jojoeasypixel.jojoeasypixeldungeon.actors.Char;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Adrenaline;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;

public class AdrenalineDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.ADRENALINE_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		
		Buff.prolong( defender, Adrenaline.class, Adrenaline.DURATION);
		
		if (attacker.alignment == defender.alignment){
			return 0;
		}
		
		return super.proc(attacker, defender, damage);
	}
}