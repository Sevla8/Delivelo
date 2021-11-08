package controller;

import org.xml.sax.SAXException;
import view.Window;
import filecontrol.XMLDeserializer;
import filecontrol.XMLException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * @author 4IF-4114
 */
public class InitialState implements State {

    /**
     * Default constructor
     */
    public InitialState() {
    }

    public void loadMap(Controller c, Window w) throws XMLException, ParserConfigurationException, IOException, SAXException {
        XMLDeserializer.loadCityMap(c.getCitymap());
        c.setCurrentState(c.citymapState);
    }

}