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

package com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls;

import com.jojoeasypixel.jojoeasypixeldungeon.Challenges;
import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.ShatteredPixelDungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.items.EquipableItem;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Generator;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Item;
import com.jojoeasypixel.jojoeasypixeldungeon.items.artifacts.Artifact;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.AlchemicalCatalyst;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.Potion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.brews.Brew;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.Elixir;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.exotic.ExoticPotion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.rings.Ring;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.jojoeasypixel.jojoeasypixeldungeon.items.stones.Runestone;
import com.jojoeasypixel.jojoeasypixeldungeon.items.wands.Wand;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.Weapon;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.MagesStaff;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.MeleeWeapon;
import com.jojoeasypixel.jojoeasypixeldungeon.journal.Catalog;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.jojoeasypixel.jojoeasypixeldungeon.plants.Plant;
import com.jojoeasypixel.jojoeasypixeldungeon.utils.GLog;
import com.jojoeasypixel.jojoeasypixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

public class ScrollOfTransmutation extends InventoryScroll {
	
	{
		initials = 10;
		mode = WndBag.Mode.TRANMSUTABLE;
		
		bones = true;
	}
	
	public static boolean canTransmute(Item item){
		return item instanceof MeleeWeapon ||
				(item instanceof Potion && !(item instanceof Elixir || item instanceof Brew || item instanceof AlchemicalCatalyst)) ||
				item instanceof Scroll ||
				item instanceof Ring ||
				item instanceof Wand ||
				item instanceof Plant.Seed ||
				item instanceof Runestone ||
				item instanceof Artifact;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		Item result;
		
		if (item instanceof MagesStaff) {
			result = changeStaff( (MagesStaff)item );
		} else if (item instanceof MeleeWeapon) {
			result = changeWeapon( (MeleeWeapon)item );
		} else if (item instanceof Scroll) {
			result = changeScroll( (Scroll)item );
		} else if (item instanceof Potion) {
			result = changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			result = changeRing( (Ring)item );
		} else if (item instanceof Wand) {
			result = changeWand( (Wand)item );
		} else if (item instanceof Plant.Seed) {
			result = changeSeed((Plant.Seed) item);
		} else if (item instanceof Runestone) {
			result = changeStone((Runestone) item);
		} else if (item instanceof Artifact) {
			result = changeArtifact( (Artifact)item );
		} else {
			result = null;
		}
		
		if (result == null){
			//This shouldn't ever trigger
			GLog.n( Messages.get(this, "nothing") );
			curItem.collect( curUser.belongings.backpack );
		} else {
			if (item.isEquipped(Dungeon.hero)){
				item.cursed = false; //to allow it to be unequipped
				((EquipableItem)item).doUnequip(Dungeon.hero, false);
				((EquipableItem)result).doEquip(Dungeon.hero);
			} else {
				item.detach(Dungeon.hero.belongings.backpack);
				if (!result.collect()){
					Dungeon.level.drop(result, curUser.pos).sprite.drop();
				}
			}
			if (result.isIdentified()){
				Catalog.setSeen(result.getClass());
			}
			//TODO visuals
			GLog.p( Messages.get(this, "morph") );
		}
		
	}
	
	private MagesStaff changeStaff( MagesStaff staff ){
		Class<?extends Wand> wandClass = staff.wandClass();
		
		if (wandClass == null){
			return null;
		} else {
			Wand n;
			do {
				n = (Wand) Generator.random(Generator.Category.WAND);
			} while (Challenges.isItemBlocked(n) || n.getClass() == wandClass);
			n.level(0);
			staff.imbueWand(n, null);
		}
		
		return staff;
	}
	
	private Weapon changeWeapon(MeleeWeapon w ) {
		
		Weapon n;
		Generator.Category c = Generator.wepTiers[w.tier-1];
		
		do {
			try {
				n = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
				return null;
			}
		} while (Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
		
		int level = w.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.enchantment = w.enchantment;
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.augment = w.augment;
		
		return n;
		
	}
	
	private Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.random( Generator.Category.RING );
		} while (Challenges.isItemBlocked(n) || n.getClass() == r.getClass());
		
		n.level(0);
		
		int level = r.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;
		
		return n;
	}
	
	private Artifact changeArtifact( Artifact a ) {
		Artifact n = Generator.randomArtifact();
		
		if (n != null && !Challenges.isItemBlocked(n)){
			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
			return n;
		}
		
		return null;
	}
	
	private Wand changeWand( Wand w ) {
		
		Wand n;
		do {
			n = (Wand)Generator.random( Generator.Category.WAND );
		} while ( Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
		
		n.level( 0 );
		n.upgrade( w.level() );
		
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		
		return n;
	}
	
	private Plant.Seed changeSeed( Plant.Seed s ) {
		
		Plant.Seed n;
		
		do {
			n = (Plant.Seed)Generator.random( Generator.Category.SEED );
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private Runestone changeStone( Runestone r ) {
		
		Runestone n;
		
		do {
			n = (Runestone) Generator.random( Generator.Category.STONE );
		} while (n.getClass() == r.getClass());
		
		return n;
	}
	
	private Scroll changeScroll( Scroll s ) {
		try {
			if (s instanceof ExoticScroll) {
				return ExoticScroll.exoToReg.get(s.getClass()).newInstance();
			} else {
				return ExoticScroll.regToExo.get(s.getClass()).newInstance();
			}
		} catch ( Exception e ){
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
	
	private Potion changePotion( Potion p ) {
		try {
			if (p instanceof ExoticPotion) {
				return ExoticPotion.exoToReg.get(p.getClass()).newInstance();
			} else {
				return ExoticPotion.regToExo.get(p.getClass()).newInstance();
			}
		} catch ( Exception e ){
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
	
	@Override
	public void empoweredRead() {
		//does nothing, this shouldn't happen
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
