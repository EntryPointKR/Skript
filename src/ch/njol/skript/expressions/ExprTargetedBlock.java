/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011, 2012 Peter Güttinger
 * 
 */

package ch.njol.skript.expressions;

import java.util.WeakHashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.Skript.ExpressionType;
import ch.njol.skript.api.Converter;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;

/**
 * @author Peter Güttinger
 * 
 */
public class ExprTargetedBlock extends PropertyExpression<Block> {
	
	static {
		Skript.registerExpression(ExprTargetedBlock.class, Block.class, ExpressionType.NORMAL, "[the] target[ed] block[s] [of %players%]", "%players%'[s] target[ed] block[s]");
	}
	
	private Expression<Player> players;
	
	private static Event last = null;
	private final static WeakHashMap<Player, Block> targetedBlocks = new WeakHashMap<Player, Block>();
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final ParseResult parser) {
		players = (Expression<Player>) vars[0];
		setExpr(players);
		return true;
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		if (e == null)
			return "the targeted block" + (players.isSingle() ? "" : "s") + " of " + players.toString(e, debug);
		return Skript.getDebugMessage(getAll(e));
	}
	
	private Block getTargetedBlock(final Player p, final Event e) {
		if (p == null)
			return null;
		if (last != e)
			targetedBlocks.clear();
		last = e;
		if (getTime() <= 0 && targetedBlocks.containsKey(p))
			return targetedBlocks.get(p);
//		if (e instanceof PlayerInteractEvent && p == ((PlayerInteractEvent) e).getPlayer() && (((PlayerInteractEvent) e).getAction() == Action.LEFT_CLICK_BLOCK || ((PlayerInteractEvent) e).getAction() == Action.RIGHT_CLICK_BLOCK)) {
//			targetedBlocks.put(((PlayerInteractEvent) e).getPlayer(), ((PlayerInteractEvent) e).getClickedBlock());
//			return ((PlayerInteractEvent) e).getClickedBlock();
//		}
		Block b = p.getTargetBlock(null, Skript.TARGETBLOCKMAXDISTANCE);
		if (b.getTypeId() == 0)
			b = null;
		targetedBlocks.put(p, b);
		return b;
	}
	
	@Override
	protected Block[] get(final Event e) {
		return players.getArray(e, Block.class, new Converter<Player, Block>() {
			@Override
			public Block convert(final Player p) {
				return getTargetedBlock(p, e);
			}
		});
	}
	
	@Override
	public Class<? extends Block> getReturnType() {
		return Block.class;
	}
	
	@Override
	public boolean isDefault() {
		return false;
	}
	
	@Override
	public boolean setTime(final int time) {
		super.setTime(time);
		return true;
	}
	
}