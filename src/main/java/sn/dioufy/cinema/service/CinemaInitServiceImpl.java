package sn.dioufy.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sn.dioufy.cinema.model.Categorie;
import sn.dioufy.cinema.model.Cinema;
import sn.dioufy.cinema.model.Film;
import sn.dioufy.cinema.model.Place;
import sn.dioufy.cinema.model.Projection;
import sn.dioufy.cinema.model.Salle;
import sn.dioufy.cinema.model.Seance;
import sn.dioufy.cinema.model.Ticket;
import sn.dioufy.cinema.model.Ville;
import sn.dioufy.cinema.repository.CategorieRepository;
import sn.dioufy.cinema.repository.CinemaRepository;
import sn.dioufy.cinema.repository.FilmRepository;
import sn.dioufy.cinema.repository.PlaceRepository;
import sn.dioufy.cinema.repository.ProjectionRepository;
import sn.dioufy.cinema.repository.SalleRepository;
import sn.dioufy.cinema.repository.SeanceRepository;
import sn.dioufy.cinema.repository.TicketRepository;
import sn.dioufy.cinema.repository.VilleRepository;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {

	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public void initVilles() {
		Stream.of("Dakar", "Mbour", "Thies", "Pikine").forEach(nameVille -> {
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
	}

	@Override
	public void initCinemas() throws Exception {
		
			villeRepository.findAll().forEach(ville -> {
				Stream.of("HOLIWOOD", "IMAX", "CINEGARE", "CINETROUPE", "SOLEIL-LEVANT").forEach(nameCinema -> {
					Cinema cinema = new Cinema();
					cinema.setName(nameCinema);
					cinema.setNombreSalles(3 + (int) (Math.random() * 7));
					cinema.setVille(ville);
					cinemaRepository.save(cinema);
				});
			});
			
		
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setCinema(cinema);
				salle.setNombrePlaces(15 + (int) (Math.random() * 20));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle -> {
			for (int i = 0; i < salle.getNombrePlaces(); i++) {
				Place place = new Place();
				place.setNumero(i + 1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		seance();
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire", "Action", "Fiction", "Drame").forEach(cat -> {
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] { 1, 1.5, 2, 2.5, 3 };
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("Game of thrones", "NCIS LA", "BlackList", "Blindspot", "Seals").forEach(titreFilm -> {
			Film film = new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "") + ".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
	}

	@Override
	public void initProjections() {
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		double[] prices = new double[] { 30, 50, 60, 70, 90, 100 };
		List<Film> films = filmRepository.findAll();
		villeRepository.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					int index = new Random().nextInt(films.size());
					Film film = films.get(index);
					seanceRepository.findAll().forEach(seance -> {

						Projection projection = new Projection();
						projection.setDateProjection(new Date());
						projection.setFilm(film);
						
						projection.setPrix(prices[new Random().nextInt(prices.length)]);

						/*
						 * try { if (seance.getHeureDebut().equals(dateFormat.parse("08:00"))) {
						 * projection.setPrix(100); } else if
						 * (seance.getHeureDebut().equals(dateFormat.parse("17:00"))) {
						 * projection.setPrix(20); } else { projection.setPrix(prices[new
						 * Random().nextInt(prices.length)]); } } catch (ParseException e) {
						 * e.printStackTrace(); }
						 */

						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionRepository.save(projection);
					});
				});
			});
		});
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p -> {
			p.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReservee(false);
				ticketRepository.save(ticket);
			});
		});
	}

	public Seance seance() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Seance seance = new Seance();
		try {
			seance.setHeureDebut(dateFormat.parse("08:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return seanceRepository.save(seance);

	}

}
