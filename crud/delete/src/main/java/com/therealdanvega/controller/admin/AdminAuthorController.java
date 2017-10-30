package com.therealdanvega.controller.admin;

import com.therealdanvega.domain.Author;
import com.therealdanvega.domain.Post;
import com.therealdanvega.service.AuthorService;
import com.therealdanvega.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Secured( {"ROLE_ADMIN"} )
public class AdminAuthorController {

	private PostService postService;
	private AuthorService authorService;

	@Autowired
	public AdminAuthorController(PostService postService, AuthorService authorService) {
		this.postService = postService;
		this.authorService = authorService;
	}
	
	@RequestMapping("/admin/authors")
	public String list(ModelMap model) {
		List<Author> authors = authorService.list();
		Map<String,Integer> postsCountForAuthor = new HashMap<>();
		for (Author author: authors) {
			List<Post> posts = postService.listByAuthor(author.getId());
			postsCountForAuthor.put(author.getId().toString(),posts.size());
		}
		model.addAttribute("authors", authors);
		model.addAttribute("postsCountForAuthor", postsCountForAuthor);
		return "admin/author/list";
	}
	
	// create | save

	@RequestMapping("/admin/author/create")
	public String create(Model model) {
		model.addAttribute("author", new Author("","",""));
		return "admin/author/postForm";
	}

	@RequestMapping( value = "/admin/author/save", method = RequestMethod.POST )
	public String save(@Valid Author author, BindingResult bindingResult) {

		if( bindingResult.hasErrors() ){
			return "admin/author/postForm";
		} else {
			authorService.save(author);
			return "redirect:/admin/authors";
		}

	}

	@RequestMapping("/admin/author/edit/{id}")
	public String edit(@PathVariable Long id, Model model){
		model.addAttribute("author", authorService.get(id));
		return "admin/author/postForm";
	}

	@RequestMapping("/admin/author/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
		authorService.delete(id);
		redirectAttrs.addFlashAttribute("message", "The Author and all associated posts have been deleted!");
		return "redirect:/admin/authors";
	}
	
}
