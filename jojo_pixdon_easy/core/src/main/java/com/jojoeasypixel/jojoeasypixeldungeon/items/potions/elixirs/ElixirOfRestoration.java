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

package com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs;

import com.jojoeasypixel.jojoeasypixeldungeon.Assets;
import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.Actor;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Healing;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.hero.Hero;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.PotionOfHealing;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ElixirOfRestoration extends Elixir {
	
	{
		image = ItemSpriteSheet.ELIXIR_RESTO;
	}
	
	@Override
	public void apply(Hero hero) {
		Buff.affect( hero, Healing.class ).setHeal((int)(0.8f*hero.HT + 14), 0.25f, 0);
		PotionOfCleansing.cleanse(hero);
	}
	
	@Override
	public void shatter(int cell) {
		if (Actor.findChar(cell) == null){
			super.shatter(cell);
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Sample.INSTANCE.play(Assets.SND_SHATTER);
				splash(cell);
			}
			
			if (Actor.findChar(cell) != null){
				PotionOfCleansing.cleanse(Actor.findChar(cell));
			}
		}
	}
	
	@Override
	public int price() {
		//prices of ingredients
		return quantity * (30 + 60);
	}
	
	public static class Recipe extends com.jojoeasypixel.jojoeasypixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfHealing.class, PotionOfCleansing.class};
			inQuantity = new int[]{1, 1};
			
			cost = 1;
			
			output = ElixirOfRestoration.class;
			outQuantity = 1;
		}
		
	}
}
