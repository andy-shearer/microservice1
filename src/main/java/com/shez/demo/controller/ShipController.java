package com.shez.demo.controller;

import com.shez.demo.model.Ship;
import com.shez.demo.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * RestController which has request mapping methods for RESTful requests
 */
@RestController // Define a controller and indicate that the return value of the methods should be be bound to the web response body
@RequestMapping("/api") // Declares that all APIs’ url in the controller will start with /api
@CrossOrigin(origins="http://localhost:8081") // Configuring allowed request origins
public class ShipController {
    @Autowired // inject ShipService bean to local variable
    ShipService shipService;

    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getAllShips(@RequestParam(required = false) String model) {
        try {
            List<Ship> ships = new ArrayList<>();
            if(model != null) {
                ships.addAll(shipService.findByModelContaining(model));
            } else {
                ships.addAll(shipService.findAll());
            }

            if(ships.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ships, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShipById(@PathVariable("id") long id) {
        Ship ship = shipService.findById(id);
        if(ship != null) {
            return new ResponseEntity<>(ship, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/ships")
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        try {
            Ship newShip = shipService.save(new Ship(ship.getModel(), ship.getDescription(), ship.isFlown()));
            return new ResponseEntity<>(newShip, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable("id") long id, @RequestBody Ship ship) {
        Ship foundShip = shipService.findById(id);

        if(foundShip != null) {
            foundShip.setModel(ship.getModel());
            foundShip.setDescription(ship.getDescription());
            foundShip.setFlown(ship.isFlown());
            return new ResponseEntity<>(shipService.save(foundShip), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity<HttpStatus> deleteShip(@PathVariable("id") long id) {
        try {
            shipService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/ships")
    public ResponseEntity<HttpStatus> deleteAllShips() {
        try {
            shipService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ships/flown")
    public ResponseEntity<List<Ship>> findByFlown() {
        try {
            List<Ship> flownShips = shipService.findByFlown(true);
            if(flownShips.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(flownShips, HttpStatus.OK);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
