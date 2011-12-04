package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensor.action.canvas.DrawTensorAction;
import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.util.PointUtil;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

/**
 * Feature for duplicatable editparts.
 * 
 * @author mpdeimos
 * 
 */
public interface IDuplicatable extends IFeatureEditPart
{
	/** @return a duplicate of the editparts model. */
	public IModelData duplicateModel();

	/** feature class for movable EditParts */
	public class Feature extends
			FeatureBase<IDuplicatable, SelectEditPartAction>
	{
		/** offset multiplicator. */
		private short multiplicator = 0;

		/** Constructor. */
		public Feature(IDuplicatable editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnKeyPressed(ICanvasAction action, KeyEvent e)
		{
			if (!e.isControlDown())
				return false;

			if ((KeyEvent.VK_V == e.getKeyCode() && ((SelectEditPartAction) action).getCopiedEditParts().contains(
					this.editPart))
					|| (KeyEvent.VK_D == e.getKeyCode() && this.editPart.isSelected()))
			{
				IModelData md = this.editPart.duplicateModel();
				// TODO make positionable base ifc
				if (md instanceof TensorBase)
				{
					this.multiplicator++;
					TensorBase tensor = (TensorBase) md;
					Point2D p = PointUtil.move(
							tensor.getPosition(),
							this.multiplicator * 10,
							this.multiplicator * 10);
					tensor.setPosition(new Point((int) p.getX(), (int) p.getY()));
				}
				DrawTensorAction.drawTensor(md);
				return false;
			}

			return false;
		}
	}
}
