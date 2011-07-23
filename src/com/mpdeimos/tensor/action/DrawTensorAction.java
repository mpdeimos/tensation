package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.editpart.LineTensorEditPart;
import com.mpdeimos.tensor.editpart.TensorEditPartBase;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.LineTensor;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.ui.EditPartListCellRenderer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resources.R;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 * 
 */
public class DrawTensorAction extends CanvasActionBase
{
	/** position of the current drawing figure */
	private final Point position;

	/** the editpart being drawn ontop of the canvas */
	private TensorEditPartBase editPart;

	/** the context panel. */
	private final ContextPanelContent contextPanelcontent;

	/** The list of different tensors. */
	public TensorListModel tensorList = new TensorListModel();

	/** flag whether the mouse is hovered over the canvas or not. */
	private boolean isMouseOverCanvas;

	/**
	 * Constructor.
	 */
	public DrawTensorAction(
			ApplicationWindow applicationWindow,
			DrawingCanvas drawingPanel)
	{
		super(
				applicationWindow,
				drawingPanel,
				R.strings.getString("window_action_draw"), //$NON-NLS-1$
				new ImageIcon(R.drawable.getURL("draw_tensor"))); //$NON-NLS-1$

		this.position = new Point(0, 0);
		this.editPart = new EpsilonTensorEditPart(new EpsilonTensor(
				null,
				this.position,
				EDirection.SOURCE));

		this.contextPanelcontent = this.new ContextPanelContent();
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		this.editPart.setPosition(e.getPoint());
		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseEntered(MouseEvent e)
	{
		this.isMouseOverCanvas = true;
		return false;
	}

	@Override
	public boolean doOnMouseExited(MouseEvent e)
	{
		this.isMouseOverCanvas = false;
		this.canvas.repaint();
		return false;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e)
	{

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			ModelRoot root = this.canvas.getModel();
			root.addChild(((TensorBase) this.editPart.getModel()).duplicate(root));

			return true;
		}

		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		if (this.isMouseOverCanvas)
			this.editPart.draw(gfx);
		return true;
	}

	@Override
	protected ContextPanelContentBase getContextPanel()
	{
		return this.contextPanelcontent;
	}

	/** The Context Panel for this action instance. */
	private class ContextPanelContent extends ContextPanelContentBase implements
			ListSelectionListener
	{
		/** cell size of the editpart list. */
		private static final int CELL_SIZE = 40;

		/** Constructor. */
		public ContextPanelContent()
		{
			DividerLabel label = new DividerLabel(
						R.strings.getString("window_contextpanel_drawtensor_select")); //$NON-NLS-1$
			this.add(label);

			JList list = new JList(DrawTensorAction.this.tensorList);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list.setVisibleRowCount(-1);
			list.setCellRenderer(new EditPartListCellRenderer(
					CELL_SIZE, CELL_SIZE));
			list.addListSelectionListener(this);

			JScrollPane listScroller = new JScrollPane(list);
			stretchToFullWidth(listScroller, CELL_SIZE * 2);
			this.add(listScroller);

			list.setSelectedIndex(0);

			this.add(Box.createVerticalGlue());
		}

		@Override
		public void valueChanged(ListSelectionEvent event)
		{
			if (event.getValueIsAdjusting() == false)
			{
				JList list = (JList) event.getSource();

				int index = list.getSelectedIndex();

				if (index == -1)
					return;

				TensorEditPartBase editPart = DrawTensorAction.this.tensorList.getElementAt(index);
				TensorBase tensor = ((TensorBase) editPart.getModel()).duplicate(null);
				EditPartFactory editPartFactory = DrawTensorAction.this.canvas.getEditPartFactory();
				DrawTensorAction.this.editPart = (TensorEditPartBase) editPartFactory.createEditPart(tensor);
			}
		}
	}

	/** The ListModel backed by a list of EditParts. */
	private class TensorListModel extends AbstractListModel
	{
		/** The arraylist of editparts. */
		private final TensorEditPartBase[] editParts;

		/** Constructor. */
		public TensorListModel()
		{
			Point p = new Point(
					ContextPanelContent.CELL_SIZE / 2,
					ContextPanelContent.CELL_SIZE / 2);

			this.editParts = new TensorEditPartBase[] {
					new EpsilonTensorEditPart(new EpsilonTensor(
							null,
							p,
							EDirection.SINK)),

					new EpsilonTensorEditPart(new EpsilonTensor(
							null,
							p,
							EDirection.SOURCE)),

					new LineTensorEditPart(new LineTensor(
							null,
							p)),
			};
		}

		@Override
		public TensorEditPartBase getElementAt(int index)
		{
			return this.editParts[index];
		}

		@Override
		public int getSize()
		{
			return this.editParts.length;
		}
	}
}
