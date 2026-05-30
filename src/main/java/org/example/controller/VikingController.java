package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.BeardStyle;
import org.example.model.HairColor;
import org.example.model.Viking;
import org.example.service.VikingLambdaService;
import org.example.service.VikingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "CRUD и лямбда-выборки")
public class VikingController {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService,
                            VikingLambdaService lambdaService,
                            VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Все викинги")
    public List<Viking> getAll() {
        return vikingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Викинг по id")
    public Viking getById(@PathVariable int id) {
        return vikingService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать викинга")
    public Viking create(@RequestBody Viking viking) {
        Viking created = vikingService.createViking(viking);
        vikingListener.onVikingAdded(created);
        return created;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить викинга")
    public Viking update(@PathVariable int id, @RequestBody Viking viking) {
        Viking updated = vikingService.updateById(id, viking);
        vikingListener.onVikingAdded(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить викинга")
    public void delete(@PathVariable int id) {
        vikingService.deleteById(id);
        vikingListener.onVikingDeleted(id);
    }

    @PostMapping("/random")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Случайный викинг")
    public Viking createRandom() {
        Viking created = vikingService.createRandomViking();
        vikingListener.onVikingAdded(created);
        return created;
    }

    @PostMapping("/generate/{count}")
    @Operation(summary = "Массовая генерация")
    public List<Viking> generate(@PathVariable int count) {
        List<Viking> generated = vikingService.createRandomVikings(count);
        generated.forEach(vikingListener::onVikingAdded);
        return generated;
    }

    @GetMapping("/lambda/older/{age}")
    public long olderThan(@PathVariable int age) {
        return lambdaService.countOlderThan(age);
    }

    @GetMapping("/lambda/younger/{age}")
    public long youngerThan(@PathVariable int age) {
        return lambdaService.countYoungerThan(age);
    }

    @GetMapping("/lambda/range")
    public long inRange(@RequestParam int from, @RequestParam int to) {
        return lambdaService.countInAgeRange(from, to);
    }

    @GetMapping("/lambda/outside")
    public long outsideRange(@RequestParam int from, @RequestParam int to) {
        return lambdaService.countOutsideAgeRange(from, to);
    }

    @GetMapping("/lambda/beard-hair")
    public long byBeardAndHair(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return lambdaService.countByBeardAndHair(beard, hair);
    }

    @GetMapping("/lambda/axes")
    public long withAxes() {
        return lambdaService.countWithAxes();
    }

    @GetMapping("/lambda/tall")
    public Viking randomTall() {
        return lambdaService.getRandomTallViking();
    }

    @GetMapping("/lambda/legendary")
    public List<Viking> legendary() {
        return lambdaService.getLegendaryVikings();
    }

    @GetMapping("/lambda/redbeard")
    public List<Viking> redBearded() {
        return lambdaService.getSortedRedBeardedVikings();
    }

    @GetMapping("/lambda/max-id")
    public int maxId() {
        return lambdaService.findMaxId();
    }

    @GetMapping("/lambda/even-ids")
    public int[] evenIds() {
        return lambdaService.findEvenIds();
    }

    @GetMapping("/test")
    @Operation(summary = "Проверка связи")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }
}