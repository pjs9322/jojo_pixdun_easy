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

package com.jojoeasypixel.jojoeasypixeldungeon.items.spells;

import com.jojoeasypixel.jojoeasypixeldungeon.Assets;
import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.ArtifactRecharge;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Recharging;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.hero.Hero;
import com.jojoeasypixel.jojoeasypixeldungeon.effects.Lightning;
import com.jojoeasypixel.jojoeasypixeldungeon.items.quest.MetalShard;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfRecharging;
import com.jojoeasypixel.jojoeasypixeldungeon.levels.traps.Trap;
import com.jojoeasypixel.jojoeasypixeldungeon.mechanics.Ballistica;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;
import com.jojoeasypixel.jojoeasypixeldungeon.tiles.DungeonTilemap;
import com.jojoeasypixel.jojoeasypixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ReclaimTrap extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.RECLAIM_TRAP;
	}
	
	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		Trap t = Dungeon.level.traps.get(bolt.collisionPos);
		if (t != null && t.active){
			if (!t.visible) t.reveal();
			t.disarm();
			
			Sample.INSTANCE.play( Assets.SND_LIGHTNING );
			hero.sprite.parent.addToFront( new Lightning(DungeonTilemap.tileCenterToWorld(t.pos), hero.sprite.center(), null) );
			
			ScrollOfRecharging.charge(hero);
			Buff.prolong(hero, Recharging.class, 15f);
			Buff.affect(hero, ArtifactRecharge.class).set( 15 );
			
		} else {
			GLog.w(Messages.get(this, "no_trap"));
		}
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 100) / 3f));
	}
	
	public static class Recipe extends com.jojoeasypixel.jojoeasypixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfRecharging.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 8;
			
			output = ReclaimTrap.class;
			outQuantity = 3;
		}
		
	}
	
}
