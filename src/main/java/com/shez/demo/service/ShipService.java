package com.shez.demo.service;

import com.shez.demo.model.Ship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service component that implement CRUD methods and custom finder methods. Autowired in ShipController
 */
@Service
public class ShipService {
    static List<Ship> ships = new ArrayList<>();
    static long id = 0;

    public List<Ship> findAll() {
        return ships;
    }

    public List<Ship> findByModelContaining(String model) {
        return ships.stream()
                .filter(s -> s.getModel().contains(model))
                .collect(Collectors.toList());
    }

    public Ship findById(long id) {
        return ships.stream()
                .filter(s -> s.getId() == id)
                .findAny()
                .orElse(null);
    }

    public Ship save(Ship ship) {
        if(ship.getId() != 0) {
            // Update the ship
            for(int index = 0; index < ships.size(); index++) {
                if(ships.get(index).getId() == ship.getId()) {
                    ships.set(index, ship);
                    break;
                }
            }
            return ship;
        } else {
            // Newly created ship, so add it to the fleet of existing ships
            ship.setId(++id);
            ships.add(ship);
            return ship;
        }
    }

    public void deleteById(long id) {
        ships.removeIf(s -> s.getId() == id);
    }

    public void deleteAll() {
        ships.clear();
    }

    public List<Ship> findByFlown(boolean hasFlown) {
        return ships.stream()
                .filter(s -> s.isFlown() == hasFlown)
                .collect(Collectors.toList());
    }
}
