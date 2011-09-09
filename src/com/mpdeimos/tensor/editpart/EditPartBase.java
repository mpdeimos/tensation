package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.editpart.feature.FeatureBase;
import com.mpdeimos.tensor.editpart.feature.IFeature;
import com.mpdeimos.tensor.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.model.IModelChangedListener;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.util.Log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * abstract base class for EditParts.
 * 
 * @author mpdeimos
 * 
 */
public abstract class EditPartBase implements IFeatureEditPart
{

	/** Flag whether the mouse is hovered over this EditPart. */
	private boolean highlighted;

	/** Flag whether the EditPart is selected. */
	private boolean selected;

	/** the data model object linked to this EditPart */
	private final IModelData model;

	/** the figure for drawing this object */
	private final IFigure figure;

	/** The list of Features linked to this EditPart. Default is null. */
	protected HashMap<Class<? extends ICanvasAction>, List<IFeature>> featureMap = new HashMap<Class<? extends ICanvasAction>, List<IFeature>>();

	/** The model change listener for this class. */
	private final ModelChangedListener listener;

	/** @return the newly created figure for this EditPart. */
	abstract protected IFigure createFigure();

	/**
	 * Constructor.
	 */
	public EditPartBase(IModelData modelData)
	{
		this.model = modelData;
		this.figure = createFigure();

		this.listener = new ModelChangedListener();
		this.model.addModelChangedListener(this.listener);

		Class<?> currentClass = this.getClass();
		while (!currentClass.equals(EditPartBase.class))
		{
			for (Class<?> editPartIfc : currentClass.getInterfaces())
			{
				if (!IFeatureEditPart.class.isAssignableFrom(editPartIfc))
					continue;

				for (Class<?> feature : editPartIfc.getClasses())
				{
					if (!FeatureBase.class.isAssignableFrom(feature))
						continue;

					try
					{
						@SuppressWarnings("unchecked")
						// is checked
						Constructor<? extends IFeature> constructor = (Constructor<? extends IFeature>) feature.getConstructor(editPartIfc);
						IFeature featureInstance = constructor.newInstance(this);

						List<IFeature> features = this.featureMap.get(featureInstance.getActionGroup());
						if (features == null)
						{
							features = new ArrayList<IFeature>();
							this.featureMap.put(
										featureInstance.getActionGroup(),
										features);
						}
						features.add(featureInstance);
					}
					catch (InvocationTargetException e)
					{
						if (e.getCause() instanceof RuntimeException)
							throw (RuntimeException) e.getCause();
						Log.e(this, e);
					}
					catch (Exception e)
					{
						Log.e(this, e);
					}
				}
			}

			currentClass = currentClass.getSuperclass();
		}
	}

	@Override
	protected void finalize() throws Throwable
	{
		this.model.removeModelDataChangedListener(this.listener);
	}

	@Override
	public IModelData getModel()
	{
		return this.model;
	}

	@Override
	public void draw(Graphics2D gfx)
	{
		Color oldPaint = gfx.getColor();
		if (this.highlighted)
			gfx.setColor(Color.MAGENTA);
		if (this.selected)
			gfx.setColor(Color.BLUE);

		getFigure().draw(gfx);

		if (this.highlighted || this.selected)
			gfx.setColor(oldPaint);
	}

	/** @return the figure responsible for drawing this object */
	@Override
	public IFigure getFigure()
	{
		return this.figure;
	}

	@Override
	public boolean intersects(Rectangle rect)
	{
		return this.getFigure().intersects(rect);
	}

	@Override
	public Rectangle getBoundingRectangle()
	{
		return this.getFigure().getBoundingRectangle();
	}

	/** private model data change listener */
	private class ModelChangedListener implements IModelChangedListener
	{

		@Override
		public void onModelChanged(IModelData model)
		{
			EditPartBase.this.figure.redraw();
		}

		@Override
		public void onChildAdded(IModelData child)
		{
			EditPartBase.this.figure.redraw();
		}

		@Override
		public void onChildRemoved(IModelData child)
		{
			EditPartBase.this.figure.redraw();
		}

	}

	@Override
	public List<IFeature> getFeatures(
			Class<? extends ICanvasAction> group)
	{
		return this.featureMap.get(group);
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;

		List<IFeature> features = getFeatures(SelectEditPartAction.class);
		if (features == null)
			return;

		for (IFeature feature : features)
		{
			feature.doOnEditPartSelected(selected);
		}
	}

	@Override
	public void setHighlighted(boolean highlighted)
	{
		this.highlighted = highlighted;
	}

	@Override
	public boolean isSelected()
	{
		return this.selected;
	}

	@Override
	public boolean isHighlighted()
	{
		return this.highlighted;
	}
}
