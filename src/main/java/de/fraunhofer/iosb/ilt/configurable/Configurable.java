/*
 * Copyright (C) 2017 Fraunhofer IOSB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.configurable;

import com.google.gson.JsonElement;

/**
 *
 * @author hylke
 * @param <C> The class type that provides context at runtime.
 * @param <D> The class type that provides context while editing.
 */
public interface Configurable<C, D> {

	/**
	 * Configure the instance using the given configuration.
	 *
	 * @param config The configuration to use for this instance.
	 * @param context the object that defines the context at runtime.
	 * @param edtCtx the object that defines the context while editing.
	 */
	public void configure(JsonElement config, C context, D edtCtx);

	/**
	 * Returns an editor for this class. Changing the configuration of this
	 * editor <em>may</em> change the configuration of the instance that
	 * generated the editor, but it is not guaranteed to do so.
	 *
	 * @param context the object that defines the context at runtime.
	 * @param edtCtx the object that defines the context while editing.
	 * @return A generic editor for any instance of this class.
	 */
	public ConfigEditor<?> getConfigEditor(C context, D edtCtx);
}
