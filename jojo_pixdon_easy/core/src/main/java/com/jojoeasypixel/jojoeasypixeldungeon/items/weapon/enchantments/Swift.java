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

package com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.enchantments;

import com.jojoeasypixel.jojoeasypixeldungeon.actors.Char;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.FlavourBuff;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.Weapon;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.MeleeWeapon;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSprite;
import com.jojoeasypixel.jojoeasypixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Swift extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF00 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 13%
		// lvl 1 - 22%
		// lvl 2 - 30%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 8 ) >= 7) {
			Buff.prolong(attacker, SwiftAttack.class, 5).setSourceType(weapon instanceof MeleeWeapon);
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return YELLOW;
	}
	
	public static class SwiftAttack extends FlavourBuff {
		
		boolean sourceWasMelee;
		
		public void setSourceType( boolean melee ){
			this.sourceWasMelee = melee;
		}
		
		public boolean boostsMelee(){
			return !sourceWasMelee;
		}
		
		public boolean boostsRanged(){
			return sourceWasMelee;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.WEAPON;
		}
		
		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1, 1, 0);
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
		@Override
		public String desc() {
			return Messages.get(this, sourceWasMelee ? "desc_melee" : "desc_ranged", dispTurns());
		}
		
		private static final String WAS_MELEE = "was_melee";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WAS_MELEE, sourceWasMelee);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			sourceWasMelee = bundle.getBoolean(WAS_MELEE);
		}
	}
}
