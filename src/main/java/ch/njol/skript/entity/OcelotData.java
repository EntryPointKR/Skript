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

package ch.njol.skript.entity;

import org.bukkit.entity.Ocelot;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.StringUtils;

/**
 * @author Peter Güttinger
 */
public class OcelotData extends EntityData<Ocelot> {
	private static final long serialVersionUID = -7803473515795672650L;
	
	static {
		EntityData.register(OcelotData.class, "ocelot", Ocelot.class,
				"(wild|untamed|unowned) ocelot[s]", "ocelot[s]", "([(tamed|owned)] cat|(tamed|owned) ocelot)[s]");
	}
	
	int tamed = 0;
	
	private boolean plural;
	
	@Override
	protected boolean init(final Literal<?>[] exprs, final int matchedPattern, final ParseResult parseResult) {
		tamed = matchedPattern - 1;
		plural = StringUtils.endsWithIgnoreCase(parseResult.expr, "s");
		return true;
	}
	
	@Override
	public void set(final Ocelot entity) {
		if (tamed != 0)
			entity.setTamed(tamed == 1);
	}
	
	@Override
	protected boolean match(final Ocelot entity) {
		return tamed == 0 || entity.isTamed() == (tamed == 1);
	}
	
	@Override
	public Class<? extends Ocelot> getType() {
		return Ocelot.class;
	}
	
	@Override
	public String toString() {
		return tamed == -1 ? "wild ocelot" : tamed == 1 ? "cat" : "ocelot";
	}
	
	@Override
	public boolean isPlural() {
		return plural;
	}
	
	@Override
	public int hashCode() {
		return tamed;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OcelotData))
			return false;
		final OcelotData other = (OcelotData) obj;
		return tamed == other.tamed;
	}
	
	@Override
	public String serialize() {
		return "" + tamed;
	}
	
	@Override
	protected boolean deserialize(final String s) {
		try {
			tamed = Integer.parseInt(s);
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}
	
}