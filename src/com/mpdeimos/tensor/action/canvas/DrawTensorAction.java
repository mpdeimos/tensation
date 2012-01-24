package com.mpdeimos.tensor.action.canvas;

import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.GenericTensorEditPart;
import com.mpdeimos.tensor.editpart.TensorEditPartBase;
import com.mpdeimos.tensor.model.GenericTensor;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.ui.EditPartListCellRenderer;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;
import com.mpdeimos.tensor.util.LayoutUtil;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
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
	public DrawTensorAction()
	{
		super(
				R.string.WINDOW_ACTION_DRAWTENSOR.string(),
				new ImageIcon(R.drawable.DRAW_TENSOR.url()), KeyEvent.VK_D);

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
			TensorBase duplicate = ((TensorBase) this.editPart.getModel()).duplicate(root);

			drawTensor(duplicate);

			return true;
		}

		return false;
	}

	// TODO Refactor to Util Class
	/** Adds a tensor to the current model. */
	public static void drawTensor(final IModelData tensor)
	{
		DrawingCanvas activeCanvas = Application.getApp().getActiveCanvas();
		final ModelRoot root = activeCanvas.getModel();
		activeCanvas.getUndoManager().addEdit(
				new InfiniteUndoableEdit()
		{
			@Override
			public String getPresentationName()
			{
				return R.string.WINDOW_ACTION_DRAWTENSOR.string();
			}

			@Override
			public void undo()
			{
				root.removeChild(tensor);
			}

			@Override
			public void redo()
			{
				root.addChild(tensor);
			}
		}.act());
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

		/** the editpart factory. */
		private final EditPartFactory editPartFactory = new EditPartFactory();

		/** Constructor. */
		public ContextPanelContent()
		{
			DividerLabel label = new DividerLabel(
						R.string.WINDOW_CONTEXTPANEL_DRAWTENSOR_SELECT.string());
			this.add(label);

			JList list = new JList(DrawTensorAction.this.tensorList);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list.setVisibleRowCount(-1);
			list.setCellRenderer(new EditPartListCellRenderer(
					CELL_SIZE, CELL_SIZE));
			list.addListSelectionListener(this);

			JScrollPane listScroller = new JScrollPane(list);
			LayoutUtil.setHeight(listScroller, CELL_SIZE * 2);
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
				DrawTensorAction.this.editPart = (TensorEditPartBase) this.editPartFactory.createEditPart(tensor);
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
					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SINK,
									EDirection.SINK, EDirection.SINK })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SOURCE,
									EDirection.SOURCE, EDirection.SOURCE })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SINK,
									EDirection.SINK })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SOURCE,
									EDirection.SOURCE, })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SINK,
									EDirection.SOURCE, })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SINK })),

					new GenericTensorEditPart(new GenericTensor(
							null,
							p,
							new EDirection[] { EDirection.SOURCE })),
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
