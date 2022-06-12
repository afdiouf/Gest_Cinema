package sn.dioufy.cinema.service;

import sn.dioufy.cinema.model.Seance;

public interface ICinemaInitService {

	public void initVilles();

	public void initCinemas() throws Exception;

	public void initSalles();

	public void initPlaces();

	public void initSeances();

	public void initProjections();

	public void initCategories();

	public void initFilms();

	public void initTickets();
	
	public Seance seance();

}
