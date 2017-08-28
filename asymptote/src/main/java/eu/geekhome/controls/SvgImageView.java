package eu.geekhome.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.anddev.andengine.extension.svg.SVGParser;
import org.anddev.andengine.extension.svg.adt.SVG;

import eu.geekhome.asymptote.R;

public class SvgImageView extends ImageView {
    private String _svgRawName;
    private int _svgColor = -1;
    private int _svgResourceId = -1;
    private boolean _isInitialized = false;

    @SuppressWarnings({"UnusedDeclaration"})
    public SvgImageView(Context context) {
        super(context);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public SvgImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttributes(context, attrs);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public SvgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.svgimageview, 0, 0);
            _svgResourceId = a.getResourceId(R.styleable.svgimageview_svgId, -1);
            _svgColor = a.getColor(R.styleable.svgimageview_svgColor, getResources().getColor(R.color.white));
            _svgRawName = a.getString(R.styleable.svgimageview_svgRawName);
            a.recycle();
        }
    }

    private void initialize(int svgWidth, int svgHeight) {
        if (_svgResourceId == -1) {
            _svgResourceId = getResources().getIdentifier(_svgRawName, "raw", getContext().getPackageName());
        }

        if (_svgResourceId != 0 && svgHeight >0 && svgWidth > 0) {
            SVG svg = SVGParser.parseSVGFromResource(getResources(), _svgResourceId);
            Bitmap bitmap = getIcon(svg, svgWidth,svgHeight);
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            setImageDrawable(bd);
            if (_svgColor != -1) {
                setColorFilter(_svgColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void refresh() {
        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            initialize(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean hasIconReference = _svgResourceId != -1 || _svgRawName != null;
        if (!_isInitialized && hasIconReference) {
            initialize(getMeasuredWidth(), getMeasuredHeight());
            _isInitialized = true;
        }
        super.onDraw(canvas);
    }

    @Override
    public Drawable getDrawable() {
        return super.getDrawable();
    }

    private Bitmap getIcon(SVG svg, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Rect dst = new Rect(0, 0, width, height);
        c.drawPicture(svg.getPicture(), dst);

        return Bitmap.createBitmap(bmp);
    }

    public void setSvgId(int svgId) {
        _svgResourceId = svgId;
        refresh();
    }

    public void setSvgColor(@ColorRes int svgColorId) {
        _svgColor = svgColorId;
        if (_svgColor != -1) {
            setColorFilter(_svgColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setSvgRawName(String rawName) {
        _svgRawName = rawName;
    }
}