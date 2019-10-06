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

package com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs;

import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.hero.Hero;
import com.jojoeasypixel.jojoeasypixeldungeon.items.artifacts.ChaliceOfBlood;
import com.jojoeasypixel.jojoeasypixeldungeon.items.artifacts.ChaliceOfBloodCopy;

public class Regeneration extends Buff {
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			// 피의 반지 옵션 추가
			ChaliceOfBloodCopy.chaliceRegen regenBuffCopy = Dungeon.hero.buff( ChaliceOfBloodCopy.chaliceRegen.class);

			if (target.HP < regencap() && !((Hero)target).isStarving()) {
				LockedFloor lock = target.buff(LockedFloor.class);
				if (target.HP > 0 && (lock == null || lock.regenOn())) {
					if (regenBuffCopy != null) {
						// 피의 반지 착용시 회복량 최대 체력의 1/4
						target.HP = Math.min(target.HT, (int) (target.HP + target.HT/4f));
					} else {
						target.HP += 1;
					}
					if (target.HP == regencap()) {
						((Hero) target).resting = false;
					}
				}
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			if (regenBuff != null)
				if (regenBuff.isCursed())
					spend( REGENERATION_DELAY * 1.5f );
				else
					spend( REGENERATION_DELAY - regenBuff.itemLevel()*0.9f );
			else if (regenBuffCopy != null)
				// 피의 반지 착용시 회복 딜레이 1턴 고정
				spend( 1f );
			else
				spend( REGENERATION_DELAY );

		} else {
			
			diactivate();
			
		}
		
		return true;
	}
	
	public int regencap(){
		return target.HT;
	}
}
