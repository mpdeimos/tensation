package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.editpart.feature.IDuplicatable;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.DrawingCanvas;
import com.mpdeimos.tensation.util.CompoundInfiniteUndoableEdit;
import com.mpdeimos.tensation.util.InfiniteUndoableEdit;
import com.mpdeimos.tensation.util.PointUtil;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import resources.R;

/**
 * Implements a canvas transient clipboard.
 * 
 * @author mpdeimos
 * 
 */
public class Clipboard
{
	/** the singleton instance. */
	private static Clipboard instance = null;

	/** The currently copied EditParts. */
	private final ArrayList<IDuplicatable> clipList;

	/** private constructor. */
	private Clipboard()
	{
		this.clipList = new ArrayList<IDuplicatable>();
	}

	/**
	 * @return the singleton instance of the clipboard.
	 */
	public static Clipboard get()
	{
		if (instance == null)
			instance = new Clipboard();

		return instance;
	}

	/** adds a list of elements to the clipboard. */
	public void clip(List<IDuplicatable> elements)
	{
		Set<IDuplicatable> set = new HashSet<IDuplicatable>(elements);
		for (IDuplicatable dup : elements)
		{
			Collection<? extends IDuplicatable> linkedEditParts = dup.getLinkedEditParts();
			if (linkedEditParts != null)
			{
				set.addAll(linkedEditParts);
			}
		}
		this.clipList.clear();
		this.clipList.addAll(set);
	}

	/**
	 * Copies the clipboard data to the active canvas.
	 * 
	 * @return the new editparts as map (old -> new)
	 */
	public Map<IDuplicatable, IModelData> copyToCanvas()
	{
		return copyToCanvas(null, true);
	}

	/**
	 * Copies the clipboard data to the active canvas.
	 * 
	 * @return the new editparts as map (old -> new)
	 */
	public Map<IDuplicatable, IModelData> copyToCanvas(
			CompoundInfiniteUndoableEdit edit, final boolean move)
	{
		Collections.sort(this.clipList, IDuplicatable.COMPARATOR);

		final IDuplicatable[] duplicatables = new IDuplicatable[this.clipList.size()];
		this.clipList.toArray(duplicatables);

		DrawingCanvas activeCanvas = Application.getApp().getActiveCanvas();
		final ModelRoot root = activeCanvas.getModel();
		final HashMap<IDuplicatable, IModelData> elements = new HashMap<IDuplicatable, IModelData>();
		InfiniteUndoableEdit operation = new InfiniteUndoableEdit()
		{
			@Override
			public String getPresentationName()
			{
				return R.string.WINDOW_ACTION_DRAWTENSOR.string();
			}

			@Override
			public void redo()
			{
				elements.clear();
				for (IDuplicatable duplicatable : duplicatables)
				{
					IModelData d = duplicatable.duplicateModel(elements);

					// TODO make positionable base ifc
					if (move && d instanceof TensorBase)
					{
						TensorBase tensor = (TensorBase) d;
						Point2D p = PointUtil.move(
								tensor.getPosition(),
								10,
								10);
						tensor.setPosition(new Point(
								(int) p.getX(),
								(int) p.getY()));
					}
					elements.put(duplicatable, d);
					root.addChild(d);
				}
			}

			@Override
			public void undo()
			{
				Application.getApp().getActiveCanvas().setByPassModelEvents(
						true);
				for (IDuplicatable d : elements.keySet())
				{
					elements.get(d).remove();
				}
				Application.getApp().getActiveCanvas().setByPassModelEvents(
						false);
			}
		}.act();

		if (edit == null)
		{
			activeCanvas.getUndoManager().addEdit(operation);
		}
		else
		{
			edit.add(operation);
		}

		final List<IEditPart> newParts = new ArrayList<IEditPart>();
		for (IDuplicatable d : elements.keySet())
		{
			newParts.add(activeCanvas.getEditPartForModelData(elements.get(d)));
		}

		activeCanvas.setSelectedEditParts(newParts);

		return elements;
	}

	/** Clips the active canvas selection. */
	public void clipSelection()
	{
		List<IDuplicatable> dups = new ArrayList<IDuplicatable>();
		for (IEditPart part : Application.getApp().getActiveCanvas().getSelectedEditParts())
		{
			if (part instanceof IDuplicatable)
				dups.add((IDuplicatable) part);
		}
		clip(dups);
	}
}
