package org.example.service;

import org.example.model.Viking;
import org.example.repository.VikingStorage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;

    public VikingService(VikingFactory vikingFactory, VikingStorage vikingStorage) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking findById(int id) {
        return vikingStorage.findById(id)
                .orElseThrow(() -> missingViking(id));
    }

    public Viking createViking(Viking viking) {
        return vikingStorage.save(viking);
    }

    public Viking createRandomViking() {
        return createViking(vikingFactory.createRandomViking());
    }

    public Viking updateById(int id, Viking newState) {
        return vikingStorage.update(id, newState)
                .orElseThrow(() -> missingViking(id));
    }

    public void deleteById(int id) {
        if (!vikingStorage.delete(id)) {
            throw missingViking(id);
        }
    }

    public List<Viking> createRandomVikings(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> vikingFactory.createRandomViking())
                .map(vikingStorage::save)
                .toList();
    }

    public Viking createRandom() {
        return vikingStorage.save(vikingFactory.createRandomViking());
    }

    public List<Viking> loadAll() {
        return vikingStorage.findAll();
    }

    private ResponseStatusException missingViking(int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Викинг с id " + id + " не найден");
    }
}