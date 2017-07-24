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
package de.fraunhofer.iosb.ilt.configurable.editor;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hylke van der Schaaf
 */
public final class EditorInt extends EditorDefault<Object, Object, Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EditorInt.class);
	private final int min;
	private final int max;
	private final int step;
	private final int deflt;
	private int value;
	/**
	 * Flag indicating we are in JavaFX mode.
	 */
	private Boolean fx;
	// Swing components
	private SpinnerNumberModel swModel;
	private JSpinner swComponent;
	// FX Nodes
	private Spinner<Integer> fxNode;

	public EditorInt(int min, int max, int step, int deflt, String label, String description) {
		this.deflt = deflt;
		this.value = deflt;
		this.min = min;
		this.max = max;
		this.step = step;
		setLabel(label);
		setDescription(description);
	}

	@Override
	public void setConfig(JsonElement config, Object context, Object edtCtx) {
		if (config != null && config.isJsonPrimitive() && config.getAsJsonPrimitive().isNumber()) {
			value = config.getAsInt();
		} else {
			value = deflt;
		}
		fillComponent();
	}

	@Override
	public JsonElement getConfig() {
		return new JsonPrimitive(getValue());
	}

	private void setFx(boolean fxMode) {
		if (fx != null && fx != fxMode) {
			throw new IllegalStateException("Can not switch between swing and FX mode.");
		}
		fx = fxMode;
	}

	@Override
	public JComponent getComponent() {
		setFx(false);
		if (swComponent == null) {
			createComponent();
		}
		return swComponent;
	}

	@Override
	public Node getNode() {
		setFx(true);
		if (fxNode == null) {
			createComponent();
		}
		return fxNode;
	}

	private void createComponent() {
		if (value < min || value > max) {
			LOGGER.error("min < value < max is false: {} < {} < {}.", min, value, max);
			value = Math.max(min, Math.min(value, max));
		}

		if (fx) {
			SpinnerValueFactory.IntegerSpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value, step);
			fxNode = new Spinner<>(factory);
			fxNode.setEditable(true);
			// hook in a formatter with the same properties as the factory
			TextFormatter formatter = new TextFormatter(factory.getConverter(), factory.getValue());
			fxNode.getEditor().setTextFormatter(formatter);
			// bidi-bind the values
			factory.valueProperty().bindBidirectional(formatter.valueProperty());
		} else {
			swModel = new SpinnerNumberModel(value, min, max, step);
			swComponent = new JSpinner(swModel);
		}
		fillComponent();
	}

	/**
	 * Ensure the component represents the current value.
	 */
	private void fillComponent() {
		if (fx == null) {
			return;
		}
		if (fx) {
			fxNode.getValueFactory().setValue(value);
		} else {
			swComponent.setValue(value);
		}
	}

	@Override
	public Integer getValue() {
		if (swComponent != null) {
			value = swModel.getNumber().intValue();
		}
		if (fxNode != null) {
			value = fxNode.getValue();
		}
		return value;
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
		fillComponent();
	}

}
