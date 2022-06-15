import Toybox.Graphics;
import Toybox.WatchUi;

(:glance)
class SampleWidgetGlanceView extends WatchUi.GlanceView{
  function initialize() {
    GlanceView.initialize();
  }
    
    function onUpdate(dc as Dc) {    
      dc.setColor(Graphics.COLOR_BLUE, Graphics.COLOR_TRANSPARENT);
    	//dc.drawRectangle(0, 0, dc.getWidth() * 0.75, dc.getHeight() * 0.75);
      dc.drawText(0, 15, Graphics.FONT_MEDIUM, "Sample", Graphics.TEXT_JUSTIFY_LEFT);
    }
}