package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.figure.AppearanceContainer;
import com.mpdeimos.tensation.figure.ELineStyle;
import com.mpdeimos.tensation.figure.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.ContextPanelContentBase;
import com.mpdeimos.tensation.ui.DividerLabel;
import com.mpdeimos.tensation.ui.RefreshablePanel;
import com.mpdeimos.tensation.util.LayoutUtil;
import com.mpdeimos.tensation.util.ListUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import resources.R;

/**
 * Interface for EditParts that have customizable appearance attributes.
 * 
 * @author mpdeimos
 * 
 */
public interface ICustomizable extends IFeatureEditPart, IAppearanceHolder
{
	/** feature class for EditParts with connection sources. */
	public class Feature extends
			SelectEditPartAction.IContextFeature<ICustomizable>
	{
		/** the context panel for this feature. */
		private static RefreshablePanel contextPanel = null;

		/** Constructor. */
		public Feature(ICustomizable editPart)
		{
			super(editPart);
			createContextPanel();
		}

		@Override
		public RefreshablePanel getStaticContextPanel()
		{
			return contextPanel;
		}

		/** Inits the context panel. */
		private static void createContextPanel()
		{
			if (contextPanel != null)
				return;

			contextPanel = new ContextPanel();
		}

		/** @ignore */
		private static class ContextPanel extends ContextPanelContentBase
		{
			/** width of the customizing controls. */
			private static final int CONTROL_WIDTH = 80;

			/** the currently selected customizables. */
			private List<ICustomizable> customizables = new ArrayList<ICustomizable>();

			/** The common color of all editparts. */
			private Color commonColor = null;

			/** the color preview rect. */
			private final JPanel uiColorPreview;

			/** the line pattern combobox model. */
			private final DefaultComboBoxModel uiLineStyle;

			/** the line width combobox model. */
			private final DefaultComboBoxModel uiLineWidth;

			/** action listener that triggers the update actions. */
			private final ActionListener updateTrigger = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					updateEditParts();
				}
			};

			/** Constructor. */
			private ContextPanel()
			{
				this.add(new DividerLabel(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE.string()));

				this.uiColorPreview = new JPanel();
				LayoutUtil.setSize(this.uiColorPreview, CONTROL_WIDTH - 12, 12);
				JButton colorChooser = new JButton(new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Color color = JColorChooser.showDialog(
								ContextPanel.this,
								R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_COLOR_TITLE.string(),
								ContextPanel.this.commonColor);

						if (color != null)
						{
							ContextPanel.this.commonColor = color;
							updateEditParts();
						}
					}
				});
				colorChooser.add(this.uiColorPreview);
				LayoutUtil.setWidth(colorChooser, CONTROL_WIDTH);
				this.add(label(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_COLOR.string(),
						colorChooser));

				this.uiLineWidth = new DefaultComboBoxModel(
						new Integer[] { null, 1, 2, 3, 4, 5, 6 });
				JComboBox combo = new JComboBox(this.uiLineWidth);
				combo.addActionListener(this.updateTrigger);
				LayoutUtil.setWidth(combo, CONTROL_WIDTH);
				this.add(label(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_WIDTH.string(),
						combo));
				this.uiLineStyle = new DefaultComboBoxModel();
				this.uiLineStyle.addElement(null);
				for (Object o : ELineStyle.values())
					this.uiLineStyle.addElement(o);
				combo = new JComboBox(this.uiLineStyle);
				combo.addActionListener(this.updateTrigger);
				LayoutUtil.setWidth(combo, CONTROL_WIDTH);
				this.add(label(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE.string(),
						combo));
			}

			@Override
			public void refresh()
			{
				this.customizables = ListUtil.filterByClass(
						Application.getApp().getActiveCanvas().getSelectedEditParts(),
						ICustomizable.class);

				// Color
				this.commonColor = readCommonParam(new ParamReader<Color>()
				{
					@Override
					public Color read(AppearanceContainer c)
					{
						return c.getColor();
					}
				});
				this.uiColorPreview.setBackground(this.commonColor);

				// Line Width
				Integer commonLineWidth = readCommonParam(new ParamReader<Integer>()
				{
					@Override
					public Integer read(AppearanceContainer c)
					{
						return c.getLineWidth();
					}
				});
				this.uiLineWidth.setSelectedItem(commonLineWidth);

				// Line Style
				Object commonLineStyle = readCommonParam(new ParamReader<Object>()
				{
					@Override
					public Object read(AppearanceContainer c)
					{
						return c.getLineStyle();
					}
				});
				this.uiLineStyle.setSelectedItem(commonLineStyle);

				// for (int i = 0; i < this.uiLineWidth.getSize(); i++)
				// {
				// Object e = this.uiLineWidth.getElementAt(i);
				// if (e == this.commonLineWidth)
				// this.uiLineWidth.setSelectedItem(e);
				// if (e != null && e.equals(commonLineWidth))
				// {
				// }
				// }
			}

			/** Functional iterator for reading common params. */
			private <T> T readCommonParam(ParamReader<T> r)
			{
				T common = null;
				T current = null;
				for (ICustomizable c : this.customizables)
				{
					current = r.read(c.getAppearanceContainer());
					if (common == null)
					{
						common = current;
					}

					if (common == null || !common.equals(current))
					{
						return null;
					}
				}
				return common;
			}

			/** Updates the EditParts with the current ui data. */
			private void updateEditParts()
			{
				if (this.commonColor != null)
				{
					for (ICustomizable c : this.customizables)
					{
						c.getAppearanceContainer().setColor(this.commonColor);
					}
				}

				Integer commonLineWidth = (Integer) ContextPanel.this.uiLineWidth.getSelectedItem();
				for (ICustomizable c : this.customizables)
				{
					c.getAppearanceContainer().setLineWidth(commonLineWidth);
				}

				ELineStyle commonLineStyle = (ELineStyle) ContextPanel.this.uiLineStyle.getSelectedItem();
				for (ICustomizable c : this.customizables)
				{
					c.getAppearanceContainer().setLineStyle(commonLineStyle);
				}

				Application.getApp().getActiveCanvas().repaint();
				refresh();
			}
		}

		/** @ignore. */
		private static abstract class ParamReader<T>
		{
			/** reads an object and returns it. */
			public abstract T read(AppearanceContainer c);
		}
	}
}
