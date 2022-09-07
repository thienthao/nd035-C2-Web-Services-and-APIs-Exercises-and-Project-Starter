package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;
    private static Car car;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @BeforeEach
    public void setup() {
        car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     *
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        mvc.perform(
                        post(new URI("/cars"))
                                .content(json.write(car).getJson())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        mvc.perform(get(new URI("/cars")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.carList[0].id")
                        .value(1L))
                .andExpect(jsonPath("$._embedded.carList[0].condition")
                        .value(car.getCondition().toString()))
                .andExpect(jsonPath("$._embedded.carList[0].details.body")
                        .value(car.getDetails().getBody()))
                .andExpect(jsonPath("$._embedded.carList[0].details.model")
                        .value(car.getDetails().getModel()))
                .andExpect(jsonPath("$._embedded.carList[0].details.manufacturer.code")
                        .value(car.getDetails().getManufacturer().getCode()))
                .andExpect(jsonPath("$._embedded.carList[0].details.manufacturer.name")
                        .value(car.getDetails().getManufacturer().getName()))
                .andExpect(jsonPath("$._embedded.carList[0].details.numberOfDoors")
                        .value(car.getDetails().getNumberOfDoors()))
                .andExpect(jsonPath("$._embedded.carList[0].details.fuelType")
                        .value(car.getDetails().getFuelType()))
                .andExpect(jsonPath("$._embedded.carList[0].details.engine")
                        .value(car.getDetails().getEngine()))
                .andExpect(jsonPath("$._embedded.carList[0].details.mileage")
                        .value(car.getDetails().getMileage()))
                .andExpect(jsonPath("$._embedded.carList[0].details.modelYear")
                        .value(car.getDetails().getModelYear()))
                .andExpect(jsonPath("$._embedded.carList[0].details.productionYear")
                        .value(car.getDetails().getProductionYear()))
                .andExpect(jsonPath("$._embedded.carList[0].details.externalColor")
                        .value(car.getDetails().getExternalColor()))
                .andReturn();
    }

    /**
     * Tests the read operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        mvc.perform(get(new URI("/cars/1")))
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("condition")
                        .value(car.getCondition().toString()))
                .andExpect(jsonPath("details.body")
                        .value(car.getDetails().getBody()))
                .andExpect(jsonPath("details.model")
                        .value(car.getDetails().getModel()))
                .andExpect(jsonPath("details.manufacturer.code")
                        .value(car.getDetails().getManufacturer().getCode()))
                .andExpect(jsonPath("details.manufacturer.name")
                        .value(car.getDetails().getManufacturer().getName()))
                .andExpect(jsonPath("details.numberOfDoors")
                        .value(car.getDetails().getNumberOfDoors()))
                .andExpect(jsonPath("details.fuelType")
                        .value(car.getDetails().getFuelType()))
                .andExpect(jsonPath("details.engine")
                        .value(car.getDetails().getEngine()))
                .andExpect(jsonPath("details.mileage")
                        .value(car.getDetails().getMileage()))
                .andExpect(jsonPath("details.modelYear")
                        .value(car.getDetails().getModelYear()))
                .andExpect(jsonPath("details.productionYear")
                        .value(car.getDetails().getProductionYear()))
                .andExpect(jsonPath("details.externalColor")
                        .value(car.getDetails().getExternalColor()))
                .andReturn();
    }

    /**
     * Tests the deletion of a single car by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(delete(new URI("/cars/1")))
                .andExpect(status().isNoContent());
    }

    /**
     * Creates an example Car object for use in testing.
     *
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}