package com.shez.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shez.demo.controller.ShipController;
import com.shez.demo.model.Ship;
import com.shez.demo.service.ShipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The {@link WebMvcTest} annotation disables full autoconfiguration (@Component, @Service or @Repository beans will not
 * be scanned) and applies only configuration that is relevant to the web layer.
 */
@WebMvcTest(ShipController.class)
public class ShipControllerTest {

	@MockBean
	private ShipService shipService;

	@Autowired
	private MockMvc mockMvc; // Allows us to configure and send fake HTTP requests to the controller

	@Autowired
	private ObjectMapper objectMapper;

	private final List<Ship> exampleShipsList = Arrays.asList(
			new Ship("Starship V1", "SN8, launched 09/12/2021.", true),
			new Ship("Starship V1", "SN9, launched 02/02/2022.", true),
			new Ship("Starship V1", "SN10, launched 03/03/2022.", true),
			new Ship("Starship V1", "SN12, scrapped before completion", false),
			new Ship("Starship V2", "S??, in development", false));

	@Test
	public void testCreateShip() throws Exception {
		Ship ship = exampleShipsList.get(0);
		mockMvc.perform(post("/api/ships").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ship)))
				.andExpect(status().isCreated()).andDo(print());
	}

	@Test
	public void testGetShip() throws Exception {
		long id = 1L;
		Ship ship = exampleShipsList.get(0);

		when(shipService.findById(id)).thenReturn(ship);
		mockMvc.perform(get("/api/ships/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.model").value(ship.getModel()))
				.andExpect(jsonPath("$.description").value(ship.getDescription()))
				.andExpect(jsonPath("$.flown").value(ship.isFlown()))
				.andDo(print());
	}

	@Test
	public void testNotFoundShip() throws Exception {
		long id = 1L;
		when(shipService.findById(id)).thenReturn(null);
		mockMvc.perform(get("/api/ships/{id}", id))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testListOfShips() throws Exception {
		when(shipService.findAll()).thenReturn(exampleShipsList);
		mockMvc.perform(get("/api/ships"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(exampleShipsList.size()))
				.andDo(print());
	}

	@Test
	public void testFilteredListOfShipsFound() throws Exception {
		String targetModel = "Starship V2";
		when(shipService.findByModelContaining(targetModel)).thenReturn(exampleShipsList.subList(4, 5));

		mockMvc.perform(get("/api/ships").param("model", targetModel))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$.[0].flown").value(false))
				.andDo(print());
	}

	@Test
	public void testFilteredListOfShipsNotFound() throws Exception {
		String nonExistentModel = "Starship 2.0";
		when(shipService.findByModelContaining(nonExistentModel)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/ships").param("model", nonExistentModel))
				.andExpect(status().isNoContent())
				.andDo(print());
	}

	@Test
	public void testFilterFlownShips() throws Exception {
		when(shipService.findByFlown(true)).thenReturn(exampleShipsList.subList(0,3));

		mockMvc.perform(get("/api/ships/flown"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(3))
				.andDo(print());
	}

	@Test
	public void testUpdateShipFound() throws Exception {
		long id = 1L;
		Ship ship = new Ship("Starship V1", "S99, test article", false);
		Ship updatedShip = new Ship("Starship V2", "S99, launched 31/08/2034", true);

		when(shipService.findById(id)).thenReturn(ship);
		when(shipService.save(any(Ship.class))).thenReturn(updatedShip);

		mockMvc.perform(put("/api/ships/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedShip)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.model").value(updatedShip.getModel()))
				.andExpect(jsonPath("$.description").value(updatedShip.getDescription()))
				.andExpect(jsonPath("$.flown").value(updatedShip.isFlown()))
				.andDo(print());
	}

	@Test
	public void testUpdateShipNotFound() throws Exception {
		long id = 1L;
		Ship updatedShip = new Ship("Starship V2", "S99, launched 31/08/2034", true);

		when(shipService.findById(id)).thenReturn(null);
		when(shipService.save(any(Ship.class))).thenReturn(updatedShip);

		mockMvc.perform(put("/api/ships/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedShip)))
				.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testDeleteShip() throws Exception {
		long id = 1L;
		doNothing().when(shipService).deleteById(id);

		mockMvc.perform(delete("/api/ships/{id}", id))
				.andExpect(status().isNoContent())
				.andDo(print());
	}

	@Test
	public void testDeleteAllShips() throws Exception {
		doNothing().when(shipService).deleteAll();

		mockMvc.perform(delete("/api/ships"))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
}
