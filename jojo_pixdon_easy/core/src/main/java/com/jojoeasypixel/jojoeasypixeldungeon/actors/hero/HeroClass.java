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

package com.jojoeasypixel.jojoeasypixeldungeon.actors.hero;

import com.jojoeasypixel.jojoeasypixeldungeon.Assets;
import com.jojoeasypixel.jojoeasypixeldungeon.Badges;
import com.jojoeasypixel.jojoeasypixeldungeon.Challenges;
import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.items.BrokenSeal;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Item;
import com.jojoeasypixel.jojoeasypixeldungeon.items.armor.ClothArmor;
import com.jojoeasypixel.jojoeasypixeldungeon.items.artifacts.CloakOfShadowsCopy;
import com.jojoeasypixel.jojoeasypixeldungeon.items.artifacts.DriedRoseCopy;
import com.jojoeasypixel.jojoeasypixeldungeon.items.bags.PotionBandolier;
import com.jojoeasypixel.jojoeasypixeldungeon.items.bags.ScrollHolder;
import com.jojoeasypixel.jojoeasypixeldungeon.items.bags.VelvetPouch;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.Food;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.SmallRation;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.PotionOfHealing;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.PotionOfInvisibility;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.PotionOfLiquidFlame;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.PotionOfMindVision;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfIdentify;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfLullaby;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfRage;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.jojoeasypixel.jojoeasypixeldungeon.items.wands.WandOfCorruptionCopy;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.SpiritBow;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.Dagger;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.Gloves;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.MagesStaff;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.WornShortsword;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.missiles.ThrowingStone;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.SNIPER, HeroSubClass.WARDEN );

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}
		
		new ScrollOfIdentify().identify();

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		// 직업이 전사일 경우 사거리 증가를 적용
		hero.reach = true;
		// 피의 반지를 시작 아이템에 추가
		// ChaliceOfBloodCopy bloodRing = new ChaliceOfBloodCopy();
		// bloodRing.identify().collect();
		
		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		
		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfCorruptionCopy());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		// 지배의 막대를 시작 아이템에 추가
		// WandOfCorruptionCopy wand = new WandOfCorruptionCopy();
		// wand.identify().collect();

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadowsCopy cloak = new CloakOfShadowsCopy();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		// 보이지 않는 무언가를 시작 아이템에 추가
		// CloakOfShadowsCopy enigma = new CloakOfShadowsCopy();
		// enigma.identify().collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();

		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		// 댕댕막대기를 시작 아이템에 추가
		DriedRoseCopy rose = new DriedRoseCopy();
		(hero.belongings.misc1 = rose).identify();
		hero.belongings.misc1.activate( hero );

		Dungeon.quickslot.setSlot(0, bow);
		Dungeon.quickslot.setSlot(1, rose);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		return subClasses;
	}
	
	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.WARRIOR;
			case MAGE:
				return Assets.MAGE;
			case ROGUE:
				return Assets.ROGUE;
			case HUNTRESS:
				return Assets.HUNTRESS;
		}
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
		}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
		}
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
