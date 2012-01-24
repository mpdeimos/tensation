package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IDuplicatable;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;
import com.mpdeimos.tensor.util.PointUtil;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	 * @return the new editparts
	 */
	public List<IEditPart> copyToCanvas()
	{
		Collections.sort(this.clipList, IDuplicatable.COMPARATOR);

		final IDuplicatable[] duplicatables = new IDuplicatable[this.clipList.size()];
		this.clipList.toArray(duplicatables);

		DrawingCanvas activeCanvas = Application.getApp().getActiveCanvas();
		final ModelRoot root = activeCanvas.getModel();
		final List<IModelData> elements = new ArrayList<IModelData>();
		activeCanvas.getUndoManager().addEdit(
				new InfiniteUndoableEdit()
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
				HashMap<IDuplicatable, IModelData> dupes = new HashMap<IDuplicatable, IModelData>();
				for (IDuplicatable duplicatable : duplicatables)
				{
					IModelData d = duplicatable.duplicateModel(dupes);
					// TODO make positionable base ifc
					if (d instanceof TensorBase)
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
					dupes.put(duplicatable, d);
					elements.add(d);
					root.addChild(d);
				}
			}

			@Override
			public void undo()
			{
				for (IModelData d : elements)
				{
					root.removeChild(d);
				}
			}
		}.act());

		final List<IEditPart> newParts = new ArrayList<IEditPart>();
		for (IModelData d : elements)
		{
			newParts.add(activeCanvas.getEditPartForModelData(d));
		}

		activeCanvas.setSelectedEditParts(newParts);

		return newParts;
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
