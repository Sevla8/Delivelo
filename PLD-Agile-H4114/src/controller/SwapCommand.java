package controller;

import model.CityMap;
import model.DeliveryAddress;
import model.PickupAddress;
import model.PointOfInterest;

/**
 * @author 4IF-4114
 */
public class SwapCommand implements Command {

    private CityMap map;
    private int sens;
    private int position;
    private PointOfInterest poi;
    private boolean authorized;

    /**
     * Default constructor
     */


    public SwapCommand(CityMap map, PointOfInterest poi, int sens) {
        this.map = map;
        this.poi = poi;
        this.sens = sens;
        this.position = map.tour.getPointOfInterests().indexOf(poi);
        if (position == 0 || position == map.tour.getPointOfInterests().size() - 1 ||
                (poi instanceof PickupAddress && map.tour.getPointOfInterests().get(position+sens)==map.distribution.getDelivery((PickupAddress) poi)) ||
                (poi instanceof DeliveryAddress && map.tour.getPointOfInterests().get(position+sens)==map.distribution.getPickup((DeliveryAddress) poi))) {
            authorized = false;
        } else {
            authorized = true;
        }
    }


    /**
     * @return
     */
    public void doCommand() {
        if (authorized)
            map.changePosition(poi, position + sens);
    }

    /**
     * @return
     */
    public void undoCommand() {
        if (authorized)
            map.changePosition(poi, position);
    }
}
