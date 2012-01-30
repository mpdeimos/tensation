package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.editpart.feature.ICustomizable;
import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.IFeature;
import com.mpdeimos.tensation.feature.contract.ICanvasFeatureContract;
import com.mpdeimos.tensation.figure.AppearanceContainer;
import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.figure.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.model.IModelChangedListener;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.util.Log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
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
public abstract class EditPartBase implements ICustomizable
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
	protected HashMap<Class<?>, List<? super IFeature>> featureMap = new HashMap<Class<?>, List<? super IFeature>>();

	/** The model change listener for this class. */
	private final ModelChangedListener listener;

	/** dummy appearance container, if not supported by the given model. */
	private AppearanceContainer dummyAppearanceContainer;

	/** @return the newly created figure for this EditPart. */
	abstract protected IFigure createFigure();

	/**
	 * Constructor.
	 */
	public EditPartBase(IModelData modelData)
	{
		if (!(modelData instanceof IAppearanceHolder))
		{
			this.dummyAppearanceContainer = new AppearanceContainer()
			{
				@Override
				public void applyAppearance(Graphics2D gfx)
				{
					// do nothing
				}
			};
		}

		this.model = modelData;
		this.figure = createFigure();

		this.listener = new ModelChangedListener();
		this.model.addModelChangedListener(this.listener);

		Class<?> currentClass = this.getClass();
		do
		{
			for (Class<?> editPartIfc : currentClass.getInterfaces())
			{
				if (!IFeatureEditPart.class.isAssignableFrom(editPartIfc))
					continue;

				for (Class<?> feature : editPartIfc.getClasses())
				{
					if (!IFeature.class.isAssignableFrom(feature))
						continue;

					try
					{
						@SuppressWarnings("unchecked")
						// is checked
						Constructor<? extends IFeature> constructor = (Constructor<? extends IFeature>) feature.getConstructor(editPartIfc);
						IFeature featureInstance = constructor.newInstance(this);

						List<? super IFeature> features = this.featureMap.get(featureInstance.getFeatureContract());
						if (features == null)
						{
							features = new ArrayList<IFeature>();
							this.featureMap.put(
										featureInstance.getFeatureContract(),
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
		while (currentClass != null);
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
		Stroke oldStroke = gfx.getStroke();

		getAppearanceContainer().applyAppearance(gfx);

		if (this.highlighted)
			gfx.setColor(Color.MAGENTA);
		if (this.selected)
			gfx.setColor(Color.BLUE);

		getFigure().draw(gfx);

		gfx.setColor(oldPaint);
		gfx.setStroke(oldStroke);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IFeature> List<T> getFeatures(Class<T> contract)
	{
		return (List<T>) this.featureMap.get(contract);
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;

		List<? extends ICanvasFeatureContract> features = getFeatures(SelectEditPartAction.IFeature.class);
		if (features == null)
			return;

		for (ICanvasFeatureContract feature : features)
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

	@Override
	public AppearanceContainer getAppearanceContainer()
	{
		return this.dummyAppearanceContainer != null ? this.dummyAppearanceContainer
				: ((IAppearanceHolder) this.model).getAppearanceContainer();
	}
}
