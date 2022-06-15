import Toybox.Activity;
import Toybox.Lang;
import Toybox.Time;
import Toybox.WatchUi;
import Toybox.FitContributor;

class FitContributorTestView extends WatchUi.SimpleDataField {

    var bananasEarnedField = null;
    var totalBananas = 0.0;

    const CALORIES_PER_BANANA = 105.0;
    const BANANAS_FIELD_ID = 0;

    // Set the label of the data field here.
    function initialize() {
        SimpleDataField.initialize();
        
        // Create the custom FIT data field we want to record.
        bananasEarnedField = createField(
            "bananas_earned",
            BANANAS_FIELD_ID,
            FitContributor.DATA_TYPE_FLOAT,
            {:mesgType=>FitContributor.MESG_TYPE_RECORD, :units=>"B"}
        );
        bananasEarnedField.setData(0.0);
    }

    // The given info object contains all the current workout
    // information. Calculate a value and return it in this method.
    // Note that compute() and onUpdate() are asynchronous, and there is no
    // guarantee that compute() will be called before onUpdate().
    function compute(info as Activity.Info) as Numeric or Duration or String or Null {
        if (info != null && info.calories != null) {
            // Calculate and set data to be written to the Field
            totalBananas = (info.calories / CALORIES_PER_BANANA).toFloat();
            bananasEarnedField.setData(totalBananas);
        }
        // Display the data on the screen of the device
        return totalBananas;
    }

}