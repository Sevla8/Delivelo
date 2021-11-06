package view;

import model.*;

import observer.Observable;
import observer.Observer;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.Duration;
import  java.util.*;
import java.util.List;

/**
 * @author 4IF-4114
 */
public class RoadmapView extends JPanel implements Observer {

    private Tour tour;
    private Distribution distribution;
    private final int VIEW_HEIGHT = 760;
    private final int VIEW_WIDTH = 300;
    private JButton addButton;
    private JButton delButton;
    private final int BUTTON_HEIGHT = 30;
    private final int BUTTON_WIDTH = 100;
    private JPanel roadmap;
    private boolean start = true;
    private int arrivalTime;

    /**
     * Default constructor
     * @param tour
     * @param window
     */
    public RoadmapView(CityMap citymap, Window window) {
        super();

        this.tour = citymap.getTour();
        this.tour.addObserver(this); // this observes tour
        this.distribution = citymap.getDistribution();
        this.distribution.addObserver(this); // this observes distribution

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setSize(VIEW_WIDTH, VIEW_HEIGHT);

        GridLayout grid = new GridLayout(0,1);
        grid.setVgap(10);

        this.roadmap = new JPanel(grid);
        this.roadmap.setBackground(Color.WHITE);
        JScrollPane scrollPanel = new JScrollPane(this.roadmap,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPanel);
        scrollPanel.setBackground(Color.BLACK);
        scrollPanel.setSize(0, VIEW_HEIGHT - this.BUTTON_HEIGHT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.lightGray);
        delButton = new JButton("Remove");
        addButton = new JButton("Add");
        delButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        buttonPanel.add(this.addButton);
        buttonPanel.add(this.delButton);

        this.add(scrollPanel);
        this.add(buttonPanel);

        window.getContentPane().add(this);
    }


    public void update(Observable observed, Object object) {
        System.out.println("Roadmap/update");
        System.out.println(this.tour.getPointOfInterests().size());
        System.out.println(this.tour.getPaths().size());
        System.out.println(this.distribution.getRequests().size());

        this.start = true;
        int i = 0;
        this.arrivalTime = this.distribution.getDepot().getDepartureTime().toSecondOfDay();

        this.roadmap.removeAll();

        if (this.tour.getPointOfInterests().size() == 0) { // Load distribution

            Set<Request> requestList = this.distribution.getRequests();
            if(requestList.size() != 0 ) {
                addRequestToRoadmap(requestList);
            }

        }
        else { //Compute Tour

            List<PointOfInterest> pointList = this.tour.getPointOfInterests();
            addPointOfInterestToRoadMap(pointList);

        }

        this.revalidate();
        this.repaint();
    }

    public int getViewHeight() {
        return VIEW_HEIGHT;
    }

    public int getViewWidth() {
        return VIEW_WIDTH;
    }

    public void addRequestToRoadmap(Set<Request> requestList){ //Add Request to roadmap in order
        int number = 0;

        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.Y_AXIS));
        firstPanel.setBackground(Color.lightGray);
        firstPanel.setBorder(BorderFactory.createTitledBorder("Starting point"));

        int hours = arrivalTime / 3600;
        int minutes = (arrivalTime % 3600) / 60;
        int seconds = arrivalTime % 60;

        firstPanel.add(new JLabel("    Departure Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds)));
        firstPanel.add(new JLabel("    Latitude: "+ this.distribution.getDepot().getIntersection().getLatitude()));
        firstPanel.add(new JLabel("    Longitude: "+ this.distribution.getDepot().getIntersection().getLongitude()));

        roadmap.add(firstPanel);

        for (Request request : requestList) {
            number++;
            System.out.println(request);

            JPanel subPanel1 = new JPanel();
            subPanel1.setLayout(new BoxLayout(subPanel1, BoxLayout.Y_AXIS));
            subPanel1.setBackground(Color.LIGHT_GRAY);
            subPanel1.setBorder(BorderFactory.createTitledBorder("Pickup Point #"+ number ));

            JPanel subPanel2 = new JPanel();
            subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
            subPanel2.setBackground(Color.LIGHT_GRAY);
            subPanel2.setBorder(BorderFactory.createTitledBorder("Delivery Point #"+ number ));

            subPanel1.add(new JLabel("    Latitude: " + request.getPickup().getIntersection().getLatitude()));
            subPanel1.add(new JLabel("    Longitude: " + request.getPickup().getIntersection().getLongitude()));
            subPanel2.add(new JLabel("    Latitude: " + request.getDelivery().getIntersection().getLatitude()));
            subPanel2.add(new JLabel("    Longitude: " + request.getDelivery().getIntersection().getLongitude()));

            roadmap.add(subPanel1);
            roadmap.add(subPanel2);

        }
    }

    public void addPointOfInterestToRoadMap(List<PointOfInterest> pointList) {

        List<Path> pathList = this.tour.getPaths();

        for (int poiNum = 0; poiNum < pointList.size(); poiNum++) {
            PointOfInterest poi = pointList.get(poiNum);

            JPanel subPanel = new JPanel();
            subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
            subPanel.setBackground(Color.WHITE);

            if (poi.getIdPointOfInterest() == 0) { // Depot
                if (this.start) {
                    subPanel.setBorder(BorderFactory.createTitledBorder("START"));
                } else {
                    subPanel.setBorder(BorderFactory.createTitledBorder("END"));
                }
            } else {
                if (poi instanceof DeliveryAddress) {
                    subPanel.setBorder(BorderFactory.createTitledBorder("Delivery Point #" + poi.getIdPointOfInterest()));
                } else if (poi instanceof PickupAddress) {
                    subPanel.setBorder(BorderFactory.createTitledBorder("Pickup Point #" + poi.getIdPointOfInterest()));
                }
            }

            int durationRoad=0;


            if(poiNum<pointList.size()-1) {
                Path path = (Path) (pathList.get(poiNum));
                for (Road road : path.getRoads()) {
                    durationRoad += (int) (road.getLength() / 15000. * 3600.);
                }
            }

            arrivalTime += poi.getDuration();
            int hours = arrivalTime / 3600;
            int minutes = (arrivalTime % 3600) / 60;
            int seconds = arrivalTime % 60;


            subPanel.add(new JLabel("    Latitude: " + poi.getIntersection().getLatitude()));
            subPanel.add(new JLabel("    Longitude: " + poi.getIntersection().getLongitude()));
            subPanel.add(new JLabel("    Duration: " + poi.getDuration() + " seconds"));
            if(this.start){
                subPanel.add(new JLabel("    Departure Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds)));
            }else {
                subPanel.add(new JLabel("    Arrival Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds)));
            }

            if(this.start){
                this.start = false;
            }


            arrivalTime +=durationRoad;
            roadmap.add(subPanel);

        }
    }


}

//Below is the code we used for adding roads on the roadmap : Keeping it for future use

/*                  //Add roads
                    Path path = this.tour.getPaths().get(i);
                    int duration = 0;
                    double length = 0;
                    String name;
                    int nbIntersection = 0;

                    for (int j = 0; j < path.getRoads().size(); ++j) {
                        duration += (int) (path.getRoads().get(j).getLength() / 15000. * 3600.);
                        length += path.getRoads().get(j).getLength();
                        name = path.getRoads().get(j).getName();
                        nbIntersection += 1;

                        if (j+1 < path.getRoads().size() && name.equals(path.getRoads().get(j+1).getName())) {
                            continue;
                        }

                        JPanel subPanel2 = new JPanel();
                        subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
                        subPanel2.setBackground(Color.PINK);
                        subPanel2.setLayout(new GridLayout(0, 1));
                        subPanel2.setBorder(BorderFactory.createTitledBorder("\uD83D\uDEB2"));

                        subPanel2.add(new JLabel(" via " + name));
                        int minutes = (duration / 60);
                        int seconds = (duration % 60);
                        if (minutes > 0) {
                            subPanel2.add(new JLabel(" for " + minutes + "min" + seconds + "s (" + String.format("%,.0f", length)+ " m) " + nbIntersection + " intersections"));
                        }else {
                            subPanel2.add(new JLabel(" for " + seconds + "s (" + String.format("%,.0f", length)+ " m) " + nbIntersection + " intersections"));
                        }

                        panel.add(subPanel2);

                        duration = 0;
                        length = 0;
                        nbIntersection = 0;
                    }
                    i += 1;
*/