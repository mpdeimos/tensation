package com.mpdeimos.tensation.restructure;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelDataBase;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;
import com.mpdeimos.tensation.util.Log;

import resources.R;

/**
 * Performs epsilon-delta transformations.
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonDeltaRestruction implements IRestruction
{
	@Override
	public void restruct(IEditPart restructable)
	{
		Log.v(this, "restructure called"); //$NON-NLS-1$

		if (restructable.getModel() instanceof TensorConnection)
		{
			TensorConnection connection = (TensorConnection) restructable.getModel();

			IModelData parent = connection.getParent();

			TensorConnectionAnchor sinkAnchor = connection.getSink();
			TensorBase sink = sinkAnchor.getTensor();
			TensorConnectionAnchor sourceAnchor = connection.getSource();
			TensorBase source = sourceAnchor.getTensor();

			TensorConnectionAnchor sinkNext = sink.getAnchors().get(
					(sinkAnchor.getId() + 1) % 3).getConnection().getSource();
			TensorConnectionAnchor sinkPrev = sink.getAnchors().get(
					(sinkAnchor.getId() - 1) % 3).getConnection().getSource();

			TensorConnectionAnchor sourceNext = source.getAnchors().get(
					(sourceAnchor.getId() + 1) % 3).getConnection().getSink();
			TensorConnectionAnchor sourcePrev = source.getAnchors().get(
					(sourceAnchor.getId() - 1) % 3).getConnection().getSink();

			sink.remove();
			source.remove();

			TensorConnection tc1 = new TensorConnection(
					(ModelDataBase) parent,
					sourceNext,
					sinkNext);
			parent.addChild(tc1);

			TensorConnection tc2 = new TensorConnection(
					(ModelDataBase) parent,
					sourcePrev,
					sinkPrev);
			parent.addChild(tc2);

		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getDisplayName()
	{
		return R.string.WINDOW_ACTION_RESTRUCTURE_EPSILONDELTA.string();
	}

	@Override
	public boolean isRestructable(IEditPart restructable)
	{
		if (restructable.getModel() instanceof TensorConnection)
		{
			TensorConnection connection = (TensorConnection) restructable.getModel();

			TensorBase sink = connection.getSink().getTensor();
			TensorBase source = connection.getSource().getTensor();

			return checkTensor(source) && checkTensor(sink);
		}
		return false;
	}

	/** Checks the tensor for being simplifyable w/ epsilon-delta rule. */
	private boolean checkTensor(TensorBase tensor)
	{
		return tensor.getAnchors().size() == 3
				&& tensor.getOccupiedAnchors().size() == 3;
	}
}