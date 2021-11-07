package me.dwamuya.fakeddit.services;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.dto.SubredditRequest;
import me.dwamuya.fakeddit.dto.SubredditResponse;
import me.dwamuya.fakeddit.dto.UserResponse;
import me.dwamuya.fakeddit.exceptions.CustomException;
import me.dwamuya.fakeddit.mapper.SubredditMapper;
import me.dwamuya.fakeddit.mapper.UserMapper;
import me.dwamuya.fakeddit.models.Subreddit;
import me.dwamuya.fakeddit.models.User;
import me.dwamuya.fakeddit.repositories.SubredditRepository;
import me.dwamuya.fakeddit.repositories.UserRepository;
import me.dwamuya.fakeddit.security.AuthStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthStatus authStatus;

    private final SubredditMapper subredditMapper;
    private final UserMapper userMapper;

    public List<Subreddit> findAllSubreddits() {
        return subredditRepository.findAll();
    }

    public List<Subreddit> findAllById(List<Long> subredditIds) {
        return subredditRepository.findAllById(subredditIds);
    }

    public Subreddit findSubredditByUrl(String url) {
        return subredditRepository
                .findByUrl(url)
                .orElseThrow(() ->
                        new CustomException("Unable to find Subreddit of Url : " + url)
                );
    }

    public Subreddit findOneSubreddit(Long subredditId) {
        return subredditRepository
                .findById(subredditId)
                .orElseThrow(() ->
                    new CustomException("Unable to find Subreddit of Id : " + subredditId)
                );
    }

    public SubredditResponse createSubreddit(SubredditRequest subredditRequest) {
        Subreddit subreddit = subredditRepository
                .save(subredditMapper.builder(subredditRequest));

        User user = authStatus.getUser();

        List<Subreddit> subreddits = new ArrayList<>();
        subreddits.add(subreddit);
        user.setSubreddits(subreddits);
        userService.updateUser(user);

        return getSubredditResponse(subreddit);
    }

    public Object joinSubreddit(Long subredditId) {
        Subreddit subreddit = findOneSubreddit(subredditId);
        User user = authStatus.getUser();

        List<Subreddit> subreddits = user.getSubreddits();

        if(!subreddits.contains(subreddit)) {
            subreddits.add(subreddit);
            user.setSubreddits(subreddits);
            userRepository.save(user);
        }

        List<User> members = subreddit.getMembers();

        if(!members.contains(user)) {
            members.add(user);
            subreddit.setMembers(members);
            subredditRepository.save(subreddit);
        }

        return "Welcome to " + subreddit.getName() + ", " + user.getUsername() + "!";
    }

    public Object leaveSubreddit(Long subredditId) {
        Subreddit subreddit = findOneSubreddit(subredditId);
        User user = authStatus.getUser();

        List<Subreddit> subreddits = user.getSubreddits();

        if(subreddits.contains(subreddit)) {
            subreddits.remove(subreddit);
            user.setSubreddits(subreddits);
            userRepository.save(user);
        }

        List<User> members = subreddit.getMembers();

        if(members.contains(user)) {
            members.remove(user);
            subreddit.setMembers(members);
            subredditRepository.save(subreddit);
        }

        return "Thanks for being with " + subreddit.getName() + ", " + user.getUsername() + "!";

    }

    public Object updateSubreddit(
            SubredditRequest subredditRequest,
            Long subredditId
    ) {
        Subreddit subreddit = subredditMapper.mapper(subredditRequest, findOneSubreddit(subredditId));

        if(isOwner(subreddit)) {
            subredditRepository.save(subreddit);
            return getSubredditResponse(subreddit);
        }

        return "Illegal attempt to update subreddit";
    }

    public Object deleteSubreddit(Long subredditId) {
        Subreddit subreddit = findOneSubreddit(subredditId);

        if(isOwner(subreddit)) {
            subredditRepository.delete(subreddit);
            updateOwner(subreddit);
            
            return "Subreddit of Id : " + subreddit.getSubredditId() + " deleted.";
        }

        return "Illegal attempt to delete subreddit";
    }

    @Async
    private void updateOwner(Subreddit subreddit) {
        User user = authStatus.getUser();
        List<Subreddit> subreddits = user.getSubreddits();
        subreddits.remove(subreddit);
        user.setSubreddits(subreddits);
        userService.updateUser(user);
    }

    private boolean isOwner(Subreddit subreddit) {
        return subreddit
                .getOwner()
                .getUserId()
                .equals(
                        authStatus.getUser().getUserId()
                );
    }

    /* Responses */

    public SubredditResponse getSubredditResponse(Subreddit subreddit) {
        return subredditMapper.toResponse(subreddit);
    }

    public SubredditResponse getSubredditResponse(Long subredditId) {
        return subredditMapper.toResponse(findOneSubreddit(subredditId));
    }

    public List<SubredditResponse> getAllSubredditResponses() {
        List<Subreddit> subreddits = findAllSubreddits();

        return subreddits
                .stream()
                .map(subredditMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllMembers(Long subredditId) {
        Subreddit subreddit = findOneSubreddit(subredditId);

        return subreddit.getMembers()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubredditResponse> getUserSubreddits() {
        return findAllById(
                authStatus.getUser()
                .getSubreddits()
                .stream()
                .map(Subreddit::getSubredditId)
                .collect(Collectors.toList())
        ).stream().map(subredditMapper::toResponse).collect(Collectors.toList());
    }

    public List<SubredditResponse> getNonMemberSubs() {
        List<Subreddit> allSubreddits = findAllSubreddits();
        List<Subreddit> res = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Subreddit random = getRandom(allSubreddits);
            if (!res.contains(random) && !isMember(random)) {
                res.add(random);
            }
        }

        return res.stream()
                .map(subredditMapper::toResponse)
                .collect(Collectors.toList());
    }

    private boolean isMember(Subreddit subreddit) {
        return subreddit.getMembers().contains(authStatus.getUser());
    }


    public static Subreddit getRandom(List<Subreddit> subreddits) {
        int rnd = new Random().nextInt(subreddits.size());
        return subreddits.get(rnd);
    }
}