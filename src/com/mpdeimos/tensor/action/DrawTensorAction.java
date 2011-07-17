package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DrawingCanvas;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

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
	private EpsilonTensorEditPart editPart;

	/** the context panel. */
	private final ContextPanelContent contextPanelcontent;

	public EDirection direction = EDirection.SOURCE;

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
				new ImageIcon(R.drawable.getURL("draw")));

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
	public boolean doOnMouseClicked(MouseEvent e)
	{

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			ModelRoot root = this.canvas.getModel();
			root.addChild(new EpsilonTensor(root, e.getPoint(), this.direction));

			return true;
		}

		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
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
			ActionListener
	{
		/** The buttons within this view. */
		private final ButtonGroup btnGroup;

		/** Constructor. */
		public ContextPanelContent()
		{
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

			this.btnGroup = new ButtonGroup();

			JRadioButton covariantTensor = new JRadioButton(
					"Cov. ε-Tensor",
					true);
			covariantTensor.addActionListener(this);
			this.btnGroup.add(covariantTensor);
			this.add(covariantTensor);

			JRadioButton contravariantTensor = new JRadioButton(
					"Contrav. ε-Tensor");
			contravariantTensor.addActionListener(this);
			this.btnGroup.add(contravariantTensor);
			this.add(contravariantTensor);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// FIXME this is all a bit ugly, let's do it better!
			int i = 0;
			Enumeration<AbstractButton> elements = this.btnGroup.getElements();
			while (elements.hasMoreElements())
			{
				AbstractButton nextElement = elements.nextElement();
				if (nextElement.isSelected())
					break;
				i++;
			}

			if (i == 0)
				DrawTensorAction.this.direction = EDirection.SOURCE;
			else
				DrawTensorAction.this.direction = EDirection.SINK;

			DrawTensorAction.this.editPart = new EpsilonTensorEditPart(
					new EpsilonTensor(
							null,
							DrawTensorAction.this.position,
							DrawTensorAction.this.direction));
		}
	}
}
