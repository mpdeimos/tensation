package com.mpdeimos.tensor.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * Class representing Toolbar Buttons.
 * 
 * @author mpdeimos
 * 
 */
public class ToolBarButton extends JButton
{
	/** The linked Toolbar */
	private final JToolBar toolBar;

	/** Constructor. */
	public ToolBarButton(JToolBar toolBar, AbstractAction action)
	{
		super(action);
		this.toolBar = toolBar;
		this.setHideActionText(true);
		action.addPropertyChangeListener(new ToolBarButton.PropertyChangeListener());
	}

	/** Listener class for action property changes. */
	private class PropertyChangeListener implements
			java.beans.PropertyChangeListener
	{

		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			if (Action.SELECTED_KEY.equals(evt.getPropertyName()))
			{
				if (!(evt.getNewValue() instanceof Boolean))
					return;

				Boolean newValue = (Boolean) evt.getNewValue();
				if (newValue)
				{
					for (Component comp : ToolBarButton.this.toolBar.getComponents())
					{
						if (comp instanceof ToolBarButton)
						{
							((ToolBarButton) comp).setSelected(false);
						}
					}
				}

				ToolBarButton.this.setSelected(newValue);
			}
		}
	}
}
