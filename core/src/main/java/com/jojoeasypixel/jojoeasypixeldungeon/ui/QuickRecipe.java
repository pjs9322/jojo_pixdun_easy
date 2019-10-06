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

package com.jojoeasypixel.jojoeasypixeldungeon.ui;

import com.jojoeasypixel.jojoeasypixeldungeon.Dungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.ShatteredPixelDungeon;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Generator;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Item;
import com.jojoeasypixel.jojoeasypixeldungeon.items.Recipe;
import com.jojoeasypixel.jojoeasypixeldungeon.items.bombs.Bomb;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.Blandfruit;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.Food;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.MeatPie;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.MysteryMeat;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.Pasty;
import com.jojoeasypixel.jojoeasypixeldungeon.items.food.StewedMeat;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.AlchemicalCatalyst;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.Potion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.brews.BlizzardBrew;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.brews.CausticBrew;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.brews.InfernalBrew;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.brews.ShockingBrew;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.jojoeasypixel.jojoeasypixeldungeon.items.potions.exotic.ExoticPotion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.Scroll;
import com.jojoeasypixel.jojoeasypixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.Alchemize;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.AquaBlast;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.ArcaneCatalyst;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.BeaconOfReturning;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.CurseInfusion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.FeatherFall;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.MagicalInfusion;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.MagicalPorter;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.PhaseShift;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.ReclaimTrap;
import com.jojoeasypixel.jojoeasypixeldungeon.items.spells.Recycle;
import com.jojoeasypixel.jojoeasypixeldungeon.items.stones.Runestone;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.missiles.darts.Dart;
import com.jojoeasypixel.jojoeasypixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.jojoeasypixel.jojoeasypixeldungeon.messages.Messages;
import com.jojoeasypixel.jojoeasypixeldungeon.plants.Plant;
import com.jojoeasypixel.jojoeasypixeldungeon.scenes.AlchemyScene;
import com.jojoeasypixel.jojoeasypixeldungeon.scenes.PixelScene;
import com.jojoeasypixel.jojoeasypixeldungeon.sprites.ItemSpriteSheet;
import com.jojoeasypixel.jojoeasypixeldungeon.windows.WndBag;
import com.jojoeasypixel.jojoeasypixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickRecipe extends Component {
	
	private ArrayList<Item> ingredients;
	
	private ArrayList<ItemSlot> inputs;
	private QuickRecipe.arrow arrow;
	private ItemSlot output;
	
	public QuickRecipe(Recipe.SimpleRecipe r){
		this(r, r.getIngredients(), r.sampleOutput(null));
	}
	
	public QuickRecipe(Recipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		for (final Item in : inputs) {
			anonymize(in);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(in));
				}
			};
			
			ArrayList<Item> similar = Dungeon.hero.belongings.getAllSimilar(in);
			int quantity = 0;
			for (Item sim : similar) {
				//if we are looking for a specific item, it must be IDed
				if (sim.getClass() != in.getClass() || sim.isIdentified()) quantity += sim.quantity();
			}
			
			if (quantity < in.quantity()) {
				curr.icon.alpha(0.3f);
				hasInputs = false;
			}
			curr.showParams(true, false, true);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0) {
			arrow = new arrow(Icons.get(Icons.RESUME), cost);
			arrow.hardlightText(0x00CCFF);
		} else {
			arrow = new arrow(Icons.get(Icons.RESUME));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(ShatteredPixelDungeon.scene() instanceof AlchemyScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (!hasInputs){
			this.output.icon.alpha(0.3f);
		}
		this.output.showParams(true, false, true);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;
		
		for (ItemSlot item : inputs){
			item.setRect(x + width, y, 16, 16);
			width += 16;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class arrow extends IconButton {
		
		BitmapText text;
		
		public arrow(){
			super();
		}
		
		public arrow( Image icon ){
			super( icon );
		}
		
		public arrow( Image icon, int count ){
			super( icon );
			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((AlchemyScene)ShatteredPixelDungeon.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickRecipe> getRecipes( int pageIdx ){
		ArrayList<QuickRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				result.add(new QuickRecipe( new Potion.SeedToPotion(), new ArrayList<>(Arrays.asList(new Plant.Seed.PlaceHolder().quantity(3))), new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
					{
						name = Messages.get(Potion.SeedToPotion.class, "name");
					}
					
					@Override
					public String info() {
						return "";
					}
				}));
				return result;
			case 1:
				Recipe r = new Scroll.ScrollToStone();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					try{
						Scroll scroll = (Scroll) cls.newInstance();
						if (!scroll.isKnown()) scroll.anonymize();
						ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(scroll));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 2:
				r = new TippedDart.TipDart();
				for (Class<?> cls : Generator.Category.SEED.classes){
					try{
						Plant.Seed seed = (Plant.Seed) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(seed, new Dart()));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 3:
				r = new ExoticPotion.PotionToExotic();
				for (Class<?> cls : Generator.Category.POTION.classes){
					try{
						Potion pot = (Potion) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(pot, new Plant.Seed.PlaceHolder().quantity(2)));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 4:
				r = new ExoticScroll.ScrollToExotic();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					try{
						Scroll scroll = (Scroll) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(scroll, new Runestone.PlaceHolder().quantity(2)));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 5:
				result.add(new QuickRecipe( new StewedMeat.oneMeat() ));
				result.add(new QuickRecipe( new StewedMeat.twoMeat() ));
				result.add(new QuickRecipe( new StewedMeat.threeMeat() ));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new MeatPie.Recipe(),
						new ArrayList<Item>(Arrays.asList(new Pasty(), new Food(), new MysteryMeat.PlaceHolder())),
						new MeatPie()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new Blandfruit.CookFruit(),
						new ArrayList<>(Arrays.asList(new Blandfruit(), new Plant.Seed.PlaceHolder())),
						new Blandfruit(){
							{
								name = Messages.get(Blandfruit.class, "cooked");
							}
							
							@Override
							public String info() {
								return "";
							}
						}));
				return result;
			case 6:
				r = new Bomb.EnhanceBomb();
				int i = 0;
				for (Class<?> cls : Bomb.EnhanceBomb.validIngredients.keySet()){
					try{
						if (i == 2){
							result.add(null);
							i = 0;
						}
						Item item = (Item) cls.newInstance();
						ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(new Bomb(), item));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
						i++;
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 7:
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Plant.Seed.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Runestone.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Runestone.PlaceHolder())), new ArcaneCatalyst()));
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Plant.Seed.PlaceHolder())), new ArcaneCatalyst()));
				return result;
			case 8:
				result.add(new QuickRecipe(new InfernalBrew.Recipe()));
				result.add(new QuickRecipe(new BlizzardBrew.Recipe()));
				result.add(new QuickRecipe(new ShockingBrew.Recipe()));
				result.add(new QuickRecipe(new CausticBrew.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ElixirOfHoneyedHealing.Recipe()));
				result.add(new QuickRecipe(new ElixirOfAquaticRejuvenation.Recipe()));
				result.add(new QuickRecipe(new ElixirOfMight.Recipe()));
				result.add(new QuickRecipe(new ElixirOfDragonsBlood.Recipe()));
				result.add(new QuickRecipe(new ElixirOfIcyTouch.Recipe()));
				result.add(new QuickRecipe(new ElixirOfToxicEssence.Recipe()));
				return result;
			case 9:
				result.add(new QuickRecipe(new MagicalPorter.Recipe()));
				result.add(new QuickRecipe(new PhaseShift.Recipe()));
				result.add(new QuickRecipe(new BeaconOfReturning.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new AquaBlast.Recipe()));
				result.add(new QuickRecipe(new FeatherFall.Recipe()));
				result.add(new QuickRecipe(new ReclaimTrap.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new CurseInfusion.Recipe()));
				result.add(new QuickRecipe(new MagicalInfusion.Recipe()));
				result.add(new QuickRecipe(new Alchemize.Recipe()));
				result.add(new QuickRecipe(new Recycle.Recipe()));
				return result;
		}
	}
	
}
