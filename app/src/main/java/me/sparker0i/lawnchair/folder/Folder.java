/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.sparker0i.lawnchair.folder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.sparker0i.lawnchair.Alarm;
import me.sparker0i.lawnchair.AppInfo;
import me.sparker0i.lawnchair.CellLayout;
import me.sparker0i.lawnchair.DeviceProfile;
import me.sparker0i.lawnchair.DragSource;
import me.sparker0i.lawnchair.DropTarget;
import me.sparker0i.lawnchair.ExtendedEditText;
import me.sparker0i.lawnchair.FolderInfo;
import me.sparker0i.lawnchair.FolderInfo.FolderListener;
import me.sparker0i.lawnchair.ItemInfo;
import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.LauncherAnimUtils;
import me.sparker0i.lawnchair.LauncherModel;
import me.sparker0i.lawnchair.LauncherSettings;
import me.sparker0i.lawnchair.LogDecelerateInterpolator;
import me.sparker0i.lawnchair.OnAlarmListener;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lawnchair.ShortcutInfo;
import me.sparker0i.lawnchair.UninstallDropTarget.DropTargetSource;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.Workspace.ItemOperator;
import me.sparker0i.lawnchair.accessibility.AccessibleDragListenerAdapter;
import me.sparker0i.lawnchair.blur.BlurDrawable;
import me.sparker0i.lawnchair.blur.BlurWallpaperProvider;
import me.sparker0i.lawnchair.config.FeatureFlags;
import me.sparker0i.lawnchair.dragndrop.DragController;
import me.sparker0i.lawnchair.dragndrop.DragController.DragListener;
import me.sparker0i.lawnchair.dragndrop.DragLayer;
import me.sparker0i.lawnchair.dragndrop.DragOptions;
import me.sparker0i.lawnchair.pageindicators.PageIndicatorDots;
import me.sparker0i.lawnchair.util.CircleRevealOutlineProvider;
import me.sparker0i.lawnchair.util.Thunk;
import me.sparker0i.lawnchair.AppInfo;
import me.sparker0i.lawnchair.CellLayout;
import me.sparker0i.lawnchair.DeviceProfile;
import me.sparker0i.lawnchair.DragSource;
import me.sparker0i.lawnchair.DropTarget;
import me.sparker0i.lawnchair.ExtendedEditText;
import me.sparker0i.lawnchair.FolderInfo;
import me.sparker0i.lawnchair.ItemInfo;
import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.LauncherAnimUtils;
import me.sparker0i.lawnchair.LauncherModel;
import me.sparker0i.lawnchair.LauncherSettings;
import me.sparker0i.lawnchair.LogDecelerateInterpolator;
import me.sparker0i.lawnchair.OnAlarmListener;
import me.sparker0i.lawnchair.ShortcutInfo;
import me.sparker0i.lawnchair.UninstallDropTarget;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.accessibility.AccessibleDragListenerAdapter;
import me.sparker0i.lawnchair.blur.BlurDrawable;
import me.sparker0i.lawnchair.blur.BlurWallpaperProvider;
import me.sparker0i.lawnchair.config.FeatureFlags;
import me.sparker0i.lawnchair.dragndrop.DragController;
import me.sparker0i.lawnchair.dragndrop.DragLayer;
import me.sparker0i.lawnchair.dragndrop.DragOptions;
import me.sparker0i.lawnchair.pageindicators.PageIndicatorDots;
import me.sparker0i.lawnchair.util.CircleRevealOutlineProvider;
import me.sparker0i.lawnchair.util.Thunk;
import me.sparker0i.lawnchair.AppInfo;
import me.sparker0i.lawnchair.CellLayout;
import me.sparker0i.lawnchair.DeviceProfile;
import me.sparker0i.lawnchair.DragSource;
import me.sparker0i.lawnchair.DropTarget;
import me.sparker0i.lawnchair.ExtendedEditText;
import me.sparker0i.lawnchair.FolderInfo;
import me.sparker0i.lawnchair.ItemInfo;
import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.LauncherAnimUtils;
import me.sparker0i.lawnchair.LauncherModel;
import me.sparker0i.lawnchair.LauncherSettings;
import me.sparker0i.lawnchair.LogDecelerateInterpolator;
import me.sparker0i.lawnchair.OnAlarmListener;
import me.sparker0i.lawnchair.ShortcutInfo;
import me.sparker0i.lawnchair.UninstallDropTarget;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.accessibility.AccessibleDragListenerAdapter;
import me.sparker0i.lawnchair.blur.BlurDrawable;
import me.sparker0i.lawnchair.blur.BlurWallpaperProvider;
import me.sparker0i.lawnchair.config.FeatureFlags;
import me.sparker0i.lawnchair.dragndrop.DragController;
import me.sparker0i.lawnchair.dragndrop.DragLayer;
import me.sparker0i.lawnchair.dragndrop.DragOptions;
import me.sparker0i.lawnchair.pageindicators.PageIndicatorDots;
import me.sparker0i.lawnchair.util.CircleRevealOutlineProvider;
import me.sparker0i.lawnchair.util.Thunk;

/**
 * Represents a set of icons chosen by the user or generated by the system.
 */
public class Folder extends LinearLayout implements DragSource, View.OnClickListener,
        View.OnLongClickListener, DropTarget, FolderInfo.FolderListener, TextView.OnEditorActionListener,
        View.OnFocusChangeListener, DragController.DragListener, UninstallDropTarget.DropTargetSource {
    private static final String TAG = "Launcher.Folder";

    /**
     * We avoid measuring {@link #mContent} with a 0 width or height, as this
     * results in CellLayout being measured as UNSPECIFIED, which it does not support.
     */
    private static final int MIN_CONTENT_DIMEN = 5;

    static final int STATE_NONE = -1;
    static final int STATE_SMALL = 0;
    static final int STATE_ANIMATING = 1;
    static final int STATE_OPEN = 2;

    /**
     * Time for which the scroll hint is shown before automatically changing page.
     */
    public static final int SCROLL_HINT_DURATION = DragController.SCROLL_DELAY;

    /**
     * Fraction of icon width which behave as scroll region.
     */
    private static final float ICON_OVERSCROLL_WIDTH_FACTOR = 0.45f;

    private static final int FOLDER_NAME_ANIMATION_DURATION = 633;

    private static final int REORDER_DELAY = 250;
    private static final int ON_EXIT_CLOSE_DELAY = 400;
    private static final Rect sTempRect = new Rect();

    private static String sDefaultFolderName;
    private static String sHintText;

    private final Alarm mReorderAlarm = new Alarm();
    private final Alarm mOnExitAlarm = new Alarm();
    private final Alarm mOnScrollHintAlarm = new Alarm();
    @Thunk
    final Alarm mScrollPauseAlarm = new Alarm();

    @Thunk
    final ArrayList<View> mItemsInReadingOrder = new ArrayList<>();

    private final int mExpandDuration;
    private final int mMaterialExpandDuration;
    private final int mMaterialExpandStagger;

    private final InputMethodManager mInputMethodManager;

    protected final Launcher mLauncher;
    protected DragController mDragController;
    private BlurDrawable mBlurDrawable;
    public FolderInfo mInfo;

    @Thunk
    FolderIcon mFolderIcon;

    @Thunk
    FolderPagedView mContent;
    public ExtendedEditText mFolderName;
    private PageIndicatorDots mPageIndicator;

    private View mFooter;
    private int mFooterHeight;

    // Cell ranks used for drag and drop
    @Thunk
    int mTargetRank, mPrevTargetRank, mEmptyCellRank;

    @ViewDebug.ExportedProperty(category = "launcher",
            mapping = {
                    @ViewDebug.IntToString(from = STATE_NONE, to = "STATE_NONE"),
                    @ViewDebug.IntToString(from = STATE_SMALL, to = "STATE_SMALL"),
                    @ViewDebug.IntToString(from = STATE_ANIMATING, to = "STATE_ANIMATING"),
                    @ViewDebug.IntToString(from = STATE_OPEN, to = "STATE_OPEN"),
            })
    @Thunk
    int mState = STATE_NONE;
    @ViewDebug.ExportedProperty(category = "launcher")
    private boolean mRearrangeOnClose = false;
    boolean mItemsInvalidated = false;
    private View mCurrentDragView;
    private boolean mIsExternalDrag;
    private boolean mDragInProgress = false;
    private boolean mDeleteFolderOnDropCompleted = false;
    private boolean mSuppressFolderDeletion = false;
    private boolean mItemAddedBackToSelfViaIcon = false;
    @Thunk
    float mFolderIconPivotX;
    @Thunk
    float mFolderIconPivotY;
    private boolean mIsEditingName = false;

    @ViewDebug.ExportedProperty(category = "launcher")
    private boolean mDestroyed;

    @Thunk
    Runnable mDeferredAction;
    private boolean mDeferDropAfterUninstall;
    private boolean mUninstallSuccessful;

    // Folder scrolling
    private int mScrollAreaOffset;

    @Thunk
    int mScrollHintDir = DragController.SCROLL_NONE;
    @Thunk
    int mCurrentScrollDir = DragController.SCROLL_NONE;

    /**
     * Used to inflate the Workspace from XML.
     *
     * @param context The application's context.
     * @param attrs   The attributes set containing the Workspace's customization values.
     */
    public Folder(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAlwaysDrawnWithCacheEnabled(false);
        mInputMethodManager = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        Resources res = getResources();
        mExpandDuration = res.getInteger(R.integer.config_folderExpandDuration);
        mMaterialExpandDuration = res.getInteger(R.integer.config_materialFolderExpandDuration);
        mMaterialExpandStagger = res.getInteger(R.integer.config_materialFolderExpandStagger);

        if (sDefaultFolderName == null) {
            sDefaultFolderName = res.getString(R.string.folder_name);
        }
        if (sHintText == null) {
            sHintText = res.getString(R.string.folder_hint_text);
        }
        mLauncher = Launcher.getLauncher(context);
        // We need this view to be focusable in touch mode so that when text editing of the folder
        // name is complete, we have something to focus on, thus hiding the cursor and giving
        // reliable behavior when clicking the text field (since it will always gain focus on click).
        setFocusableInTouchMode(true);

        if (BlurWallpaperProvider.Companion.isEnabled(BlurWallpaperProvider.BLUR_FOLDER)) {
            int tintColor = Utilities.resolveAttributeData(context, R.attr.folderBgColorBlur);

            mBlurDrawable = BlurWallpaperProvider.Companion.getInstance().createDrawable(
                    res.getDimensionPixelSize(R.dimen.folder_background_radius), false);
            mBlurDrawable.setBlurredView(mLauncher.getWorkspace());
            mBlurDrawable.setShouldProvideOutline(true);
            mBlurDrawable.setOverlayColor(tintColor);
            setBackground(mBlurDrawable);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = findViewById(R.id.folder_content);
        mContent.setFolder(this);

        mPageIndicator = findViewById(R.id.folder_page_indicator);
        mFolderName = findViewById(R.id.folder_name);
        mFolderName.setOnBackKeyListener(new ExtendedEditText.OnBackKeyListener() {
            @Override
            public boolean onBackKey() {
                // Close the activity on back key press
                doneEditingFolderName(true);
                return false;
            }
        });
        mFolderName.setOnFocusChangeListener(this);
        mFolderName.setEnabled(!mLauncher.isEditingDisabled());

        if (!Utilities.ATLEAST_MARSHMALLOW) {
            // We disable action mode in older OSes where floating selection menu is not yet
            // available.
            mFolderName.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }
        mFolderName.setOnEditorActionListener(this);
        mFolderName.setSelectAllOnFocus(true);
        mFolderName.setInputType(mFolderName.getInputType() |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        mFooter = findViewById(R.id.folder_footer);

        // We find out how tall footer wants to be (it is set to wrap_content), so that
        // we can allocate the appropriate amount of space for it.
        int measureSpec = MeasureSpec.UNSPECIFIED;
        mFooter.measure(measureSpec, measureSpec);
        mFooterHeight = mFooter.getMeasuredHeight();
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof ShortcutInfo) {
            mLauncher.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return !mLauncher.isDraggingEnabled() || startDrag(v, new DragOptions());
    }

    public boolean startDrag(View v, DragOptions options) {
        Object tag = v.getTag();
        if (tag instanceof ShortcutInfo) {
            ShortcutInfo item = (ShortcutInfo) tag;
            if (!v.isInTouchMode()) {
                return false;
            }

            mEmptyCellRank = item.rank;
            mCurrentDragView = v;

            mDragController.addDragListener(this);
            if (options.isAccessibleDrag) {
                mDragController.addDragListener(new AccessibleDragListenerAdapter(
                        mContent, CellLayout.FOLDER_ACCESSIBILITY_DRAG) {

                    @Override
                    protected void enableAccessibleDrag(boolean enable) {
                        super.enableAccessibleDrag(enable);
                        mFooter.setImportantForAccessibility(enable
                                ? IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
                                : IMPORTANT_FOR_ACCESSIBILITY_AUTO);
                    }
                });
            }

            mLauncher.getWorkspace().beginDragShared(v, this, options);
        }
        return true;
    }

    @Override
    public void onDragStart(DropTarget.DragObject dragObject, DragOptions options) {
        if (dragObject.dragSource != this) {
            return;
        }

        mContent.removeItem(mCurrentDragView);
        if (dragObject.dragInfo instanceof ShortcutInfo) {
            mItemsInvalidated = true;

            // We do not want to get events for the item being removed, as they will get handled
            // when the drop completes
            try (SuppressInfoChanges s = new SuppressInfoChanges()) {
                mInfo.remove((ShortcutInfo) dragObject.dragInfo, true);
            }
        }
        mDragInProgress = true;
        mItemAddedBackToSelfViaIcon = false;
    }

    @Override
    public void onDragEnd() {
        if (mIsExternalDrag && mDragInProgress) {
            completeDragExit();
        }
        mDragController.removeDragListener(this);
    }

    public boolean isEditingName() {
        return mIsEditingName;
    }

    public void startEditingFolderName() {
        post(new Runnable() {
            @Override
            public void run() {
                mFolderName.setHint("");
                mIsEditingName = true;
            }
        });
    }

    public void dismissEditingName() {
        mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        doneEditingFolderName(true);
    }

    public void doneEditingFolderName(boolean commit) {
        mFolderName.setHint(sHintText);
        // Convert to a string here to ensure that no other state associated with the text field
        // gets saved.
        String newTitle = mFolderName.getText().toString();
        mInfo.setTitle(newTitle);
        LauncherModel.updateItemInDatabase(mLauncher, mInfo);

        if (commit) {
            Utilities.sendCustomAccessibilityEvent(
                    this, AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                    getContext().getString(R.string.folder_renamed, newTitle));
        }

        // This ensures that focus is gained every time the field is clicked, which selects all
        // the text and brings up the soft keyboard if necessary.
        mFolderName.clearFocus();

        Selection.setSelection(mFolderName.getText(), 0, 0);
        mIsEditingName = false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            dismissEditingName();
            return true;
        }
        return false;
    }

    public View getEditTextRegion() {
        return mFolderName;
    }

    /**
     * We need to handle touch events to prevent them from falling through to the workspace below.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setDragController(DragController dragController) {
        mDragController = dragController;
    }

    public void setFolderIcon(FolderIcon icon) {
        mFolderIcon = icon;
    }

    @Override
    protected void onAttachedToWindow() {
        // requestFocus() causes the focus onto the folder itself, which doesn't cause visual
        // effect but the next arrow key can start the keyboard focus inside of the folder, not
        // the folder itself.
        requestFocus();
        super.onAttachedToWindow();
        if (mBlurDrawable != null)
            mBlurDrawable.startListening();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        // When the folder gets focus, we don't want to announce the list of items.
        return true;
    }

    @Override
    public View focusSearch(int direction) {
        // When the folder is focused, further focus search should be within the folder contents.
        return FocusFinder.getInstance().findNextFocus(this, null, direction);
    }

    /**
     * @return the FolderInfo object associated with this folder
     */
    public FolderInfo getInfo() {
        return mInfo;
    }

    void bind(FolderInfo info) {
        mInfo = info;
        ArrayList<ShortcutInfo> children = info.contents;
        Collections.sort(children, ITEM_POS_COMPARATOR);

        mContent.bindItems(children);

        DragLayer.LayoutParams lp = (DragLayer.LayoutParams) getLayoutParams();
        if (lp == null) {
            lp = new DragLayer.LayoutParams(0, 0);
            lp.customPosition = true;
            setLayoutParams(lp);
        }
        centerAboutIcon();

        mItemsInvalidated = true;
        updateTextViewFocus();
        mInfo.addListener(this);

        if (!sDefaultFolderName.contentEquals(mInfo.title)) {
            mFolderName.setText(mInfo.title);
        } else {
            mFolderName.setText("");
        }

        // In case any children didn't come across during loading, clean up the folder accordingly
        mFolderIcon.post(new Runnable() {
            @Override
            public void run() {
                if (getItemCount() <= 1) {
                    replaceFolderWithFinalItem();
                }
            }
        });
    }

    /**
     * Creates a new UserFolder, inflated from R.layout.user_folder.
     *
     * @param launcher The main activity.
     * @return A new UserFolder.
     */
    @SuppressLint("InflateParams")
    static Folder fromXml(Launcher launcher) {
        return (Folder) LayoutInflater.from(FeatureFlags.INSTANCE.applyDarkTheme(launcher, FeatureFlags.DARK_FOLDER)).inflate(R.layout.user_folder_icon_normalized, null);
    }

    private void prepareReveal() {
        setScaleX(1f);
        setScaleY(1f);
        setAlpha(1f);
        mState = STATE_SMALL;
    }

    public void animateOpen() {
        if (!(getParent() instanceof DragLayer)) return;

        mContent.completePendingPageChanges();
        if (!mDragInProgress) {
            // Open on the first page.
            mContent.snapToPageImmediately(0);
        }

        // This is set to true in close(), but isn't reset to false until onDropCompleted(). This
        // leads to an inconsistent state if you drag out of the folder and drag back in without
        // dropping. One resulting issue is that replaceFolderWithFinalItem() can be called twice.
        mDeleteFolderOnDropCompleted = false;

        Animator openFolderAnim;
        final Runnable onCompleteRunnable;
        prepareReveal();
        centerAboutIcon();

        AnimatorSet anim = LauncherAnimUtils.createAnimatorSet();
        int width = getPaddingLeft() + getPaddingRight() + mContent.getDesiredWidth();
        int height = getFolderHeight();

        float transX = -0.075f * (width / 2 - getPivotX());
        float transY = -0.075f * (height / 2 - getPivotY());
        setTranslationX(transX);
        setTranslationY(transY);
        PropertyValuesHolder tx = PropertyValuesHolder.ofFloat(TRANSLATION_X, transX, 0);
        PropertyValuesHolder ty = PropertyValuesHolder.ofFloat(TRANSLATION_Y, transY, 0);

        Animator drift = ObjectAnimator.ofPropertyValuesHolder(this, tx, ty);
        drift.setDuration(mMaterialExpandDuration);
        drift.setStartDelay(mMaterialExpandStagger);
        drift.setInterpolator(new LogDecelerateInterpolator(100, 0));

        int rx = (int) Math.max(Math.max(width - getPivotX(), 0), getPivotX());
        int ry = (int) Math.max(Math.max(height - getPivotY(), 0), getPivotY());
        float radius = (float) Math.hypot(rx, ry);

        Animator reveal = new CircleRevealOutlineProvider((int) getPivotX(),
                (int) getPivotY(), 0, radius).createRevealAnimator(this, false, true);
        reveal.setDuration(mMaterialExpandDuration);
        reveal.setInterpolator(new LogDecelerateInterpolator(100, 0));

        mContent.setAlpha(0f);
        Animator iconsAlpha = ObjectAnimator.ofFloat(mContent, "alpha", 0f, 1f);
        iconsAlpha.setDuration(mMaterialExpandDuration);
        iconsAlpha.setStartDelay(mMaterialExpandStagger);
        iconsAlpha.setInterpolator(new AccelerateInterpolator(1.5f));

        mFooter.setAlpha(0f);
        Animator textAlpha = ObjectAnimator.ofFloat(mFooter, "alpha", 0f, 1f);
        textAlpha.setDuration(mMaterialExpandDuration);
        textAlpha.setStartDelay(mMaterialExpandStagger);
        textAlpha.setInterpolator(new AccelerateInterpolator(1.5f));

        anim.play(drift);
        anim.play(iconsAlpha);
        anim.play(textAlpha);
        anim.play(reveal);

        openFolderAnim = anim;

        mContent.setLayerType(LAYER_TYPE_HARDWARE, null);
        mFooter.setLayerType(LAYER_TYPE_HARDWARE, null);
        onCompleteRunnable = new Runnable() {
            @Override
            public void run() {
                mContent.setLayerType(LAYER_TYPE_NONE, null);
                mFooter.setLayerType(LAYER_TYPE_NONE, null);
            }
        };
        openFolderAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Utilities.sendCustomAccessibilityEvent(
                        Folder.this,
                        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                        mContent.getAccessibilityDescription());
                mState = STATE_ANIMATING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_OPEN;

                onCompleteRunnable.run();
                mContent.setFocusOnFirstChild();
            }
        });

        // Footer animation
        if (mContent.getPageCount() > 1 && !mInfo.hasOption(FolderInfo.FLAG_MULTI_PAGE_ANIMATION)) {
            int footerWidth = mContent.getDesiredWidth()
                    - mFooter.getPaddingLeft() - mFooter.getPaddingRight();

            float textWidth = mFolderName.getPaint().measureText(mFolderName.getText().toString());
            float translation = (footerWidth - textWidth) / 2;
            mFolderName.setTranslationX(mContent.mIsRtl ? -translation : translation);
            mPageIndicator.prepareEntryAnimation();

            // Do not update the flag if we are in drag mode. The flag will be updated, when we
            // actually drop the icon.
            final boolean updateAnimationFlag = !mDragInProgress;
            openFolderAnim.addListener(new AnimatorListenerAdapter() {

                @SuppressLint("InlinedApi")
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFolderName.animate().setDuration(FOLDER_NAME_ANIMATION_DURATION)
                            .translationX(0)
                            .setInterpolator(AnimationUtils.loadInterpolator(mLauncher,
                                    android.R.interpolator.fast_out_slow_in));
                    mPageIndicator.playEntryAnimation();

                    if (updateAnimationFlag) {
                        mInfo.setOption(FolderInfo.FLAG_MULTI_PAGE_ANIMATION, true, mLauncher);
                    }
                }
            });
        } else {
            mFolderName.setTranslationX(0);
        }

        mPageIndicator.stopAllAnimations();
        openFolderAnim.start();

        // Make sure the folder picks up the last drag move even if the finger doesn't move.
        if (mDragController.isDragging()) {
            mDragController.forceTouchMove();
        }

        mContent.verifyVisibleHighResIcons(mContent.getNextPage());
    }

    public void beginExternalDrag() {
        mEmptyCellRank = mContent.allocateRankForNewItem();
        mIsExternalDrag = true;
        mDragInProgress = true;

        // Since this folder opened by another controller, it might not get onDrop or
        // onDropComplete. Perform cleanup once drag-n-drop ends.
        mDragController.addDragListener(this);
    }

    public void animateClosed() {
        if (!(getParent() instanceof DragLayer)) return;
        final ObjectAnimator oa = LauncherAnimUtils.ofViewAlphaAndScale(this, 0, 0.9f, 0.9f);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setLayerType(LAYER_TYPE_NONE, null);
                close(true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                Utilities.sendCustomAccessibilityEvent(
                        Folder.this,
                        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                        getContext().getString(R.string.folder_closed));
                mState = STATE_ANIMATING;
            }
        });
        oa.setDuration(mExpandDuration);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        oa.start();
    }

    public void close(boolean wasAnimated) {
        // TODO: Clear all active animations.
        DragLayer parent = (DragLayer) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        mDragController.removeDropTarget(this);
        clearFocus();
        if (wasAnimated) {
            mFolderIcon.requestFocus();
        }

        if (mRearrangeOnClose) {
            rearrangeChildren();
            mRearrangeOnClose = false;
        }
        if (getItemCount() <= 1) {
            if (!mDragInProgress && !mSuppressFolderDeletion) {
                replaceFolderWithFinalItem();
            } else if (mDragInProgress) {
                mDeleteFolderOnDropCompleted = true;
            }
        }
        mSuppressFolderDeletion = false;
        clearDragInfo();
        mState = STATE_SMALL;
    }

    @Override
    public boolean acceptDrop(DragObject d) {
        final ItemInfo item = d.dragInfo;
        final int itemType = item.itemType;
        return ((itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION ||
                itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT ||
                itemType == LauncherSettings.Favorites.ITEM_TYPE_DEEP_SHORTCUT));
    }

    @Override
    public void onDragEnter(DragObject d) {
        mPrevTargetRank = -1;
        mOnExitAlarm.cancelAlarm();
        // Get the area offset such that the folder only closes if half the drag icon width
        // is outside the folder area
        mScrollAreaOffset = d.dragView.getDragRegionWidth() / 2 - d.xOffset;
    }

    OnAlarmListener mReorderAlarmListener = new OnAlarmListener() {
        @Override
        public void onAlarm(Alarm alarm) {
            mContent.realTimeReorder(mEmptyCellRank, mTargetRank);
            mEmptyCellRank = mTargetRank;
        }
    };

    public boolean isLayoutRtl() {
        return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
    }

    private int getTargetRank(DragObject d, float[] recycle) {
        recycle = d.getVisualCenter(recycle);
        return mContent.findNearestArea(
                (int) recycle[0] - getPaddingLeft(), (int) recycle[1] - getPaddingTop());
    }

    @Override
    public void onDragOver(DragObject d) {
        if (mScrollPauseAlarm.alarmPending()) {
            return;
        }
        final float[] r = new float[2];
        mTargetRank = getTargetRank(d, r);

        if (mTargetRank != mPrevTargetRank) {
            mReorderAlarm.cancelAlarm();
            mReorderAlarm.setOnAlarmListener(mReorderAlarmListener);
            mReorderAlarm.setAlarm(REORDER_DELAY);
            mPrevTargetRank = mTargetRank;

            if (d.stateAnnouncer != null) {
                d.stateAnnouncer.announce(getContext().getString(R.string.move_to_position,
                        mTargetRank + 1));
            }
        }

        float x = r[0];
        int currentPage = mContent.getNextPage();

        float cellOverlap = mContent.getCurrentCellLayout().getCellWidth()
                * ICON_OVERSCROLL_WIDTH_FACTOR;
        boolean isOutsideLeftEdge = x < cellOverlap;
        boolean isOutsideRightEdge = x > (getWidth() - cellOverlap);

        if (currentPage > 0 && (mContent.mIsRtl ? isOutsideRightEdge : isOutsideLeftEdge)) {
            showScrollHint(DragController.SCROLL_LEFT, d);
        } else if (currentPage < (mContent.getPageCount() - 1)
                && (mContent.mIsRtl ? isOutsideLeftEdge : isOutsideRightEdge)) {
            showScrollHint(DragController.SCROLL_RIGHT, d);
        } else {
            mOnScrollHintAlarm.cancelAlarm();
            if (mScrollHintDir != DragController.SCROLL_NONE) {
                mContent.clearScrollHint();
                mScrollHintDir = DragController.SCROLL_NONE;
            }
        }
    }

    private void showScrollHint(int direction, DragObject d) {
        // Show scroll hint on the right
        if (mScrollHintDir != direction) {
            mContent.showScrollHint(direction);
            mScrollHintDir = direction;
        }

        // Set alarm for when the hint is complete
        if (!mOnScrollHintAlarm.alarmPending() || mCurrentScrollDir != direction) {
            mCurrentScrollDir = direction;
            mOnScrollHintAlarm.cancelAlarm();
            mOnScrollHintAlarm.setOnAlarmListener(new OnScrollHintListener(d));
            mOnScrollHintAlarm.setAlarm(SCROLL_HINT_DURATION);

            mReorderAlarm.cancelAlarm();
            mTargetRank = mEmptyCellRank;
        }
    }

    OnAlarmListener mOnExitAlarmListener = new OnAlarmListener() {
        @Override
        public void onAlarm(Alarm alarm) {
            completeDragExit();
        }
    };

    public void completeDragExit() {
        if (mInfo.opened) {
            mLauncher.closeFolder();
            mRearrangeOnClose = true;
        } else if (mState == STATE_ANIMATING) {
            mRearrangeOnClose = true;
        } else {
            rearrangeChildren();
            clearDragInfo();
        }
    }

    private void clearDragInfo() {
        mCurrentDragView = null;
        mIsExternalDrag = false;
    }

    @Override
    public void onDragExit(DragObject d) {
        // We only close the folder if this is a true drag exit, ie. not because
        // a drop has occurred above the folder.
        if (!d.dragComplete) {
            mOnExitAlarm.setOnAlarmListener(mOnExitAlarmListener);
            mOnExitAlarm.setAlarm(ON_EXIT_CLOSE_DELAY);
        }
        mReorderAlarm.cancelAlarm();

        mOnScrollHintAlarm.cancelAlarm();
        mScrollPauseAlarm.cancelAlarm();
        if (mScrollHintDir != DragController.SCROLL_NONE) {
            mContent.clearScrollHint();
            mScrollHintDir = DragController.SCROLL_NONE;
        }
    }

    /**
     * When performing an accessibility drop, onDrop is sent immediately after onDragEnter. So we
     * need to complete all transient states based on timers.
     */
    @Override
    public void prepareAccessibilityDrop() {
        if (mReorderAlarm.alarmPending()) {
            mReorderAlarm.cancelAlarm();
            mReorderAlarmListener.onAlarm(mReorderAlarm);
        }
    }

    @Override
    public void onDropCompleted(final View target, final DragObject d,
                                final boolean isFlingToDelete, final boolean success) {
        if (mDeferDropAfterUninstall) {
            Log.d(TAG, "Deferred handling drop because waiting for uninstall.");
            mDeferredAction = new Runnable() {
                @Override
                public void run() {
                    onDropCompleted(target, d, isFlingToDelete, success);
                    mDeferredAction = null;
                }
            };
            return;
        }

        boolean beingCalledAfterUninstall = mDeferredAction != null;
        boolean successfulDrop =
                success && (!beingCalledAfterUninstall || mUninstallSuccessful);

        if (successfulDrop) {
            if (mDeleteFolderOnDropCompleted && !mItemAddedBackToSelfViaIcon && target != this) {
                replaceFolderWithFinalItem();
            }
        } else if (!mDragController.isDeferringDrag()) {
            // The drag failed, we need to return the item to the folder
            ShortcutInfo info = (ShortcutInfo) d.dragInfo;
            View icon = (mCurrentDragView != null && mCurrentDragView.getTag() == info)
                    ? mCurrentDragView : mContent.createNewView(info);
            ArrayList<View> views = getItemsInReadingOrder();
            views.add(info.rank, icon);
            mContent.arrangeChildren(views, views.size());
            mItemsInvalidated = true;

            try (SuppressInfoChanges s = new SuppressInfoChanges()) {
                mFolderIcon.onDrop(d);
            }
        }

        if (target != this) {
            if (mOnExitAlarm.alarmPending()) {
                mOnExitAlarm.cancelAlarm();
                if (!successfulDrop) {
                    mSuppressFolderDeletion = true;
                }
                mScrollPauseAlarm.cancelAlarm();
                completeDragExit();
            }
        }

        mDeleteFolderOnDropCompleted = false;
        mDragInProgress = false;
        mItemAddedBackToSelfViaIcon = false;
        mCurrentDragView = null;

        // Reordering may have occured, and we need to save the new item locations. We do this once
        // at the end to prevent unnecessary database operations.
        updateItemLocationsInDatabaseBatch();

        // Use the item count to check for multi-page as the folder UI may not have
        // been refreshed yet.
        if (getItemCount() <= mContent.itemsPerPage()) {
            // Show the animation, next time something is added to the folder.
            mInfo.setOption(FolderInfo.FLAG_MULTI_PAGE_ANIMATION, false, mLauncher);
        }

        if (!isFlingToDelete) {
            // Fling to delete already exits spring loaded mode after the animation finishes.
            mLauncher.exitSpringLoadedDragModeDelayed(successfulDrop,
                    Launcher.EXIT_SPRINGLOADED_MODE_SHORT_TIMEOUT, null);
        }
    }

    @Override
    public void deferCompleteDropAfterUninstallActivity() {
        mDeferDropAfterUninstall = true;
    }

    @Override
    public void onDragObjectRemoved(boolean success) {
        mDeferDropAfterUninstall = false;
        mUninstallSuccessful = success;
        if (mDeferredAction != null) {
            mDeferredAction.run();
        }
    }

    @Override
    public float getIntrinsicIconScaleFactor() {
        return 1f;
    }

    @Override
    public boolean supportsFlingToDelete() {
        return true;
    }

    @Override
    public boolean supportsAppInfoDropTarget() {
        return true;
    }

    @Override
    public boolean supportsDeleteDropTarget() {
        return true;
    }

    @Override
    public void onFlingToDelete(DragObject d, PointF vec) {
        // Do nothing
    }

    @Override
    public void onFlingToDeleteCompleted() {
        // Do nothing
    }

    private void updateItemLocationsInDatabaseBatch() {
        ArrayList<View> list = getItemsInReadingOrder();
        ArrayList<ItemInfo> items = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            View v = list.get(i);
            ItemInfo info = (ItemInfo) v.getTag();
            info.rank = i;
            items.add(info);
        }

        LauncherModel.moveItemsInDatabase(mLauncher, items, mInfo.id, 0);
    }

    public void notifyDrop() {
        if (mDragInProgress) {
            mItemAddedBackToSelfViaIcon = true;
        }
    }

    @Override
    public boolean isDropEnabled() {
        return true;
    }

    private void centerAboutIcon() {
        DeviceProfile grid = mLauncher.getDeviceProfile();

        DragLayer.LayoutParams lp = (DragLayer.LayoutParams) getLayoutParams();
        DragLayer parent = mLauncher.findViewById(R.id.drag_layer);
        int width = getPaddingLeft() + getPaddingRight() + mContent.getDesiredWidth();
        int height = getFolderHeight();

        parent.getDescendantRectRelativeToSelf(mFolderIcon, sTempRect);
        int centerX = sTempRect.centerX();
        int centerY = sTempRect.centerY();
        int centeredLeft = centerX - width / 2;
        int centeredTop = centerY - height / 2;

        // We need to bound the folder to the currently visible workspace area
        mLauncher.getWorkspace().getPageAreaRelativeToDragLayer(sTempRect);
        int left = Math.min(Math.max(sTempRect.left, centeredLeft),
                sTempRect.right - width);
        int top = Math.min(Math.max(sTempRect.top, centeredTop),
                sTempRect.bottom - height);

        int distFromEdgeOfScreen = mLauncher.getWorkspace().getPaddingLeft() + getPaddingLeft();

        if (grid.isPhone && (grid.availableWidthPx - width) < 4 * distFromEdgeOfScreen) {
            // Center the folder if it is very close to being centered anyway, by virtue of
            // filling the majority of the viewport. ie. remove it from the uncanny valley
            // of centeredness.
            left = (grid.availableWidthPx - width) / 2;
        } else if (width >= sTempRect.width()) {
            // If the folder doesn't fit within the bounds, center it about the desired bounds
            left = sTempRect.left + (sTempRect.width() - width) / 2;
        }
        if (height >= sTempRect.height()) {
            // Folder height is greater than page height, center on page
            top = sTempRect.top + (sTempRect.height() - height) / 2;
        } else {
            // Folder height is less than page height, so bound it to the absolute open folder
            // bounds if necessary
            Rect folderBounds = grid.getAbsoluteOpenFolderBounds();
            left = Math.max(folderBounds.left, Math.min(left, folderBounds.right - width));
            top = Math.max(folderBounds.top, Math.min(top, folderBounds.bottom - height));
        }

        int folderPivotX = width / 2 + (centeredLeft - left);
        int folderPivotY = height / 2 + (centeredTop - top);
        setPivotX(folderPivotX);
        setPivotY(folderPivotY);
        mFolderIconPivotX = (int) (mFolderIcon.getMeasuredWidth() *
                (1.0f * folderPivotX / width));
        mFolderIconPivotY = (int) (mFolderIcon.getMeasuredHeight() *
                (1.0f * folderPivotY / height));

        lp.width = width;
        lp.height = height;
        lp.x = left;
        lp.y = top;
    }

    public float getPivotXForIconAnimation() {
        return mFolderIconPivotX;
    }

    public float getPivotYForIconAnimation() {
        return mFolderIconPivotY;
    }

    private int getContentAreaHeight() {
        DeviceProfile grid = mLauncher.getDeviceProfile();
        int maxContentAreaHeight = grid.availableHeightPx
                - grid.getTotalWorkspacePadding().y - mFooterHeight;
        int height = Math.min(maxContentAreaHeight,
                mContent.getDesiredHeight());
        return Math.max(height, MIN_CONTENT_DIMEN);
    }

    private int getContentAreaWidth() {
        return Math.max(mContent.getDesiredWidth(), MIN_CONTENT_DIMEN);
    }

    private int getFolderHeight() {
        return getFolderHeight(getContentAreaHeight());
    }

    private int getFolderHeight(int contentAreaHeight) {
        return getPaddingTop() + getPaddingBottom() + contentAreaHeight + mFooterHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int contentWidth = getContentAreaWidth();
        int contentHeight = getContentAreaHeight();

        int contentAreaWidthSpec = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY);
        int contentAreaHeightSpec = MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.EXACTLY);

        mContent.setFixedSize(contentWidth, contentHeight);
        mContent.measure(contentAreaWidthSpec, contentAreaHeightSpec);

        if (mContent.getChildCount() > 0) {
            int cellIconGap = (mContent.getPageAt(0).getCellWidth()
                    - mLauncher.getDeviceProfile().iconSizePx) / 2;
            mFooter.setPadding(mContent.getPaddingLeft() + cellIconGap,
                    mFooter.getPaddingTop(),
                    mContent.getPaddingRight() + cellIconGap,
                    mFooter.getPaddingBottom());
        }
        mFooter.measure(contentAreaWidthSpec,
                MeasureSpec.makeMeasureSpec(mFooterHeight, MeasureSpec.EXACTLY));

        int folderWidth = getPaddingLeft() + getPaddingRight() + contentWidth;
        int folderHeight = getFolderHeight(contentHeight);
        setMeasuredDimension(folderWidth, folderHeight);
    }

    /**
     * Rearranges the children based on their rank.
     */
    public void rearrangeChildren() {
        rearrangeChildren(-1);
    }

    /**
     * Rearranges the children based on their rank.
     *
     * @param itemCount if greater than the total children count, empty spaces are left at the end,
     *                  otherwise it is ignored.
     */
    public void rearrangeChildren(int itemCount) {
        ArrayList<View> views = getItemsInReadingOrder();
        mContent.arrangeChildren(views, Math.max(itemCount, views.size()));
        mItemsInvalidated = true;
    }

    public int getItemCount() {
        return mContent.getItemCount();
    }

    @Thunk
    void replaceFolderWithFinalItem() {
        // Add the last remaining child to the workspace in place of the folder
        Runnable onCompleteRunnable = new Runnable() {
            @Override
            public void run() {
                int itemCount = mInfo.contents.size();
                if (itemCount <= 1) {
                    View newIcon = null;

                    if (itemCount == 1) {
                        // Move the item from the folder to the workspace, in the position of the
                        // folder
                        CellLayout cellLayout = mLauncher.getCellLayout(mInfo.container,
                                mInfo.screenId);
                        ShortcutInfo finalItem = mInfo.contents.remove(0);
                        newIcon = mLauncher.createShortcut(cellLayout, finalItem);
                        LauncherModel.addOrMoveItemInDatabase(mLauncher, finalItem, mInfo.container,
                                mInfo.screenId, mInfo.cellX, mInfo.cellY);
                    }

                    // Remove the folder
                    mLauncher.removeItem(mFolderIcon, mInfo, true /* deleteFromDb */);
                    if (mFolderIcon instanceof DropTarget) {
                        mDragController.removeDropTarget((DropTarget) mFolderIcon);
                    }

                    if (newIcon != null) {
                        // We add the child after removing the folder to prevent both from existing
                        // at the same time in the CellLayout.  We need to add the new item with
                        // addInScreenFromBind() to ensure that hotseat items are placed correctly.
                        mLauncher.getWorkspace().addInScreenFromBind(newIcon, mInfo.container,
                                mInfo.screenId, mInfo.cellX, mInfo.cellY, mInfo.spanX, mInfo.spanY);

                        // Focus the newly created child
                        newIcon.requestFocus();
                    }
                }
            }
        };
        View finalChild = mContent.getLastItem();
        if (finalChild != null) {
            mFolderIcon.performDestroyAnimation(finalChild, onCompleteRunnable);
        } else {
            onCompleteRunnable.run();
        }
        mDestroyed = true;
    }

    public boolean isDestroyed() {
        return mDestroyed;
    }

    // This method keeps track of the first and last item in the folder for the purposes
    // of keyboard focus
    public void updateTextViewFocus() {
        final View firstChild = mContent.getFirstItem();
        final View lastChild = mContent.getLastItem();
        if (firstChild != null && lastChild != null) {
            mFolderName.setNextFocusDownId(lastChild.getId());
            mFolderName.setNextFocusRightId(lastChild.getId());
            mFolderName.setNextFocusLeftId(lastChild.getId());
            mFolderName.setNextFocusUpId(lastChild.getId());
            // Hitting TAB from the folder name wraps around to the first item on the current
            // folder page, and hitting SHIFT+TAB from that item wraps back to the folder name.
            mFolderName.setNextFocusForwardId(firstChild.getId());
            // When clicking off the folder when editing the name, this Folder gains focus. When
            // pressing an arrow key from that state, give the focus to the first item.
            this.setNextFocusDownId(firstChild.getId());
            this.setNextFocusRightId(firstChild.getId());
            this.setNextFocusLeftId(firstChild.getId());
            this.setNextFocusUpId(firstChild.getId());
            // When pressing shift+tab in the above state, give the focus to the last item.
            setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    boolean isShiftPlusTab = keyCode == KeyEvent.KEYCODE_TAB &&
                            event.hasModifiers(KeyEvent.META_SHIFT_ON);
                    return isShiftPlusTab && Folder.this.isFocused() && lastChild.requestFocus();
                }
            });
        }
    }

    @Override
    public void onDrop(DragObject d) {
        Runnable cleanUpRunnable = null;

        // If we are coming from All Apps space, we defer removing the extra empty screen
        // until the folder closes
        if (d.dragSource != mLauncher.getWorkspace() && !(d.dragSource instanceof Folder)) {
            cleanUpRunnable = new Runnable() {
                @Override
                public void run() {
                    mLauncher.exitSpringLoadedDragModeDelayed(true,
                            Launcher.EXIT_SPRINGLOADED_MODE_SHORT_TIMEOUT,
                            null);
                }
            };
        }

        // If the icon was dropped while the page was being scrolled, we need to compute
        // the target location again such that the icon is placed of the final page.
        if (!mContent.rankOnCurrentPage(mEmptyCellRank)) {
            // Reorder again.
            mTargetRank = getTargetRank(d, null);

            // Rearrange items immediately.
            mReorderAlarmListener.onAlarm(mReorderAlarm);

            mOnScrollHintAlarm.cancelAlarm();
            mScrollPauseAlarm.cancelAlarm();
        }
        mContent.completePendingPageChanges();

        View currentDragView;
        final ShortcutInfo si;
        if (d.dragInfo instanceof AppInfo) {
            // Came from all apps -- make a copy.
            si = ((AppInfo) d.dragInfo).makeShortcut();
        } else {
            // ShortcutInfo
            si = (ShortcutInfo) d.dragInfo;
        }
        if (mIsExternalDrag) {
            currentDragView = mContent.createAndAddViewForRank(si, mEmptyCellRank);
            // Actually move the item in the database if it was an external drag. Call this
            // before creating the view, so that ShortcutInfo is updated appropriately.
            LauncherModel.addOrMoveItemInDatabase(
                    mLauncher, si, mInfo.id, 0, si.cellX, si.cellY);

            // We only need to update the locations if it doesn't get handled in #onDropCompleted.
            if (d.dragSource != this) {
                updateItemLocationsInDatabaseBatch();
            }
            mIsExternalDrag = false;
        } else {
            currentDragView = mCurrentDragView;
            if (!mDragController.isDeferringDrag()) {
                mContent.addViewForRank(currentDragView, si, mEmptyCellRank);
            }
        }

        if (d.dragView.hasDrawn()) {

            // Temporarily reset the scale such that the animation target gets calculated correctly.
            float scaleX = getScaleX();
            float scaleY = getScaleY();
            setScaleX(1.0f);
            setScaleY(1.0f);
            mLauncher.getDragLayer().animateViewIntoPosition(d.dragView, currentDragView,
                    cleanUpRunnable, null);
            setScaleX(scaleX);
            setScaleY(scaleY);
        } else {
            d.deferDragViewCleanupPostAnimation = false;
            currentDragView.setVisibility(VISIBLE);
        }
        mItemsInvalidated = true;
        rearrangeChildren();

        if (!mDragController.isDeferringDrag()) {
            // Temporarily suppress the listener, as we did all the work already here.
            try (SuppressInfoChanges s = new SuppressInfoChanges()) {
                mInfo.add(si, false);
            }
        }

        // Clear the drag info, as it is no longer being dragged.
        mDragInProgress = false;

        if (mContent.getPageCount() > 1) {
            // The animation has already been shown while opening the folder.
            mInfo.setOption(FolderInfo.FLAG_MULTI_PAGE_ANIMATION, true, mLauncher);
        }

        if (d.stateAnnouncer != null) {
            d.stateAnnouncer.completeAction(R.string.item_moved);
        }
    }

    // This is used so the item doesn't immediately appear in the folder when added. In one case
    // we need to create the illusion that the item isn't added back to the folder yet, to
    // to correspond to the animation of the icon back into the folder. This is
    public void hideItem(ShortcutInfo info) {
        View v = getViewForInfo(info);
        v.setVisibility(INVISIBLE);
    }

    public void showItem(ShortcutInfo info) {
        View v = getViewForInfo(info);
        v.setVisibility(VISIBLE);
    }

    @Override
    public void onAdd(ShortcutInfo item) {
        mContent.createAndAddViewForRank(item, mContent.allocateRankForNewItem());
        mItemsInvalidated = true;
        LauncherModel.addOrMoveItemInDatabase(
                mLauncher, item, mInfo.id, 0, item.cellX, item.cellY);
    }

    @Override
    public void onRemove(ShortcutInfo item) {
        mItemsInvalidated = true;
        View v = getViewForInfo(item);
        mContent.removeItem(v);
        if (mState == STATE_ANIMATING) {
            mRearrangeOnClose = true;
        } else {
            rearrangeChildren();
        }
        if (getItemCount() <= 1) {
            if (mInfo.opened) {
                mLauncher.closeFolder(this, true);
            } else {
                replaceFolderWithFinalItem();
            }
        }
    }

    private View getViewForInfo(final ShortcutInfo item) {
        return mContent.iterateOverItems(new ItemOperator() {

            @Override
            public boolean evaluate(ItemInfo info, View view) {
                return info == item;
            }
        });
    }

    @Override
    public void onItemsChanged(boolean animate) {
        updateTextViewFocus();
    }

    @Override
    public void onTitleChanged(CharSequence title) {
    }

    public ArrayList<View> getItemsInReadingOrder() {
        if (mItemsInvalidated) {
            mItemsInReadingOrder.clear();
            mContent.iterateOverItems(new ItemOperator() {

                @Override
                public boolean evaluate(ItemInfo info, View view) {
                    mItemsInReadingOrder.add(view);
                    return false;
                }
            });
            mItemsInvalidated = false;
        }
        return mItemsInReadingOrder;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mFolderName) {
            if (hasFocus) {
                startEditingFolderName();
            } else {
                dismissEditingName();
            }
        }
    }

    @Override
    public void getHitRectRelativeToDragLayer(Rect outRect) {
        getHitRect(outRect);
        outRect.left -= mScrollAreaOffset;
        outRect.right += mScrollAreaOffset;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBlurDrawable != null)
            mBlurDrawable.stopListening();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mBlurDrawable == null) return;
        mBlurDrawable.setOverscroll(l);
        mBlurDrawable.setTranslation(t);
        mBlurDrawable.invalidateSelf();
    }

    private class OnScrollHintListener implements OnAlarmListener {

        private final DragObject mDragObject;

        OnScrollHintListener(DragObject object) {
            mDragObject = object;
        }

        /**
         * Scroll hint has been shown long enough. Now scroll to appropriate page.
         */
        @Override
        public void onAlarm(Alarm alarm) {
            if (mCurrentScrollDir == DragController.SCROLL_LEFT) {
                mContent.scrollLeft();
                mScrollHintDir = DragController.SCROLL_NONE;
            } else if (mCurrentScrollDir == DragController.SCROLL_RIGHT) {
                mContent.scrollRight();
                mScrollHintDir = DragController.SCROLL_NONE;
            } else {
                // This should not happen
                return;
            }
            mCurrentScrollDir = DragController.SCROLL_NONE;

            // Pause drag event until the scrolling is finished
            mScrollPauseAlarm.setOnAlarmListener(new OnScrollFinishedListener(mDragObject));
            mScrollPauseAlarm.setAlarm(DragController.RESCROLL_DELAY);
        }
    }

    private class OnScrollFinishedListener implements OnAlarmListener {

        private final DragObject mDragObject;

        OnScrollFinishedListener(DragObject object) {
            mDragObject = object;
        }

        /**
         * Page scroll is complete.
         */
        @Override
        public void onAlarm(Alarm alarm) {
            // Reorder immediately on page change.
            onDragOver(mDragObject);
        }
    }

    // Compares item position based on rank and position giving priority to the rank.
    public static final Comparator<ItemInfo> ITEM_POS_COMPARATOR = new Comparator<ItemInfo>() {

        @Override
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            if (lhs.rank != rhs.rank) {
                return lhs.rank - rhs.rank;
            } else if (lhs.cellY != rhs.cellY) {
                return lhs.cellY - rhs.cellY;
            } else {
                return lhs.cellX - rhs.cellX;
            }
        }
    };

    /**
     * Temporary resource held while we don't want to handle info changes
     */
    private class SuppressInfoChanges implements AutoCloseable {

        SuppressInfoChanges() {
            mInfo.removeListener(Folder.this);
        }

        @Override
        public void close() {
            mInfo.addListener(Folder.this);
            updateTextViewFocus();
        }
    }
}
