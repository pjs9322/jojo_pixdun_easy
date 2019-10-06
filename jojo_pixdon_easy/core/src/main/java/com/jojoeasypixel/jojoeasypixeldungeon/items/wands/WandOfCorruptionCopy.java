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

package com.jojoeasypixel.jojoeasypixeldungeon.items.wands;

import com.jojoeasypixel.jojoeasypixeldungeon.Assets;
import com.jojoeasypixel.jojoeasypixeldungeon.Badges;
import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.Statistics;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.Actor;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.Char;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Amok;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Bleeding;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Blindness;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Buff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Burning;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Charm;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Chill;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Corrosion;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Corruption;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.CorruptionCopy;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Cripple;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.DoomCopy;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Drowsy;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.FlavourBuff;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Frost;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.MagicalSleep;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Ooze;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Paralysis;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.PinCushion;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Poison;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Roots;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Slow;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.SoulMark;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Terror;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Vertigo;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.buffs.Weakness;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Bee;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.King;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Mimic;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Mob;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Piranha;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Statue;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Swarm;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Wraith;
import com.jojoeasypixel.jojoeasypixeldungeon.actors.mobs.Yog;
import com.jojoeasypixel.jojoeasypixeldungeon.effects.MagicMissile;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.melee.MagesStaff;
import com.jojoeasypixel.jojoeasypixeldungeon.mechanics.Ballistica;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.CharSprite;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;
import com.jojoeasypixel.jojoeasypixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashMap;

public class WandOfCorruptionCopy extends Wand {

	{
		image = ItemSpriteSheet.WAND_CORRUPTION;
	}
	
	//Note that some debuffs here have a 0% chance to be applied.
	// This is because the wand of corruption considers them to be a certain level of harmful
	// for the purposes of reducing resistance, but does not actually apply them itself
	
	private static final float MINOR_DEBUFF_WEAKEN = 4/5f;
	private static final HashMap<Class<? extends Buff>, Float> MINOR_DEBUFFS = new HashMap<>();
	static{
		MINOR_DEBUFFS.put(Weakness.class,       0f);
		MINOR_DEBUFFS.put(Cripple.class,        0f);
		MINOR_DEBUFFS.put(Blindness.class,      0f);
		MINOR_DEBUFFS.put(Terror.class,         0f);
		
		MINOR_DEBUFFS.put(Chill.class,          0f);
		MINOR_DEBUFFS.put(Ooze.class,           0f);
		MINOR_DEBUFFS.put(Roots.class,          0f);
		MINOR_DEBUFFS.put(Vertigo.class,        0f);
		MINOR_DEBUFFS.put(Drowsy.class,         0f);
		MINOR_DEBUFFS.put(Bleeding.class,       0f);
		MINOR_DEBUFFS.put(Burning.class,        0f);
		MINOR_DEBUFFS.put(Poison.class,         0f);
	}
	
	private static final float MAJOR_DEBUFF_WEAKEN = 2/3f;
	private static final HashMap<Class<? extends Buff>, Float> MAJOR_DEBUFFS = new HashMap<>();
	static{
		MAJOR_DEBUFFS.put(Amok.class,           0f);
		MAJOR_DEBUFFS.put(Slow.class,           0f);
		MAJOR_DEBUFFS.put(Paralysis.class,      0f);
		
		MAJOR_DEBUFFS.put(Charm.class,          0f);
		MAJOR_DEBUFFS.put(MagicalSleep.class,   0f);
		MAJOR_DEBUFFS.put(SoulMark.class,       0f);
		MAJOR_DEBUFFS.put(Corrosion.class,      0f);
		MAJOR_DEBUFFS.put(Frost.class,          0f);
		MAJOR_DEBUFFS.put(DoomCopy.class,       0f);
	}
	
	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);

		if (ch != null){
			
			if (!(ch instanceof Mob)){
				return;
			}

			Mob enemy = (Mob) ch;

			float corruptingPower = 999 + level();
			
			//base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
			float enemyResist = 1 + enemy.EXP;
			if (ch instanceof Mimic || ch instanceof Statue){
				enemyResist = 1 + Dungeon.depth;
			} else if (ch instanceof Piranha || ch instanceof Bee) {
				enemyResist = 1 + Dungeon.depth/2f;
			} else if (ch instanceof Wraith) {
				//this is so low because wraiths are always at max hp
				enemyResist = 0.5f + Dungeon.depth/8f;
			} else if (ch instanceof Yog.BurningFist || ch instanceof Yog.RottingFist) {
				enemyResist = 1 + 30;
			} else if (ch instanceof Yog.Larva || ch instanceof King.Undead){
				enemyResist = 1 + 5;
			} else if (ch instanceof Swarm){
				//child swarms don't give exp, so we force this here.
				enemyResist = 1 + 3;
			}
			
			//100% health: 3x resist   75%: 2.1x resist   50%: 1.5x resist   25%: 1.1x resist
			enemyResist *= 1 + 2*Math.pow(enemy.HP/(float)enemy.HT, 2);
			
			//debuffs placed on the enemy reduce their resistance
			for (Buff buff : enemy.buffs()){
				if (MAJOR_DEBUFFS.containsKey(buff.getClass()))         enemyResist *= MAJOR_DEBUFF_WEAKEN;
				else if (MINOR_DEBUFFS.containsKey(buff.getClass()))    enemyResist *= MINOR_DEBUFF_WEAKEN;
				else if (buff.type == Buff.buffType.NEGATIVE)           enemyResist *= MINOR_DEBUFF_WEAKEN;
			}
			
			//cannot re-corrupt or doom an enemy, so give them a major debuff instead
			if(enemy.buff(CorruptionCopy.class) != null || enemy.buff(DoomCopy.class) != null){
				corruptingPower = enemyResist - 0.001f;
			}
			
			if (corruptingPower > enemyResist){
				corruptEnemy( enemy );
			} else {
				float debuffChance = corruptingPower / enemyResist;
				if (Random.Float() < debuffChance){
					debuffEnemy( enemy, MAJOR_DEBUFFS);
				} else {
					debuffEnemy( enemy, MINOR_DEBUFFS);
				}
			}

			processSoulMark(ch, chargesPerCast());
			
		} else {
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}
	
	private void debuffEnemy( Mob enemy, HashMap<Class<? extends Buff>, Float> category ){
		
		//do not consider buffs which are already assigned, or that the enemy is immune to.
		HashMap<Class<? extends Buff>, Float> debuffs = new HashMap<>(category);
		for (Buff existing : enemy.buffs()){
			if (debuffs.containsKey(existing.getClass())) {
				debuffs.put(existing.getClass(), 0f);
			}
		}
		for (Class<?extends Buff> toAssign : debuffs.keySet()){
			 if (debuffs.get(toAssign) > 0 && enemy.isImmune(toAssign)){
			 	debuffs.put(toAssign, 0f);
			 }
		}
		
		//all buffs with a > 0 chance are flavor buffs
		Class<?extends FlavourBuff> debuffCls = (Class<? extends FlavourBuff>) Random.chances(debuffs);
		
		if (debuffCls != null){
			Buff.append(enemy, debuffCls, 6 + level()*3);
		} else {
			//if no debuff can be applied (all are present), then go up one tier
			if (category == MINOR_DEBUFFS)          debuffEnemy( enemy, MAJOR_DEBUFFS);
			else if (category == MAJOR_DEBUFFS)     corruptEnemy( enemy );
		}
	}
	
	private void corruptEnemy( Mob enemy ) {
		//cannot re-corrupt or doom an enemy, so give them a major debuff instead
		if (enemy.buff(CorruptionCopy.class) != null || enemy.buff(DoomCopy.class) != null) {
			GLog.w(Messages.get(this, "already_corrupted"));
			return;
		}

		if (!enemy.isImmune(Corruption.class)) {
			enemy.HP = enemy.HT;
			for (Buff buff : enemy.buffs()) {
				if (buff.type == Buff.buffType.NEGATIVE
						&& !(buff instanceof SoulMark)) {
					buff.detach();
				} else if (buff instanceof PinCushion) {
					buff.detach();
				}
			}
			if (enemy.alignment == Char.Alignment.ENEMY) {
				enemy.rollToDropLoot();
			}

			Buff.affect(enemy, CorruptionCopy.class);

			Statistics.enemiesSlain++;
			Badges.validateMonstersSlain();
			Statistics.qualifiedForNoKilling = false;
			if (enemy.EXP > 0 && curUser.lvl <= enemy.maxLvl) {
				curUser.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy, "exp", enemy.EXP));
				curUser.earnExp(enemy.EXP, enemy.getClass());
			} else {
				curUser.earnExp(0, enemy.getClass());
			}
		} else {
			Buff.affect(enemy, DoomCopy.class);
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		if (Random.Int( level() + 4 ) >= 3){
			Buff.prolong( defender, Amok.class, 4+level()*2);
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.SHADOW,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0 );
		particle.am = 0.6f;
		particle.setLifespan(2f);
		particle.speed.set(0, 5);
		particle.setSize( 0.5f, 2f);
		particle.shuffleXY(1f);
	}

	@Override
	public void wandUsed() {

		updateQuickslot();

		curUser.spendAndNext( 1f );
	}

	@Override
	public String status() {
		return null;
	}
}
