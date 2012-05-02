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

package ch.njol.skript.effects;

import java.util.regex.Matcher;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.api.Effect;
import ch.njol.skript.api.intern.Variable;
import ch.njol.skript.util.VariableString;

/**
 * 
 * @author Peter Güttinger
 * 
 */
public class EffMessage extends Effect {
	
	static {
		Skript.addEffect(EffMessage.class,
				"((send )?message|send) %variablestring%( to %commandsender%)?",
				"((send )?message|send) (to )?%commandsender% %variablestring%");
	}
	
	private Variable<VariableString> messages;
	private Variable<CommandSender> recipients;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(final Variable<?>[] vars, final int matchedPattern, final Matcher matcher) {
		if (matchedPattern == 0) {
			messages = (Variable<VariableString>) vars[0];
			recipients = (Variable<CommandSender>) vars[1];
		} else {
			recipients = (Variable<CommandSender>) vars[0];
			messages = (Variable<VariableString>) vars[1];
		}
	}
	
	@Override
	protected void execute(final Event e) {
		for (final VariableString messageVS : messages.get(e, false)) {
			final String message = messageVS.get(e);
			if (message == null)
				continue;
			for (final CommandSender s : recipients.get(e, false)) {
				s.sendMessage(message);
			}
		}
	}
	
	@Override
	public String getDebugMessage(final Event e) {
		return "send " + messages.getDebugMessage(e) + " to " + recipients.getDebugMessage(e);
	}
}