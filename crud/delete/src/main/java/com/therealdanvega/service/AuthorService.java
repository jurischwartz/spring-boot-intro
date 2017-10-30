package com.therealdanvega.service;

import java.util.List;

import com.therealdanvega.domain.Post;
import com.therealdanvega.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.therealdanvega.domain.Author;
import com.therealdanvega.repository.AuthorRepository;

@Service
public class AuthorService {

	private AuthorRepository authorRepository;
	private PostService postService;

	@Autowired
	public AuthorService(AuthorRepository authorRepository) {
		super();
		this.authorRepository = authorRepository;
	}

	@Autowired
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public List<Author> list() {
		return authorRepository.findAllByOrderByLastNameAscFirstNameAsc();
	}

	public void save(Author author) {
		authorRepository.save(author);
	}

	public void delete(Long id) {
		//delete all posts by author
		List<Post> posts = postService.listByAuthor(id);
		for (Post post : posts) {
			postService.delete(post.getId());
		}
		//delete author
		authorRepository.delete(id);
	}

	public Author get(Long id) {
		return authorRepository.findOne(id);
	}

}
