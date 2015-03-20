public class NewHorizontalLinearlayout extends LinearLayout implements 
OnGestureListener {

private int mScreenWidth;
private int mChildWidth;
private int mFirstPos;
private int mMiddlePos;
private int mLastPos;
private float lastX;
private GestureDetector mDetector;
private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
private static final int BUTTTON_COUNT = 5;
LayoutParams params;

public NewHorizontalLinearlayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    WindowManager wm = (WindowManager) context
            .getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    mScreenWidth = outMetrics.widthPixels;
    mDetector = new GestureDetector(getContext(), this);
    init();
}

public void init() {
    mChildWidth = mScreenWidth / 3;
    params = new LayoutParams(mChildWidth, LayoutParams.WRAP_CONTENT);
    Button b;
    for (int i = 0; i < BUTTTON_COUNT; i++) {
        b = new Button(getContext());
        b.setText("button" + i);
        // b.setEnabled(false);
        addView(b, params);
        mViewPos.put(b, i);
    }
    scrollTo(mChildWidth, 0);

    mFirstPos = 1;
    mMiddlePos = 2;
    mLastPos = 3;
}

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    return true;
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
        lastX = event.getRawX();
        break;
    case MotionEvent.ACTION_UP:
        float nowX = event.getRawX();
        float delta = lastX - nowX;
        if (delta >= 0) {
            if ((delta) >= mChildWidth / 3) {
                loadNext();

            } else {
                // scrollback
                scrollBy((int) -delta, 0);
            }
        } else {
            if ((delta) <= -mChildWidth / 3) {
                loadPre();

            } else {
                scrollBy((int) -delta, 0);
            }
        }
    default:
        break;
    }
    mDetector.onTouchEvent(event);
    return true;
}

private void loadPre() {
    int addIndex = mViewPos.get(getChildAt(getChildCount() - 1));
    Button addButton = new Button(getContext());

    addButton.setText("button" + addIndex);
    removeViewAt(getChildCount() - 1);
    if (mFirstPos == 0) {
        mFirstPos = BUTTTON_COUNT - 1;

    } else {
        mFirstPos = mFirstPos - 1;
    }
    countIndex(mFirstPos);
    scrollTo(mChildWidth, 0);
    addView(addButton, 0, params);
    mViewPos.put(addButton, addIndex);
}

private void loadNext() {
    int addIndex = mViewPos.get(getChildAt(0));
    Button addButton = new Button(getContext());
    addButton.setText("button" + addIndex);
    removeViewAt(0);
    mFirstPos = mMiddlePos;
    mMiddlePos = mLastPos;
    if (mMiddlePos == BUTTTON_COUNT - 1) {
        mLastPos = 0;
    } else {
        mLastPos = mMiddlePos + 1;
    }
    scrollTo(mChildWidth, 0);
    addView(addButton, BUTTTON_COUNT - 1, params);

    mViewPos.put(addButton, addIndex);
}

private void countIndex(int firstIndex) {
    if (firstIndex == BUTTTON_COUNT - 1) {
        mMiddlePos = 0;
        mLastPos = 1;
    }
    if (firstIndex == BUTTTON_COUNT - 2) {
        mMiddlePos = BUTTTON_COUNT - 1;
        mLastPos = 0;
    } else {
        mMiddlePos = firstIndex + 1;
        mLastPos = firstIndex + 2;
    }
}

@Override
public boolean onDown(MotionEvent arg0) {
    // TODO Auto-generated method stub
    return false;
}

@Override
public boolean onFling(MotionEvent mCurrentDownEvent, MotionEvent ev,
        float scrollX, float scrollY) {

    // scrollTo((int) scrollX, (int) scrollY);
    return false;
}

@Override
public void onLongPress(MotionEvent arg0) {
    // TODO Auto-generated method stub

}

@Override
public boolean onScroll(MotionEvent mCurrentDownEvent, MotionEvent ev,
        float scrollX, float scrollY) {
    scrollBy((int) scrollX, 0);
    return false;
}

@Override
public void onShowPress(MotionEvent arg0) {
    // TODO Auto-generated method stub

}

private boolean inChild(int x, int y, View child) {
    int[] loc = new int[2];
    child.getLocationOnScreen(loc);
    int childX = loc[0];
    int childY = loc[1];
    return (y > childY && y <= childY + child.getHeight() && x > childX && x <= childX
            + child.getWidth());
}

@Override
public boolean onSingleTapUp(MotionEvent ev) {
    // Toast.makeText(getContext(), "show", 0).show();
    int x = (int) ev.getRawX();
    int y = (int) ev.getRawY();
    for (int i = 0; i < 3; i++) {
        View v = getChildAt(i + 1);
        if (inChild(x, y, v)) {
            int pos = mViewPos.get(v);
            Toast.makeText(getContext(), "b" + pos, Toast.LENGTH_SHORT)
                    .show();
        }
    }
    return false;
}
}