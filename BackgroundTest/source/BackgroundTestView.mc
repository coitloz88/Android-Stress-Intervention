import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.background;
import Toybox.Time;

class BackgroundTestView extends WatchUi.View {

    const FIVE_MINUTES = new Time.Duration(5 * 60);
    var eventTime = Time.now().add(FIVE_MINUTES);

    function initialize() {
        View.initialize();
        Background.registerForTemporalEvent(eventTime);
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
