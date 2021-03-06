/**
 * AddState4
 *
 * @author 4IF-4114
 */
package controller;

import model.CityMap;
import model.Intersection;
import model.PointOfInterest;
import view.Window;

/**
 * AddState4, a state used when the user want to select the point of interest after which the new delivery point will be added
 */
public class AddState4 implements State {
    private Intersection i1;
    private Integer d1;
    private PointOfInterest p1;
    private Intersection i2;
    private Integer d2;

    public void leftClick(Controller controller, Window window, CityMap map, ListOfCommands listOfCommands, Intersection i, PointOfInterest poi) {
        if (poi != null) {
            try {
                listOfCommands.add(new AddCommand(map, this.i1,this.d1, this.p1, this.i2,this.d2, poi));
            } catch (Exception e) {
                window.parsingError(e.getMessage());
            }
            controller.getCityMap().resetSelected();
            controller.setCurrentState(controller.TOUR_STATE);
            window.enableJtextField(false);
            window.displayMessage("");
        } else {
            window.parsingError("No point error: Click on a valid point.");

        }

    }
    @Override
    public void rightClick(Controller c){
        c.getCityMap().resetSelected();
        c.setCurrentState(c.TOUR_STATE);
    }

    public void entryAction(Intersection i1,Integer d1 ,PointOfInterest p, Intersection i2,Integer d2) {
        this.i1 = i1;
        this.d1=d1;
        this.p1 = p;
        this.i2 = i2;
        this.d2=d2;
    }
}
