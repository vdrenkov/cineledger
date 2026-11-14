package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.CategoryDto;
import dev.vdrenkov.cineledger.models.entities.Category;
import dev.vdrenkov.cineledger.models.requests.CategoryRequest;
import dev.vdrenkov.cineledger.services.CategoryService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
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
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(URIConstants.CATEGORIES_PATH)
    public ResponseEntity<Void> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        final Category category = categoryService.addCategory(categoryRequest);

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.CATEGORIES_ID_PATH)
            .buildAndExpand(category.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(URIConstants.CATEGORIES_PATH)
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        final List<CategoryDto> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = URIConstants.CATEGORIES_PATH, params = "categoryName")
    public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam String categoryName) {
        final CategoryDto category = categoryService.getCategoryDtoByName(categoryName);

        return ResponseEntity.ok(category);
    }

    @PutMapping(URIConstants.CATEGORIES_ID_PATH)
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
        @PathVariable int id, @RequestParam(required = false) Boolean returnOld) {

        final CategoryDto categoryDto = categoryService.updateCategory(categoryRequest, id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.CATEGORIES_ID_PATH)
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable int id,
        @RequestParam(required = false) Boolean returnOld) {

        final CategoryDto categoryDto = categoryService.deleteCategory(id);

        return Boolean.TRUE.equals(returnOld) ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
    }
}
