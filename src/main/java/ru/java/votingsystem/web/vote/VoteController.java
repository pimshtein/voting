package ru.java.votingsystem.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.java.votingsystem.model.AuthUser;
import ru.java.votingsystem.repository.VoteRepository;
import ru.java.votingsystem.usecase.VoteForRestaurant;
import ru.java.votingsystem.web.vote.request.CreateVoteTo;
import ru.java.votingsystem.web.vote.response.CountVoteByRestaurantPerDay;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    static final String REST_URL = "/api/v1/votes/";

    private final VoteRepository repository;
    private final VoteForRestaurant useCase;

    @GetMapping("by-restaurant/")
    @Operation(description = "Get all votes sorted by votes")
    public List<CountVoteByRestaurantPerDay> getAll() {
        log.info("getAll");
        return repository.countVotesByRestaurantPerDayNative();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateVoteTo voteTo
    ) {
        int userId = authUser.id();
        log.info("create {} for user {}", voteTo, userId);
        useCase.execute(voteTo, userId);
    }
}