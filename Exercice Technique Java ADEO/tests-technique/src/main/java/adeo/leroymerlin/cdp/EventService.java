package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();

        // Filter the events list in pure JAVA here
        return events.stream()
                .filter(v -> v.getBands()
                        .stream()
                        .flatMap(u -> u.getMembers().stream())
                        .anyMatch(u -> u.getName().contains(query))
                )
                .map(v -> {
                    v.setTitle(v.getTitle() + " [" + v.getBands().size() + "]");
                    v.getBands().forEach(u -> u.setName(u.getName() + " [" + u.getMembers().size() + "]"));
                    return v;
                })
                .collect(Collectors.toList());
    }

    public void updateEvent(Long id, Event event) {
        eventRepository.save(event);
    }
}
