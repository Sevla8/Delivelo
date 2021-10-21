package controller;

import org.xml.sax.SAXException;
import view.Window;
import xml.XMLDeserializer;
import xml.XMLException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * @author 4IF-4114
 */
public class CityMapState1 implements State {

    /**
     * Default constructor
     */
    public CityMapState1() {
    }

    public void loadDistribution(Controller c, Window w) throws XMLException, ParserConfigurationException, IOException, SAXException {
        XMLDeserializer.loadDistribution(c.getCitymap());
        c.setCurrentState(c.citymapState2);
    }

}