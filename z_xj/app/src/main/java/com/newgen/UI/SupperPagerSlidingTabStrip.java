package com.newgen.UI;

import java.util.Locale;

import com.newgen.tools.DisplayTools;
import com.newgen.xj_app.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;


/***
 * 产生栏目滑动的效果 当使用PagerSlidingTabStrip 与 ViewPager配合使用时，ViewPager 的 Adapter
 * 一定要实现PagerSlidingTabStripAdapterInterface接口
 * 
 * @author Administrator
 * 
 * 蒙文的
 * 
 */
public class SupperPagerSlidingTabStrip extends HorizontalScrollView {

	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}

	private static final int[] ATTRS = new int[] { android.R.attr.textSize,
			android.R.attr.textColor };

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	private LinearLayout tabsContainer;
	private ViewPager pager;

	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private boolean checkedTabWidths = false;

	private boolean shouldExpand = false;
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	// 下划线字体颜色
	private int indicatorColor = 0xFF1D88CE;
	// private int indicatorHeight = 4;
	// private int underlineColor = 0xFF0072BB;//下划线颜色
	private int indicatorHeight = 2;
//	#F10000 D70D1B #f86835 1D88CE
	private int underlineColor = 0xFF1D88CE;// 下划线颜色
	private int dividerColor = 0x00000000;// 分隔线颜色
	private int underlineHeight = 1 / 2;// 被选中栏目下划线高度
	private int dividerPadding = 12;// 两栏目中间的分隔线内填充数（上下）
	private int tabPadding = 13;// 栏目的左右填充数
	private int dividerWidth = 1;// 分隔线宽度

	private int tabTextSize = 14;// 栏目字体大小
	// private int tabTextColor = 0xFF555555;//栏目字体颜色
	// private int tabTextSelectColor = 0xFF0072BB;//被选中栏目字体颜色
	private int tabTextColor = 0xFFaab1b3;// 栏目字体颜色#5D5D5D
	private int tabTextSelectColor = 0xFFF90202;// 被选中栏目字体颜色
	private int tabTypefaceStyle = Typeface.BOLD;
	// private int tabTypefaceStyle = Typeface.NORMAL;
	private Typeface tabTypeface = Typeface.create("黑体", tabTypefaceStyle);

	public int tabStripWidth;
	public int minWidth;

	private int lastScrollX = 0;

	private int tabBackgroundResId = R.drawable.background_tab;

	private Locale locale;

	public SupperPagerSlidingTabStrip(Context context) {
		this(context, null);
	}

	public SupperPagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SupperPagerSlidingTabStrip(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
//		tabsContainer.setGravity(Gravity.RIGHT);
		tabsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		a = context.obtainStyledAttributes(attrs,
				R.styleable.PagerSlidingTabStrip);

		indicatorColor = a
				.getColor(R.styleable.PagerSlidingTabStrip_indicatorColor,
						indicatorColor);
		underlineColor = a
				.getColor(R.styleable.PagerSlidingTabStrip_underlineColor,
						underlineColor);
		dividerColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_dividerColor, dividerColor);
		indicatorHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_indicatorHeight,
				indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_underlineHeight,
				underlineHeight);
		dividerPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_dividerPadding_,
				dividerPadding);
		tabPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_tabPaddingLeftRight,
				tabPadding);
		tabBackgroundResId = a.getResourceId(
				R.styleable.PagerSlidingTabStrip_tabBackground,
				tabBackgroundResId);
		shouldExpand = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_shouldExpand, shouldExpand);
		scrollOffset = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_scrollOffset, scrollOffset);
		textAllCaps = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_textAllCaps_, textAllCaps);

		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}

	public void setViewPager(ViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}

		pager.setOnPageChangeListener(pageListener);
		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged() {

		tabsContainer.removeAllViews();
		tabCount = pager.getAdapter().getCount();

		// 满足项目需要
		minWidth = DisplayTools.sp2px(getContext(), 120);

		 int maxTxtNumber = 0;
		 for (int i = 0; i < tabCount; i++) {
//		 int temp = pager.getAdapter().getPageTitle(i).toString().trim()
//		 .length();
//
//		 minWidth = DisplayTools.sp2px(getContext(), temp *
//				 tabTextSize)
//				 + tabPadding / 2;
//				 minWidth = minWidth > tabStripWidth / tabCount ? minWidth
//				 : tabStripWidth / tabCount;
		 PagerSlidingTabStripAdapterInterface adapter = (PagerSlidingTabStripAdapterInterface) pager
					.getAdapter();
			addTextTab(i, adapter.getPageSummary(i).toString(), null);
		 }

		updateTabStyles();

		checkedTabWidths = false;

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							getViewTreeObserver().removeGlobalOnLayoutListener(
									this);
						} else {
							getViewTreeObserver().removeOnGlobalLayoutListener(
									this);
						}

						currentPosition = pager.getCurrentItem();
						scrollToChild(currentPosition, 0);
					}
				});

	}
	
	// 显示的textTab
	private void addTextTab(final int position, String title, String summary) {
		MSupperTabView tab = new MSupperTabView(getContext());
		tab.setValues(title, summary);
		tab.setFocusable(true);
		tab.setGravity(Gravity.CENTER);
		
		tab.setMaxWidth(title);

		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position);
			}
		});

		tabsContainer.addView(tab);
	}

	private void addIconTab(final int position, int resId) {

		ImageButton tab = new ImageButton(getContext());
		tab.setFocusable(true);
		tab.setImageResource(resId);

		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position);
			}
		});
		tabsContainer.addView(tab);
	}

	private void updateTabStyles() {
			for (int i = 0; i < tabCount; i++) {

				View v = tabsContainer.getChildAt(i);
				v.setBackgroundResource(tabBackgroundResId);
				if (shouldExpand) {
					v.setPadding(10, 0, 10, 0);
				} else {
					 v.setPadding(25, 0, 25, 0);
				}

				if (v instanceof MSupperTabView) {

					MSupperTabView tab = (MSupperTabView) v;
					tab.setColor(tabTextColor);

				}
			}
		}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!shouldExpand
				|| MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			return;
		}

		int myWidth = getMeasuredWidth();
		int childWidth = 0;
		for (int i = 0; i < tabCount; i++) {
			childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
		}

		if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

			if (childWidth <= myWidth) {
				for (int i = 0; i < tabCount; i++) {
					tabsContainer.getChildAt(i).setLayoutParams(
							expandedTabLayoutParams);
				}
			}

			checkedTabWidths = true;
		}
	}

	private void scrollToChild(int position, int offset) {

		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		final int height = getHeight();

		// draw indicator line

		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		updateTabStyles();
		// 设置字体大小
		MSupperTabView currentTab = (MSupperTabView) tabsContainer
				.getChildAt(currentPosition);
		
		currentTab.setColor(tabTextSelectColor);

		// 选中字体的颜色 #f86835 (红色)D70D1B #1D88CE 
//		int parseColor = Color.parseColor("#1d1a1a");
//		
//		currentTab.setColor(parseColor);
		
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}

		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
				rectPaint);

		// draw underline

		// rectPaint.setColor(underlineColor);
		// canvas.drawRect(0, height - underlineHeight,
		// tabsContainer.getWidth(), height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getLeft(), dividerPadding, tab.getRight(),
					height - dividerPadding, dividerPaint);
		}
	}

	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			currentPosition = position;
			currentPositionOffset = positionOffset;
			if(tabsContainer.getChildAt(position)!=null)
				scrollToChild(position, (int) (positionOffset * tabsContainer
						.getChildAt(position).getWidth()));
			invalidate();
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(pager.getCurrentItem(), 0);
			}

		}

		@Override
		public void onPageSelected(int position) {

		}

	}

	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight() {
		return indicatorHeight;
	}

	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor() {
		return underlineColor;
	}

	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor() {
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight() {
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding() {
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	public boolean getShouldExpand() {
		return shouldExpand;
	}

	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	public int getTextSize() {
		return tabTextSize;
	}

	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		updateTabStyles();
	}

	public void setTextColorResource(int resId) {
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor() {
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	public void setTabBackground(int resId) {
		this.tabBackgroundResId = resId;
	}

	public int getTabBackground() {
		return tabBackgroundResId;
	}

	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	public int getTabTextSize() {
		return tabTextSize;
	}

	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}

	public int getTabTextColor() {
		return tabTextColor;
	}
	
	public int getTabWidth() {
		MSupperTabView tab;
		if(tabsContainer!=null){
			tab = (MSupperTabView) tabsContainer.getChildAt(0);
			return tab.getWidth();
		}else 
			return 0;
	}

	public void setTabTextColor(int tabTextColor) {
		this.tabTextColor = tabTextColor;
	}

	public int getTabTextSelectColor() {
		return tabTextSelectColor;
	}

	public void setTabTextSelectColor(int tabTextSelectColor) {
		this.tabTextSelectColor = tabTextSelectColor;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	/***
	 * 当使用PagerSlidingTabStrip 与 ViewPager配合使用时，ViewPager 的 Adapter
	 * 一定要实现PagerSlidingTabStripAdapterInterface接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface PagerSlidingTabStripAdapterInterface {
		/**
		 * 返回在tab显示的title
		 * 
		 * @return
		 */
		// public CharSequence getPageTitle(int position);

		public CharSequence getPageSummary(int position);

	}

}
