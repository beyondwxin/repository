package net.yoojia.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import net.yoojia.imagemap.core.Bubble;
import net.yoojia.imagemap.core.Shape;
import net.yoojia.imagemap.core.ShapeExtension;
import net.yoojia.imagemap.core.ShapeExtension.OnShapeActionListener;
import net.yoojia.imagemap.support.TranslateAnimation;

/**
 * author : chenyoca@gmail.com date : 2013-5-19 An HTML map like widget in an
 * Android view controller
 */
public class ImageMap extends FrameLayout implements ShapeExtension,
		ShapeExtension.OnShapeActionListener,
		TranslateAnimation.OnAnimationListener {

	private HighlightImageView highlightImageView;
	private Bubble bubble;
	private View viewForAnimation;
	private Context context;

	public ImageMap(Context context ) {
		//this.context=context;
		this(context, null);
		// initialImageView(context);
	}

	public ImageMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialImageView(context);
	}

	public ImageMap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialImageView(context);
	}

	private void initialImageView(Context context) {
		this.context=context;
		highlightImageView = new HighlightImageView(context,1.0f);
		highlightImageView.setOnShapeClickListener(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(highlightImageView, params);
		viewForAnimation = new View(context);
		addView(viewForAnimation, 0, 0);
	}
	
//	public void SetinitialImageView(Context context,float newNum) {
//		highlightImageView = new HighlightImageView(context,newNum);
//		highlightImageView.setOnShapeClickListener(this);
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//		addView(highlightImageView, params);
//		viewForAnimation = new View(context);
//		addView(viewForAnimation, 0, 0);
//	}
//
	// private OnShapeActionListener onShapeClickListener;
	//
	// public void setOnShapeClickListener(OnShapeActionListener
	// onShapeClickListener){
	// this.onShapeClickListener = onShapeClickListener;
	// }

	/**
	 * Set a bubble view controller and it's renderDelegate interface.
	 * 
	 * @param bubbleView
	 *            A view controller object for display on image map.
	 * @param renderDelegate
	 *            The display interface for bubble view controller render.
	 */
	public void setBubbleView(View bubbleView,
			Bubble.RenderDelegate renderDelegate) {
		if (bubbleView == null) {
			throw new IllegalArgumentException(
					"View for bubble cannot be null !");
		}
		bubble = new Bubble(bubbleView);
		bubble.setRenderDelegate(renderDelegate);
		addView(bubble);
		bubble.view.setVisibility(View.INVISIBLE);
	}

	/**
	 * 添加Shape，并关联到Bubble的位置 - Add a shape and set reference to the bubble.
	 * 
	 * @param shape
	 *            Shape
	 */
	public void addShapeAndRefToBubble(final Shape shape) {
		addShape(shape);
		if (bubble != null) {
			shape.createBubbleRelation(bubble);
		}
	}

	@Override
	public void onTranslate(float deltaX, float deltaY) {
		highlightImageView.moveBy(deltaX, deltaY);
	}

	@Override
	public void addShape(Shape shape) {

		float scale = highlightImageView.getScale();
		shape.onScale(scale);

		// 将图像中心移动到目标形状的中心坐标上
		// Move the center point of the image to the target shape center.
		PointF from = highlightImageView.getAbsoluteCenter();
		PointF to = shape.getCenterPoint();
		TranslateAnimation movingAnimation = new TranslateAnimation(from.x,
				to.x, from.y, to.y);
		movingAnimation.setOnAnimationListener(this);
		movingAnimation.setInterpolator(new DecelerateInterpolator());
		movingAnimation.setDuration(500);
		movingAnimation.setFillAfter(true);
		viewForAnimation.startAnimation(movingAnimation);

		PointF offset = highlightImageView.getAbsoluteOffset();
		shape.onTranslate(offset.x, offset.y);
		highlightImageView.addShape(shape);

	}

	@Override
	public void removeShape(Object tag) {
		highlightImageView.removeShape(tag);
	}

	@Override
	public void clearShapes() {
		for (Shape item : highlightImageView.getShapes()) {
			item.cleanBubbleRelation();
		}
		highlightImageView.clearShapes();
		if (bubble != null) {
			bubble.view.setVisibility(View.GONE);
		}
	}

	@Override
	public final void onShapeClick(Shape shape, float xOnImage, float yOnImage) {
		for (Shape item : highlightImageView.getShapes()) {
			item.cleanBubbleRelation();
		}
		if (bubble != null) {
			bubble.showAtShape(shape);
		}
	}

	/**
	 * set a bitmap for image map.
	 * 
	 * @param bitmap
	 *            image
	 */
	public void setMapBitmap(Bitmap bitmap,float newNum) {
		
		highlightImageView = new HighlightImageView(context,newNum);
		highlightImageView.setOnShapeClickListener(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(highlightImageView, params);
		viewForAnimation = new View(context);
		addView(viewForAnimation, 0, 0);
		highlightImageView.setImageBitmap(bitmap);
	}

	// HighlightImageView highlightImageView;
	public HighlightImageView getHighlightImageView() {
		return highlightImageView;
	}

}
