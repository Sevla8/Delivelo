import model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class TourModificationTest {
    public static CityMap cm;


    @BeforeClass
    public static void beforeClass() {
        cm = new CityMap();
    }

    @Before
    public void setUp() throws Exception {
        try{
            XMLDeserializerForTests.loadCityMap(cm,"src/test/testRessources/RouffiacCityMap.xml");
            XMLDeserializerForTests.loadDistribution(cm,"src/test/testRessources/DeliveryBasic.xml");
        }catch (Exception e){
            System.err.println("Erreur lors de la lecture du fichier");
        }
        cm.computeTour();
    }

    @Test
    public void addRequestTest(){
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();

        Intersection i1 = cm.getIntersections().get("811508");
        PickupAddress p1 = new PickupAddress(i1,120,5);
        Intersection i2 = cm.getIntersections().get("811509");
        DeliveryAddress p2 = new DeliveryAddress(i2,120,6);

        try{
            cm.addRequest(p1,listPOI.get(3),p2, listPOI.get(4));
            cm.distribution.addRequest(p1,p2,p1.getIdPointOfInterest());
        }catch (Exception e){
            fail();
        }
        listPOI = cm.getTour().getPointOfInterests();

        assertEquals(8,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 5,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(5).getIdPointOfInterest());
        assertEquals((Integer) 6,listPOI.get(6).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(7).getIdPointOfInterest());

    }

    @Test
    public void addRequestAdjacentTest(){
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();

        Intersection i1 = cm.getIntersections().get("811508");
        PickupAddress p1 = new PickupAddress(i1,120,5);
        Intersection i2 = cm.getIntersections().get("811509");
        DeliveryAddress p2 = new DeliveryAddress(i2,120,6);

        try{
            cm.addRequest(p1,listPOI.get(3),p2, listPOI.get(3));
            cm.distribution.addRequest(p1,p2,p1.getIdPointOfInterest());
        }catch (Exception e){
            fail();
        }
        listPOI = cm.getTour().getPointOfInterests();

        assertEquals(8,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 5,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 6,listPOI.get(5).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(6).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(7).getIdPointOfInterest());
    }

    @Test
    public void addRequestExceptionTest(){
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();

        Intersection i1 = cm.getIntersections().get("811508");
        PickupAddress p1 = new PickupAddress(i1,120,5);
        Intersection i2 = cm.getIntersections().get("811509");
        DeliveryAddress p2 = new DeliveryAddress(i2,120,6);

        assertThrows(Exception.class,() -> cm.addRequest(p1,listPOI.get(3),p2, listPOI.get(5)));
        assertThrows(Exception.class,() -> cm.addRequest(p1,listPOI.get(3),p2, listPOI.get(1)));



    }

    @Test
    public void removeExistingRequestTest(){
        PickupAddress pickupAddress = (PickupAddress) cm.getTour().getPointOfInterests().get(1);
        DeliveryAddress deliveryAddress = (DeliveryAddress) cm.getTour().getPointOfInterests().get(2);

        cm.removeRequest(pickupAddress,deliveryAddress);

        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();
        assertEquals(4,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(3).getIdPointOfInterest());

        pickupAddress = (PickupAddress) listPOI.get(1);
        deliveryAddress = (DeliveryAddress) listPOI.get(2);

        cm.removeRequest(pickupAddress,deliveryAddress);

        listPOI = cm.getTour().getPointOfInterests();

        assertEquals(2,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(1).getIdPointOfInterest());

    }

    @Test
    public void removeAddedRequestTest(){
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();

        Intersection i1 = cm.getIntersections().get("811508");
        PickupAddress p1 = new PickupAddress(i1,120,5);
        Intersection i2 = cm.getIntersections().get("811509");
        DeliveryAddress p2 = new DeliveryAddress(i2,120,6);

        try{
            cm.addRequest(p1,listPOI.get(3),p2, listPOI.get(4));
            cm.distribution.addRequest(p1,p2,p1.getIdPointOfInterest());
        }catch (Exception e){
            fail();
        }

        listPOI = cm.getTour().getPointOfInterests();
        cm.removeRequest((PickupAddress) listPOI.get(4),(DeliveryAddress) listPOI.get(6));
        listPOI = cm.getTour().getPointOfInterests();

        assertEquals(6,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(5).getIdPointOfInterest());

    }

    @Test
    public void wrongRemoveRequestTest(){
        PickupAddress pickupAddress = (PickupAddress) cm.getTour().getPointOfInterests().get(1);
        DeliveryAddress deliveryAddress = (DeliveryAddress) cm.getTour().getPointOfInterests().get(4);

        cm.removeRequest(pickupAddress,deliveryAddress);
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();
        assertEquals(6,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(5).getIdPointOfInterest());

        Intersection i1 = cm.getIntersections().get("811508");
        PickupAddress p1 = new PickupAddress(i1,60,5);
        Intersection i2 = cm.getIntersections().get("811509");
        DeliveryAddress p2 = new DeliveryAddress(i2,120,6);

        cm.removeRequest(p1,p2);

    }

    @Test
    public void changePositionTest(){
        PointOfInterest poi = cm.getTour().getPointOfInterests().get(3);
        cm.changePosition(poi,1);

        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();
        assertEquals(6,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(5).getIdPointOfInterest());

    }

    @Test
    public void changePositionNonExistingTest(){
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();
        Intersection i1 = cm.getIntersections().get("811508");
        PointOfInterest p1 = new PointOfInterest(i1,5);

        assertThrows(Exception.class, ()->cm.changePosition(p1,2));


    }

    @Test
    public void changePositionDeliveryBeforePickupTest(){
        PointOfInterest poi = cm.getTour().getPointOfInterests().get(4);

        cm.changePosition(poi,3);
        List<PointOfInterest> listPOI = cm.getTour().getPointOfInterests();


        assertEquals(6,listPOI.size());
        assertEquals((Integer) 0,listPOI.get(0).getIdPointOfInterest());
        assertEquals((Integer) 1,listPOI.get(1).getIdPointOfInterest());
        assertEquals((Integer) 2,listPOI.get(2).getIdPointOfInterest());
        assertEquals((Integer) 3,listPOI.get(3).getIdPointOfInterest());
        assertEquals((Integer) 4,listPOI.get(4).getIdPointOfInterest());
        assertEquals((Integer) 0,listPOI.get(5).getIdPointOfInterest());
    }
}
