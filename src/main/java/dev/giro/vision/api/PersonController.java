package dev.giro.vision.api;

import dev.giro.vision.face.domain.FaceService;
import dev.giro.vision.face.domain.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
@Tag(name = "Persons", description = "Person identity management")
public class PersonController {

    private final FaceService faceService;

    public PersonController(FaceService faceService) {
        this.faceService = faceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new person")
    public PersonResponse create(@Valid @RequestBody CreatePersonRequest request) {
        Person person = faceService.registerPerson(request.name());
        return PersonResponse.from(person);
    }

    @GetMapping
    @Operation(summary = "List all persons")
    public List<PersonResponse> list() {
        return faceService.listPersons().stream().map(PersonResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a person by ID")
    public PersonResponse get(@PathVariable UUID id) {
        return PersonResponse.from(faceService.getPerson(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a person")
    public void delete(@PathVariable UUID id) {
        faceService.deletePerson(id);
    }

    public record CreatePersonRequest(@NotBlank String name) {}

    public record PersonResponse(UUID id, String name, String createdAt) {
        static PersonResponse from(Person p) {
            return new PersonResponse(p.id(), p.name(), p.createdAt().toString());
        }
    }
}
