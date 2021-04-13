package com.spring.messenger.controller;

import com.spring.messenger.exception.ResourceNotFoundException;
import com.spring.messenger.model.Post;
import com.spring.messenger.repository.PostRepository;
import com.spring.messenger.security.model.User;
import com.spring.messenger.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class PostController {
    private PostRepository postRepository;
    private UserRepository userRepository;

    @GetMapping("/getposts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("No such User"));
        post.setUser(user);
        return postRepository.save(post);
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }


    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

}