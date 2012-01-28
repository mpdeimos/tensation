package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.model.IModelData;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Feature for duplicatable editparts.
 * 
 * @author mpdeimos
 * 
 */
public interface IDuplicatable
{
	/**
	 * Duplicates the EditPart to the active canvas.
	 * 
	 * @param duplicates
	 *            a hash map that maps from old EditParts to new ones. Note:
	 *            This map may not include all EditParts yet; use the
	 *            duplicationPriority to steer the order.
	 * @return a duplicate of the EditPart's model.
	 */
	public IModelData duplicateModel(
			HashMap<IDuplicatable, IModelData> duplicates);

	/**
	 * @return the priority of duplication of an EditPart, higher values mean
	 *         earlier duplication.
	 */
	public int getDuplicatePriority();

	/**
	 * @return a collection of EditParts that this EditPart requires upon
	 *         duplicating. Nullable.
	 */
	public Collection<? extends IDuplicatable> getLinkedEditParts();

	/** Comparator for IDuplicatables by priority. */
	public static final Comparator<? super IDuplicatable> COMPARATOR = new Comparator<IDuplicatable>()
	{
		@Override
		public int compare(IDuplicatable o1, IDuplicatable o2)
		{
			return o1.getDuplicatePriority() - o2.getDuplicatePriority();
		}
	};
}
