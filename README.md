# AndroidScreenshot

![Alt Text](https://media.giphy.com/media/frAXxP3JtHRynjvQwL/giphy.gif)

### Take Screenshot for a View

we are going to learn how to take screenshot programmatically in android and store it in storage as well.

        public Bitmap takeScreenshotForView(View view) {
            view.measure(MeasureSpec.makeMeasureSpec(view.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(view.getHeight(), MeasureSpec.EXACTLY));
            view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(), (int) view.getY() + view.getMeasuredHeight());
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return bitmap;
         }
       
## Thanks

Permission Check : https://github.com/Karumi/Dexter
