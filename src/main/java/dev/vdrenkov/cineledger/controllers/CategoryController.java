package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.services.CategoryService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(URIConstants.CATEGORIES_PATH)
    public ResponseEntity<Void> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(categoryRequest);
        log.info("A request for a category to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.CATEGORIES_ID_PATH)
            .buildAndExpand(category.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.CATEGORIES_PATH)
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        log.info("All categories were requested from the database");

        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = URIConstants.CATEGORIES_PATH, params = "categoryName")
    public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam String categoryName) {
        CategoryDto category = categoryService.getCategoryDtoByName(categoryName);
        log.info(String.format("Category with name %s has been requested from database", categoryName));

        return ResponseEntity.ok(category);
    }

    @PutMapping(URIConstants.CATEGORIES_ID_PATH)
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
        @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

        CategoryDto categoryDto = categoryService.updateCategory(categoryRequest, id);
        log.info(String.format("Category with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.CATEGORIES_ID_PATH)
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        CategoryDto categoryDto = categoryService.deleteCategory(id);
        log.info(String.format("Category with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
    }
}



